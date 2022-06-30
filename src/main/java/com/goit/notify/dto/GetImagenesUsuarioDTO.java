package com.goit.notify.dto;

import lombok.Data;

@Data
public class GetImagenesUsuarioDTO {

	private Integer idImagenUsuario;
    private String idEmpresa;
    private String idUsuario;
    private String nombre;
    private String rutaLocal;
    private String urlPublica;
    private String esPublica;
    private String estado;
}
