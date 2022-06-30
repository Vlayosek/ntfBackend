package com.goit.notify.bo;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.goit.notify.dto.ListaPlantillasDTO;
import com.goit.notify.dto.RequestPlantillasDTO;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.exceptions.RestClientException;
import com.goit.notify.exceptions.UnauthorizedException;
import com.goit.notify.model.NotPlantilla;

public interface INotPlantillasBO {

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
	
	public Map<String, Object> postCrearPlantillas(RequestPlantillasDTO objPlantillasDTO, String strIdUsuario,
			String strIdEmpresa, String strUsuario) throws BOException, IOException, RestClientException, UnauthorizedException;
	
	public Optional<NotPlantilla> getPlantilla(Integer strIdPlantilla, String idEmpresa, String idUsuario) throws BOException;
	
	public Map<String, Object> actualizarPlantillas(RequestPlantillasDTO objPlantillasDTO, Integer strIdPlantilla, String strIdUsuario,
			String strIdEmpresa, String strUsuario) throws BOException, IOException, RestClientException, UnauthorizedException;
	
	public Map<String, Object> eliminarPlantilla(Integer strIdPlantilla, String strIdUsuario,
			String strIdEmpresa, String strUsuario) throws BOException, IOException, RestClientException, UnauthorizedException;
	
	public Map<String, Object> getPlantillas() throws BOException;
	
	public Map<String, Object> listarPlantillasPorUsuario(String usuario, String empresa) throws BOException; 

	public Map<String, Object> getTipo(String idUsuario, String idEmpresa, String tipo) throws BOException;
	
	public Map<String, Object> copyPlantilla (Integer idPlantilla, String strIdUsuario,
			String strIdEmpresa, String strUsuario) throws BOException;

	public Map<String, Object> consultarPorNombre(String nombre, String strIdUsuario,
			String strIdEmpresa) throws BOException, IOException, RestClientException, UnauthorizedException;
	
	public Map<String, Object> consultarMasRecientes(String strIdEmpresa,
			String strIdUsuario) throws BOException, IOException, RestClientException, UnauthorizedException;
	
}
