package com.goit.notify.dto;

import java.io.Serializable;

import javax.ws.rs.core.Response.Status;

import lombok.Data;

@Data
public class ResponseOk implements Serializable {

	private static final long serialVersionUID = 1L;

	private int code;
	private boolean success;
	private String message;
	private Object data;
	public static String messageDataFound = "Data found";
	public static String messageDataNotFound = "Data not found";
	
	
	public ResponseOk(String message, Object data) {
		super();
		this.code = Status.OK.getStatusCode();
		this.success = true;
		this.message = message;
		this.data = data;
	}
}
