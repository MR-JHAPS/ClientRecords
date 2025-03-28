package com.jhaps.clientrecords.exceptionHandler;


import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;

import com.jhaps.clientrecords.enums.ResponseMessage;
import com.jhaps.clientrecords.exception.ClientNotFoundException;
import com.jhaps.clientrecords.exception.DuplicateDataException;
import com.jhaps.clientrecords.exception.ImageNotFoundException;
import com.jhaps.clientrecords.exception.RoleNotFoundException;
import com.jhaps.clientrecords.exception.UserNotFoundException;
import com.jhaps.clientrecords.response.ApiResponseModel;

import lombok.extern.slf4j.Slf4j;

import com.jhaps.clientrecords.response.ApiResponseBuilder;




/*
 * In this class all the Exception are Intercepted
 * 						and proper ResponseEntity are 
 * 						created accordingly.
*/


@RestControllerAdvice
@Slf4j
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
		log.error("General Exception occured ", e);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	
	//THIS IS FOR THE @Validation annotation exceptions.
	/*there may be more than one field validation error so we map each of the validationError*/
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponseModel<Object>> handleArgumentNotValidException(MethodArgumentNotValidException e){
		log.error("Method Argument Not Valid Exception | Probably @Validation Error occured : {}",e.getMessage(), e);
		HashMap<String, String> validationErrors = new HashMap<>();
		BindingResult bindingResult = e.getBindingResult();  //binding result contains field Errors etc. 
		bindingResult.getFieldErrors().forEach(ex ->
		validationErrors.put(ex.getField(), ex.getDefaultMessage())
		);			
		return apiResponseBuilder.buildApiResponse(ResponseMessage.VALIDATION_FAILED, HttpStatus.BAD_REQUEST, validationErrors);
	}

	
	@ExceptionHandler(LockedException.class)
	public ResponseEntity<ApiResponseModel<String>> handleLockedException(LockedException e){
		log.error("InternalAuthenticationServiceException ---> Locked Exception Occured : {} ",e.getMessage(), e);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.LOCKED, HttpStatus.LOCKED);
	}
	
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ApiResponseModel<String>> handleBadCredentialsException(BadCredentialsException e){
		log.error("Bad Credentials Exception Occured : {} ",e.getMessage(), e);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.BAD_CREDENTIALS, HttpStatus.UNAUTHORIZED);
	}
	
	
	@ExceptionHandler(UsernameNotFoundException.class)
	public ResponseEntity<ApiResponseModel<String>> handleUsernameNotFoundException(UsernameNotFoundException e){
		log.error("Username Not Found Exception Occured : {} ",e.getMessage(), e);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.BAD_CREDENTIALS, HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(Unauthorized.class)
	public ResponseEntity<ApiResponseModel<String>> handleUnauthorizedException(Unauthorized e){
		log.error("Access Denied Exception Occured : {} ",e.getMessage(), e);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.ACCESS_DENIED, HttpStatus.UNAUTHORIZED);
	}
	
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ApiResponseModel<String>> handleAccessDeniedException(AccessDeniedException e){
		log.error("Access Denied Exception Occured : {} ",e.getMessage(), e);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.ACCESS_DENIED, HttpStatus.FORBIDDEN);
	}
	
	
	@ExceptionHandler(DuplicateDataException.class)
	public ResponseEntity<ApiResponseModel<String>> handleDuplicateDataException(DuplicateDataException e){
		log.error("Duplicate Data Exception Occured : {} ",e.getMessage(), e);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.DUPLICATE_DATA, HttpStatus.CONFLICT);
	}
	
	
	@ExceptionHandler(ClientNotFoundException.class)
	public ResponseEntity<ApiResponseModel<String>> handleClientNotFoundException(ClientNotFoundException e){
		log.error("Client Not Found Exception Occured : {} ",e.getMessage(), e);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.CLIENT_NOT_FOUND, HttpStatus.NOT_FOUND);
	}
	
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ApiResponseModel<String>> handleUserNotFoundException(UserNotFoundException e){
		log.error("User Not Found Exception Occured : {} ",e.getMessage(), e);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
	}
	
	
	//This is for the case the roleName is not found in the Database.
	@ExceptionHandler(RoleNotFoundException.class)
	public ResponseEntity<ApiResponseModel<String>> handleRoleNotFoundException(RoleNotFoundException e){
		log.error("Role Not Found Exception Occured : {} ",e.getMessage(), e);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.INVALID_ROLE, HttpStatus.BAD_REQUEST);
	}
	
	
	//This is for the case the Image is not found in the Database.
	@ExceptionHandler(ImageNotFoundException.class)
	public ResponseEntity<ApiResponseModel<String>> handleImageNotFoundException(ImageNotFoundException e){
		log.error("Image Not Found Exception Occured : {} ",e.getMessage(), e);
		return apiResponseBuilder.buildApiResponse(ResponseMessage.INVALID_IMAGE, HttpStatus.NOT_FOUND);
	}
	
	
}//ends class


