package co.tmpdb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import co.exception.DataAlreadyExistsException;
import co.exception.DataNotFoundException;
import co.exception.InvalidMetricTypeException;
import co.model.Measurement;
import co.model.Metric;
import co.model.StatisticData;
import co.service.IMeasureData;
import co.util.DateHelper;

/**
 * Simulate a database.  Write/Update methods are synchronized to simulate a transaction.
 * Main data holder class:  dbTable
 * @author chenvic
 *
 */
public enum StubDatabase implements IMeasureData{
	INSTANCE;
	 
	/* Database class.  The most important class dbTable holds all the data.
	 * It represents database structure: |Date<Long>|List of Metrics\  where Date is a key.  Essentially Date is a hashmap key.
	 * So it's a map<Long,List <Metrics>>  Long.  is Date in long form.
	 */
	Map <Long,List> dbTable = Collections.synchronizedMap(new HashMap());
	
	private final static String CONST_FUNCTIONAVG = "avg";
	private final static String CONST_FUNCTIONMIN = "min";
	private final static String CONST_FUNCTIONMAX = "max";	

	/**
	 * Add a measurement.  A measurement is a date followed by a list of metrics from different metric types
	 * @param Measurement
	 * @return Measurement
	 */
	public Measurement addMeasurement(Measurement m) throws DataAlreadyExistsException{
		List <Metric> l = m.getMetricsList();
		Date dateMeasured = DateHelper.formatJavaScriptDateString(m.getDateMeasured());
		if (dbTable.get(dateMeasured.getTime())!=null)
			throw new DataAlreadyExistsException();
		dbTable.put(dateMeasured.getTime(), l);
		return m;
	} 
	

	/**
	 * Delete function.  Simulate transaction by making it synchronized.
	 * Check the timestamp.  If there is a row/measurement for that date, delete it.  Otherwise, throw exception
	 * @param date
	 * @throws DataNotFoundException
	 */
	public synchronized void deleteMeasurement(Date d) throws DataNotFoundException{

		List l = dbTable.get(d.getTime());
		if (l == null || l.size() ==0) {
			throw new DataNotFoundException();
		} else {
			dbTable.remove(d.getTime());
		}
	} 

	/**
	 * replace the metrics for a particular measurement time.  The passed in measurement will contain a list of Metrics that want to be replaced.
	 * If a measurement for that timestamp is not found, throw an exception.  Make it synchronized to simulate a transaction.
	 * @param Measurement
	 * @return Measurement
	 * @throws DataNotFoundException
	 */
	public synchronized Measurement replaceMeasurement(Measurement m) throws DataNotFoundException{
		String s = m.getDateMeasured();
		Date d = DateHelper.formatJavaScriptDateString(s);
		List <Metric> l = dbTable.get(d.getTime());
		
		/**
		 * If we can't find any data for that timestamp (in long format), we want to throw an exception.
		 */
		if (l == null || l.size() == 0)
			throw new DataNotFoundException();
		
		List <Metric> currentMetrics = m.getMetricsList();
		l.clear();
		l.addAll(currentMetrics);
		Measurement m2 = new Measurement();
		m2.setDateMeasured(s);
		m2.setMetricsList(l);
		return m2;
		
	}

