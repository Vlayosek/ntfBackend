package com.goit.notify.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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
@Table(name = "NOT_PLANTILLAS")
public class NotPlantilla implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PLANTILLA")
    private Integer idPlantilla;
		
	@Size(max=450)
	@Column(name = "NOMBRE")
	private String nombre;
	
	@Size(max=4000)
	@Column(name = "DESCRIPCION")
	private String descripcion;
	
	@Size(max=1)
	@Column(name = "TIPO")
	private String tipo;
	
	@Size(max=1)
	@Column(name = "ESTADO")
	private String estado;
	
	@Column(name = "FECHA_CREA")
	private String fechaCreacion;
	
	@Column(name = "FECHA_ACTUALIZA")
	private String fechActualiza;
	
	@Column(name = "FECHA_INACTIVA")
	private String fechaInactiva;
	
	@Size(max=45)
	@Column(name = "USUARIO_CREA")
	private String usuarioCreacion;
	
	@Size(max=45)
	@Column(name = "USUARIO_ACTUALIZA")
	private String usuarioActualiza;
	
	@Size(max=45)
	@Column(name = "USUARIO_INACTIVA")
	private String usuarioInactiva;
	
	@Size(max=100)
	@Column(name = "ID_USUARIO")
	private String idUsuario;
	
	@Size(max=100)
	@Column(name = "ID_EMPRESA")
	private String idEmpresa;
	
	@Size(max=1)
	@Column(name = "ES_PUBLICA")
	private String esPublica;
	
	@Size(max=4000)
	@Column(name = "CONTENIDO_JSON")
	private String contenidoJson;
	
}
