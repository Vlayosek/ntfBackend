package com.goit.notify.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
public class RequestDestinatariosDTO {
	@JsonInclude(Include.NON_EMPTY)
	private String primerNombre;
	@JsonInclude(Include.NON_EMPTY)
	private String primerApellido;
	@JsonInclude(Include.NON_EMPTY)
	private String correo;
	@JsonInclude(Include.NON_EMPTY)
	private String genero;
	@JsonInclude(Include.NON_EMPTY)
	private String fechaNacimiento;
	@JsonInclude(Include.NON_EMPTY)
	private String pais;
	@JsonInclude(Include.NON_EMPTY)
	private String error;
	@JsonIgnore
	private String state="S";
	@JsonInclude(Include.NON_EMPTY)
	private List<CamposPersonalizadosDTO> camposPersonalizados;
}
