package co.service;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import co.exception.DataAlreadyExistsException;
import co.exception.DataNotFoundException;
import co.model.Measurement;
import co.model.StatisticData;
import co.tmpdb.StubDatabase;

/**
 * Service injected into MeasurementController that handles all data flow regarding measurements.
 * @author chenvic
 *
 */
@Service
public class MeasurementDAO implements IMeasureData{

	@Override
	public Measurement addMeasurement(Measurement m) throws DataAlreadyExistsException{
		return StubDatabase.INSTANCE.addMeasurement(m);
	}

	@Override
	public Measurement getMeasurement(Date d) throws DataNotFoundException{
		return StubDatabase.INSTANCE.getMeasurement(d);
	}

	@Override
	public List<Measurement> getMeasurementsForTheDay(Date d) throws DataNotFoundException {
		return StubDatabase.INSTANCE.getMeasurementsForTheDay(d);
	}

	@Override
	public Measurement replaceMeasurement(Measurement m) throws DataNotFoundException{
		return StubDatabase.INSTANCE.replaceMeasurement(m);
	}

	@Override
	public Measurement updateMeasurement(Measurement m) throws DataNotFoundException {
		return StubDatabase.INSTANCE.updateMeasurement(m);
	}

	@Override
	public void deleteMeasurement(Date d) throws DataNotFoundException {
		StubDatabase.INSTANCE.deleteMeasurement(d);
	}

	@Override
	public List <StatisticData> getStatistics(String[] statArr, String[] metricArr, Date fromTime, Date toTime) {
		return StubDatabase.INSTANCE.getStatistics(statArr, metricArr, fromTime, toTime);
	}

	
}
