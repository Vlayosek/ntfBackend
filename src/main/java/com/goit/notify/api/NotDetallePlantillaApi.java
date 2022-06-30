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

import com.goit.notify.bo.INotDetallePlantillaBO;
import com.goit.notify.dto.ResponseOk;
import com.goit.notify.dto.RequestDetallePlantillaDTO;
import com.goit.notify.dto.UsuarioLogin;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.exceptions.CustomExceptionHandler;
import com.goit.notify.exceptions.RestClientException;
import com.goit.notify.exceptions.UnauthorizedException;
import com.goit.notify.model.NotDetallesPlantilla;
import com.goit.notify.model.NotPlantilla;
import com.goit.notify.util.MensajesUtil;

@RestController
@RequestMapping("/not_detalles_plantilla")
public class NotDetallePlantillaApi {
	
	@SuppressWarnings("unused")
	private static final Logger logger = LogManager.getLogger(NotCampanasApi.class);
	
	@Autowired
	private INotDetallePlantillaBO objINotDetallePlantillaBO;
	
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<?> postDetallePlantilla(
			@RequestHeader(	value = "Accept-Language", 	required = false) String strLanguage,
			@RequestBody RequestDetallePlantillaDTO objRequestDetallePlantillaDTO)
		throws BOException, IOException, RestClientException, UnauthorizedException{
		NotPlantilla objNotPlantilla = new NotPlantilla();
		try {
			
			UsuarioLogin usuarioLogin = (UsuarioLogin) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			
			Map<String, Object> mapDetallePlantilla = 
					objINotDetallePlantillaBO.postDetallePlantilla(objRequestDetallePlantillaDTO,
							usuarioLogin.getIdUsuario(), usuarioLogin.getIdEmpresa(), usuarioLogin.getUsuario());
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapDetallePlantilla), HttpStatus.OK);

		}catch (BOException e) {
			logger.error("Error =>" + e.getTranslatedMessage(strLanguage));
			throw new CustomExceptionHandler(e.getTranslatedMessage(strLanguage), e.getData());
		}
	}
	
	//GET
	@RequestMapping(value = "/getDetallePlantilla/{idDetallePlantilla}", method = RequestMethod.GET)
	public ResponseEntity<?> getDetallePlantilla(
			@RequestHeader(value = "Accept-Language", 	required = false) String strLanguage,
			@PathVariable(value = "idDetallePlantilla", required = false) Integer intIdDetallePlantilla)
	throws BOException, IOException, RestClientException, UnauthorizedException{
		try {
			Map<String, Object> mapDetallesPlantilla = 
					objINotDetallePlantillaBO.getDetallePlantilla(intIdDetallePlantilla);
			
			return new ResponseEntity<>(new ResponseOk(
					MensajesUtil.getMensaje("not.response.ok", MensajesUtil.validateSupportedLocale(strLanguage)),
					mapDetallesPlantilla), HttpStatus.OK);
		}catch (BOException e) {
			throw new CustomExceptionHandler(e.getTranslatedMessage(strLanguage), e.getData());
		}
	}
}