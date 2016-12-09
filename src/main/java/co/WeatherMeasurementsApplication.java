package co;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import co.tmpdb.StubDatabase;

/**
 * Start up spring boot!
 * @author chenvic
 *
 */

@Configuration
@EnableAutoConfiguration
@ComponentScan({"co.controller","co.service","co.interceptor", "co.config", "co.service"})
public class WeatherMeasurementsApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherMeasurementsApplication.class, args);
	}
}
