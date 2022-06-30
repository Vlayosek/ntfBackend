package com.goit.notify.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
@ComponentScan(basePackages = {"com.goit"})
public class WebConfig implements WebMvcConfigurer{

    @Autowired
    private Environment env;
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
		String strCorsOrigins = env.getProperty("cors.origins");
    	String strCorsHeaders = env.getProperty("cors.headers");
    	String strCorsMethods = env.getProperty("cors.methods");
        registry.addMapping("/**")
        	.allowedOrigins(strCorsOrigins)
        	.allowedHeaders(strCorsHeaders)
        	.allowedMethods(strCorsMethods);
    }
    
}
