package com.goit.notify.bo;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import com.goit.notify.dto.RequestListaGrisTlfDTO;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.exceptions.RestClientException;
import com.goit.notify.exceptions.UnauthorizedException;
import com.goit.notify.model.NotListaGrisTelefono;

public interface INotListaGrisTelefonoBO {
	
	public Map<String, Object> guardarListaGrisTlf(RequestListaGrisTlfDTO objListaGrisTlfDTO, String strIdUsuario,
			String strIdEmpresa, String strUsuario)
			 throws BOException, IOException, RestClientException, UnauthorizedException;
	
	public RequestListaGrisTlfDTO getListaGrisTlf(String idListaGrisTlf, String idEmpresa, String idUsuario)
			 throws BOException, IOException, RestClientException, UnauthorizedException;
	
	public Map <String, Object> actualizarListaGrisTlf(RequestListaGrisTlfDTO objListaGrisTlf, String idListaGrisTlf, String strIdUsuario,
			String strIdEmpresa, String strUsuario ) throws BOException, IOException, RestClientException, UnauthorizedException;
	
	public Map<String, Object> eliminarListaGrisTlf(String idListaGrisTlf, String strIdUsuario,
			String strIdEmpresa, String strUsuario) throws BOException, IOException, RestClientException, UnauthorizedException;
	
	public Map<String, Object> listarTodas () throws BOException, IOException, RestClientException, UnauthorizedException;
}
