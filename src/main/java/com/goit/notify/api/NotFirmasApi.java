package com.goit.notify.api;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

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

import com.goit.notify.bo.INotFirmasBO;
import com.goit.notify.dto.RequestFirmasDTO;
import com.goit.notify.dto.ResponseOk;
import com.goit.notify.dto.UsuarioLogin;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.exceptions.CustomExceptionHandler;
import com.goit.notify.exceptions.RestClientException;
import com.goit.notify.exceptions.UnauthorizedException;
import com.goit.notify.model.NotFirma;
import com.goit.notify.util.MensajesUtil;

@RestController
@RequestMapping("/firmas")
public class NotFirmasApi {
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(NotPlantillasApi.class);
	
	@Autowired
	private INotFirmasBO objFirmasBO;
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> crearFirmas(
			@RequestHeader(value = "Accept-Language", 	required = false) String strLanguage,
			@RequestBody RequestFirmasDTO objFirmaDTO)
			throws BOException, IOException, RestClientException, UnauthorizedException {
	try {	
			
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
	
			Map<String, Object> mapFirma =
					objFirmasBO.crearFirmas(objFirmaDTO,
					usuarioLogin.getIdUsuario(),usuarioLogin.getIdEmpresa(),usuarioLogin.getUsuario());
			
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapFirma) , HttpStatus.OK);

		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}
	}
	
	@RequestMapping(value = "/mostrarFirma/{idFirma}/{idEmpresa}/{idUsuario}", method = RequestMethod.GET)
	public ResponseEntity<?> mostrarPlantilla(
			@RequestHeader(value = "Accept-Language", required = false) String strLanguage,
			@PathVariable(value ="idFirma", required = false) Integer intIdFirma,
			@PathVariable(value ="idEmpresa", required = false) String idEmpresa, 
		    @PathVariable(value ="idUsuario", required = false) String idUsuario) 
	throws BOException, IOException, RestClientException, UnauthorizedException {
		try {
			
			Optional<NotFirma> mapFirma = objFirmasBO.mostrarFirma(intIdFirma, idEmpresa, idUsuario);

			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapFirma), HttpStatus.OK);
		} catch (BOException be){
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage),be.getData());
		}
	}

	@RequestMapping(value = "/editarFirma/{idFirma}/{idUsuario}/{idEmpresa}", method = RequestMethod.PUT)
	public ResponseEntity<?> editarFirma(
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage,
			@RequestBody RequestFirmasDTO objFirmasDTO,
			@PathVariable (value ="idFirma", required = false) Integer intIdFirma,
			@PathVariable (value ="idUsuario", required = false) String idUsuario,
			@PathVariable (value ="idEmpresa", required = false) String idEmpresa)
			throws BOException, IOException, RestClientException, UnauthorizedException {
		try {	
			
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Map<String, Object> mapPlantilla =
					objFirmasBO.actualizarFirma(objFirmasDTO, intIdFirma, 
							usuarioLogin.getIdUsuario(),usuarioLogin.getIdEmpresa(),usuarioLogin.getUsuario());

			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapPlantilla) , HttpStatus.OK);
			
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}

	}
	
	@RequestMapping(value = "/listarFirmas", method = RequestMethod.GET)
	public ResponseEntity<?> listarFirmas(
			@RequestHeader(value = "Accept-Language", required = false) String strLanguage)

	throws BOException, IOException, RestClientException, UnauthorizedException {

		try {
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
	
			Map<String, Object> mapFirma =
					objFirmasBO.listarFirmas();
			
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapFirma), HttpStatus.OK);
		} catch (BOException be){
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage),be.getData());
		}
	}
	
	@RequestMapping(value = "/eliminarFirma/{idFirma}", method = RequestMethod.DELETE)
	public ResponseEntity<?> eliminarFirma(
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage, 
			@PathVariable (value ="idFirma", required = false) Integer intIdFirma)
	throws BOException, IOException, RestClientException, UnauthorizedException {

		try {	
			
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Map<String, Object> mapFirma =
					objFirmasBO.eliminarFirma(intIdFirma, usuarioLogin.getIdUsuario(),usuarioLogin.getIdEmpresa(),usuarioLogin.getUsuario());
					
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapFirma) , HttpStatus.OK);
			
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}
	}
}