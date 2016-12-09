package co.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.exception.DataNotFoundException;
import co.model.Measurement;
import co.model.StatisticData;
import co.service.MeasurementDAO;
import co.util.DateHelper;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {

	@Autowired
	MeasurementDAO dao;
	
	/**
	 * Add a measurement
	 * Example JSON:
	 * {"dateMeasured":"2015-09-01T17:00:00.000Z","metricsList":[{"value":17,"name":"temperature"},{"value":17,"name":"dewPoint"},{"value":17,"name":"precipitation"}]}
	 * @param measurement
	 * @return Measurement
	 */
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Measurement> addMeasurements(@RequestBody Measurement measurement) {
		measurement = dao.addMeasurement(measurement);
		
		//HATEOS links
		measurement.add(linkTo(methodOn(MeasurementController.class).addMeasurements(measurement)).withSelfRel());
		measurement.add(linkTo(methodOn(MeasurementController.class).deleteMeasurementForTimestamp(measurement.getDateMeasured())).withRel("delete"));
		measurement.add(linkTo(methodOn(MeasurementController.class).getMeasurementForTimestamp(measurement.getDateMeasured())).withRel("getfordate"));

		return new ResponseEntity<Measurement>(measurement, HttpStatus.CREATED); //code 201
	} 


	/**
	 * Method to get a list of Measurements for a given day.
	 * We also do a rough URL string check to see if the date format may is for an entire day or for a timestamp.
	 * If it contains T we assume Javascript format.  There were some issues with
	 * URL matching and TimeZone due to yyyy-MM-dd also being recognized as ISO.  And in the case where it was
	 * properly formatted, there were timezone issues.  This is due in part because we are creating a hacked up cache
	 * to simulate a db.
	 * 
	 * Jackson does some auto date conversion that will just cause additional formatting
	 * 
	 * Example: http://localhost:8080/measurements/2015-09-01
	 * @param date
	 * @return List<Measurement>
	 */
	//^\d{4}-\d{2}-\d{2}$
//	@RequestMapping(value="/{datestring:^[^T]*$}",method=RequestMethod.GET)
	@RequestMapping(value="/{datestring:^\\d{4}-\\d{2}-\\d{2}$}",method=RequestMethod.GET)
	public ResponseEntity getMeasurementsForDate(@PathVariable("datestring") @DateTimeFormat(pattern="yyyy-MM-dd") Date date) {
		List <Measurement> results = dao.getMeasurementsForTheDay(date);
		return new ResponseEntity<List<Measurement>>(results, HttpStatus.OK);
	}  

	/**
	 * Method to get ONE Measurement for a given timestamp.
	 * We also do a rough URL string check to see if the date format may is for an entire day or for a timestamp.
	 * If it contains T we assume Javascript format.  There were some issues with
	 * URL matching and TimeZone due to yyyy-MM-dd also being recognized as ISO.  And in the case where it was
	 * properly formatted, there were timezone issues.  This is due in part because we are creating a hacked up cache
	 * to simulate a db.
	 * 
	 * Jackson does some auto date conversion that will just cause additional formatting

	 * Example: http://localhost:8080/measurements/2015-09-01T18:00:22.011Z
	 * @param date
	 * @return Measurement
	 */
	//
	@RequestMapping(value="/{datestring:\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d\\:\\d\\d\\:\\d\\d.\\d\\d\\dZ}", method=RequestMethod.GET)
	public ResponseEntity getMeasurementForTimestamp(@PathVariable("datestring") String date) throws DataNotFoundException{
		Measurement measurement = dao.getMeasurement(DateHelper.formatJavaScriptDateString(date));
		//HATEOS links
		measurement.add(linkTo(methodOn(MeasurementController.class).getMeasurementForTimestamp(date)).withSelfRel());
		return new ResponseEntity<Measurement>(measurement, HttpStatus.OK);
	}  

	/**
	 * Method to replace a measurement for a given timestamp.  This will overwrite the original.
	 * We also do a rough URL string check to see if the date format may is for an entire day or for a timestamp.
	 * If it contains T we assume Javascript format.  There were some issues with
	 * URL matching and TimeZone due to yyyy-MM-dd also being recognized as ISO.  And in the case where it was
	 * properly formatted, there were timezone issues.  This is due in part because we are creating a hacked up cache
	 * to simulate a db.
	 * 
	 * Jackson does some auto date conversion that will just cause additional formatting

	 * Example: http://localhost:8080/measurements/2015-09-01T18:00:00.000Z
	 * @param date
	 * @return <none>
	 */
	@RequestMapping(value="/{datestring:\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d\\:\\d\\d\\:\\d\\d.\\d\\d\\dZ}", method=RequestMethod.PUT)
	public ResponseEntity replaceMeasurementForTimestamp(@RequestBody Measurement measure, @PathVariable("datestring") String date) {
		measure.setDateMeasured(date);
		dao.replaceMeasurement(measure);
		return new ResponseEntity<Measurement>(HttpStatus.NO_CONTENT);
	}  


	/**
	 * Method to update a measurement for a given timestamp.  So if you pass in Measurement (List = temperature = 100) 
	 * it will change just temperature metric and not any of the others for that time.
	 * We also do a rough URL string check to see if the date format may is for an entire day or for a timestamp.
	 * If it contains T we assume Javascript format.  There were some issues with
	 * URL matching and TimeZone due to yyyy-MM-dd also being recognized as ISO.  And in the case where it was
	 * properly formatted, there were timezone issues.  This is due in part because we are creating a hacked up cache
	 * to simulate a db.
	 * 
	 * Jackson does some auto date conversion that will just cause additional formatting

	 * Example: http://localhost:8080/measurements/2015-09-02T18:00:00.01Z
	 * @param date
	 * @return <none>
	 */
	@RequestMapping(value="/{datestring:\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d\\:\\d\\d\\:\\d\\d.\\d\\d\\dZ}", method=RequestMethod.PATCH)
	public ResponseEntity updateMeasurementForTimestamp(@RequestBody Measurement measure,@PathVariable("datestring") String date) {
		measure.setDateMeasured(date);
		dao.updateMeasurement(measure);
		return new ResponseEntity(HttpStatus.NO_CONTENT);
	}  

	/**
	 * Method to delete a measurement for a given timestamp.  
	 * We also do a rough URL string check to see if the date format may is for an entire day or for a timestamp.
	 * If it contains T we assume Javascript format.  There were some issues with
	 * URL matching and TimeZone due to yyyy-MM-dd also being recognized as ISO.  And in the case where it was
	 * properly formatted, there were timezone issues.  This is due in part because we are creating a hacked up cache
	 * to simulate a db.
	 * 
	 * Jackson does some auto date conversion that will just cause additional formatting

	 * Example: http://localhost:8080/measurements/2015-09-01T18:00:22.000Z
	 * @param date
	 * @return <none>
	 */
	@RequestMapping(value="/{datestring:\\d\\d\\d\\d-\\d\\d-\\d\\dT\\d\\d\\:\\d\\d\\:\\d\\d.\\d\\d\\dZ}", method=RequestMethod.DELETE)
	public ResponseEntity deleteMeasurementForTimestamp(@PathVariable("datestring") String date) {
//	public String getMeasurementForTimestamp(Date id) {
		Date d = DateHelper.formatJavaScriptDateString(date);
		dao.deleteMeasurement(d);
		return new ResponseEntity<String>(HttpStatus.NO_CONTENT);
	}  

	/**
	 * Get statistics for a timestamp range.  Sample usage is as follows:
	 * http://localhost:8080/measurements/stats?stat=avg&stat=min&stat=max&metric=precipitation&metric=temperature&fromDateTime=2015-09-01T18:00:00.000Z&toDateTime=2015-09-01T18:00:00.000Z
	 * where stat parameter is an array of all the "stat" params in the url, and "metric" behaves the same way.
	 * 
	 * Jackson does some auto date conversion that will just cause additional formatting.  So dates are strings.

	 * Excample: http://localhost:8080/measurements/stats?stat=avg&stat=min&stat=max&metric=precipitation&metric=temperature&fromDateTime=2015-09-01T15:00:00.000Z&toDateTime=2015-09-01T19:00:00.000Z
	 * @param statArr
	 * @param metricArr
	 * @param fromTime
	 * @param toTime
	 * @return List <StatisticData>
	 */
	@RequestMapping(value="/stats", method=RequestMethod.GET)
	public ResponseEntity getStatistics(@RequestParam(value="stat") String[] statArr,@RequestParam(value="metric") String[] metricArr,
			@RequestParam(value="fromDateTime") String fromTime,@RequestParam(value="toDateTime") String toTime) {
		Date fromDateO  = DateHelper.formatJavaScriptDateString(fromTime);
		Date toDateO  = DateHelper.formatJavaScriptDateString(toTime);
		List <StatisticData> list = dao.getStatistics(statArr, metricArr, fromDateO, toDateO);
		return new ResponseEntity<List <StatisticData>>(list, HttpStatus.OK);
	}  

}
