package com.goit.notify.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "NOT_DESTINATARIOS")
public class NotDestinatarios implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Size(max=100)
	@NotNull
    @Column(name = "ID_DESTINATARIO")
    private String idDestinatario;
	
	@Size(max=50)
	@Column(name = "CORREO")
	private String correo;
	
	@Size(max=100)
	@Column(name = "PRIMER_NOMBRE")
	private String primerNombre;
	
	@Size(max=100)
	@Column(name = "PRIMER_APELLIDO")
	private String primerApellido;
	
	@Size(max=50)
	@Column(name = "GENERO")
	private String genero;
	
	@Size(max=50)
	@Column(name = "FECHA_NACIMIENTO")
	private String fechaNacimiento;
	
	@Size(max=50)
	@Column(name = "PAIS")
	private String pais;
	
	@Size(max=100)
	@Column(name = "ID_ENTIDAD_REDIS")
	private String 	idEntidadRedis;
	
	@Size(max=2)
	@Column(name = "ESTADO")
	private String estado;
	
	@Column(name = "FECHA_CREA")
	private Date fechaCreacion;
	
	@Column(name = "FECHA_ACTUALIZA")
	private Date fechActualiza;
	
	@Column(name = "FECHA_INACTIVA")
	private Date fechaInactiva;
	
	@Size(max=45)
	@Column(name = "USUARIO_CREA")
	private String usuarioCreacion;
	
	@Size(max=45)
	@Column(name = "USUARIO_ACTUALIZA")
	private String usuarioActualiza;
	
	@Size(max=45)
	@Column(name = "USUARIO_INACTIVA")
	private String usuarioInactiva;
	
	@Column(name = "ID_GRUPO_DESTINATARIO")
	private Integer idGrupoDestinatario;
	
	@Size(max=100)
	@Column(name = "ID_USUARIO")
	private String idUsuario;
	
	@Size(max=100)
	@Column(name = "ID_EMPRESA")
	private String idEmpresa;
	
	
}
