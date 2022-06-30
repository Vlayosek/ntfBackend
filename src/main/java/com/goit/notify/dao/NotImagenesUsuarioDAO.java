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

import com.goit.notify.dto.GeneralImagenesUsuarioDTO;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.model.NotImagenesUsuario;

import lombok.NonNull;

@Service
public class NotImagenesUsuarioDAO extends BaseDAO<NotImagenesUsuario,Integer>{

    protected NotImagenesUsuarioDAO(){super(NotImagenesUsuario.class);}

    @PersistenceContext
    private EntityManager em;
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void persist(NotImagenesUsuario t) throws PersistenceException {
        super.persist(t);
    }

    @Override
    public void update(NotImagenesUsuario t) throws PersistenceException {
        super.update(t);
    }

    @Override
    public Optional<NotImagenesUsuario> find(@NonNull Integer id) {
        return super.find(id);
    }

    public Optional<NotImagenesUsuario> findWithValidacionCampo(Integer id) throws BOException {

        Optional<NotImagenesUsuario> objImagenUsuario=find(id);

        // Valida que exista
        if (!objImagenUsuario.isPresent())
            throw new BOException("not.warn.campoNoExiste", new Object[] { "not.campo.idPlantilla" });

        // Valida este activo.
        if (objImagenUsuario.get().getEstado()==null || !"A".equalsIgnoreCase(objImagenUsuario.get().getEstado()))
            throw new BOException("not.warn.campoInactivo", new Object[] { "not.campo.idPlantilla" });

        return objImagenUsuario;
    }
    
	/*
	 * public Optional<NotImagenesUsuario> findWithValidacionCampo_(String id)
	 * throws BOException {
	 * 
	 * Optional<NotImagenesUsuario> objAdjuntoPlantilla=find(id);
	 * 
	 * // Valida que exista if (!objAdjuntoPlantilla.isPresent()) throw new
	 * BOException("not.warn.campoNoExiste", new Object[] { "not.campo.idPlantilla"
	 * });
	 * 
	 * // Valida este activo. if (objAdjuntoPlantilla.get().getEstado()==null ||
	 * !"A".equalsIgnoreCase(objAdjuntoPlantilla.get().getEstado())) throw new
	 * BOException("not.warn.campoInactivo", new Object[] { "not.campo.idPlantilla"
	 * });
	 * 
	 * return objAdjuntoPlantilla; }
	 */
    
    public List<GeneralImagenesUsuarioDTO> consultarImagenesUsuario(){
    	StringBuilder strJPQL = new StringBuilder();
    	strJPQL.append(" SELECT iu.idImagenUsuario as idImagenUsuario, ");
    	strJPQL.append(" 		iu.idEmpresa as idEmpresa, ");
    	strJPQL.append(" 		iu.idUsuario as idUsuario, ");
		strJPQL.append(" 		iu.nombre as nombre, ");
		strJPQL.append(" 		iu.rutaLocal as rutaLocal, ");
		strJPQL.append(" 		iu.urlPublica as urlPublica, ");
		strJPQL.append(" 		iu.esPublica as esPublica, ");
		strJPQL.append(" 		iu.estado as estado ");
		strJPQL.append(" FROM 	NotImagenesUsuario iu ");
		//strJPQL.append(" WHERE	 ap.estado='A'");
		strJPQL.append(" ORDER BY iu.idImagenUsuario DESC ");
		
		TypedQuery<Tuple> query = em.createQuery(strJPQL.toString(), Tuple.class);
		
		//List<Tuple> lres = query.getResultList();
		
		return query.getResultList().stream().map(tuple -> {
			return GeneralImagenesUsuarioDTO.builder().idImagenUsuario(tuple.get("idImagenUsuario",Integer.class))
					.idEmpresa(tuple.get("idEmpresa", String.class))
					.idUsuario(tuple.get("idUsuario", String.class))
					.nombre(tuple.get("nombre", String.class))
					.rutaLocal(tuple.get("rutaLocal", String.class))
					.urlPublica(tuple.get("urlPublica", String.class))
					.esPublica(tuple.get("esPublica", String.class))
					.estado(tuple.get("estado", String.class))
					.build();
		}).collect(Collectors.toList());
    }
    
    public Long consultarImagenesUsuarioCount() {
    	StringBuilder strJPQL = new StringBuilder();

		try {

			strJPQL.append(" SELECT count(iu)");
			strJPQL.append(" FROM 	NotImagenesUsuario iu");
			strJPQL.append(" WHERE	 iu.estado='A' ");

			Query query = em.createQuery(strJPQL.toString());

			Long lonCantQuery = (Long) query.getSingleResult();

			return lonCantQuery;

		} catch (NoResultException e) {
			return new Long(0);
		}
    	
    }

    public List<GeneralImagenesUsuarioDTO> consultarImagenesXUsuario (String strIdUsuario){
    	
        StringBuilder strJPQL = new StringBuilder();
    	strJPQL.append(" SELECT iu.idImagenUsuario as idImagenUsuario, ");
    	strJPQL.append(" 		iu.idEmpresa as idEmpresa, ");
    	strJPQL.append(" 		iu.idUsuario as idUsuario, ");
		strJPQL.append(" 		iu.nombre as nombre, ");
		strJPQL.append(" 		iu.rutaLocal as rutaLocal, ");
		strJPQL.append(" 		iu.urlPublica as urlPublica, ");
		strJPQL.append(" 		iu.esPublica as esPublica, ");
		strJPQL.append(" 		iu.estado as estado ");
		strJPQL.append(" FROM 	NotImagenesUsuario iu ");
		strJPQL.append(" WHERE iu.idUsuario=:idUsuario ");

        TypedQuery<Tuple> query = em.createQuery(strJPQL.toString(), Tuple.class);
        query.setParameter("idUsuario", strIdUsuario);

        return query.getResultList().stream().map(tuple -> {
			return GeneralImagenesUsuarioDTO.builder()
					.idImagenUsuario(tuple.get("idImagenUsuario",Integer.class))
					.idEmpresa(tuple.get("idEmpresa", String.class))
					.idUsuario(tuple.get("idUsuario", String.class))
					.nombre(tuple.get("nombre", String.class))
					.rutaLocal(tuple.get("rutaLocal", String.class))
					.urlPublica(tuple.get("urlPublica", String.class))
					.esPublica(tuple.get("esPublica", String.class))
					.estado(tuple.get("estado", String.class))
					.build();
		}).collect(Collectors.toList());


    }
}
