package com.jhaps.clientrecords.response;

import org.springframework.http.HttpStatusCode;


/*THIS CLASS IS TO
 * Create An API RESPONSE as [{message : 'message'},
 * 							  {status : 'OK'},
 * 							  {data : could be any type(List/Object/String)} ]
*/
public class ApiResponseModel<T> {
	//Attributes
	private String message;
	private int status;
	private T data;
	
	//Constructor
	public ApiResponseModel(ResponseMessage responseMessage, HttpStatusCode status, T data) {
		super();
		this.message = responseMessage.getMessage();
		this.status = status.value();
		this.data = data;
	}
	
	//constructor with 2 parameters:
	public ApiResponseModel(ResponseMessage responseMessage, HttpStatusCode status) {
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
	
	
	
	
	
	
	
	
	
	
}//ends class
