package co.model;

/**
 * Holder of data for /stat calls. To be parsed as JSON to return to GUI
 * Similar to Metric, but it is used primarily for the GUI .. so it includes"stat" which is a function
 * 
 * @author chenvic
 *
 */
public class StatisticData {

	private String metric;  //example temperature
	private String stat;  //example "avg"
	private Float value; //example 100
	
	public String getMetric() {
		return metric;
	}

	public void setMetric(String metric) {
		this.metric = metric;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}
	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return "StatisticData [metric=" + metric + ", stat=" + stat + ", value=" + value + "]";
	}

}
