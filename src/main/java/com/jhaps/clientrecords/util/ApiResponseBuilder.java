package com.jhaps.clientrecords.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.jhaps.clientrecords.response.ApiResponse;
import com.jhaps.clientrecords.response.ResponseMessage;


/*This class helps build ResponseEntity*/

@Component
public class ApiResponseBuilder {

	//Constructor Without Data:
	public <T> ResponseEntity<ApiResponse<T>> buildApiResponse(ResponseMessage responseMessage, HttpStatus status){
		ApiResponse<T> response = new ApiResponse<>(responseMessage, status);
		return ResponseEntity.status(status).body(response);	
	}
	
	
	//Constructor With Data:
	public <T> ResponseEntity<ApiResponse<T>> buildApiResponse(ResponseMessage responseMessage, HttpStatus status, T body){
		ApiResponse<T> response = new ApiResponse<>(responseMessage, status, body);
		return ResponseEntity.status(status).body(response);	
	}
	
	
	
	
	
	
}//ends class
