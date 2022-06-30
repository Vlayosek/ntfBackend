package com.goit.notify.bo;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.goit.notify.dto.ListaCampanaDTO;
import com.goit.notify.dto.RequestCampanasDTO;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.exceptions.RestClientException;
import com.goit.notify.exceptions.UnauthorizedException;
import com.goit.notify.model.NotCampanas;

public interface INotCampanasBO {

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

	public Map<String, Object> postCrearCampanas(RequestCampanasDTO objRequestCampanasDTO,
			String strIdEmpresa, String strIdUsuario, String strUsuario)
			throws BOException,IOException, RestClientException, UnauthorizedException;
	
	public Optional<NotCampanas> getCampanas(Integer intIdCampana, String strIdUsuario, String strIdEmpresa) throws BOException;

	public Map<String, Object> putCampana(RequestCampanasDTO objRequestCampanasDTO, Integer strIdCampana,
			String strIdEmpresa, String strIdUsuario, String strUsuario)
					throws BOException,IOException, RestClientException, UnauthorizedException;
	
	public Map<String, Object> deleteCampana(Integer strIdCampana,
			String strIdEmpresa, String strIdUsuario, String strUsuario)
					throws BOException,IOException, RestClientException, UnauthorizedException;
	
	public Map<String, Object> getListaCampana() throws BOException;

	public List<ListaCampanaDTO> getListaCampanaId(String strIdUsuario, String strIdEmpresa) throws BOException;
}
