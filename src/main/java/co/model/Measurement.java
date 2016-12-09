package co.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.ResourceSupport;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * A measurement is a holder class of a List of Metrics.  Each metric containing nameOfMetric to Value assocation.
 * So you can have a list of metrics: ["humidity",50], ["dewpoint", 6] with an associated timestamp.
 * This will provide the ability to send Metrics in bulk as well as associated a timestamp for each.  
 * So if the mobile device has periodic thread cycles to batch persist all Metrics from all Instruments at a certain time  
 * this object can be used. If only one metric needs to be persisted or updated, then the List will only contain one metric. 
 * This can be thought of an object oriented representation of a database row
 * containing | timestamp| temperature | dewPoint | precipitation | etc.  
 * 
 * Example usage in a list: 
 * Measurement 1
 * 	- Date 
 * 		- Metric (temp:100)
 * 		- Metric (dew: 200)
 * 		- Metric (prec: 300)
 * 
 * Measurement 2
 * 	- Date
 * 		-Metric (temp;100)
 * 		-Metric (dew:200)
 * 		-Metric (prec:300)
 * 
 * When we use it in our stub database, it essentially just flattens this structure into:
 * | timestamp                  | temperature | dewPoint | precipitation |
 * | "2015-09-01T16:00:00.000Z" | 27.1        | 16.7     | 0             |
 * @author chenvic
 *
 */
public class Measurement extends ResourceSupport{



	@JsonInclude(Include.NON_NULL)
	private String dateMeasured;
	
	private List <Metric> metricsList = new ArrayList();

	public List <Metric> getMetricsList() {
		return metricsList;
	}

	public void setMetricsList(List <Metric> metricsList) {
		this.metricsList = metricsList;
	}

	public String getDateMeasured() {
		return dateMeasured;
	}

	public void setDateMeasured(String dateMeasuredString) {
		this.dateMeasured = dateMeasuredString;
	}
	@Override
	public String toString() {
		return "Measurement [ dateMeasured=" + dateMeasured
				+ ", metricsList=" + metricsList + "]";
	}
}
