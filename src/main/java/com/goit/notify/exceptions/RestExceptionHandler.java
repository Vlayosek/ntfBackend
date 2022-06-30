package com.goit.notify.exceptions;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Clase que permite manejar excepciones en toda la aplicación, no solo en un controlador individual.
 * Puede considerarlo como un interceptor de excepciones generadas por métodos anotados 
 * con RequestMapping o uno de los accesos directos
 * 
 * @author Raúl Yugcha
 *
 */
@ControllerAdvice
public class RestExceptionHandler {
	
	private static final Logger logger = LogManager.getLogger(RestExceptionHandler.class.getName());
	
   @ExceptionHandler(value=UnauthorizedException.class)
   public ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException e) {
		return new ResponseEntity<Object>(
				new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), false, e.getMessage(), 
						e.getData()!=null?new Object[] {e.getData()}:new Object[]{}
					), HttpStatus.BAD_REQUEST);
   }
   
   @ExceptionHandler(value=RestClientException.class)
   public ResponseEntity<Object> handleRestClientException(RestClientException e) {
		return new ResponseEntity<Object>(
				new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), false, e.getMessage(), 
						e.getData()!=null?new Object[] {e.getData()}:new Object[]{}
					), HttpStatus.BAD_REQUEST);
   }
   
	@ExceptionHandler(value=CustomExceptionHandler.class)
	public final ResponseEntity<Object> handleCustomExceptions(CustomExceptionHandler e) {
		return new ResponseEntity<Object>(
				new ApiErrorResponse(HttpStatus.BAD_REQUEST.value(), false, 
						e.getMessage()!=null?e.getMessage():HttpStatus.BAD_REQUEST.name(), 
						e.getData()!=null?new Object[] {e.getData()}:new Object[]{}
					), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value= Exception.class)
	public ResponseEntity<Object> handleAllExceptions(final Exception ex) {
		logger.error(ExceptionUtils.getStackTrace(ex));
		
		return new ResponseEntity<Object>(
				new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), false, 
						ex.getMessage(),
						new Object[] {ExceptionUtils.getStackTrace(ex)}
				), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
