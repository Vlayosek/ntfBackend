package com.goit.notify.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.springframework.stereotype.Service;

import com.goit.notify.model.NotDetallesPlantilla;

import lombok.NonNull;

@Service
public class NotDetallePlantillaDAO extends BaseDAO<NotDetallesPlantilla,Integer>{

	protected NotDetallePlantillaDAO() {
		super(NotDetallesPlantilla.class);
	}
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public void persist(NotDetallesPlantilla t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(NotDetallesPlantilla t) throws PersistenceException {
		super.update(t);
	}

	@Override
	public Optional<NotDetallesPlantilla> find(@NonNull Integer id) {
		return super.find(id);
	}
	
	
	/**
	 * Se valida que existe el id plantilla en la tabla NotDetallesPlantilla
	 * 
	 * @param idPlantilla
	 * @return
	 */
	public List<NotDetallesPlantilla> consultarListDetaPlant(Integer idPlantilla) {
		try {	
			return em.createQuery(
						"SELECT nap \n" +
						"  FROM NotDetallesPlantilla nap \n" +
						"  		JOIN nap.notPlantilla np \n" +
						"WHERE np.idPlantilla=:idPlantilla \n"+
						"AND  nap.estado='A' \n"+
						"AND  np.estado='A' ",NotDetallesPlantilla.class)
						.setParameter("idPlantilla",idPlantilla)
						.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	
}
