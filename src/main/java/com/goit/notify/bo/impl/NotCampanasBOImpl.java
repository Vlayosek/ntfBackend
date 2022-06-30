package com.goit.notify.bo.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.goit.helper.FechasHelper;
import com.goit.helper.enums.FormatoFecha;
import com.goit.notify.bo.INotCampanasBO;
import com.goit.notify.dao.NotCampanasDAO;
import com.goit.notify.dto.ListaCampanaDTO;
import com.goit.notify.dto.RequestCampanasDTO;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.exceptions.RestClientException;
import com.goit.notify.exceptions.UnauthorizedException;
import com.goit.notify.model.NotCampanas;
import com.goit.notify.util.ValidacionUtil;

@Service
public class NotCampanasBOImpl implements INotCampanasBO{
	
	@Autowired
	private NotCampanasDAO objCampanasDAO;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor= {Exception.class})
	public Map<String, Object> postCrearCampanas(RequestCampanasDTO objRequestCampanasDTO,
			String strIdUsuario, String strIdEmpresa, String strUsuario) throws BOException {
		ValidacionUtil.validarCampoRequeridoBO(objRequestCampanasDTO.getCampana(),"not.campo.nombre");
		
		Date datFechaActual = new Date();
		
		NotCampanas objCampanas = new NotCampanas();
		
		objCampanas.setCampana(objRequestCampanasDTO.getCampana());
		objCampanas.setDescripcion(objRequestCampanasDTO.getDescripcion());
		objCampanas.setEstado("A");
		objCampanas.setIdUsuario(strIdUsuario);
		objCampanas.setIdEmpresa(strIdEmpresa);
		objCampanas.setUsuarioCrea(strUsuario);
		objCampanasDAO.persist(objCampanas);
		objCampanas.setFechaCrea(FechasHelper.dateToString(datFechaActual, FormatoFecha.YYYY_MM_DD_HH_MM_SS));
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("CampanaCrea", objCampanas.getId_campana());
		
		return map;
	}

	@Override
	//@Transactional(propagation= Propagation.REQUIRED, rollbackFor= {Exception.class}) se lo usa mejor para post put delete
	public Optional<NotCampanas> getCampanas(Integer strIdCampana, String strIdUsuario, String strIdEmpresa) throws BOException {
		
		ValidacionUtil.validarCampoRequeridoBO(strIdCampana, "not.campo.idCampana");
		
		Long longConsultarCampana = objCampanasDAO.valCampana(strIdUsuario, strIdEmpresa);
		
		if(longConsultarCampana == 0)
			throw new BOException("not.warn.campoUsuario", new Object[]{"usuario o empresa"});

		Optional<NotCampanas> optCampana = objCampanasDAO.find(strIdCampana);
		
		return optCampana;
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED, rollbackFor= {Exception.class})
	public Map<String, Object> putCampana(RequestCampanasDTO objRequestCampanasDTO, Integer strIdCampana,
			String strIdEmpresa, String strIdUsuario, String strUsuario)
			throws BOException, IOException, RestClientException, UnauthorizedException {
		NotCampanas objCampanas = new NotCampanas();
		ValidacionUtil.validarCampoRequeridoBO(strIdCampana, "not.campo.idCampana");
		
		Optional<NotCampanas> optCampanas = objCampanasDAO.find(strIdCampana);
		
		Date datFechaActual = new Date();
		
		if(optCampanas.isPresent()) {
			optCampanas.get().setCampana(objRequestCampanasDTO.getCampana());
			optCampanas.get().setDescripcion(objRequestCampanasDTO.getDescripcion());
			optCampanas.get().setUsuarioActualiza(strUsuario);
			optCampanas.get().setFechaActualiza(FechasHelper.dateToString(datFechaActual, FormatoFecha.YYYY_MM_DD_HH_MM_SS));
			objCampanasDAO.update(optCampanas.get());
		}		
		else if(objCampanas.getId_campana() != strIdCampana) {
			throw new BOException("not.warn.idCampanaNoExiste", new Object[]{"not.campo.idCampana"});
		}
		
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("idCampanaActualizada", strIdCampana);
		
		return map;
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED, rollbackFor= {Exception.class})
	public Map<String, Object> deleteCampana(Integer strIdCampana, String strIdEmpresa, String strIdUsuario,
			String strUsuario) throws BOException, IOException, RestClientException, UnauthorizedException {
		ValidacionUtil.validarCampoRequeridoBO(strIdCampana, "not.campo.idCampana");
		Optional<NotCampanas> optCampanas = objCampanasDAO.find(strIdCampana);
		Optional<NotCampanas> objCampanas = objCampanasDAO.ValidarCampanas(strIdCampana/*, strIdUsuario, strIdEmpresa*/);
		Date datFechaActual = new Date();
			objCampanas.get().setEstado("I");
			optCampanas.get().setUsuarioInactiva(strUsuario);
			optCampanas.get().setFechaInactiva(FechasHelper.dateToString(datFechaActual, FormatoFecha.YYYY_MM_DD_HH_MM_SS));
			objCampanasDAO.update(optCampanas.get());
			
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("campanaEliminada", strIdCampana);

		return map;
	}

	@Override
	public Map<String, Object> getListaCampana() throws BOException {
		
		//Long longConsultarCampana = objCampanasDAO.consultarCampana();
		
		List<ListaCampanaDTO> lisCampana = objCampanasDAO.listarCampana();
		
		Map<String, Object> map = new HashMap<String, Object>();
		//map.put("listaCampana", longConsultarCampana);
		map.put("Lista", lisCampana);
		return map;
	}

	@Override
	public List<ListaCampanaDTO> getListaCampanaId(String strIdUsuario, String strIdEmpresa) throws BOException {
		ValidacionUtil.validarCampoRequeridoBO(strIdUsuario, "not.campo.idUsuario");
		
		Long longConsultarCampana = objCampanasDAO.valCampana(strIdUsuario, strIdEmpresa);
		
		if(longConsultarCampana == 0)
			throw new BOException("not.warn.campoUsuario", new Object[]{"usuario o empresa"});
			
		//if(longConsultarCampana ==0)
			//throw new BOException("not.warn.campoEmpresa", new Object[] {"empresa"});
		
		List<ListaCampanaDTO> lisCampanaId = objCampanasDAO.listarCampanaId(strIdUsuario, strIdEmpresa);
		return lisCampanaId;
	}
	
	
}
