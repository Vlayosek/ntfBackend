package com.goit.notify.model;

import java.io.Serializable;
import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "NOT_DETALLES_PLANTILLA")
public class NotDetallesPlantilla implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Size(max=100)
	@Column(name = "ID_DETALLE_PLANTILLA")
	private Integer id_detalle_plantilla;
	
	@OneToOne(optional = false)
	@JoinColumn(name = "ID_PLANTILLA", referencedColumnName = "ID_PLANTILLA")
    private NotPlantilla notPlantilla;
	
//	@Column(name = "ID_PLANTILLA")
//	private Integer idPlantilla;
	
	@Column(name = "ID_FIRMA")
	private Integer idFirma;
	
	@Size(max=1)
	@Column(name = "ESTADO")
	private String estado;
	
	@Column(name = "FECHA_CREA")
	private Date fechaCrea;
	
	@Column(name = "FECHA_ACTUALIZA")
	private Date fechaActualiza;
	
	@Column(name = "FECHA_INACTIVA")
	private String fechaInactiva;

	@Column(name = "USUARIO_CREA")
	private String usuarioCrea;
	
	@Column(name = "USUARIO_ACTUALIZA")
	private String usuarioActualiza;
	
	@Column(name = "USUARIO_INACTIVA")
	private String usuarioInactiva;
	
	@Size(max=500)
	@Column(name = "ASUNTO")
	private String asunto;
	
	@Size(max=400)
	@Column(name = "CABECERA")
	private String cabecera;
	
	@Size(max=4000)
	@Column(name = "CONTENIDO")
	private String contenido;
	
	@Size(max=400)
	@Column(name = "PIE")
	private String pie;
	
	@Size(max=100)
	@Column(name = "MAIL_REMITENTE")
	private String mail_remitente;
	
	@Size(max=100)
	@Column(name ="NOMBRE_REMITENTE")
	private String nombre_remitente;

}
