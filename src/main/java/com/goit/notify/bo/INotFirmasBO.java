package com.goit.notify.bo;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.goit.notify.dto.RequestFirmasDTO;
import com.goit.notify.dto.RequestPlantillasDTO;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.exceptions.RestClientException;
import com.goit.notify.exceptions.UnauthorizedException;
import com.goit.notify.model.NotFirma;

public interface INotFirmasBO {

	/**
	 * Se crea las plantillas
	 * 
	 * @author Scarlet Carvaca
	 * @param lsRequestPlantillasDTO
	 * @param nombre
	 * @param descripcion
	 * @return
	 * @throws BOException
	 * @throws UnauthorizedException 
	 * @throws RestClientException 
	 * @throws JsonProcessingException 
	 * @throws JsonMappingException 
	 */
	
	public Map<String, Object> crearFirmas(RequestFirmasDTO objFirmaDTO, String strIdUsuario,
			String strIdEmpresa, String strUsuario) throws BOException, IOException, RestClientException, UnauthorizedException;
	
	public Optional<NotFirma> mostrarFirma(Integer intIdFirma, String idEmpresa, String idUsuario)throws BOException;
	
	public Map<String, Object> listarFirmas() throws BOException;

	public Map<String, Object> actualizarFirma(RequestFirmasDTO objFirmaDTO, Integer intIdFirma, String strIdUsuario,
			String strIdEmpresa, String strUsuario) throws BOException, IOException, RestClientException, UnauthorizedException;
	
	public Map<String, Object> eliminarFirma(Integer intIdFirma, String strIdUsuario,
			String strIdEmpresa, String strUsuario) throws BOException, IOException, RestClientException, UnauthorizedException;
}
