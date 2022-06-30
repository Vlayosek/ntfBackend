package com.goit.notify.security;


import java.util.Date;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.goit.notify.exceptions.BOException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class JwtUtil {
		
    public String extractSubject(String token) throws BOException {
       return extractClaim(token, Claims::getSubject);
    }
   
    public Date extractExpiration(String token) throws BOException {
       return extractClaim(token, Claims::getExpiration); 
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws BOException {
        final Claims claims = extractBody(token);
        return claimsResolver.apply(claims);
    }
    public Claims extractBody(String token) throws BOException {
    	Claims claims = null ;
    	 try {
    		//Firma JWT
  			String jwtSecret="goit.me";
        	claims=Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody();
    	 }catch(ExpiredJwtException ex) {
     		throw new BOException("con.warn.tokenCaducado");
     	}catch(Exception e){
    		 throw new BOException("con.warn.tokenInvalido");
    	 }
    	 
    	 return claims;
    }
    
    public Boolean isTokenExpired(String token) throws BOException {
        return extractExpiration(token).before(new Date());
    }

    public String generateToken(Map<String, Object> header, String subject,Date datFechaExpiracion, String jwtSecret) {

        return Jwts.builder().setHeader(header).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(datFechaExpiracion)
                .signWith(SignatureAlgorithm.HS256, jwtSecret).compact();
    }
    
    public Boolean validateToken(String token, UserDetails userDetails) throws BOException {
    	
        final String username = extractSubject(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        
    }
}