package com.goit.notify.bo.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.goit.helper.FechasHelper;
import com.goit.helper.enums.FormatoFecha;
import com.goit.notify.bo.INotDetallePlantillaBO;
import com.goit.notify.dao.NotDetallePlantillaDAO;
import com.goit.notify.dao.NotPlantillasDAO;
import com.goit.notify.dto.RequestDetallePlantillaDTO;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.model.NotCampanas;
import com.goit.notify.model.NotDetallesPlantilla;
import com.goit.notify.model.NotPlantilla;
import com.goit.notify.util.ValidacionUtil;

@Service
public class NotDetallePlantillaBOImpl implements INotDetallePlantillaBO{
	
	@Autowired
	private NotDetallePlantillaDAO objDetallePlantillaDAO;
	
	@Autowired
	private NotPlantillasDAO objPlantillaDAO;
	
	//@Autowired
	// private NotFirmasDAO objFirmaDAO;
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor= {Exception.class})
	public Map<String, Object> postDetallePlantilla(RequestDetallePlantillaDTO objRequestDetallePlantillaDTO,
			String strIdUsuario, String strIdEmpresa, String strUsuario) 
			throws BOException{
		
		//Crear detalle Plantilla
		NotDetallesPlantilla objDetallesPlantilla = new NotDetallesPlantilla();
		
		Optional<NotPlantilla> objNotPlantilla =objPlantillaDAO.ValidacionPlantilla(objRequestDetallePlantillaDTO.getIdPlantilla());
		
		Date datFechaActual = new Date();		
		
		if(objNotPlantilla.isPresent()) {
			ValidacionUtil.validarCampoRequeridoBO(objRequestDetallePlantillaDTO.getContenido(), "not.campo.contenido");
			
			objDetallesPlantilla.setContenido(objRequestDetallePlantillaDTO.getContenido());

			if(objNotPlantilla.get().getTipo().equalsIgnoreCase("H")) {

				ValidacionUtil.validarCampoRequeridoBO(objRequestDetallePlantillaDTO.getNombreRemitente(), "not.campo.nombreRemitente");
				ValidacionUtil.validarCampoRequeridoBO(objRequestDetallePlantillaDTO.getMailRemitente(), "not.campo.mailRemitente");
				ValidacionUtil.validarCampoRequeridoBO(objRequestDetallePlantillaDTO.getCabecera(), "not.campo.cabecera");
				ValidacionUtil.validarCampoRequeridoBO(objRequestDetallePlantillaDTO.getPie(), "not.campo.pie");
				ValidacionUtil.validarCampoRequeridoBO(objRequestDetallePlantillaDTO.getAsunto(), "not.campo.asunto");
				
				objDetallesPlantilla.setNombre_remitente(objRequestDetallePlantillaDTO.getNombreRemitente());
				objDetallesPlantilla.setMail_remitente(objRequestDetallePlantillaDTO.getMailRemitente());
				objDetallesPlantilla.setCabecera(objRequestDetallePlantillaDTO.getCabecera());
				objDetallesPlantilla.setPie(objRequestDetallePlantillaDTO.getPie());
				objDetallesPlantilla.setAsunto(objRequestDetallePlantillaDTO.getAsunto());
			}
		}

		objDetallesPlantilla.setFechaCrea(FechasHelper.dateToString(datFechaActual, FormatoFecha.YYYY_MM_DD_hh_MM_SS));
		objDetallesPlantilla.setEstado("A");
		objDetallesPlantilla.setUsuarioCrea(strUsuario);
		objDetallesPlantilla.setId_detalle_plantilla(objNotPlantilla.get().getIdPlantilla());
		//objDetallesPlantilla.setNotPlantilla(objNotPlantilla.get());//.setNotDetallesPlantilla(objNotPlantilla.get());
		//objDetallesPlantilla.setNotFima(objNotPlantilla.get());
		objDetallePlantillaDAO.persist(objDetallesPlantilla);
		
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("idPlantilla", objDetallesPlantilla.getId_detalle_plantilla());
		
		return map;
	}

	@Override
	public Map<String, Object> getDetallePlantilla(Integer strIdPlantilla) throws BOException {
		
		ValidacionUtil.validarCampoRequeridoBO(strIdPlantilla, "not.campo.idDetallePlantilla");
		Optional<NotDetallesPlantilla> optDetallePlantilla = objDetallePlantillaDAO.find(strIdPlantilla);
		//Optional<NotPlantilla> objNotPlantilla =objPlantillaDAO.ValidacionPlantilla(optDetallePlantilla.get().getIdPlantilla());
		RequestDetallePlantillaDTO objRequestDetallePlantillaDTO = null;
		NotDetallesPlantilla objNotDetallesPlantilla = null;
		
		if (optDetallePlantilla.isPresent()) {
			objNotDetallesPlantilla = optDetallePlantilla.get();
			objRequestDetallePlantillaDTO = new RequestDetallePlantillaDTO();
			objRequestDetallePlantillaDTO.setIdPlantilla(objNotDetallesPlantilla.getIdPlantilla());
			objRequestDetallePlantillaDTO.setAsunto(objNotDetallesPlantilla.getAsunto());
			objRequestDetallePlantillaDTO.setCabecera(objNotDetallesPlantilla.getCabecera());
			objRequestDetallePlantillaDTO.setContenido(objNotDetallesPlantilla.getContenido());
			//objRequestDetallePlantillaDTO.setIdFirma(objNotDetallesPlantilla.getIdFirma());
			objRequestDetallePlantillaDTO.setMailRemitente(objNotDetallesPlantilla.getMail_remitente());
			objRequestDetallePlantillaDTO.setNombreRemitente(objNotDetallesPlantilla.getNombre_remitente());
			objRequestDetallePlantillaDTO.setPie(objNotDetallesPlantilla.getPie());
			//objRequestDetallePlantillaDTO.setNotPlantilla(objNotPlantilla.get().getTipo());
			//objRequestDetallePlantillaDTO.setTipo(objNotDetallesPlantilla.getT);
			//objNotPlantilla.get().getTipo()
		}
		
		
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("row", objRequestDetallePlantillaDTO);
		
		return map;
	}
}
