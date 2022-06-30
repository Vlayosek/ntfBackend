package com.goit.notify.model;

import java.io.Serializable;

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
@Table(name = "NOT_CAMPANAS")
public class NotCampanas implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Size(max=100)
	@Column(name = "ID_CAMPANA")
	private Integer id_campana;
	
	@Size(max=100)
	@Column(name = "ID_USUARIO")
	private String idUsuario;
	
	@Size(max=100)
	@Column(name = "ID_EMPRESA")
	private String idEmpresa;
	
	@Size(max=400)
	@Column(name = "CAMPANA")
	private String campana;
	
	@Size(max=4000)
	@Column(name = "DESCRIPCION")
	private String descripcion;
	
	@Size(max=1)
	@Column(name = "ESTADO")
	private String estado;
	
	@Column(name = "FECHA_CREA")
	private String fechaCrea;
	
	@Column(name = "FECHA_ACTUALIZA")
	private String fechaActualiza;
	
	@Column(name = "FECHA_INACTIVA")
	private String fechaInactiva;

	@Column(name = "USUARIO_CREA")
	private String usuarioCrea;
	
	@Column(name = "USUARIO_ACTUALIZA")
	private String usuarioActualiza;
	
	@Column(name = "USUARIO_INACTIVA")
	private String usuarioInactiva;
}
