package com.goit.notify.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
public class CamposPersonalizadosDTO {
	@JsonInclude(Include.NON_EMPTY)
	public String campo;
	@JsonInclude(Include.NON_EMPTY)
	public String valor;
}
