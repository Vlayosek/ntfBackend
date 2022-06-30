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

import com.goit.notify.dto.ListaPlantillasDTO;
import com.goit.notify.dto.RequestListaNegraDTO;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.model.NotListaNegra;
import com.goit.notify.model.NotPlantilla;

import lombok.NonNull;

@Service
public class NotListaNegraDAO extends BaseDAO<NotListaNegra, String> {

	public NotListaNegraDAO() {
		super(NotListaNegra.class);
	}

	@PersistenceContext
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public void persist(NotListaNegra t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(NotListaNegra t) throws PersistenceException {
		super.update(t);
	}

	@Override
	public Optional<NotListaNegra> find(@NonNull String id) {
		return super.find(id);
	}

	public Optional<NotListaNegra> Valida(String id) throws BOException {

		Optional<NotListaNegra> objListaNegra = find(id);
		// Valida que exista
		if (!objListaNegra.isPresent())
			throw new BOException("not.warn.idPlantillaNoExiste", new Object[] { "not.campo.idPlantilla" });

		// Valida este activo.
		if (objListaNegra.get().getEstado() == null || !"A".equalsIgnoreCase(objListaNegra.get().getEstado()))
			throw new BOException("not.warn.headerInactivo", new Object[] { "not.campo.idPlantilla" });

		return objListaNegra;
	}

	public Long validarListaNegraCount(String idEmpresa, String idUsuario) {

		StringBuilder strJPQL = new StringBuilder();
		strJPQL.append(" SELECT count(ln)");
		strJPQL.append(" FROM 	NotListaNegra ln");
		strJPQL.append(" WHERE	 ln.estado='A' ");
		strJPQL.append(" AND	 ln.idEmpresa=:empresa ");
		strJPQL.append(" AND	 ln.idUsuario=:usuario ");

		Query query = em.createQuery(strJPQL.toString());

		query.setParameter("empresa", idEmpresa);
		query.setParameter("usuario", idUsuario);

		Long longCantQuery = (Long) query.getSingleResult();
		return longCantQuery;
	}

	public Long countListaNegra() throws BOException {
		StringBuilder builder = new StringBuilder();

		try {
			builder.append(" SELECT count(estado)");
			builder.append(" FROM NotListaNegra");

			Query query = em.createQuery(builder.toString());
			Long countQuery = (Long) query.getSingleResult();

			return countQuery;

		} catch (NoResultException e) {
			return new Long(0);
		}

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

		    Long  l1 = countQuery.longValue();

			return l1;

		} catch (NoResultException e) {
			return new Long(0);
		}

	}

	public List<RequestListaNegraDTO> listarListaNegra() throws BOException {

		StringBuilder strJPQL = new StringBuilder();
		strJPQL.append(" SELECT mail as mail, ");
		strJPQL.append(" 	    fechaRegistro as fecha, ");
		strJPQL.append("        asunto as asunto, ");
		strJPQL.append("        estado as estado, ");
		strJPQL.append("        observacion as observacion, ");
		strJPQL.append("        contenidoRebote as contenidoRebote, ");
		strJPQL.append("        idTicket as idTicket ");
		strJPQL.append(" FROM   NotListaNegra ");
		strJPQL.append(" where estado = 'A' ");
		TypedQuery<Tuple> query = em.createQuery(strJPQL.toString(), Tuple.class);

		return query.getResultList().stream().map(tuple -> {
			return RequestListaNegraDTO.builder()
					.mail(tuple.get("mail", String.class))
					.fechaRegistro(tuple.get("fecha", String.class))
					.asunto(tuple.get("asunto", String.class))
					.estado(tuple.get("estado", String.class))
					.observacion(tuple.get("observacion", String.class))
					.contenidoRebote(tuple.get("contenidoRebote", String.class))
					.idTicket(tuple.get("idTicket", String.class))
					.build();
		}).collect(Collectors.toList());
	}
}
