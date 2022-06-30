package com.goit.notify.bo;

import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.goit.notify.dto.RequestDetallePlantillaDTO;
import com.goit.notify.exceptions.*;
import com.goit.notify.model.NotDetallesPlantilla;

public interface INotDetallePlantillaBO {

	/**
	 * @author Cristhofer Sarez
	 * @param lsRequestDestinatariosDTO
	 * @param intIdGrupo
	 * @param strIdUsuario
	 * @param strIdEmpresa
	 * @param strUsuario
	 * @return
	 * @throws BOException
	 * @throws UnauthorizedException 
	 * @throws RestClientException 
	 * @throws JsonProcessingException 
	 * @throws JsonMappingException 
	 */
	
	public Map<String, Object> postDetallePlantilla(RequestDetallePlantillaDTO objRequestDetallePlantillaDTO, 
			/*Integer intIdFirma,*/ String strIdUsuario, String strIdEmpresa, String strUsuario)
			throws BOException,IOException, RestClientException, UnauthorizedException;
	
	public Map<String, Object> getDetallePlantilla(Integer intIdDetallePlantilla) throws BOException;
}
