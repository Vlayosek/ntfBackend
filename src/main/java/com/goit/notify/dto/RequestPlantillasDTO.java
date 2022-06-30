package com.goit.notify.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class RequestPlantillasDTO {

	private String nombre;
	private Integer idPlantilla;
	private String descripcion;
	private String tipo;
	private String estado;
	private String esPublica;
	private String fechaCreacion;
	private String contenidoJson;
}
