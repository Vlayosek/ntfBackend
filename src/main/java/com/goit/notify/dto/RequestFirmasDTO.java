package com.goit.notify.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RequestFirmasDTO {
	
	private String contenido;
	private String link;
	private String esPublica;
}
