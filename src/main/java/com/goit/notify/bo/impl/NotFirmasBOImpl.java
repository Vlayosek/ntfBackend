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
import com.goit.notify.bo.INotFirmasBO;
import com.goit.notify.dao.NotFirmasDAO;
import com.goit.notify.dto.ListarFirmasDTO;
import com.goit.notify.dto.RequestFirmasDTO;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.exceptions.RestClientException;
import com.goit.notify.exceptions.UnauthorizedException;
import com.goit.notify.model.NotFirma;
import com.goit.notify.util.ValidacionUtil;

@Service
public class NotFirmasBOImpl implements INotFirmasBO{

	@Autowired
	private NotFirmasDAO objFirmasDAO;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class})
	public Map<String, Object> crearFirmas(RequestFirmasDTO objFirmaDTO, String strIdUsuario, String strIdEmpresa,
			String strUsuario) throws BOException, IOException, RestClientException, UnauthorizedException {
		ValidacionUtil.validarCampoRequeridoBO(objFirmaDTO.getContenido(), "not.campo.contenido");
		ValidacionUtil.validarCampoRequeridoBO(objFirmaDTO.getLink(), "not.campo.link");
		
		Date dateFechaActual = new Date();
		String firma = "Notify";
		NotFirma objFirma = new NotFirma();
		objFirma.setContenido(objFirmaDTO.getContenido() + " " + firma);
		objFirma.setLink(objFirmaDTO.getLink());
		objFirma.setFechaCreacion(FechasHelper.dateToString(dateFechaActual, FormatoFecha.YYYY_MM_DD_HH_MM_SS));
		objFirma.setUsuarioCreacion(strUsuario);
		objFirma.setEsPublica("S");
		objFirma.setEstado("A");
		objFirma.setIdEmpresa(strIdEmpresa);
		objFirma.setIdUsuario(strIdUsuario);
		objFirmasDAO.persist(objFirma);
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Id de Firma Creada", objFirma.getIdFirma());

		return map;
	}

	@Override
	public Optional<NotFirma> mostrarFirma(Integer intIdFirma, String idEmpresa, String idUsuario) throws BOException {
		//Campo requerido
		ValidacionUtil.validarCampoRequeridoBO(intIdFirma, "not.campo.idFirma");
		ValidacionUtil.validarCampoRequeridoBO(idEmpresa, "not.campo.empresa"); 
	    ValidacionUtil.validarCampoRequeridoBO(idUsuario, "not.campo.usuario"); 
		     
		Long longConsultarFirma = objFirmasDAO.validarFirmasCount(idEmpresa, idUsuario); 
		if(longConsultarFirma == 0 ) 
			throw new BOException("campo invalido"); 
		    
		Optional<NotFirma> optFirma = objFirmasDAO.validacionFirma(intIdFirma);
		return optFirma;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class})
	public Map<String, Object> actualizarFirma(RequestFirmasDTO objFirmaDTO, Integer intIdFirma, String strIdUsuario,
			String strIdEmpresa, String strUsuario)
			throws BOException, IOException, RestClientException, UnauthorizedException {
		//Valida campo requerido
		ValidacionUtil.validarCampoRequeridoBO(intIdFirma, "not.campo.idFirma");

		NotFirma objFirma = new NotFirma();
		Date dateFechaActual = new Date();
		String firma = " Notify";

		Optional<NotFirma> optFirma = objFirmasDAO.find(intIdFirma);
		
		if(optFirma.isPresent()) {
			optFirma.get().setContenido(objFirmaDTO.getContenido() + firma);
			optFirma.get().setLink(objFirmaDTO.getLink());
			optFirma.get().setIdUsuario(strIdUsuario);
			objFirma.setIdUsuario(strIdUsuario);
			optFirma.get().setEsPublica(objFirmaDTO.getEsPublica());
			optFirma.get().setFechActualiza(FechasHelper.dateToString(dateFechaActual, FormatoFecha.YYYY_MM_DD_HH_MM_SS));
			objFirmasDAO.update(optFirma.get());
	
		}else if(objFirma.getIdFirma() != intIdFirma) {
			throw new BOException("not.warn.idPlantillaNoExiste", new Object[]{"not.campo.idPlantilla"}); 
		}

		Map<String,Object> map=new HashMap<String,Object>();
		map.put("idPlantilla", intIdFirma);
	
		return map;
	}

	@Override
	public Map<String, Object> listarFirmas() throws BOException {
		Long countFirma = objFirmasDAO.countFirma();
		List<ListarFirmasDTO> lsConsultarFirmas = objFirmasDAO.listarFirmas();

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("totalFilas", countFirma);
		map.put("Fila", lsConsultarFirmas);
		
		return map;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class})
	public Map<String, Object> eliminarFirma(Integer intIdFirma, String strIdUsuario, String strIdEmpresa,
			String strUsuario) throws BOException, IOException, RestClientException, UnauthorizedException {
		//Valido Campo requerido
		ValidacionUtil.validarCampoRequeridoBO(intIdFirma, "not.campo.idFirma");
		Date dateFechaActual = new Date();
		//Valida que exista Firma y Valida que la firma este en estado activo 
		Optional<NotFirma> optFirma = objFirmasDAO.validacionFirma(intIdFirma);
		optFirma.get().setEstado("I");
		optFirma.get().setFechaInactiva(FechasHelper.dateToString(dateFechaActual, FormatoFecha.YYYY_MM_DD_HH_MM_SS));
		objFirmasDAO.update(optFirma.get());
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("Plantilla Inactiva", intIdFirma);
		return map;
	}
}
