package com.goit.notify.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RequestListaGrisTlfDTO {
	private String telefono;
	private String fechaRegistro;
	private String asunto;
	private String estado;
	private String observacion;
	private String canal;
}
