package co.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import co.interceptor.MeasurementDateTimeInterceptor;

/**
 * Register the interceptor.  This really doesn't do anything for now.
 * @author chenvic
 *
 */
@Configuration
public class AppConfig extends WebMvcConfigurerAdapter {

	@Autowired
	MeasurementDateTimeInterceptor dateTimeInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(dateTimeInterceptor);
    }
}
