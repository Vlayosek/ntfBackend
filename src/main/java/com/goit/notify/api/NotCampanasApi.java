package com.goit.notify.api;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
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

import com.goit.notify.bo.INotCampanasBO;
import com.goit.notify.dto.ListaCampanaDTO;
import com.goit.notify.dto.RequestCampanasDTO;
import com.goit.notify.dto.ResponseOk;
import com.goit.notify.dto.UsuarioLogin;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.exceptions.CustomExceptionHandler;
import com.goit.notify.exceptions.RestClientException;
import com.goit.notify.exceptions.UnauthorizedException;
import com.goit.notify.model.NotCampanas;
import com.goit.notify.util.MensajesUtil;

@RestController
@RequestMapping("/not_campanas")
public class NotCampanasApi {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(NotCampanasApi.class);
	
	@Autowired
	private INotCampanasBO objINotCampanasBO;
	
//----------------------------------------------POST---------------------------------------------------
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> postCampana(
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage,
			@RequestBody RequestCampanasDTO objRequestCampanasDTO)
	
		throws BOException, IOException, RestClientException, UnauthorizedException{
		
		try {	
			
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
	
			Map<String, Object> mapCampanas = 
					objINotCampanasBO.postCrearCampanas(objRequestCampanasDTO, 
							usuarioLogin.getIdUsuario(),usuarioLogin.getIdEmpresa(),usuarioLogin.getUsuario());
			
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapCampanas), HttpStatus.OK);

		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}
	}
	
//----------------------------------------------GET POR ID-------------------------------------------
	@RequestMapping(value = "/getCampanas/{idCampana}/{idUsuario}/{idEmpresa}", method = RequestMethod.GET)
	public ResponseEntity<?> getCampanas(
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage,
			@PathVariable(value ="idCampana", required = false) Integer intIdCampana,
			@PathVariable(value = "idUsuario", required = false) String strIdUsuario,
			@PathVariable(value = "idEmpresa", required = false) String strIdEmpresa
			)
	
			throws BOException, IOException, RestClientException, UnauthorizedException{
		
		try {
			
			Optional<NotCampanas> mapCampanas = 
			objINotCampanasBO.getCampanas(intIdCampana, strIdUsuario, strIdEmpresa);
			
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapCampanas), HttpStatus.OK);
		}catch(BOException e) {
			throw new CustomExceptionHandler(e.getTranslatedMessage(strLanguage),e.getData());
		}
	}

//----------------------------------------------GET POR ID-USUARIO-----------------------------------
	@RequestMapping(value = "/getCampanaLista/{idUsuario}/{idEmpresa}", method = RequestMethod.GET)
	public ResponseEntity<?> getListaCampanaId(
			@RequestHeader (value = "Accept-Language", required = false) String strLanguage,
			@PathVariable(value = "idUsuario", required = false) String strIdUsuario,
			@PathVariable(value = "idEmpresa", required = false) String strIdEmpresa)
			throws BOException, IOException, RestClientException, UnauthorizedException{
		try {
			//UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
				//.getPrincipal();
			List<ListaCampanaDTO> mapCampanas=
					objINotCampanasBO.getListaCampanaId(strIdUsuario, strIdEmpresa);
			
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapCampanas), HttpStatus.OK);
			
		}catch(BOException e) {
			throw new CustomExceptionHandler(e.getTranslatedMessage(strLanguage),e.getData());
		}
	}	
	
//-----------------------------------------GET LISTA PARA USUARIO------------------------------------
	@RequestMapping(value = "/getCampanaLista", method = RequestMethod.GET)
	public ResponseEntity<?> getListaCampana(
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage)
	
			throws BOException, IOException, RestClientException, UnauthorizedException{
		
		try {
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			
			Map<String, Object> mapCampanas = 
			objINotCampanasBO.getListaCampana();
			
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapCampanas), HttpStatus.OK);
		}catch(BOException e) {
			throw new CustomExceptionHandler(e.getTranslatedMessage(strLanguage),e.getData());
		}
	}

//----------------------------------------------PUT---------------------------------------------------
	@RequestMapping(value = "/actualizarCampana/{idCampana}", method = RequestMethod.PUT)
	public ResponseEntity<?> actualizarCampana(
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage,
			@RequestBody RequestCampanasDTO objRequestCampanasDTO,
			@PathVariable(value = "idCampana", required = false) Integer intIdCampana) 
			throws BOException, FileNotFoundException, ClassNotFoundException, IOException, RestClientException, UnauthorizedException{
		try {
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			
			Map<String, Object> mapCampanas = 
					objINotCampanasBO.putCampana(objRequestCampanasDTO, intIdCampana, 
							usuarioLogin.getIdUsuario(),usuarioLogin.getIdEmpresa(),usuarioLogin.getUsuario());
			
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapCampanas), HttpStatus.OK);
			
		}catch(BOException e) {
			logger.error(" ERROR => " + e.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(e.getTranslatedMessage(strLanguage),e.getData());
		}
	}
	
//----------------------------------------------DELETE---------------------------------------------------	
	@RequestMapping(value = "/eliminarCampana/{idCampana}", method = RequestMethod.DELETE)
	public ResponseEntity<?> eliminarCampana(
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage,
			@PathVariable(value = "idCampana", required = false) Integer intIdCampana) 
			throws BOException, FileNotFoundException, ClassNotFoundException, IOException, RestClientException, UnauthorizedException{
		try {
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			
			Map<String, Object> mapCampanas = 
					objINotCampanasBO.deleteCampana(intIdCampana, 
							usuarioLogin.getIdUsuario(),usuarioLogin.getIdEmpresa(),usuarioLogin.getUsuario());
			
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapCampanas), HttpStatus.OK);
			
		}catch(BOException e) {
			logger.error(" ERROR => " + e.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(e.getTranslatedMessage(strLanguage),e.getData());
		}
	}
}
