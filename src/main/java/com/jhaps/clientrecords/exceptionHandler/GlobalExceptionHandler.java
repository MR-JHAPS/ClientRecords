package com.jhaps.clientrecords.exceptionHandler;

import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jhaps.clientrecords.exception.ClientNotFoundException;
import com.jhaps.clientrecords.exception.DuplicateDataException;
import com.jhaps.clientrecords.exception.UserNotFoundException;
import com.jhaps.clientrecords.response.ApiResponseModel;
import com.jhaps.clientrecords.response.ApiResponseBuilder;
import com.jhaps.clientrecords.response.ResponseMessage;

//LOGGING IS NOT DONE YET.



/*
 * In this class all the Exception are Intercepted
 * 						and proper ResponseEntity are 
 * 						created accordingly.
*/

@RestControllerAdvice
public class GlobalExceptionHandler {
	/*
	 * ApiResponseBuilder creates a " ResponseEntity<ApiResponseModel<T>> ".
	 * 		Example : return ResponseEntity.status(status).body(response);
	*/
	private ApiResponseBuilder apiResponseBuilder;
	
	public GlobalExceptionHandler(ApiResponseBuilder apiResponseBuilder) {
		this.apiResponseBuilder = apiResponseBuilder;
	}

	
	
	
//METHODS:---------------------------------------------------------------------------------------------------------------------------------	
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponseModel<Object>> handleGeneralException(Exception e){
		System.out.println(e.getMessage() + e );
		//logger
		return apiResponseBuilder.buildApiResponse(ResponseMessage.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	//THIS IS FOR THE @Validation annotation exceptions.
	/*there may be more than one field validation error so we map each of the validationError*/
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponseModel<Object>> handleArgumentNotValidException(MethodArgumentNotValidException e){
		HashMap<String, String> validationErrors = new HashMap<>();
		BindingResult bindingResult = e.getBindingResult();  //binding result contains field Errors etc. 
		bindingResult.getFieldErrors().forEach(ex ->
		validationErrors.put(ex.getField(), ex.getDefaultMessage())
		);			
		return apiResponseBuilder.buildApiResponse(ResponseMessage.VALIDATION_FAILED, HttpStatus.BAD_REQUEST, validationErrors);
	}
		
	
	@ExceptionHandler(DuplicateDataException.class)
	public ResponseEntity<ApiResponseModel<String>> handleDuplicateDataException(DuplicateDataException e){
		//logger here
		return apiResponseBuilder.buildApiResponse(ResponseMessage.DUPLICATE_DATA, HttpStatus.CONFLICT);
	}
	
	
	@ExceptionHandler(ClientNotFoundException.class)
	public ResponseEntity<ApiResponseModel<String>> handleClientNotFoundException(ClientNotFoundException e){
		//logger here
		return apiResponseBuilder.buildApiResponse(ResponseMessage.CLIENT_NOT_FOUND, HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ApiResponseModel<String>> handleUserNotFoundException(UserNotFoundException e){
		//logger here
		return apiResponseBuilder.buildApiResponse(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
	}
	
	
	
	
}//ends class


