package com.goit.notify.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ListarFirmasDTO {
	
	private String estado;
	private String contenido;
	private String link;

}
