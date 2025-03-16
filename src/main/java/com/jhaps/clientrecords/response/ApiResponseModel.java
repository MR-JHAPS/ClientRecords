package com.jhaps.clientrecords.response;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import com.jhaps.clientrecords.enums.ResponseMessage;


/*THIS CLASS IS TO
 * Create An API RESPONSE as [{message : 'message'},
 * 							  {status : 'OK'},
 * 							  {data : could be any type(List/Object/String)} ]
*/
public class ApiResponseModel<T> {
	//Attributes
	private LocalDateTime timestamp;
	private String message;
	private int status;
	private T data;
	
	
	//Constructor
	public ApiResponseModel(ResponseMessage responseMessage, HttpStatusCode status, T data) {
		super();
		this.timestamp = LocalDateTime.now();//timestamp.
		this.message = responseMessage.getMessage();
		this.status = status.value();
		this.data = data;
	}
	
	//constructor with 2 parameters:
	public ApiResponseModel(ResponseMessage responseMessage, HttpStatusCode status) {
		this.timestamp = LocalDateTime.now(); //timestamp.
		this.message = responseMessage.getMessage();
		this.status = status.value();
		this.data = null;
	}
	
	
	//Getters
	public String getMessage() {
		return message;
	}
	
	public int getStatus() {
		return status;
	}
	
	public T getData() {
		return this.data;
	}

	public String getTimestamp() {
		return timestamp.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME); //timestamp in string format.
	}

	
	
	
	
	
	
	
	
	
	
}//ends class
