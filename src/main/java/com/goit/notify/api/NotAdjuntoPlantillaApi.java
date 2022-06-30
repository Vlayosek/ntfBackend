package com.goit.notify.api;

import java.io.IOException;
import java.text.Normalizer;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.goit.helper.StringHelper;
import com.goit.notify.bo.INotAdjuntoPlantillaBO;
import com.goit.notify.dto.RequestAdjuntoPlantillaDTO;
import com.goit.notify.dto.ResponseOk;
import com.goit.notify.dto.UsuarioLogin;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.exceptions.CustomExceptionHandler;
import com.goit.notify.util.MensajesUtil;

@RestController
@RequestMapping("/adjunto-plantilla")
public class NotAdjuntoPlantillaApi {

	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(NotDestinatariosApi.class);

	@Autowired
	private INotAdjuntoPlantillaBO objINotAdjuntoPlantillaBO;

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> postAdjuntoPlantilla(
			@RequestHeader(value = "Accept-Language", required = false) String strLanguage,
			@RequestParam(value = "idPlantilla", required = false) Integer intIdPlantilla,
			@RequestBody List<RequestAdjuntoPlantillaDTO> lsRequestAdjuntoPlantillaDTO)
			throws BOException, ClassNotFoundException, IOException {
		try {
			logger.info("Peticion entrante de Guardado de Adjuntos Plantilla....");
			UsuarioLogin userLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();

			objINotAdjuntoPlantillaBO.postAdjuntoPlantilla(lsRequestAdjuntoPlantillaDTO, intIdPlantilla, userLogin.getUsuario());

			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					null), HttpStatus.OK);
		} catch (BOException e) {
			logger.error(" ERROR => " + e.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(e.getTranslatedMessage(strLanguage), e.getData());
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> consultarAdjuntosPlantilla(
			@RequestHeader(value = "Accept-Language", required = false) String strLanguage)
			throws BOException, ClassNotFoundException, IOException {
		try {
			logger.info("Peticion de adjuntos Plantillas entrante....");
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					objINotAdjuntoPlantillaBO.consultarAdjuntoPlantilla()), HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}

	}

	@RequestMapping(value = "/adjuntos-por-plantilla", method = RequestMethod.GET)
	public ResponseEntity<?> consultarAdjuntosPlantillaXIdPlantilla(
			@RequestHeader(value = "Accept-Language", required = false) String strLanguage,
			@RequestParam(value = "idPlantilla", required = false) Integer intIdPlantilla)
			throws BOException, ClassNotFoundException, IOException {
		try {
			logger.info("Peticion entrante de Consulta Adjuntos Por Id Plantilla...");
			return new ResponseEntity<>(
					new ResponseOk(
							MensajesUtil.getMensaje("not.response.ok",
									MensajesUtil.validateSupportedLocale(strLanguage)),
							objINotAdjuntoPlantillaBO.consultarAdjuntoPlantillaXIdPlantilla(intIdPlantilla)),
					HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}

	}

	@RequestMapping(value = "/{idAdjuntoPlantilla}", method = RequestMethod.GET)
	public ResponseEntity<?> consultarAdjuntosPlantillaXId(
			@RequestHeader(value = "Accept-Language", required = false) String strLanguage,
			// @RequestParam(name = "idAdjuntoPlantilla", required = false) Integer
			// intAdjuntoPlantilla
			@PathVariable(name = "idAdjuntoPlantilla", required = false) Integer intAdjuntoPlantilla)
			throws BOException, ClassNotFoundException, IOException {
		try {
			logger.info("Peticion entrante de adjuntos Plantillas Por ID ....");
			return new ResponseEntity<>(
					new ResponseOk(
							MensajesUtil.getMensaje("not.response.ok",
									MensajesUtil.validateSupportedLocale(strLanguage)),
							objINotAdjuntoPlantillaBO.consultarAdjuntoPlantillaXId(intAdjuntoPlantilla)),
					HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}

	}

	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<?> inactivarAdjuntoPlantilla(
			@RequestHeader(value = "Accept-Language", required = false) String strLanguage,
			@RequestParam(value = "intAdjuntoPlantilla", required = false) Integer intAdjuntoPlantilla)
			throws BOException, ClassNotFoundException, IOException {
		try {
			logger.info("Peticion entrante de Inactivacion de Adjuntos Plantilla....");
			UsuarioLogin userLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();

			objINotAdjuntoPlantillaBO.deleteAdjuntoPlantilla(intAdjuntoPlantilla, userLogin.getUsuario());

			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					null), HttpStatus.OK);
		} catch (BOException e) {
			logger.error(" ERROR => " + e.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(e.getTranslatedMessage(strLanguage), e.getData());
		}
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<?> putAdjuntoPlantilla(
			@RequestHeader(value = "Accept-Language", required = false) String strLanguage,
			@RequestParam(value = "intAdjuntoPlantilla", required = false) Integer intAdjuntoPlantilla,
			@RequestBody RequestAdjuntoPlantillaDTO objRequestAdjuntoPlantilla)
			throws BOException, ClassNotFoundException, IOException {
		try {
			logger.info("Peticion entrante de Guardado de Adjuntos Plantilla....");
			UsuarioLogin userLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();

			objINotAdjuntoPlantillaBO.putAdjuntoPlantilla(objRequestAdjuntoPlantilla, intAdjuntoPlantilla, userLogin.getUsuario());

			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					null), HttpStatus.OK);
		} catch (BOException e) {
			logger.error(" ERROR => " + e.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(e.getTranslatedMessage(strLanguage), e.getData());
		}
	}
	
	/*
	@RequestMapping(value="/prueba",method = RequestMethod.GET)
	public String postDestinararioFile(){

		String output = StringHelper.normalizeString("pingüi @nós!!!? % ?¡??@&/%?#qweasd!");
 				
 		return output;
	}*/

}
