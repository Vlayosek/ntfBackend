package com.goit.notify.api;

import java.io.IOException;
import java.util.List;

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

import com.goit.notify.bo.INotImagenesUsuarioBO;
import com.goit.notify.dto.RequestImagenesUsuarioDTO;
import com.goit.notify.dto.ResponseOk;
import com.goit.notify.dto.UsuarioLogin;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.exceptions.CustomExceptionHandler;
import com.goit.notify.util.MensajesUtil;

@RestController
@RequestMapping("/imagenes-usuario")
public class NotImagenesUsuarioApi {

	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(NotDestinatariosApi.class);

	@Autowired
	private INotImagenesUsuarioBO objINotImagenesUsuarioBO;
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> postImagenesUsuario(
			@RequestHeader(value = "Accept-Language", required = false) String strLanguage,
			@RequestParam(value = "idUsuario", required = false) String strIdUsuario,
			@RequestBody List<RequestImagenesUsuarioDTO> lsRequestImagenesUsuarioDTO)
			throws BOException, ClassNotFoundException, IOException {
		try {
			logger.info("Peticion entrante de Guardado de Imagen Usuario....");
			UsuarioLogin userLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();

			objINotImagenesUsuarioBO.postImagenUsuario(lsRequestImagenesUsuarioDTO, strIdUsuario,
					userLogin.getIdUsuario(), userLogin.getIdEmpresa(), userLogin.getUsuario());

			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					null), HttpStatus.OK);
		} catch (BOException e) {
			logger.error(" ERROR => " + e.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(e.getTranslatedMessage(strLanguage), e.getData());
		}
	}

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<?> consultarImagenesUsuario(
			@RequestHeader(value = "Accept-Language", required = false) String strLanguage)
			throws BOException, ClassNotFoundException, IOException {
		try {
			logger.info("Peticion de Imagenes Usuario entrante....");
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					objINotImagenesUsuarioBO.consultarImagenesUsuario()), HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}

	}

	@RequestMapping(value = "/imagenes-por-usuario", method = RequestMethod.GET)
	public ResponseEntity<?> consultarImagenesUsuarioXIdUsuario(
			@RequestHeader(value = "Accept-Language", required = false) String strLanguage,
			@RequestParam(value = "idUsuario", required = false) String strIdUsuario)
			throws BOException, ClassNotFoundException, IOException {
		try {
			logger.info("Peticion entrante de Consulta Imagenes Por Id Usuario...");
			return new ResponseEntity<>(
					new ResponseOk(
							MensajesUtil.getMensaje("not.response.ok",
									MensajesUtil.validateSupportedLocale(strLanguage)),
							objINotImagenesUsuarioBO.consultarImagenesXUsuario(strIdUsuario)),
					HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}

	}

	@RequestMapping(value = "/{idImagen}", method = RequestMethod.GET)
	public ResponseEntity<?> consultarImagenXId(
			@RequestHeader(value = "Accept-Language", required = false) String strLanguage,
			@PathVariable(name = "idImagen", required = false) Integer intImagen)
			throws BOException, ClassNotFoundException, IOException {
		try {
			logger.info("Peticion entrante de Imagen Por ID ....");
			return new ResponseEntity<>(
					new ResponseOk(
							MensajesUtil.getMensaje("not.response.ok",
									MensajesUtil.validateSupportedLocale(strLanguage)),
							objINotImagenesUsuarioBO.consultarImagenXId(intImagen)),
					HttpStatus.OK);
		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}

	}

	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<?> inactivarImagen(
			@RequestHeader(value = "Accept-Language", required = false) String strLanguage,
			@RequestParam(value = "idImagen", required = false) Integer intIdImagen)
			throws BOException, ClassNotFoundException, IOException {
		try {
			logger.info("Peticion entrante de Inactivacion de Imagen....");
			UsuarioLogin userLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();

			objINotImagenesUsuarioBO.deleteImagen(intIdImagen,
					userLogin.getIdUsuario(), userLogin.getIdEmpresa(), userLogin.getUsuario());

			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					null), HttpStatus.OK);
		} catch (BOException e) {
			logger.error(" ERROR => " + e.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(e.getTranslatedMessage(strLanguage), e.getData());
		}
	}

	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<?> putImagenUsuario(
			@RequestHeader(value = "Accept-Language", required = false) String strLanguage,
			@RequestParam(value = "idImagen", required = false) Integer intIdImagen,
			@RequestBody RequestImagenesUsuarioDTO objRequestImagenesUsuarioDTO)
			throws BOException, ClassNotFoundException, IOException {
		try {
			logger.info("Peticion entrante de Actualizacion de Imagenes Usuario....");
			UsuarioLogin userLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();

			objINotImagenesUsuarioBO.putImagenUsuario(objRequestImagenesUsuarioDTO, intIdImagen,
					userLogin.getIdUsuario(), userLogin.getIdEmpresa(), userLogin.getUsuario());

			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					null), HttpStatus.OK);
		} catch (BOException e) {
			logger.error(" ERROR => " + e.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(e.getTranslatedMessage(strLanguage), e.getData());
		}
	}

}
