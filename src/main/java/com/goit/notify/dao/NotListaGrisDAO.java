package com.goit.notify.dao;

import java.math.BigInteger;
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
import com.goit.notify.dto.RequestListaNegraDTO;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.model.NotListaGris;

import lombok.NonNull;

@Service
public class NotListaGrisDAO extends BaseDAO<NotListaGris,String>{
	public NotListaGrisDAO() {
		super(NotListaGris.class);
	}
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public void persist(NotListaGris t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(NotListaGris t) throws PersistenceException {
		super.update(t);
	}

	@Override
	public Optional<NotListaGris> find(@NonNull String id) {
		return super.find(id);
	}
	
	public Long validaIdTicket(String idTicket) throws BOException {
		StringBuilder builder = new StringBuilder();

		try {
			builder.append(" SELECT count(id_ticket)");
			builder.append(" FROM not_envios_historico_mail");
			builder.append(" WHERE id_ticket = :idTicket ");
			builder.append(" AND estado = 'A' ");

			Query query = em.createNativeQuery(builder.toString());
			query.setParameter("idTicket", idTicket);
			BigInteger countQuery = (BigInteger) query.getSingleResult();

		    Long  id = countQuery.longValue();

			return id;

		} catch (NoResultException e) {
			return new Long(0);
		}

	}
	
	public Optional<NotListaGris> ValidarListaGris(String id) throws BOException {
		
		Optional<NotListaGris> objListaGris = find(id);
		// Valida que exista
		if (!objListaGris.isPresent())
			throw new BOException("not.warn.idListaGrisNoExiste", new Object[] { "not.campo.idListaGris"});

		// Valida este activo.
		if (objListaGris.get().getEstado() == null || !"A".equalsIgnoreCase(objListaGris.get().getEstado()))
			throw new BOException("not.warn.headerInactivo", new Object[] { "not.campo.idPlantilla" });

		return objListaGris;
	}
	
	public Long validarListaGrisCount(String idListaGris, String idEmpresa, String idUsuario) {
		
		StringBuilder strJPQL = new StringBuilder();
		strJPQL.append(" SELECT count(lg)");
		strJPQL.append(" FROM 	NotListaGris lg");
		strJPQL.append(" WHERE	 lg.estado='A' ");
		strJPQL.append(" AND	 lg.idListaGris=:id ");
		strJPQL.append(" AND	 lg.idEmpresa=:empresa ");
		strJPQL.append(" AND	 lg.idUsuario=:usuario ");
		
		Query query = em.createQuery(strJPQL.toString());
		
        query.setParameter("id", idListaGris);
        query.setParameter("empresa", idEmpresa);
        query.setParameter("usuario", idUsuario);
        Long longCantQuery = (Long) query.getSingleResult();
       
        return longCantQuery;
	}
	
	public Long countListaGris() throws BOException {
		StringBuilder builder = new StringBuilder();

		try {
			builder.append(" SELECT count(estado)");
			builder.append(" FROM NotListaGris");

			Query query = em.createQuery(builder.toString());
			Long countQuery = (Long) query.getSingleResult();

			return countQuery;

		} catch (NoResultException e) {
			return new Long(0);
		}

	}
	
	public List<RequestListaGrisDTO> listarTodas() throws BOException {

		StringBuilder strJPQL = new StringBuilder();
		strJPQL.append(" SELECT mail as mail, ");
		strJPQL.append(" 	    fechaRegistro as fecha, ");
		strJPQL.append("        asunto as asunto, ");
		strJPQL.append("        estado as estado, ");
		strJPQL.append("        observacion as observacion, ");
		strJPQL.append("        idTicket as idTicket ");
		strJPQL.append(" FROM   NotListaGris ");
		strJPQL.append(" where estado = 'A' ");
		TypedQuery<Tuple> query = em.createQuery(strJPQL.toString(), Tuple.class);

		return query.getResultList().stream().map(tuple -> {
			return RequestListaGrisDTO.builder()
					.mail(tuple.get("mail", String.class))
					.fechaRegistro(tuple.get("fecha", String.class))
					.asunto(tuple.get("asunto", String.class))
					.estado(tuple.get("estado", String.class))
					.observacion(tuple.get("observacion", String.class))
					.idTicket(tuple.get("idTicket", String.class))
					.build();
		}).collect(Collectors.toList());
	}
}
