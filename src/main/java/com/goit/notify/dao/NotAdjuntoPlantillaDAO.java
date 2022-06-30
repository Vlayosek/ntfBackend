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

import com.goit.notify.dto.GeneralAdjuntoPlantillaDTO;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.model.NotAdjuntoPlantilla;

import lombok.NonNull;

@Service
public class NotAdjuntoPlantillaDAO extends BaseDAO<NotAdjuntoPlantilla,Integer>{

    protected NotAdjuntoPlantillaDAO(){super(NotAdjuntoPlantilla.class);}

    @PersistenceContext
    private EntityManager em;
    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    @Override
    public void persist(NotAdjuntoPlantilla t) throws PersistenceException {
        super.persist(t);
    }

    @Override
    public void update(NotAdjuntoPlantilla t) throws PersistenceException {
        super.update(t);
    }

    @Override
    public Optional<NotAdjuntoPlantilla> find(@NonNull Integer id) {
        return super.find(id);
    }

    public Optional<NotAdjuntoPlantilla> findWithValidacionCampo(Integer id) throws BOException {

        Optional<NotAdjuntoPlantilla> objAdjuntoPlantilla=find(id);

        // Valida que exista
        if (!objAdjuntoPlantilla.isPresent())
            throw new BOException("not.warn.campoNoExiste", new Object[] { "not.campo.idPlantilla" });

        // Valida este activo.
        if (objAdjuntoPlantilla.get().getEstado()==null || !"A".equalsIgnoreCase(objAdjuntoPlantilla.get().getEstado()))
            throw new BOException("not.warn.campoInactivo", new Object[] { "not.campo.idPlantilla" });

        return objAdjuntoPlantilla;
    }

    public List<GeneralAdjuntoPlantillaDTO> consultarAdjuntoPlantillaXIdPlantilla (Integer idPlantilla){
        StringBuilder strJPQLBase = new StringBuilder();
        strJPQLBase.append("select ap.idAdjuntoPlantilla as idAdjuntoPlantilla, ");
        strJPQLBase.append("	   ap.idPlantilla as idPlantilla,  ");
        strJPQLBase.append("	   ap.nombreArchivo as nombreArchivo,  ");
        strJPQLBase.append("	   ap.rutaLocal as rutaLocal,  ");
        strJPQLBase.append("	   ap.extension as extension  ");
        strJPQLBase.append("from  NotAdjuntoPlantilla ap ");
        strJPQLBase.append("where ap.idPlantilla=:idPlantilla ");

        /*Query query = em.createQuery(strJPQLBase.toString());
        query.setParameter("idPlantilla", idPlantilla);
        return query.getResultList();*/


        TypedQuery<Tuple> query = em.createQuery(strJPQLBase.toString(), Tuple.class);
        query.setParameter("idPlantilla", idPlantilla);

        return query.getResultList().stream()
                .map(tuple -> GeneralAdjuntoPlantillaDTO.builder()
                        .idAdjuntoPlantilla(tuple.get("idAdjuntoPlantilla", Integer.class))
                        .idPlantilla(tuple.get("idPlantilla", Integer.class))
                        .nombreArchivo(tuple.get("nombreArchivo", String.class))
                        .rutaLocal(tuple.get("rutaLocal", String.class))
                        .extension(tuple.get("extension", String.class))
                        .build())
                .collect(Collectors.toList());


    }
    
    public List<GeneralAdjuntoPlantillaDTO> consultarAdjuntosPlantillas(){
    	StringBuilder strJPQL = new StringBuilder();
    	strJPQL.append(" SELECT ap.idAdjuntoPlantilla as idAdjuntoPlantilla, ap.idPlantilla as idPlantilla,");
		strJPQL.append(" 		ap.nombreArchivo as nombreArchivo,");
		strJPQL.append(" 		ap.rutaLocal as rutaLocal,");
		strJPQL.append(" 		ap.extension as extension");
		strJPQL.append(" FROM 	NotAdjuntoPlantilla ap");
		strJPQL.append(" WHERE	 ap.estado='A' ");
		strJPQL.append(" ORDER BY ap.idAdjuntoPlantilla DESC ");
		
		TypedQuery<Tuple> query = em.createQuery(strJPQL.toString(), Tuple.class);
		
		//List<Tuple> lres = query.getResultList();
		
		return query.getResultList().stream().map(tuple -> {
			return GeneralAdjuntoPlantillaDTO.builder().idAdjuntoPlantilla(tuple.get("idAdjuntoPlantilla",Integer.class))
					.idPlantilla(tuple.get("idPlantilla", Integer.class))
					.nombreArchivo(tuple.get("nombreArchivo", String.class))
					.rutaLocal(tuple.get("rutaLocal", String.class))
					.extension(tuple.get("extension", String.class))
					.build();
		}).collect(Collectors.toList());
    }
    
    public Long consultarAdjuntosPlantillasCount() {
    	StringBuilder strJPQL = new StringBuilder();

		try {

			strJPQL.append(" SELECT count(ap)");
			strJPQL.append(" FROM 	NotAdjuntoPlantilla ap");
			strJPQL.append(" WHERE	 ap.estado='A' ");

			Query query = em.createQuery(strJPQL.toString());

			Long lonCantQuery = (Long) query.getSingleResult();

			return lonCantQuery;

		} catch (NoResultException e) {
			return new Long(0);
		}
    	
    }
    
    /**
	 * Busca lista de adjunto plantilla por id plantilla.
	 * 
	 * @param idPlantilla
	 * @return
	 */
	public List<NotAdjuntoPlantilla> consultarListAdjuntPlant(Integer idPlantilla) {
		try {	
			return em.createQuery(
						"SELECT nap \n" +
						"  FROM NotAdjuntoPlantilla nap \n" +
						"  		JOIN nap.notPlantilla np \n" +
						"WHERE np.idPlantilla=:idPlantilla \n"+
						"AND  nap.estado='A' \n"+
						"AND  np.estado='A' ",NotAdjuntoPlantilla.class)
						.setParameter("idPlantilla",idPlantilla)
						.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	

}