	/**
	 * Update the metrics for a particular measurement time.  The passed in measurement will contain a list of Metrics that want to be updated.
	 * If a measurement for that timestamp is not found, throw an exception.  Make it synchronized to simulate a transaction.
	 * If no data is found, throw exception
	 * @param Measurement
	 * @return Measurement
	 * @throws DataNotFoundException
	 */
	public synchronized Measurement updateMeasurement(Measurement m) throws DataNotFoundException{
		String s = m.getDateMeasured();
		Date d = DateHelper.formatJavaScriptDateString(s);
		List <Metric> l = dbTable.get(d.getTime());
		if (l == null || l.size() == 0)
			throw new DataNotFoundException();
		
		List <Metric> newMetrics = m.getMetricsList();
		
		//loop through current list and if we find a metric from the one in the api, replace that one metric only.
		for (Iterator<Metric> newIter = newMetrics.iterator(); newIter.hasNext(); ) {
			boolean wasNewFoundInOrig = false;
			Metric newM = newIter.next();
			Iterator <Metric> origIter = l.iterator();
			while (origIter.hasNext()) {
				Metric origM = origIter.next();
				if (origM.getName().equals(newM.getName())) {
					origM.setValue(newM.getValue());
					wasNewFoundInOrig = true;
				}
			}
			//Oh no! There is no metric of that type to change!  Throw exception.
			if(!wasNewFoundInOrig) {
				throw new InvalidMetricTypeException();
			}
		}

		/** Create object to return**/
		Measurement m2 = new Measurement();
		m2.setDateMeasured(s);
		m2.setMetricsList(l);
		return m2;
	}

	
	/**
	 * Get a measurement for a particular timestamp.  Only one will be returned.  However, the measurement
	 * object will contain a timestamp and a list of metrics that happened on that timestamp.  If no data is found
	 * throw exception
	 * @param Date
	 * @return Measurement
	 */
	public Measurement getMeasurement(Date d) throws DataNotFoundException{
		
		List l = dbTable.get(d.getTime());
		Measurement m = new Measurement();
		m.setDateMeasured(DateHelper.getDateInUTC(d));
		m.setMetricsList(l);
		if (l == null || l.size() == 0)
			throw new DataNotFoundException();
		return m;
	}
	/**
	 * Returns back a list of measurements for the entire day given the date.  If there is no data found
	 * throw an exception
	 * @param timestamp
	 * @return List <Measurement>
	 */
	public List <Measurement>getMeasurementsForTheDay(Date day) throws DataNotFoundException{
		List <Measurement> allMeasurementsForTheDay = new ArrayList();

		//List of all rows (measurements) for that day
		List <Metric> l = new ArrayList();
		
		//Loop through to get a list of all rows - checking if the the measurement happend on that day.
		for (Iterator<Long> iter = dbTable.keySet().iterator(); iter.hasNext(); ) {
			Long l2 = iter.next();
			Date d = new Date(l2);
			
			//we need to strip the date of hh:mm:ss to compare just the day itself.
			Date dStripped = DateHelper.stripDateOfHHMMSSMMM(d);
			
			if (dStripped.getTime() == day.getTime()) {
				//the metrics happend for the same day so create a list of measurements to send back to gui.
				List metrics = dbTable.get(l2);
				Measurement m = new Measurement();
				m.setMetricsList(metrics);
				String str = DateHelper.getDateInUTC(d);
				m.setDateMeasured(str);
				allMeasurementsForTheDay.add(m);
			}
		
		}
		
		/**
		 * Error condition.  If there were no measurements for that day, send error to gui.
		 */
		if (allMeasurementsForTheDay.size() == 0)
			throw new DataNotFoundException();
		return allMeasurementsForTheDay;
	}


	/**
	 * Helper function.  Loop through all the metrics in a list.  Search for the metric type.  Then find the average
	 * of the value.  For instance, we will loop through the list of metrics, match on temperature only (ignoring dew point)
	 * then average up all the temperature values.
	 * @param list
	 * @param metricName
	 * @return float
	 */
	private Float calculateAverageForMetricName(List <Metric> list,String metricName) {

		float avg=0;
		int avgcount=0;
		float sum=0;
		
		for (Iterator<Metric> iter = list.iterator(); iter.hasNext(); ) {
		    Metric element = iter.next();
		    if (element.getName().equals(metricName.trim())) {
		    	sum = sum + element.getValue();
		    	avgcount++;
		    }
		}		
	    //At the end tally, up the overall sum of values, then divide by number of average values
	    avg = sum/avgcount;
	    return avg;
	}

	/**
	 * Loop through the Metrics list, and for a certain metric type, i.e. temperature, get me the smallest value.
	 * @param list
	 * @param metricName
	 * @return float
	 */
	private Float getMin(List <Metric> list, String metricName) {

		float retMin = -1;
		
		for (Iterator<Metric> iter = list.iterator(); iter.hasNext(); ) {
		    Metric element = iter.next();
		    if (element.getName().equals(metricName.trim())) {
		    	float val = element.getValue();
		    	if (retMin < 0)
		    		retMin = val;
		    	if (val < retMin)
		    		retMin = val;
		    }
		}		
		return retMin;
	}

	/**
	 * Loop through the Metrics list, and for a certain metric type, i.e. temperature, get me the largest value.
	 * @param list
	 * @param metricName
	 * @return float
	 */
	private Float getMax(List <Metric> list, String metricName) {

		float curMaxPointer = 0;
		float retMax = 0;
		
		for (Iterator<Metric> iter = list.iterator(); iter.hasNext(); ) {
		    Metric element = iter.next();
		    if (element.getName().equals(metricName.trim())) {
		    	float val = element.getValue();
		    	if (val > retMax)
		    		retMax = val;
		    }
		}		
		return retMax;
	}

