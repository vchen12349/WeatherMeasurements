package co.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;


/**
 * Not used yet.  But this class allows hooks into the spring mvc to run methods pre and post 
 * method run.
 * @author chenvic
 *
 */
@Component
public class MeasurementDateTimeInterceptor implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest req, HttpServletResponse res, Object o, Exception e)
			throws Exception {
		System.out.println("after completion");

	}

	@Override
	public void postHandle(HttpServletRequest req, HttpServletResponse res, Object o, ModelAndView e)
			throws Exception {
		System.out.println("post handle");
	}

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse res, Object obj) throws Exception {
		System.out.println("pre-handle");
		return true;
	}

}
