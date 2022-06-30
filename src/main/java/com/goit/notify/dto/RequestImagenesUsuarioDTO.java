package com.goit.notify.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
public class RequestImagenesUsuarioDTO {

	@JsonInclude(Include.NON_EMPTY)
    private String idEmpresa;
	@JsonInclude(Include.NON_EMPTY)
    private String idUsuario;
	@JsonInclude(Include.NON_EMPTY)
    private String nombre;
	@JsonInclude(Include.NON_EMPTY)
    private String rutaLocal;
	@JsonInclude(Include.NON_EMPTY)
    private String urlPublica;
	@JsonInclude(Include.NON_EMPTY)
    private String esPublica;
	@JsonIgnore
	private String estado="S";

}
