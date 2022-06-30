package com.goit.notify.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.goit.notify.exceptions.CustomAuthenticationEntryPoint;

@SuppressWarnings("deprecation")
@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurity extends WebSecurityConfigurerAdapter {

	@Autowired
    private JwtFilter jwtFilter;
	@Autowired
	private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
    	// Deshabilitar Falsificación de solicitudes en sitios cruzados (CSRF - Cross‐site Request Forgery) 
    	http.csrf().disable().
				// Permitir a todos los usuarios acceder a las páginas que comienza con /
				authorizeRequests().antMatchers("/").permitAll().
				// Cualquier solicitud a la aplicación debe ser autenticado
				anyRequest().authenticated().and().
				// Un solo punto de entrada para autenticar
				exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint).and().
				// No se creará ni utilizará ninguna sesión
				sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		// Agregar un filtro para validar los tokens de cada request
    	http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        
    }
}