	/**
	 * For a list of Metrics, does it contain a certain type?  i.e. in the list are there any temperature metrics?
	 * @param list of metrics
	 * @param metric name
	 * @return boolean
	 */
	private boolean isMetricNameInList(List <Metric> list, String name) {
		for (Iterator<Metric> iter = list.iterator(); iter.hasNext(); ) {
			Metric m = iter.next();
			if (name.trim().equals(m.getName()))
				return true;
		}
		return false;
	}
	
	/**
	 * Given a list of metric, and a list of metric types, make sure both lists are the same in terms of metric types.
	 * So all metric types in metricTypes list will exist in the Metrics objects in metricData.
	 * If there is any difference, sync the metric type list.
	 * @param metricTypes
	 * @param metricData
	 * @return List <String>
	 */
	private List <String> syncValidMetricTypes(List <String> metricTypes, List <Metric> metricData) {
		List res = new ArrayList();
		for (Iterator<String> iter = metricTypes.iterator(); iter.hasNext(); ) {
			String metricType = iter.next();
			if (isMetricNameInList(metricData,metricType)) {
				res.add(metricType);
			}
		}
		return res;
	}
	/**
	 * Given the list of http parameters figure out what data to return.  Statistic Data is nothing more but
	 * a glorified object similar to Metric object - which is nothing but a metric type to metric value pair.
	 * Example request will have structure like so:
	 *  | param        | value                    |
      | stat         | min                      |
      | stat         | max                      |
      | stat         | average                  |
      | metric       | temperature              |
      | fromDateTime | 2015-09-01T16:00:00.000Z |
      | toDateTime   | 2015-09-01T17:00:00.000Z |
	 * @param statArr
	 * @param metricArray
	 * @param toTime
	 * @param fromTime
	 * @return List <StatisticData>
	 */
	public List <StatisticData> getStatistics(String [] statArr, String [] metricArr, Date fromTime, Date toTime) {
		
		List <StatisticData> returnData = new ArrayList();
		
		List <String> metricList = Arrays.asList(metricArr);
		List <String> statList = Arrays.asList(statArr);

		Long toTimeInMillis = toTime.getTime();
		Long fromTimeInMillis = fromTime.getTime();
		
		//Loop through all the data that we have and build list of all Metrics that fit within the times
		List <Metric> allMetricList = new ArrayList();
		for (Iterator<Long> iter = dbTable.keySet().iterator(); iter.hasNext(); ) {
			Long timeInMillis = iter.next();
			
			//If the time of the measurement is in between the start and end time then process the List of metrics.
			if (timeInMillis >= fromTimeInMillis && timeInMillis <= toTimeInMillis) {
				List <Metric> dataForDate = dbTable.get(timeInMillis);
				allMetricList.addAll(dataForDate);
			}
			
		}

		//If there are no matching metrics within the date, return blank!  No need to do other processing.
		if (allMetricList.size() == 0) {
			return new ArrayList();
		}
		
		//some validation.  Make sure the metrics type list has valid metrics in it.
		metricList = syncValidMetricTypes(metricList,allMetricList);
		
		//Now that we have a list of all the metrics that fall in between the timestamps, filter them out.
		for (Iterator<String> mIter = metricList.iterator(); mIter.hasNext(); ) {
		    String metricName  = mIter.next();
			for (Iterator<String> statIter = statList.iterator(); statIter.hasNext(); ) {
				StatisticData sd = new StatisticData();
				String statName = statIter.next();
				
					float val = -1;
					
					//for this metric name, get me the average.
					if (CONST_FUNCTIONAVG.equals(statName)) {
						val = this.calculateAverageForMetricName(allMetricList,metricName);
					}
					//for this metric name, get me the min value.
					else if (CONST_FUNCTIONMIN.equals(statName)) {
						val = this.getMin(allMetricList,metricName);
						
					}
					//for this metric name, get me the max value.
					else if (CONST_FUNCTIONMAX.equals(statName)) {
						val = this.getMax(allMetricList,metricName);					
					}
					
					/* create return object to gui and add it to the return list, but only if the value is good*/
					if (val!=-1) {
						sd.setMetric(metricName);
						sd.setStat(statName);
						sd.setValue(val);
						returnData.add(sd);
					}
			}
		}
		return returnData;
	}
}
