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

import com.goit.notify.dto.ListaPlantillasDTO;
import com.goit.notify.dto.RequestPlantillasDTO;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.model.NotPlantilla;

import lombok.NonNull;

@Service
public class NotPlantillasDAO extends BaseDAO<NotPlantilla, Integer> {

	public NotPlantillasDAO() {
		super(NotPlantilla.class);
	}

	@PersistenceContext
	private EntityManager em;

	@Override
	protected EntityManager getEntityManager() {
		return em;
	}

	@Override
	public void persist(NotPlantilla t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(NotPlantilla t) throws PersistenceException {
		super.update(t);
	}

	@Override
	public Optional<NotPlantilla> find(@NonNull Integer id) {
		return super.find(id);
	}

	public Optional<NotPlantilla> ValidacionPlantilla(Integer id) throws BOException {

		Optional<NotPlantilla> objPlantilla = find(id);
		// Valida que exista
		if (!objPlantilla.isPresent())
			throw new BOException("not.warn.idPlantillaNoExiste", new Object[] { "not.campo.idPlantilla" });

		// Valida este activo.
		if (objPlantilla.get().getEstado() == null || !"A".equalsIgnoreCase(objPlantilla.get().getEstado()))
			throw new BOException("not.warn.parametroInactivo", new Object[] { "not.campo.idPlantilla" });

		return objPlantilla;
	}

	public List<NotPlantilla> buscarNombre(String nombre) throws BOException {

		StringBuilder builder = new StringBuilder();
		builder.append(" SELECT nombre ");
		builder.append(" FROM NotPlantilla ");
		builder.append(" where nombre like '%nombre - copia%' ");
		builder.append(" and nombre  = :nombre ");

		Query query = em.createQuery(builder.toString());
		query.setParameter("nombre", nombre);

		List lsNombre = query.getResultList();
		return lsNombre;

	}

	public Long contadorPlantillaCopia(String nombre) throws BOException {

		StringBuilder builder = new StringBuilder();
		builder.append(" SELECT count(np) ");
		builder.append(" FROM NotPlantilla np ");
		builder.append(" where np.nombre like :nombreCopia ");

		Query query = em.createQuery(builder.toString());
		query.setParameter("nombreCopia", nombre + " - copia%");

		Long countQuery = (Long) query.getSingleResult();
		return countQuery;

	}

	public Long contadorPlantillaNum(String nombre) throws BOException {

		StringBuilder builder = new StringBuilder();
		builder.append(" SELECT count(np) ");
		builder.append(" FROM NotPlantilla np ");
		builder.append(" where np.nombre LIKE :nombreCopia ");

		Query query = em.createQuery(builder.toString());
		query.setParameter("nombreCopia", nombre + " - copia(%");

		Long countQuery = (Long) query.getSingleResult();

		return countQuery;

	}

	public Optional<NotPlantilla> ValidaTipo(Integer id, String tipo) throws BOException {

		Optional<NotPlantilla> objPlantilla = find(id);
		// Valida que exista
		if (!objPlantilla.isPresent())
			throw new BOException("not.warn.idPlantillaNoExiste", new Object[] { "not.campo.idPlantilla" });

		// Valida este activo.
		if (objPlantilla.get().getEstado() == null || !"A".equalsIgnoreCase(objPlantilla.get().getEstado()))
			throw new BOException("not.warn.headerInactivo", new Object[] { "not.campo.idPlantilla" });

		if (objPlantilla.get().getTipo() == null || !objPlantilla.equals(tipo))
			throw new BOException("not.warn.headerInactivo", new Object[] { "not.campo.idPlantilla" });

		return objPlantilla;
	}

	public Long countPlantilla() throws BOException {
		StringBuilder builder = new StringBuilder();

		try {
			builder.append(" SELECT count(estado)");
			builder.append(" FROM NotPlantilla");

			Query query = em.createQuery(builder.toString());
			Long countQuery = (Long) query.getSingleResult();

			return countQuery;

		} catch (NoResultException e) {
			return new Long(0);
		}

	}

	public Long countPlantillaPorTipo(String tipoPlantilla) throws BOException {
		StringBuilder builder = new StringBuilder();

		try {
			builder.append(" SELECT count(tipo)");
			builder.append(" FROM NotPlantilla");
			builder.append(" WHERE tipo = :tipoPlantilla");

			Query query = em.createQuery(builder.toString());
			query.setParameter("tipoPlantilla", tipoPlantilla);

			Long countQuery = (Long) query.getSingleResult();

			return countQuery;

		} catch (NoResultException e) {
			return new Long(0);
		}

	}

	public List<RequestPlantillasDTO> listarPlantillas() throws BOException {

		StringBuilder strJPQL = new StringBuilder();
		strJPQL.append(" SELECT nombre as nombre, ");
		strJPQL.append(" 	    descripcion as descripcion, ");
		strJPQL.append("        tipo as tipo, ");
		strJPQL.append("        estado as estado, ");
		strJPQL.append("        esPublica as es_publica, ");
		strJPQL.append("        fechaCreacion as fecha_crea, ");
		strJPQL.append("        contenidoJson as contenido ");
		strJPQL.append(" FROM   NotPlantilla ");
		strJPQL.append(" where estado = 'A' ");
		TypedQuery<Tuple> query = em.createQuery(strJPQL.toString(), Tuple.class);

		return query.getResultList().stream().map(tuple -> {
			return RequestPlantillasDTO.builder()
					.nombre(tuple.get("nombre", String.class))
					.descripcion(tuple.get("descripcion", String.class))
					.tipo(tuple.get("tipo", String.class))
					.estado(tuple.get("estado", String.class))
					.esPublica(tuple.get("es_publica", String.class))
					.fechaCreacion(tuple.get("fecha_crea", String.class))
					.contenidoJson(tuple.get("contenido", String.class))
					.build();
		}).collect(Collectors.toList());
	}

	public List<RequestPlantillasDTO> listarPlantillasPorUsuario(String idEmpresa, String idUsuario) throws BOException {
		StringBuilder strJPQL = new StringBuilder();
		strJPQL.append(" SELECT nombre as nombre, ");
		strJPQL.append(" 	    idPlantilla as id, ");
		strJPQL.append(" 	    descripcion as descripcion, ");
		strJPQL.append("        tipo as tipo, ");
		strJPQL.append("        estado as estado, ");
		strJPQL.append("        esPublica as es_publica, ");
		strJPQL.append("        fechaCreacion as fecha_crea, ");
		strJPQL.append("        contenidoJson as contenido ");
		strJPQL.append(" FROM   NotPlantilla ");
		strJPQL.append(" WHERE id_empresa = :idEmpresa ");
		strJPQL.append(" AND id_usuario = :idUsuario ");
		strJPQL.append(" AND estado = 'A' ");

		TypedQuery<Tuple> query = em.createQuery(strJPQL.toString(), Tuple.class);
		query.setParameter("idEmpresa", idEmpresa);
		query.setParameter("idUsuario", idUsuario);

		return query.getResultList().stream().map(tuple -> {
			return RequestPlantillasDTO.builder()
					.nombre(tuple.get("nombre", String.class))
					.idPlantilla(tuple.get("id", Integer.class))
					.descripcion(tuple.get("descripcion", String.class))
					.tipo(tuple.get("tipo", String.class))
					.estado(tuple.get("estado", String.class))
					.esPublica(tuple.get("es_publica", String.class))
					.fechaCreacion(tuple.get("fecha_crea", String.class))
					.contenidoJson(tuple.get("contenido", String.class))
					.build();
		}).collect(Collectors.toList());

	}

	public Long validarPlantillasCount(String idEmpresa, String idUsuario) {

		StringBuilder strJPQL = new StringBuilder();
		strJPQL.append(" SELECT count(ap)");
		strJPQL.append(" FROM 	NotPlantilla ap");
		strJPQL.append(" WHERE	 ap.estado='A' ");
		strJPQL.append(" AND	 ap.idEmpresa=:empresa ");
		strJPQL.append(" AND	 ap.idUsuario=:usuario ");
		
		Query query = em.createQuery(strJPQL.toString());
		
		query.setParameter("usuario", idUsuario);
		query.setParameter("empresa", idEmpresa);
		Long longCantQuery = (Long) query.getSingleResult();

		return longCantQuery;
	}

	public List<RequestPlantillasDTO> listarPlantillasPorTipo(String idUsuario, String idEmpresa, String tipo)
			throws BOException {
		StringBuilder builder = new StringBuilder();
		builder.append(" SELECT ap.nombre as nombre, ");
		builder.append(" 	    ap.idPlantilla as id, ");
		builder.append(" 	    ap.descripcion as descripcion, ");
		builder.append("        ap.tipo as tipo, ");
		builder.append("        ap.estado as estado, ");
		builder.append("        esPublica as es_publica, ");
		builder.append("        fechaCreacion as fecha_crea, ");
		builder.append("        contenidoJson as contenido ");
		builder.append(" FROM   NotPlantilla ap ");
		builder.append(" Where ap.idUsuario = :idUsuario ");
		builder.append(" and ap.estado = 'A' ");
		builder.append(" and ap.tipo = :tipo ");
		builder.append(" and ap.idEmpresa = :idEmpresa ");

		TypedQuery<Tuple> query = em.createQuery(builder.toString(), Tuple.class);

		query.setParameter("idUsuario", idUsuario);
		query.setParameter("tipo", tipo);
		query.setParameter("idEmpresa", idEmpresa);

		return query.getResultList().stream().map(tuple -> {
			return RequestPlantillasDTO.builder()
					.nombre(tuple.get("nombre", String.class))
					.idPlantilla(tuple.get("id", Integer.class))
					.descripcion(tuple.get("descripcion", String.class))
					.tipo(tuple.get("tipo", String.class))
					.estado(tuple.get("estado", String.class))
					.esPublica(tuple.get("es_publica", String.class))
					.fechaCreacion(tuple.get("fecha_crea", String.class))
					.contenidoJson(tuple.get("contenido", String.class))
					.build();
		}).collect(Collectors.toList());
	}

	public List<RequestPlantillasDTO> listarPorNombre(String nombrePlantilla, String idUsuario, String idEmpresa)
			throws BOException {

		StringBuilder strJPQL = new StringBuilder();
		strJPQL.append(" SELECT nombre as nombre, ");
		strJPQL.append(" 	    descripcion as descripcion, ");
		strJPQL.append("        tipo as tipo, ");
		strJPQL.append("        estado as estado, ");
		strJPQL.append("        esPublica as es_publica, ");
		strJPQL.append("        fechaCreacion as fecha_crea, ");
		strJPQL.append("        contenidoJson as contenido ");
		strJPQL.append(" FROM NotPlantilla ");
		strJPQL.append(" WHERE nombre = :nombre ");
		strJPQL.append(" AND idUsuario = :usuario ");
		strJPQL.append(" AND idEmpresa = :empresa ");
		strJPQL.append(" AND estado = 'A' ");


		TypedQuery<Tuple> query = em.createQuery(
				strJPQL.toString(), Tuple.class);

		query.setParameter("nombre", nombrePlantilla);
		query.setParameter("usuario", idUsuario);
		query.setParameter("empresa", idEmpresa);

		return query.getResultList().stream().map(tuple -> {
			return RequestPlantillasDTO.builder()
					.nombre(tuple.get("nombre", String.class))
					.descripcion(tuple.get("descripcion", String.class))
					.tipo(tuple.get("tipo", String.class))
					.estado(tuple.get("estado", String.class))
					.esPublica(tuple.get("es_publica", String.class))
					.fechaCreacion(tuple.get("fecha_crea", String.class))
					.contenidoJson(tuple.get("contenido", String.class))
					.build();
		}).collect(Collectors.toList());
	}

	public List<RequestPlantillasDTO> listPlantillaDesc(String idEmpresa, String idUsuario)
			throws BOException {

		StringBuilder strJPQL = new StringBuilder();
		strJPQL.append(" SELECT nombre as nombre, ");
		strJPQL.append(" 	    descripcion as descripcion, ");
		strJPQL.append("        tipo as tipo, ");
		strJPQL.append("        estado as estado, ");
		strJPQL.append("        esPublica as esPublica, ");
		strJPQL.append("        fechaCreacion as fecha_crea, ");
		strJPQL.append("        contenidoJson as contenido ");
		strJPQL.append(" FROM NotPlantilla ");
		strJPQL.append(" WHERE idEmpresa = :empresa ");
		strJPQL.append(" AND  idUsuario = :usuario ");
		strJPQL.append(" AND estado = 'A' ");
		strJPQL.append(" ORDER BY fechaCreacion desc ");

		TypedQuery<Tuple> query = em.createQuery(strJPQL.toString(), Tuple.class);
		query.setParameter("empresa", idEmpresa);
		query.setParameter("usuario", idUsuario);
		
		return query.getResultList().stream().map(tuple -> {
			return RequestPlantillasDTO.builder()
					.nombre(tuple.get("nombre", String.class))
					.descripcion(tuple.get("descripcion", String.class))
					.tipo(tuple.get("tipo", String.class))
					.estado(tuple.get("estado", String.class))
					.esPublica(tuple.get("esPublica", String.class))
					.fechaCreacion(tuple.get("fecha_crea", String.class))
					.contenidoJson(tuple.get("contenido", String.class))
					.build();
		}).collect(Collectors.toList());
	}
}
