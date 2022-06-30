package com.goit.notify.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiErrorResponse {

	private Integer code;
	private boolean success;
	private String message;
	private Object errorData;

	public ApiErrorResponse() {
		super();
	}

	public ApiErrorResponse(Integer code, boolean success, String message, Object errorData) {
		super();
		this.code = code;
		this.success = success;
		this.message = message;
		this.errorData = errorData;
	}
}
