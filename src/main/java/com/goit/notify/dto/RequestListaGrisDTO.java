package com.goit.notify.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class RequestListaGrisDTO {
	private String mail;
	private String fechaRegistro;
	private String asunto;
	private String estado;
	private String observacion;
	private String idTicket;
}
