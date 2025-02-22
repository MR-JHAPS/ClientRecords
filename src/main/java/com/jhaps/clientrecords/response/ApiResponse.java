package com.jhaps.clientrecords.response;

import org.springframework.http.HttpStatus;


/*THIS CLASS IS TO
 * Create An API RESPONSE as [{message : 'message'},
 * 							  {status : 'OK'},
 * 							  {data : could be any type(List/Object/String)} ]
*/
public class ApiResponse<T> {
	//Attributes
	private String message;
	private HttpStatus status;
	private T data;
	
	//Constructor
	public ApiResponse(ResponseMessage responseMessage, HttpStatus status, T data) {
		super();
		this.message = responseMessage.getMesage();
		this.status = status;
		this.data = data;
	}
	
	//constructor with 2 parameters:
	public ApiResponse(ResponseMessage responseMessage, HttpStatus status) {
		this.message = responseMessage.getMesage();
		this.status = status;
		this.data = null;
	}
	
	
	//Getters
	public String getMessage() {
		return message;
	}
	
	public HttpStatus getStatus() {
		return status;
	}
	
	public T getData() {
		return this.data;
	}
	
	
	
	
	
	
	
	
	
	
}//ends class
