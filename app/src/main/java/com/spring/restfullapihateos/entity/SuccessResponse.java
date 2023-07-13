package com.spring.restfullapihateos.entity;

public class SuccessResponse {
	private String message;
	private int status;
	private Object object;
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Object getObject() {
		return object;
	}
	public void setObject(Object object) {
		this.object = object;
	}
	@Override
	public String toString() {
		return "SuccessResponse [message=" + message + ", status=" + status + ", object=" + object + "]";
	}
	public SuccessResponse(String message, int status, Object object) {
		super();
		this.message = message;
		this.status = status;
		this.object = object;
	}
	


}
