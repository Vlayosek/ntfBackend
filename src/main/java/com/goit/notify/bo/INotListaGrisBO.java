package com.goit.notify.bo;

import java.io.IOException;
import java.util.Map;

import com.goit.notify.dto.RequestListaGrisDTO;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.exceptions.RestClientException;
import com.goit.notify.exceptions.UnauthorizedException;

public interface INotListaGrisBO {
	public Map <String, Object> guardarListaGris(RequestListaGrisDTO objListaGrisDTO , String strIdUsuario,
			String strIdEmpresa, String strUsuario)
			throws BOException, IOException, RestClientException, UnauthorizedException;
	
	public RequestListaGrisDTO getListaGris(String idListaGris, String idEmpresa, String idUsuario) 		
			throws BOException, IOException, RestClientException, UnauthorizedException;

	public Map<String, Object> actualizarListaGris(RequestListaGrisDTO objListaGrisDTO, String idListaNegra, String strIdUsuario,
			String strIdEmpresa, String strUsuario) throws BOException, IOException, RestClientException, UnauthorizedException;
	
	public Map<String, Object> eliminarListaGris(String idListaGris, String strIdUsuario,
			String strIdEmpresa, String strUsuario) throws BOException, IOException, RestClientException, UnauthorizedException;
	
	public Map<String, Object> getListaTodas() throws BOException, IOException, RestClientException, UnauthorizedException;
}
