package com.goit.notify.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeneralAdjuntoPlantillaDTO {

    private Integer idAdjuntoPlantilla;
    private Integer idPlantilla;
    //private NotPlantilla idPlantilla;
    private String nombreArchivo;
    private String rutaLocal;
    private String extension;
}
