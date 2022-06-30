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

import com.goit.notify.bo.INotListaNegraBO;
import com.goit.notify.dto.RequestListaNegraDTO;
import com.goit.notify.dto.ResponseOk;
import com.goit.notify.dto.UsuarioLogin;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.exceptions.CustomExceptionHandler;
import com.goit.notify.exceptions.RestClientException;
import com.goit.notify.exceptions.UnauthorizedException;
import com.goit.notify.util.MensajesUtil;

@RestController
@RequestMapping("/listaNegra")
public class NotListaNegraApi {
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(NotPlantillasApi.class);
	
	@Autowired
	private INotListaNegraBO objListaNegraBO;
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> saveListaNegra(
			@RequestHeader(value = "Accept-Language", 	required = false) String strLanguage,
			@RequestBody RequestListaNegraDTO objListaNegra)
			
		throws BOException, IOException, RestClientException, UnauthorizedException {
		
		try {	
			
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
	
			Map<String, Object> mapListaNegra =
			objListaNegraBO.guardarListaNegra(objListaNegra, usuarioLogin.getIdUsuario(), 
					usuarioLogin.getIdEmpresa(),usuarioLogin.getUsuario());
			
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapListaNegra) , HttpStatus.OK);

		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}
	}

	@RequestMapping(value = "/getListaNegra/{idListaNegra}/{idEmpresa}/{idUsuario}", method = RequestMethod.GET)
	public ResponseEntity<?> getListaNegra(
			@RequestHeader(value = "Accept-Language", required = false) String strLanguage,
			@PathVariable(value ="idListaNegra", required = false) String idListaNegra,
			@PathVariable(value ="idEmpresa", required = false) String idEmpresa,
			@PathVariable(value ="idUsuario", required = false) String idUsuario)
			throws BOException, IOException, RestClientException, UnauthorizedException {

		try {
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			RequestListaNegraDTO mapListaNegra = 
					objListaNegraBO.getListaNegra(idListaNegra, usuarioLogin.getIdUsuario(), 
							usuarioLogin.getIdEmpresa(),usuarioLogin.getUsuario());

			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapListaNegra), HttpStatus.OK);
		} catch (BOException be){
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage),be.getData());
		}
	}
	
	@RequestMapping(value = "/editarListaNegra/{idListaNegra}", method = RequestMethod.PUT)
	public ResponseEntity<?> editarPlantilla(
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage,
			@RequestBody RequestListaNegraDTO objListaNegra,
			@PathVariable (value ="idListaNegra", required = false) String idListaNegra)
			throws BOException, IOException, RestClientException, UnauthorizedException {
		
		try {	
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Map<String, Object> mapListaNegra =
					objListaNegraBO.actualizarListaNegra(objListaNegra, idListaNegra,usuarioLogin.getIdUsuario(), 
							usuarioLogin.getIdEmpresa(),usuarioLogin.getUsuario());
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapListaNegra) , HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}
	}
	
	@RequestMapping(value = "/eliminarListNegra/{idListaNegra}", method = RequestMethod.DELETE)
	public ResponseEntity<?> eliminarListNegra(
			@RequestHeader(value = "Accept-Language", 	required = false) String strLanguage,
			@PathVariable (value = "idListaNegra", required = false) String idListaNegra)
			//@PathVariable (value ="idEmpresa", required = false) String idEmpresa,
			//@PathVariable (value ="idUsuario", required = false) String idUsuario)
			throws BOException, IOException, RestClientException, UnauthorizedException {
		try {
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			
			Map<String, Object> mapListaNegra =
					objListaNegraBO.eliminarListaNegra(idListaNegra, usuarioLogin.getIdEmpresa(),
							usuarioLogin.getIdUsuario(), usuarioLogin.getUsuario());
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapListaNegra) , HttpStatus.OK);
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
	
			Map<String, Object> mapListaNegra = objListaNegraBO.getListaTodas();
			
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapListaNegra), HttpStatus.OK);
		} catch (BOException be){
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage),be.getData());
		}
	}
}
