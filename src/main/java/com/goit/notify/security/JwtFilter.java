package com.goit.notify.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.goit.helper.ServiciosHelper;
import com.goit.helper.dto.ResponsesAutenticacionDTO;
import com.goit.helper.segurity.JwtUtilHelper;
import com.goit.notify.dto.UsuarioLogin;

import io.jsonwebtoken.Claims;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtilHelper jwtUtil;
    @Autowired
    private ServiciosHelper objServiciosUtil;
    
	@Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException{
	    try {
	        String authorizationHeader = httpServletRequest.getHeader("Authorization");
	        String languaje = httpServletRequest.getHeader("Accept-Language");
	        String canal = httpServletRequest.getHeader("Canal");
	        
	        String[] arrServiciosSinSeguridad = {""};	
	        
	        //String strUserName = null;
	        String strToken=null;

	        if (!(httpServletRequest.getRequestURI()!=null && 
					Arrays.stream(arrServiciosSinSeguridad).anyMatch(StringUtils.upperCase(httpServletRequest.getRequestURI())::equalsIgnoreCase))) {
		        	
		        	 if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
		        		 
		        		 ResponsesAutenticacionDTO objResponsesAutenticacionDTO= objServiciosUtil
		        				 .validarToken(authorizationHeader,languaje, canal);
		        		 
		        		 strToken = authorizationHeader.substring(7);
		        		 Claims clBody=jwtUtil.extractBody(strToken);
						 if (objResponsesAutenticacionDTO.getCode().intValue()==200) {
							 
							UsuarioLogin objUsuarioLogin=new UsuarioLogin();
							objUsuarioLogin.setIdUsuario(clBody.get("idUsuario").toString());
							objUsuarioLogin.setUsuario(clBody.get("usuario").toString());
							objUsuarioLogin.setIdEmpresa(clBody.get("idEmpresa").toString());
				               
					        	UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
					        			objUsuarioLogin,
				        				null,
				    					new ArrayList<>());
				              
				                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

					        }
				     }
	        }  
		        
		} catch (Exception e) {
			e.printStackTrace();
			httpServletResponse.setStatus(Status.UNAUTHORIZED.getStatusCode());
			httpServletResponse.setHeader("msgAutorizadorException", e.getMessage());		}
    
	    filterChain.doFilter(httpServletRequest, httpServletResponse);
	    
    }
}
