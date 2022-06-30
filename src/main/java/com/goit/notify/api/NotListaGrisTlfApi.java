package com.goit.notify.api;

import java.io.IOException;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.goit.notify.bo.INotListaGrisBO;
import com.goit.notify.bo.INotListaGrisTelefonoBO;
import com.goit.notify.dto.RequestListaGrisDTO;
import com.goit.notify.dto.RequestListaGrisTlfDTO;
import com.goit.notify.dto.ResponseOk;
import com.goit.notify.dto.UsuarioLogin;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.exceptions.CustomExceptionHandler;
import com.goit.notify.exceptions.RestClientException;
import com.goit.notify.exceptions.UnauthorizedException;
import com.goit.notify.util.MensajesUtil;
@RestController
@RequestMapping("/listaGrisTlf")
public class NotListaGrisTlfApi {
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(NotPlantillasApi.class);
	
	@Autowired
	private INotListaGrisTelefonoBO objListaGrisTelefonoBO;
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> saveListaGrisTlf(
			@RequestHeader(value = "Accept-Language", 	required = false) String strLanguage,
			@RequestBody RequestListaGrisTlfDTO objListaGrisTlfDTO)
			
		throws BOException, IOException, RestClientException, UnauthorizedException {
		
		try {	
			
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
	
			Map<String, Object> mapListaGrisTlf = objListaGrisTelefonoBO.guardarListaGrisTlf(objListaGrisTlfDTO, usuarioLogin.getIdUsuario(), 
						usuarioLogin.getIdEmpresa(),usuarioLogin.getUsuario());
				
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapListaGrisTlf) , HttpStatus.OK);

		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}
	}

	@RequestMapping(value = "/getListaGrisTlf/{idListaGrisTlf}/{idEmpresa}/{idUsuario}", method = RequestMethod.GET)
	public ResponseEntity<?> getListaGris(
			@RequestHeader(value = "Accept-Language", required = false) String strLanguage,
			@PathVariable(value ="idListaGrisTlf", required = false) String idListaGrisTlf,
			@PathVariable(value ="idEmpresa", required = false) String idEmpresa,
			@PathVariable(value ="idUsuario", required = false) String idUsuario)
			throws BOException, IOException, RestClientException, UnauthorizedException {

		try {
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			RequestListaGrisTlfDTO mapListaGrisTlf = 
					objListaGrisTelefonoBO.getListaGrisTlf(idListaGrisTlf, usuarioLogin.getIdEmpresa(), usuarioLogin.getIdUsuario());
			
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapListaGrisTlf), HttpStatus.OK);
		} catch (BOException be){
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage),be.getData());
		}
	}
	
	@RequestMapping(value = "/editarListaGrisTlf/{idListaGrisTlf}", method = RequestMethod.PUT)
	public ResponseEntity<?> editarPlantilla(
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage,
			@RequestBody RequestListaGrisTlfDTO objListaGrisDTO,
			@PathVariable (value ="idListaGrisTlf", required = false) String idListaGrisTlf)
			throws BOException, IOException, RestClientException, UnauthorizedException {
		
		try {	
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Map<String, Object> mapListaGrisTlf =
					objListaGrisTelefonoBO.actualizarListaGrisTlf(objListaGrisDTO, idListaGrisTlf, usuarioLogin.getIdUsuario(), 
						usuarioLogin.getIdEmpresa(),usuarioLogin.getUsuario());
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapListaGrisTlf) , HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}
	}
	
	@RequestMapping(value = "/eliminarListGrisTlf/{idListaGrisTlf}", method = RequestMethod.DELETE)
	public ResponseEntity<?> eliminarPlantilla(
			@RequestHeader(value = "Accept-Language", 	required = false) String strLanguage,
			@PathVariable (value = "idListaGrisTlf", required = false) String idListaGrisTlf)
			throws BOException, IOException, RestClientException, UnauthorizedException {
		try {
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			
			Map<String, Object> mapListaGris =
					objListaGrisTelefonoBO.eliminarListaGrisTlf(idListaGrisTlf, usuarioLogin.getIdUsuario(), usuarioLogin.getIdEmpresa(),usuarioLogin.getUsuario());
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapListaGris) , HttpStatus.OK);
		}catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}
	}
	@RequestMapping(value = "/listarTodas", method = RequestMethod.GET)
	public ResponseEntity<?> getListarTodas(
			@RequestHeader(value = "Accept-Language", required = false) String strLanguage)

	throws BOException, IOException, RestClientException, UnauthorizedException {
		try {
			Map<String, Object> mapListaGrisTlf = objListaGrisTelefonoBO.listarTodas();
					
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapListaGrisTlf), HttpStatus.OK);
		} catch (BOException be){
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage),be.getData());
		}
	}
}
