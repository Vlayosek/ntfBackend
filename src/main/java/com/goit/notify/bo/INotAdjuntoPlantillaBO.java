package com.goit.notify.bo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.goit.notify.dto.RequestAdjuntoPlantillaDTO;
import com.goit.notify.exceptions.BOException;

public interface INotAdjuntoPlantillaBO {

	/**
	 * Metodo para crear un post de Adjunto Plantilla
	 * @param lsRequestAdjuntoPlantilla
	 * @param idPlantilla
	 * @param idUsuario
	 * @param idEmpresa
	 * @param usuario
	 * @return
	 * @throws BOException
	 */
	Map<String, Object> postAdjuntoPlantilla(List<RequestAdjuntoPlantillaDTO> lsRequestAdjuntoPlantilla, Integer idPlantilla, String usuario) throws BOException, ClassNotFoundException, IOException;

	/**
	 * Metodo Para consultar
	 * @param intAdjuntoPlantilla
	 * @return
	 * @throws BOException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	Map<String, Object> consultarAdjuntoPlantillaXId(Integer intAdjuntoPlantilla) throws BOException, ClassNotFoundException, IOException;

	/**
	 * Metood para consultar todos los ADjunto Plantilla
	 * @return
	 * @throws BOException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	Map<String, Object> consultarAdjuntoPlantilla() throws BOException, ClassNotFoundException, IOException;

	/**
	 * MEtodo para consultar todos los adjunto plantilla por Id Plantilla
	 * @param intIdPlantilla
	 * @return
	 * @throws BOException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	Map<String, Object> consultarAdjuntoPlantillaXIdPlantilla(Integer intIdPlantilla) throws BOException, ClassNotFoundException, IOException;
	
	/**
	 * Metodo para Actualizar un Adjunto Plantilla
	 * @param lsRequestAdjuntoPlantilla
	 * @param idPlantilla
	 * @param idUsuario
	 * @param idEmpresa
	 * @param usuario
	 * @return
	 * @throws BOException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	Map<String, Object> putAdjuntoPlantilla(RequestAdjuntoPlantillaDTO objRequestAdjuntoPlantilla, Integer idAdjuntoPlantilla, String usuario) throws BOException, ClassNotFoundException, IOException;
	
	/**
	 * MEtodo para Inactivar un Adjunto Plantilla
	 * @param lsRequestAdjuntoPlantilla
	 * @param idPlantilla
	 * @param idUsuario
	 * @param idEmpresa
	 * @param usuario
	 * @return
	 * @throws BOException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	Map<String, Object> deleteAdjuntoPlantilla(Integer idAdjuntoPlantilla, String usuario) throws BOException, ClassNotFoundException, IOException;
}
