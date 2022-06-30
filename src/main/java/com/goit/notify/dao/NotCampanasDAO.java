package com.goit.notify.dao;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Service;

import com.goit.notify.dto.ListaCampanaDTO;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.model.NotCampanas;

import lombok.NonNull;

@Service
public class NotCampanasDAO extends BaseDAO<NotCampanas,Integer>{
	
	protected NotCampanasDAO() {
		super(NotCampanas.class);
	}
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	protected EntityManager getEntityManager() {
		return em;
	}
	
	@Override
	public void persist(NotCampanas t) throws PersistenceException {
		super.persist(t);
	}

	@Override
	public void update(NotCampanas t) throws PersistenceException {
		super.update(t);
	}

	@Override
	public Optional<NotCampanas> find(@NonNull Integer id) {
		return super.find(id);
	}
	
	public Object findId(Integer idCampana) {
		return find(idCampana);
	}
	
	public Optional<NotCampanas> ValidarCampanas(Integer id/*, String idUsuario,String idEmpresa*/) throws BOException {
			
			Optional<NotCampanas> objCampanas=find(id);
			
			// Valida que exista
			if (!objCampanas.isPresent())
				throw new BOException("not.warn.idCampanaNoExiste", new Object[] { "not.campo.idCampana" });
			
			/*if (objCampanas.get().getIdEmpresa() == null || !objCampanas.get().getIdEmpresa().equals(idEmpresa))
				throw new BOException("not.warn.idEmpresaNoExiste", new Object[] { "not.campo.idEmpresa" });
			
			if(objCampanas.get().getIdUsuario() == null || !objCampanas.get().getIdUsuario().equals(idUsuario))
				throw new BOException("not.warn.idUsuarioNoExiste", new Object[] { "not.campo.idUsuario" });*/ 
			
			// Valida este activo.
			if (objCampanas.get().getEstado() ==null || !"A".equalsIgnoreCase(objCampanas.get().getEstado()))
				throw new BOException("not.warn.headerInactivo", new Object[] { "not.campo.idCampana" });
			
			return objCampanas;
	}
	
	public Long consultarCampana() throws BOException{
		StringBuilder strConsulta = new StringBuilder();
		strConsulta.append("SELECT count(estado)");
		strConsulta.append("FROM NotCampanas");
		strConsulta.append(" WHERE estado = 'A' ");
		
		Query query = em.createQuery(strConsulta.toString());
		
		Long lonCampana = (Long) query.getSingleResult();
		
		return lonCampana;
	}

	public List <ListaCampanaDTO> listarCampana () throws BOException{
		StringBuilder strListarCampana = new StringBuilder();
		
		strListarCampana.append(" SELECT campana as campana, ");
		strListarCampana.append("        descripcion as descripcion, ");
		strListarCampana.append("        estado as estado ");
		strListarCampana.append(" FROM NotCampanas ");
		//strListarCampana.append(" WHERE estado = 'A' ");
		
		TypedQuery<Tuple> query = em.createQuery(strListarCampana.toString(), Tuple.class);
		//List<Tuple> lres = query.getResultList();
		
		return query.getResultList().stream().map(tuple ->{
			return ListaCampanaDTO.builder().campana(tuple.get("campana", String.class))
					.descripcion(tuple.get("descripcion", String.class))
					.estado(tuple.get("estado",String.class))
					.build();
		}).collect(Collectors.toList());
		
	}

	public List<ListaCampanaDTO> listarCampanaId(String idUsuario, String idEmpresa) throws BOException{
		
		StringBuilder strListarCampanaId = new StringBuilder();
		strListarCampanaId.append(" SELECT campana as campana, ");
		strListarCampanaId.append("        descripcion as descripcion, ");
		strListarCampanaId.append("        estado as estado ");
		strListarCampanaId.append(" FROM NotCampanas ");
		strListarCampanaId.append(" WHERE id_usuario = :idUsuario" );
		strListarCampanaId.append(" AND id_empresa = :idEmpresa" );
		strListarCampanaId.append(" AND estado = 'A' ");
		
		TypedQuery<Tuple> query = em.createQuery(strListarCampanaId.toString(), Tuple.class);
		query.setParameter("idUsuario", idUsuario)
		.setParameter("idEmpresa", idEmpresa);
		
		return query.getResultList().stream().map(tuple ->{
			return ListaCampanaDTO.builder()
					.campana(tuple.get("campana", String.class))
					.descripcion(tuple.get("descripcion", String.class))
					.estado(tuple.get("estado", String.class))
					.build();
		}).collect(Collectors.toList());
		}
	
	public Long valCampana(String idUsuario, String idEmpresa) {
		
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
}