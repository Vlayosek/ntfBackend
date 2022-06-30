package com.goit.notify.dto;

//import com.goit.notify.model.NotPlantilla;

import lombok.Data;

@Data
public class RequestDetallePlantillaDTO {
	private Integer idPlantilla;
	//private NotPlantilla notPlantilla;
	private String asunto;
	private String cabecera;
	private String contenido;
	private String pie;
	private String mailRemitente;
	private String nombreRemitente;
	private String idFirma;
	private String tipo;
}
