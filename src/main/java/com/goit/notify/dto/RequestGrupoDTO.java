package com.goit.notify.dto;

import java.util.List;

import lombok.Data;

@Data
public class RequestGrupoDTO {
	private Integer idGrupo;
	private String nombre;
	private String descripcion;
	private String tipo;
	private List<String> camposPersonalizados;
}
