package com.goit.notify.dto;

import lombok.Data;

@Data
public class RequestAdjuntoPlantillaDTO {

    private Integer idPlantilla;
    private String nombreArchivo;
    private String rutaLocal;
    private String extension;

}
