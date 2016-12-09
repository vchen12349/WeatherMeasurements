package co.controller;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;

import co.exception.DataAlreadyExistsException;
import co.exception.DataNotFoundException;
import co.exception.InvalidDateException;
import co.exception.InvalidMetricTypeException;
import co.model.ExceptionWrapper;

/**
 * Handles certain exceptions in a custom manner so that we can return back
 * some more useful error messages to the gui.
 * @author chenvic
 *
 */
@ControllerAdvice
public class ExceptionHandlerController { 

	
	@ExceptionHandler(NoHandlerFoundException.class)
//	@ResponseStatus(value=HttpStatus.NOT_FOUND)
//	@ResponseBody
	public ResponseEntity<String> requestHandlingNoHandlerFound(NoHandlerFoundException ex,WebRequest request) {
	    return new ResponseEntity<String>("Invalid URL Request!", HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler({ DataAlreadyExistsException.class})
	protected ResponseEntity<ExceptionWrapper> handleDataAlreadyExistsException(RuntimeException e, WebRequest request) {
		ExceptionWrapper wrap = new ExceptionWrapper();
		wrap.setMessage("Data already exists!");
		wrap.setHttpStatusCode(HttpStatus.BAD_REQUEST.toString());
		return new ResponseEntity<ExceptionWrapper>(wrap,HttpStatus.BAD_REQUEST);
	}

	
	@ExceptionHandler({ InvalidMetricTypeException.class})
	protected ResponseEntity<ExceptionWrapper> handleInvalidMetricType(RuntimeException e, WebRequest request) {
		ExceptionWrapper wrap = new ExceptionWrapper();
		wrap.setMessage("Invalid Metric Type!");
		wrap.setHttpStatusCode(HttpStatus.BAD_REQUEST.toString());
		return new ResponseEntity<ExceptionWrapper>(wrap,HttpStatus.BAD_REQUEST);
	}

	
	@ExceptionHandler({ InvalidDateException.class})
	protected ResponseEntity<ExceptionWrapper> handleInvalidDate(RuntimeException e, WebRequest request) {
		ExceptionWrapper wrap = new ExceptionWrapper();
		wrap.setMessage("Please make sure you have a proper date or datetime format!");
		wrap.setHttpStatusCode(HttpStatus.BAD_REQUEST.toString());
		return new ResponseEntity<ExceptionWrapper>(wrap,HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({ ConversionFailedException.class})
	protected ResponseEntity<ExceptionWrapper> handleInvalidRequest(RuntimeException e, WebRequest request) {
		ExceptionWrapper wrap = new ExceptionWrapper();
		wrap.setMessage("Please make sure you have a proper date or datetime format in your url!");
		wrap.setHttpStatusCode(HttpStatus.BAD_REQUEST.toString());
		return new ResponseEntity<ExceptionWrapper>(wrap,HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler({InvalidFormatException.class})
	protected ResponseEntity<ExceptionWrapper> handleInvalidFormat(RuntimeException e, WebRequest request) {
		ExceptionWrapper wrap = new ExceptionWrapper();
		wrap.setMessage("Please check that you are passing in a valid values in your request!");
		wrap.setHttpStatusCode(HttpStatus.BAD_REQUEST.toString());
		return new ResponseEntity<ExceptionWrapper>(wrap,HttpStatus.BAD_REQUEST);
	}
	//DataNotFoundException

	@ExceptionHandler({DataNotFoundException.class})
	protected ResponseEntity<ExceptionWrapper> handleDataNotFound(RuntimeException e, WebRequest request) {
		ExceptionWrapper wrap = new ExceptionWrapper();
		wrap.setMessage("There are no measurements for those dates!");
		wrap.setHttpStatusCode(HttpStatus.NOT_FOUND.toString());
		return new ResponseEntity<ExceptionWrapper>(wrap,HttpStatus.NOT_FOUND);
	}


}
