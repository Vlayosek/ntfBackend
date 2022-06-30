package com.goit.notify.exceptions;

import java.io.IOException;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.goit.notify.util.ClassTypeAdapterUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Autowired
	private Gson gson;
	
	public CustomAuthenticationEntryPoint() {
		gson = new GsonBuilder().registerTypeAdapter(Class.class, new ClassTypeAdapterUtil()).create();
	}
	
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException  {
		
		Integer intErrorCode=response.getHeader("statusCodeAutorizadorException") != null
				? Integer.parseInt(response.getHeader("statusCodeAutorizadorException"))
				: HttpStatus.UNAUTHORIZED.value();
        String strMensaje=response.getHeader("msgAutorizadorException") != null
				? new String(response.getHeader("msgAutorizadorException").getBytes(),StandardCharsets.UTF_8)
				: authException.getMessage();
        
		String jsonObject = gson.toJson(
				new ApiErrorResponse(
						intErrorCode,
						false, 
						strMensaje,
						new Object[] {}),
				ApiErrorResponse.class);
		
		response.setContentType("application/json");
		byte[] utf8JsonString = jsonObject.getBytes("UTF8");
		response.getOutputStream().write(utf8JsonString, 0, utf8JsonString.length);
	}
}
