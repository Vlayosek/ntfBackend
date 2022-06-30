package com.goit.notify.dao;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.springframework.stereotype.Service;

import com.goit.notify.exceptions.BOException;
import com.goit.notify.model.NotGrupos;

import lombok.NonNull;

@Service
public class NotGruposDAO extends BaseDAO<NotGrupos,Integer>{

	protected NotGruposDAO() {
		super(NotGrupos.class);
	}
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public void persist(NotGrupos t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(NotGrupos t) throws PersistenceException {
		super.update(t);
	}

	@Override
	public Optional<NotGrupos> find(@NonNull Integer id) {
		return super.find(id);
	}
	
	/**
	 * 
	 * Consulta en la tabla SecParametro y valida si exite y si esta activo
	 * 
	 * @author Bryan Zamora
	 * @param objParametros
	 * @return
	 * @throws BOException 
	 */
	public Optional<NotGrupos> findYValidar(Integer id) throws BOException {
		
		Optional<NotGrupos> optSecParametro=find(id);
		
		// Valida que exista
		if (!optSecParametro.isPresent())
			throw new BOException("con.warn.grupoNoExiste", new Object[] { id});

		// Valida este activo.
		if (optSecParametro.get().getEstado()==null || !"A".equalsIgnoreCase(optSecParametro.get().getEstado()))
			throw new BOException("con.warn.grupoNInactivo", new Object[] {id});
				
		return optSecParametro;
	}
	
	
}