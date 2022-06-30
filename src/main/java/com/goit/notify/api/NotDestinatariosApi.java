package com.goit.notify.api;

import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.goit.notify.bo.INotDestinatariosBO;
import com.goit.notify.dto.CamposPersonalizadosDTO;
import com.goit.notify.dto.RequestDestinatariosDTO;
import com.goit.notify.dto.ResponseOk;
import com.goit.notify.dto.UsuarioLogin;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.exceptions.CustomExceptionHandler;
import com.goit.notify.exceptions.RestClientException;
import com.goit.notify.exceptions.UnauthorizedException;
import com.goit.notify.util.MensajesUtil;

@RestController
@RequestMapping("/destinatario")
public class NotDestinatariosApi {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(NotDestinatariosApi.class);
	
	@Autowired
	private INotDestinatariosBO objINotDestinatariosBO;
	
	@RequestMapping(method = RequestMethod.POST, consumes = {
			MediaType.MULTIPART_FORM_DATA_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> postDestinararioFile(
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage,
			@RequestParam(	value = "idGrupo", 	required = true) Integer intIdGrupo,
			@RequestHeader("Authorization") String strToken,
			@RequestHeader("Canal") String strCanal,
			@RequestParam(	value = "errorCompleto", 	required = false) Boolean booError,
			@RequestParam(	value = "tipoArchivo", 	required = true) String strTipoArchivo,
			@RequestParam (	value = "json", 	required = false) List<RequestDestinatariosDTO> lsRequestDestinatariosDTO,
			@RequestPart(name = "nombreCampos", required = true) List<CamposPersonalizadosDTO> lsNombreCampos,
			@RequestPart(name = "archivoDestinatario", required = true) MultipartFile archivoTarifario)
		throws BOException, IOException, RestClientException, UnauthorizedException {
		
		try {	
			
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			
			 return new ResponseEntity<>(new ResponseOk(
						MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
						objINotDestinatariosBO.postValidaTipoArchivo(strCanal,strToken,booError,strTipoArchivo,lsNombreCampos,archivoTarifario,lsRequestDestinatariosDTO,intIdGrupo,usuarioLogin.getIdUsuario(),usuarioLogin.getIdEmpresa(),usuarioLogin.getUsuario(),strLanguage)), HttpStatus.OK);

		} catch (BOException be) {
			logger.error(" ERROR => " + be.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(be.getTranslatedMessage(strLanguage), be.getData());
		}
	
	}
	
	@RequestMapping(value="/prueba",method = RequestMethod.GET)
	public String postDestinararioFile(){
		return "Prueba";
		
	}
}
