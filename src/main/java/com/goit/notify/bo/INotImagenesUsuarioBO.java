package com.goit.notify.bo;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.goit.notify.dto.RequestImagenesUsuarioDTO;
import com.goit.notify.exceptions.BOException;

public interface INotImagenesUsuarioBO {

	/**
	 * Metodo para crear un post de Imagen Usuario
	 * @param lsRequestAdjuntoPlantilla
	 * @param strIdUsuario
	 * @param idUsuario
	 * @param idEmpresa
	 * @param usuario
	 * @return
	 * @throws BOException
	 */
	Map<String, Object> postImagenUsuario(List<RequestImagenesUsuarioDTO> lsRequestImagenesUsuarioDTO, String strIdUsuario,String idUsuario,
											 String idEmpresa, String usuario) throws BOException, ClassNotFoundException, IOException;

	/**
	 * Metodo Para consultar Imagen Por Id
	 * @param intImagenUsuario
	 * @return
	 * @throws BOException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	Map<String, Object> consultarImagenXId(Integer intImagenUsuario) throws BOException, ClassNotFoundException, IOException;

	/**
	 * Metood para consultar todos las Imagenes Usuario
	 * @return
	 * @throws BOException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	Map<String, Object> consultarImagenesUsuario() throws BOException, ClassNotFoundException, IOException;

	/**
	 * MEtodo para consultar todos las imagenes de usuario por Id Usuario
	 * @param strIdUsuario
	 * @return
	 * @throws BOException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	Map<String, Object> consultarImagenesXUsuario(String strIdUsuario) throws BOException, ClassNotFoundException, IOException;
	
	/**
	 * Metodo para Actualizar una Imagen Usuario
	 * @param lsRequestAdjuntoPlantilla
	 * @param strIdUsuario
	 * @param idUsuario
	 * @param idEmpresa
	 * @param usuario
	 * @return
	 * @throws BOException
	 * @throws ClassNotFoundException
	 * @throws IOException
	 */
	Map<String, Object> putImagenUsuario(RequestImagenesUsuarioDTO objRequestImagenesUsuarioDTO, Integer intIdImagen,String idUsuario,
			 String idEmpresa, String usuario) throws BOException, ClassNotFoundException, IOException;
	
	/**
	 * MEtodo para Inactivar una Imagen Usuario
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
	Map<String, Object> deleteImagen(Integer idImagen,String idUsuario,
			 String idEmpresa, String usuario) throws BOException, ClassNotFoundException, IOException;
}
