package com.jhaps.clientrecords.exceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jhaps.clientrecords.exception.DuplicateDataException;
import com.jhaps.clientrecords.exception.EntityNotFoundException;
import com.jhaps.clientrecords.response.ApiResponse;
import com.jhaps.clientrecords.response.ResponseMessage;
import com.jhaps.clientrecords.util.ApiResponseBuilder;

//LOGGING IS NOT DONE YET.



/*
 * In this class all the Exception are Intercepted
 * 						and proper ResponseEntity are 
 * 						created accordingly.
 * */

@RestControllerAdvice
public class GlobalExceptionHandler {
	/*
	 * ApiResponseBuilder converts args(message, status, body)
 * 							to a ResponseEntity.
 * 						Example : return ResponseEntity.status(status).body(response);*/
	private ApiResponseBuilder apiResponseBuilder;
	
	public GlobalExceptionHandler(ApiResponseBuilder apiResponseBuilder) {
		this.apiResponseBuilder = apiResponseBuilder;
	}

	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Object>> handleGeneralException(Exception e){
		System.out.println(e.getMessage() + e );
		return apiResponseBuilder.buildApiResponse(ResponseMessage.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException e){
		BindingResult bindingResult = e.getBindingResult();
		List<String> errors = bindingResult.getAllErrors()
									.stream()
									.map(DefaultMessageSourceResolvable::getDefaultMessage)
									.collect(Collectors.toList());
		return apiResponseBuilder.buildApiResponse(ResponseMessage.VALIDATION_FAILED, HttpStatus.BAD_REQUEST, errors);
	}
	
	
	@ExceptionHandler(EntityNotFoundException.class)
	public ResponseEntity<ApiResponse<Object>> handleEntityNotFoundException(EntityNotFoundException e){
		return apiResponseBuilder.buildApiResponse(ResponseMessage.NOT_FOUND, HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler(DuplicateDataException.class)
	public ResponseEntity<ApiResponse<Object>> handleDuplicateDataException(DuplicateDataException e){
		return apiResponseBuilder.buildApiResponse(ResponseMessage.DUPLICATE_ENTITY, HttpStatus.CONFLICT);
	}
	
	@ExceptionHandler(DataAccessException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataAccessException(DataAccessException e) {   
        return apiResponseBuilder.buildApiResponse(ResponseMessage.DATA_ACCESS_EXCEPTION ,HttpStatus.INTERNAL_SERVER_ERROR);
    }
	
	
	
	
}//ends class


