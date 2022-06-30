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
import com.goit.notify.bo.INotListaNegraBO;
import com.goit.notify.dto.RequestListaGrisDTO;
import com.goit.notify.dto.RequestListaNegraDTO;
import com.goit.notify.dto.ResponseOk;
import com.goit.notify.dto.UsuarioLogin;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.exceptions.CustomExceptionHandler;
import com.goit.notify.exceptions.RestClientException;
import com.goit.notify.exceptions.UnauthorizedException;
import com.goit.notify.util.MensajesUtil;

@RestController
@RequestMapping("/listaGris")
public class NotListaGrisApi {
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(NotPlantillasApi.class);
	
	@Autowired
	private INotListaGrisBO objListaGrisBO;
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> saveListaGris(
			@RequestHeader(value = "Accept-Language", 	required = false) String strLanguage,
			@RequestBody RequestListaGrisDTO objListaGris)
			
		throws BOException, IOException, RestClientException, UnauthorizedException {
		
		try {	
			
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
	
			Map<String, Object> mapListaGris = objListaGrisBO.guardarListaGris(objListaGris, usuarioLogin.getIdUsuario(), 
						usuarioLogin.getIdEmpresa(),usuarioLogin.getUsuario());
				
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapListaGris) , HttpStatus.OK);

		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}
	}

	@RequestMapping(value = "/getListaGris/{idListaGris}/{idEmpresa}/{idUsuario}", method = RequestMethod.GET)
	public ResponseEntity<?> getListaGris(
			@RequestHeader(value = "Accept-Language", required = false) String strLanguage,
			@PathVariable(value ="idListaGris", required = false) String idListaGris,
			@PathVariable(value ="idEmpresa", required = false) String idEmpresa,
			@PathVariable(value ="idUsuario", required = false) String idUsuario)
			throws BOException, IOException, RestClientException, UnauthorizedException {

		try {
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			RequestListaGrisDTO mapListaGris = 
					objListaGrisBO.getListaGris(idListaGris, usuarioLogin.getIdEmpresa(), usuarioLogin.getIdUsuario());
			
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapListaGris), HttpStatus.OK);
		} catch (BOException be){
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage),be.getData());
		}
	}
	
	@RequestMapping(value = "/editarListaGris/{idListaGris}", method = RequestMethod.PUT)
	public ResponseEntity<?> editarPlantilla(
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage,
			@RequestBody RequestListaGrisDTO objListaGrisDTO,
			@PathVariable (value ="idListaGris", required = false) String idListaGris)
			throws BOException, IOException, RestClientException, UnauthorizedException {
		
		try {	
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Map<String, Object> mapListaGris =
					objListaGrisBO.actualizarListaGris(objListaGrisDTO, idListaGris, usuarioLogin.getIdUsuario(), 
						usuarioLogin.getIdEmpresa(),usuarioLogin.getUsuario());
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapListaGris) , HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}
	}
	
	@RequestMapping(value = "/eliminarListGris/{idListaGris}", method = RequestMethod.DELETE)
	public ResponseEntity<?> eliminarPlantilla(
			@RequestHeader(value = "Accept-Language", 	required = false) String strLanguage,
			@PathVariable (value = "idListaGris", required = false) String idListaGris)
			throws BOException, IOException, RestClientException, UnauthorizedException {
		try {
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			
			Map<String, Object> mapListaGris =
					objListaGrisBO.eliminarListaGris(idListaGris, usuarioLogin.getIdUsuario(), usuarioLogin.getIdEmpresa(),usuarioLogin.getUsuario());
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
	
			Map<String, Object> mapListaGris = objListaGrisBO.getListaTodas();
			
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapListaGris), HttpStatus.OK);
		} catch (BOException be){
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage),be.getData());
		}
	}
}
