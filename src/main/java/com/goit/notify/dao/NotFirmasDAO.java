package com.goit.notify.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Service;

import com.goit.notify.dto.ListarFirmasDTO;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.model.NotFirma;

import lombok.NonNull;
@Service
public class NotFirmasDAO extends BaseDAO<NotFirma,Integer>{

	protected NotFirmasDAO() {
		super(NotFirma.class);
	}

	@PersistenceContext
	private EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public void persist(NotFirma t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(NotFirma t) throws PersistenceException {
		super.update(t);
	}

	@Override
	public Optional<NotFirma> find(@NonNull Integer id) {
		return super.find(id);
	}	
	
	public Optional<NotFirma> validacionFirma(Integer id) throws BOException {
		Optional<NotFirma> objFirma = find(id);
		//Valida que exista la firma
		if(!objFirma.isPresent())
			throw new BOException("not.warn.idFirmaNoExiste", new Object[] { "not.campo.idFirma"});
		//Valida que este en estado "Activo"
		if(objFirma.get().getEstado() == null || !"A".equalsIgnoreCase(objFirma.get().getEstado()))
			throw new BOException("not.warn.parametroInactivo", new Object[] { "not.campo.idFirma" });
		return objFirma;
	}

	public Long validarFirmasCount(String idEmpresa, String idUsuario) { 
		     
	    StringBuilder strJPQL = new StringBuilder(); 
	    strJPQL.append(" SELECT count(ap)"); 
	    strJPQL.append(" FROM   NotFirma ap"); 
	    strJPQL.append(" WHERE   ap.idEmpresa=:empresa "); 
	    strJPQL.append(" AND   ap.idUsuario=:usuario "); 
	     
	    Query query = em.createQuery(strJPQL.toString()); 
		query.setParameter("empresa", idEmpresa); 
		query.setParameter("usuario", idUsuario); 
		 
		Long longCantQuery = (Long) query.getSingleResult(); 
		        
		return longCantQuery; 
		   
	} 
	
	public Long countFirma() throws BOException {
		StringBuilder builder = new StringBuilder();
		try {
			builder.append(" SELECT count(estado) ");
			builder.append(" FROM  NotFirma");
			builder.append(" where estado = 'A' ");
			
			Query query = em.createQuery(builder.toString());
			Long countQuery = (Long) query.getSingleResult();

			return countQuery;
		}catch(NoResultException e) {
			return new Long(0);		
		}
	}
	
	public List <ListarFirmasDTO> listarFirmas () throws BOException{
		
		StringBuilder builder = new StringBuilder();
		builder.append(" SELECT estado as estado, ");
		builder.append(" 	    contenido as contenido, ");
		builder.append("        link as link ");
		builder.append(" FROM   NotFirma ");
		builder.append(" where estado = 'A' ");

		TypedQuery<Tuple> query = em.createQuery(builder.toString(), Tuple.class);
		List<Tuple> respFirma = query.getResultList();

		return query.getResultList().stream().map(tuple -> {
			return ListarFirmasDTO.builder().estado(tuple.get("estado", String.class))
					.contenido(tuple.get("contenido", String.class))
					.link(tuple.get("link", String.class))
					.build();
		}).collect(Collectors.toList());
	}
}
