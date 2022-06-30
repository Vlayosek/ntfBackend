package com.goit.notify.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
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
@Table(name = "NOT_LISTA_NEGRA")
public class NotListaNegra implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@Size(max = 100)
	@NotNull
    @Column(name = "ID_LISTA_NEGRA")
    private String idListaNegra;

	@Size(max = 100)
	@NotNull
	@Column(name = "ID_TICKET")
	private String idTicket;
	
	@Size(max=100)
	@Column(name = "MAIL")
	private String mail;
	
	@Column(name = "FECHA_REGISTRO")
	private String fechaRegistro;
	
	@Size(max=2000)
	@Column(name = "ASUNTO")
	private String asunto;
	
	@Size(max=1)
	@Column(name = "ESTADO")
	private String estado;
	
	@Size(max=4000)
	@Column(name = "OBSERVACION")
	private String observacion;
	
	@Size(max=4000)
	@Column(name = "CONTENIDO_REBOTE")
	private String contenidoRebote;
	
	@Column(name = "FECHA_CREA")
	private String fechaCrea;
	
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
}
