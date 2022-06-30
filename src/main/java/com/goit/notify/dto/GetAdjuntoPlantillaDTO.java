package com.goit.notify.dto;

import lombok.Data;

@Data
public class GetAdjuntoPlantillaDTO {

    private Integer idAdjuntoPlantilla;
    private Integer idPlantilla;
    //private NotPlantilla idPlantilla;
    private String nombreArchivo;
    private String rutaLocal;
    private String extension;
}
