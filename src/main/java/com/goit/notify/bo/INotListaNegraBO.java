package com.goit.notify.bo;

import java.io.IOException;
import java.util.Map;

import com.goit.notify.dto.RequestListaNegraDTO;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.exceptions.RestClientException;
import com.goit.notify.exceptions.UnauthorizedException;

public interface INotListaNegraBO {
	public Map<String, Object> guardarListaNegra (RequestListaNegraDTO objListaNegra, String strIdUsuario, String strIdEmpresa, String strUsuario)
			throws BOException, IOException, RestClientException, UnauthorizedException;

	public RequestListaNegraDTO getListaNegra(String idListaNegra, String strIdUsuario, String strIdEmpresa, String strUsuario) 
			throws BOException,IOException, RestClientException, UnauthorizedException ;
	
	public Map<String, Object> actualizarListaNegra(RequestListaNegraDTO objListaNegraDTO, String idListaNegra, String strIdUsuario, String strIdEmpresa, String strUsuario)
			throws BOException, IOException, RestClientException, UnauthorizedException;
	
	public Map<String, Object> eliminarListaNegra(String idListaNegra, String strIdEmpresa, String strIdUsuario,
			 String strUsuario) 
			throws BOException, IOException, RestClientException, UnauthorizedException;
	
	public Map<String, Object> getListaTodas() throws BOException, IOException, RestClientException, UnauthorizedException;
}

