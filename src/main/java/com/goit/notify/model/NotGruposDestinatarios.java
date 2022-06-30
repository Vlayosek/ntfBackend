package com.goit.notify.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "NOT_GRUPOS_DESTINATARIOS")
public class NotGruposDestinatarios implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@NotNull
    @Column(name = "ID_GRUPO_DESTINATARIO")
    private Integer idGrupoDestinatario;
	
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_GRUPO", referencedColumnName = "ID_GRUPO")
	private NotGrupos notGrupos;
	
	@ManyToOne(optional = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DESTINATARIO", referencedColumnName = "ID_DESTINATARIO")
	private NotDestinatarios notDestinatarios;
	
	@Size(max=1)
	@Column(name = "ESTADO")
	private String estado;
}
