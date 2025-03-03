package com.jhaps.clientrecords.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;


/*This class helps build ResponseEntity*/

@Component
public class ApiResponseBuilder {

	//Constructor Without Data:
	public <T> ResponseEntity<ApiResponseModel<T>> buildApiResponse(ResponseMessage responseMessage, HttpStatusCode status){
		ApiResponseModel<T> response = new ApiResponseModel<>(responseMessage, status);
		return ResponseEntity.status(status).body(response);	
	}
	
	
	//Constructor With Data:
	public <T> ResponseEntity<ApiResponseModel<T>> buildApiResponse(ResponseMessage responseMessage, HttpStatus status, T body){
		ApiResponseModel<T> response = new ApiResponseModel<>(responseMessage, status, body);
		return ResponseEntity.status(status).body(response);	
	}
	
	
	
	
	
	
}//ends class
