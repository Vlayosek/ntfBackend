package com.goit.notify.bo;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.goit.notify.dto.CamposPersonalizadosDTO;
import com.goit.notify.dto.RequestDestinatariosDTO;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.exceptions.RestClientException;
import com.goit.notify.exceptions.UnauthorizedException;

public interface INotDestinatariosBO {
	
	
	/**
	 * Se crea los destinatarios
	 * 
	 * @author Bryan Zamora
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
	public List<RequestDestinatariosDTO> postFiltroDestinararios(String strCanal,String strToken,Boolean booError,List<RequestDestinatariosDTO> lsRequestDestinatariosDTO,  Integer intIdGrupo, String strIdUsuario,
			String strIdEmpresa, String strUsuario,String strLanguaje) throws BOException, JsonMappingException, JsonProcessingException, RestClientException, UnauthorizedException;


	/**
	 * 	
	 * Se crea los destinatarios mediante un archivo
	 * 
	 * @author Bryan Zamora
	 * @param strCanal 
	 * @param strToken
	 * @param booError 
	 * @param strTipoArchivo
	 * @param lsNombreCampos
	 * @param archivoTarifario
	 * @param lsRequestDestinatariosDTO 
	 * @param intIdGrupo
	 * @param idUsuario
	 * @param idEmpresa
	 * @param usuario
	 * @param strLanguage
	 * @return
	 * @throws IOException 
	 * @throws UnauthorizedException 
	 * @throws RestClientException 
	 */
	public List<RequestDestinatariosDTO> postValidaTipoArchivo( String strCanal, String strToken, Boolean booError, String strTipoArchivo, List<CamposPersonalizadosDTO> lsNombreCampos,
			MultipartFile archivoTarifario, List<RequestDestinatariosDTO> lsRequestDestinatariosDTO, Integer intIdGrupo, String idUsuario, String idEmpresa, String usuario,
			String strLanguage)throws BOException, IOException, RestClientException, UnauthorizedException;

}
