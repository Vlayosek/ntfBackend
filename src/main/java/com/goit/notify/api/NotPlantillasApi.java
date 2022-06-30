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

import com.goit.notify.bo.INotPlantillasBO;
import com.goit.notify.dto.RequestPlantillasDTO;
import com.goit.notify.dto.ResponseOk;
import com.goit.notify.dto.UsuarioLogin;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.exceptions.CustomExceptionHandler;
import com.goit.notify.exceptions.RestClientException;
import com.goit.notify.exceptions.UnauthorizedException;
import com.goit.notify.model.NotPlantilla;
import com.goit.notify.util.MensajesUtil;

@RestController
@RequestMapping("/plantillas")
public class NotPlantillasApi {

	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(NotPlantillasApi.class);
	
	@Autowired
	private INotPlantillasBO objINotPlantillasBO;
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> postPlantilla(
			@RequestHeader(value = "Accept-Language", 	required = false) String strLanguage,
			@RequestBody RequestPlantillasDTO objPlantillasDTO)
			
		throws BOException, IOException, RestClientException, UnauthorizedException {
		
		try {	
			
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
	
			Map<String, Object> mapPlantilla =
			objINotPlantillasBO.postCrearPlantillas(objPlantillasDTO,
					usuarioLogin.getIdUsuario(),usuarioLogin.getIdEmpresa(),usuarioLogin.getUsuario());
			
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapPlantilla) , HttpStatus.OK);

		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}
	}

	@RequestMapping(value = "/getPlantilla/{idPlantilla}/{idEmpresa}/{idUsuario}", method = RequestMethod.GET)
	public ResponseEntity<?> getPlantilla(
			@RequestHeader(value = "Accept-Language", required = false) String strLanguage,
			@PathVariable(value ="idPlantilla", required = false) Integer idPlantilla,
			@PathVariable(value ="idEmpresa", required = false) String idEmpresa,
			@PathVariable(value ="idUsuario", required = false) String idUsuario)

			throws BOException, IOException, RestClientException, UnauthorizedException {

		try {
	
			Optional<NotPlantilla> mapPlantilla = objINotPlantillasBO.getPlantilla(idPlantilla, idEmpresa, idUsuario);
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapPlantilla), HttpStatus.OK);
		} catch (BOException be){
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage),be.getData());
		}
	}
	
	@RequestMapping(value = "/getPlantillas", method = RequestMethod.GET)
	public ResponseEntity<?> getListarPlantillas(
			@RequestHeader(value = "Accept-Language", required = false) String strLanguage)

	throws BOException, IOException, RestClientException, UnauthorizedException {

		try {
	
			Map<String, Object> mapPlantillas =
					objINotPlantillasBO.getPlantillas();
			
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapPlantillas), HttpStatus.OK);
		} catch (BOException be){
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage),be.getData());
		}
	}
	
	@RequestMapping(value = "/editarPlantilla/{idPlantilla}", method = RequestMethod.PUT)
	public ResponseEntity<?> editarPlantilla(
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage,
			@RequestBody RequestPlantillasDTO objPlantillasDTO,
			@PathVariable (value ="idPlantilla", required = false) Integer intIdPlantilla)
			throws BOException, IOException, RestClientException, UnauthorizedException {
		
		try {	
			
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Map<String, Object> mapPlantilla =
					objINotPlantillasBO.actualizarPlantillas(objPlantillasDTO, intIdPlantilla,
							usuarioLogin.getIdUsuario(),usuarioLogin.getIdEmpresa(),usuarioLogin.getUsuario());

			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapPlantilla) , HttpStatus.OK);
			
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}
	}

	@RequestMapping(value = "/eliminarPlantilla/{idPlantilla}", method = RequestMethod.DELETE)
	public ResponseEntity<?> eliminarPlantilla(
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage, 
			@PathVariable (value ="idPlantilla", required = false) Integer intIdPlantilla)
	throws BOException, IOException, RestClientException, UnauthorizedException {

		try {	
			
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Map<String, Object> mapPlantilla =
					objINotPlantillasBO.eliminarPlantilla(intIdPlantilla, usuarioLogin.getIdUsuario(),usuarioLogin.getIdEmpresa(),usuarioLogin.getUsuario());
			
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapPlantilla) , HttpStatus.OK);
			
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}
	}
		
	@RequestMapping(value = "/listarPorUsuario/{idUsuario}/{idEmpresa}", method = RequestMethod.GET)
	public ResponseEntity<?> listarPorUsuario(
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage, 
			@PathVariable (value ="idUsuario", required = false) String strUsuario,
			@PathVariable (value ="idEmpresa", required = false) String strEmpresa)
	throws BOException, IOException, RestClientException, UnauthorizedException {

		try {	
			
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Map<String, Object> mapPlantilla =
					objINotPlantillasBO.listarPlantillasPorUsuario(strUsuario, strEmpresa);
			
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapPlantilla) , HttpStatus.OK);
			
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}
	}
		
	@RequestMapping(value = "/listarPorTipo/{idUser}/{idEmpresa}/{tipo}", method = RequestMethod.GET)
	public ResponseEntity<?> listarPorTipo(
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage, 
			@PathVariable (value ="idUser", required = false) String idUser,
			@PathVariable (value ="idEmpresa", required = false) String idEmpresa,
			@PathVariable (value ="tipo", required = false) String tipo)

	throws BOException, IOException, RestClientException, UnauthorizedException {
		try {	
			
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Map<String, Object> mapPlantilla =
					objINotPlantillasBO.getTipo(idUser, idEmpresa, tipo);
			
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapPlantilla) , HttpStatus.OK);
			
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}
	}
	
	@RequestMapping(value = "/duplicarPlantilla/{idPlantilla}", method = RequestMethod.POST)
	public ResponseEntity<?> duplicarPlantilla(
			@RequestHeader(value = "Accept-Language", 	required = false) String strLanguage,
			@PathVariable (value ="idPlantilla", required = true) Integer idPlantilla)
			
		throws BOException, IOException, RestClientException, UnauthorizedException {
		
		try {	
			
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
	
			Map<String, Object> mapPlantilla =
			objINotPlantillasBO.copyPlantilla(idPlantilla, 
					usuarioLogin.getIdUsuario(),usuarioLogin.getIdEmpresa(),usuarioLogin.getUsuario());
			
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapPlantilla) , HttpStatus.OK);

		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}
	}
	
	@RequestMapping(value = "/listarPorNombre/{nombre}/{idUsuario}/{idEmpresa}", method = RequestMethod.GET)
	public ResponseEntity<?> listarPorNombre(
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage,
			@PathVariable(value ="nombre", required = false) String nombre,
			@PathVariable(value ="idUsuario", required = false) String idUsuario,
			@PathVariable(value ="idEmpresa", required = false) String idEmpresa)
	throws BOException, IOException, RestClientException, UnauthorizedException {
		try {	
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Map<String, Object> mapPlantilla =
					objINotPlantillasBO.consultarPorNombre(nombre, 
							usuarioLogin.getIdUsuario(),usuarioLogin.getIdEmpresa());	
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapPlantilla) , HttpStatus.OK);
			
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}
	}
	
	@RequestMapping(value = "/listarDesc/{idEmpresa}/{idUsuario}", method = RequestMethod.GET)
	public ResponseEntity<?> listarMasRecientes(
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage,
			@PathVariable(value ="idEmpresa", required = false) String idEmpresa,
			@PathVariable(value ="idUsuario", required = false) String idUsuario)
	throws BOException, IOException, RestClientException, UnauthorizedException {
		try {	
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			Map<String, Object> mapPlantilla =
					objINotPlantillasBO.consultarMasRecientes(idEmpresa, idUsuario);
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapPlantilla) , HttpStatus.OK);
			
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}
	}

}
