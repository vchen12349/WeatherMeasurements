package co.service;

import java.util.Date;
import java.util.List;

import co.exception.DataAlreadyExistsException;
import co.exception.DataNotFoundException;
import co.model.Measurement;
import co.model.StatisticData;

/**
 * Interface that classes should implement if they want to create a new DAO that handles measurement data flow
 * @author chenvic
 *
 */
public interface IMeasureData {
	List <StatisticData > getStatistics(String [] statArr, String [] metricArray, Date toTime, Date fromTime);
	Measurement addMeasurement(Measurement m) throws DataAlreadyExistsException;
	Measurement replaceMeasurement(Measurement m) throws DataNotFoundException;
	Measurement updateMeasurement(Measurement m) throws DataNotFoundException;
	void deleteMeasurement(Date d) throws DataNotFoundException;
	Measurement getMeasurement(Date d) throws DataNotFoundException;
	List <Measurement> getMeasurementsForTheDay(Date d) throws DataNotFoundException;
}
