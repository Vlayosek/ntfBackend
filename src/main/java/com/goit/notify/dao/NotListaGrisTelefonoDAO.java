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

import com.goit.notify.dto.RequestListaGrisDTO;
import com.goit.notify.dto.RequestListaGrisTlfDTO;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.model.NotListaGris;
import com.goit.notify.model.NotListaGrisTelefono;

import lombok.NonNull;

@Service
public class NotListaGrisTelefonoDAO extends BaseDAO<NotListaGrisTelefono,String>{
	public NotListaGrisTelefonoDAO() {
		super(NotListaGrisTelefono.class);
	}
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public void persist(NotListaGrisTelefono t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(NotListaGrisTelefono t) throws PersistenceException {
		super.update(t);
	}

	@Override
	public Optional<NotListaGrisTelefono> find(@NonNull String id) {
		return super.find(id);
	}
	
	public Optional<NotListaGrisTelefono> ValidarListaGrisTelefono(String id) throws BOException {
		
		Optional<NotListaGrisTelefono> objListaGris = find(id);
		// Valida que exista
		if (!objListaGris.isPresent())
			throw new BOException("not.warn.idListaGrisNoExiste", new Object[] { "not.campo.idListaGrisTlf"});

		// Valida este activo.
		if (objListaGris.get().getEstado() == null || !"A".equalsIgnoreCase(objListaGris.get().getEstado()))
			throw new BOException("not.warn.headerInactivo", new Object[] { "not.campo.idPlantilla" });

		return objListaGris;
	}
	
	
	public Long countListaGrisTLF() throws BOException {
		StringBuilder builder = new StringBuilder();

		try {
			builder.append(" SELECT count(estado)");
			builder.append(" FROM NotListaGrisTelefono");

			Query query = em.createQuery(builder.toString());
			Long countQuery = (Long) query.getSingleResult();

			return countQuery;

		} catch (NoResultException e) {
			return new Long(0);
		}

	}
	
	public Long validarListaGrisTlfCount(String idListaGris, String idEmpresa, String idUsuario) {
		
		StringBuilder strJPQL = new StringBuilder();
		strJPQL.append(" SELECT count(lgt)");
		strJPQL.append(" FROM 	NotListaGrisTelefono lgt");
		strJPQL.append(" WHERE	 lgt.estado='A' ");
		strJPQL.append(" AND	 ln.idListaGris=:id ");
		strJPQL.append(" AND	 lgt.idEmpresa=:empresa ");
		strJPQL.append(" AND	 lgt.idUsuario=:usuario ");
		
		Query query = em.createQuery(strJPQL.toString());
		
        query.setParameter("id", idListaGris);
        query.setParameter("usuario", idUsuario);
        query.setParameter("empresa", idEmpresa);
        Long longCantQuery = (Long) query.getSingleResult();
       
        return longCantQuery;
	}
	
	
	public List<RequestListaGrisTlfDTO> listarTodas() throws BOException {

		StringBuilder strJPQL = new StringBuilder();
		strJPQL.append(" SELECT telefono as telefono, ");
		strJPQL.append(" 	    fechaRegistro as fecha, ");
		strJPQL.append("        asunto as asunto, ");
		strJPQL.append("        estado as estado, ");
		strJPQL.append("        observacion as observacion, ");
		strJPQL.append("        canal as canal ");
		strJPQL.append(" FROM   NotListaGrisTelefono ");
		strJPQL.append(" where estado = 'A' ");
		TypedQuery<Tuple> query = em.createQuery(strJPQL.toString(), Tuple.class);

		return query.getResultList().stream().map(tuple -> {
			return RequestListaGrisTlfDTO.builder()
					.telefono(tuple.get("telefono", String.class))
					.fechaRegistro(tuple.get("fecha", String.class))
					.asunto(tuple.get("asunto", String.class))
					.estado(tuple.get("estado", String.class))
					.observacion(tuple.get("observacion", String.class))
					.canal(tuple.get("canal", String.class))
					.build();
		}).collect(Collectors.toList());
	}
}
