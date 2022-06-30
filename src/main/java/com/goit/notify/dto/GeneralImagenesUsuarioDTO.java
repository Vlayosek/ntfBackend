package com.goit.notify.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GeneralImagenesUsuarioDTO {

	private Integer idImagenUsuario;
    private String idEmpresa;
    private String idUsuario;
    private String nombre;
    private String rutaLocal;
    private String urlPublica;
    private String esPublica;
    private String estado;
}
