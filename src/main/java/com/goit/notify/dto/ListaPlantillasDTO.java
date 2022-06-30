package com.goit.notify.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ListaPlantillasDTO {
	private String nombre;
	private String descripcion;
	private String tipo;
	private String estado;
	private String esPublica;
	private String fechaCreacion;
}
