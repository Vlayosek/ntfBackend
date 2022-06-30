package com.goit.notify.dao;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.springframework.stereotype.Service;

import com.goit.notify.model.NotDestinatarios;

import lombok.NonNull;

@Service
public class NotDestinatariosDAO extends BaseDAO<NotDestinatarios,Integer>{

	protected NotDestinatariosDAO() {
		super(NotDestinatarios.class);
	}
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public void persist(NotDestinatarios t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(NotDestinatarios t) throws PersistenceException {
		super.update(t);
	}

	@Override
	public Optional<NotDestinatarios> find(@NonNull Integer id) {
		return super.find(id);
	}	
}