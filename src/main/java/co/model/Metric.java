package co.model;


/**
 * This class represents a Metric type to Value paring.
 * Example:  temperature:100.0
 * @author chenvic
 *
 */
public class Metric {
	@Override
	public String toString() {
		return "Metric [value=" + value + ", name=" + name + "]";
	}
	private Float value;  //.example 100
	private String name; //example temperature
	
	public Float getValue() {
		return value;
	}
	public void setValue(Float value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}
