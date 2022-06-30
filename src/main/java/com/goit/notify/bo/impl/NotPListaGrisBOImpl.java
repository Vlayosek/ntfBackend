package com.goit.notify.bo.impl;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.goit.helper.FechasHelper;
import com.goit.helper.enums.Estado;
import com.goit.helper.enums.FormatoFecha;
import com.goit.notify.bo.INotListaGrisBO;
import com.goit.notify.dao.NotListaGrisDAO;
import com.goit.notify.dto.RequestListaGrisDTO;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.exceptions.RestClientException;
import com.goit.notify.exceptions.UnauthorizedException;
import com.goit.notify.model.NotListaGris;
import com.goit.notify.util.ValidacionUtil;

@Service
public class NotPListaGrisBOImpl implements INotListaGrisBO {

	@Autowired
	private NotListaGrisDAO objListaGrisDAO;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Map<String, Object> guardarListaGris(RequestListaGrisDTO objListaGrisDTO, String strIdUsuario,
			String strIdEmpresa, String strUsuario)
			throws BOException, IOException, RestClientException, UnauthorizedException {

		ValidacionUtil.validarCampoRequeridoBO(objListaGrisDTO.getIdTicket(), "not.campo.idTicket");

		NotListaGris objListaGris = null;
		objListaGris = new NotListaGris();
		Date datFechaActual = new Date();
		String uuid = null;
		uuid = UUID.randomUUID().toString();
		
		Long validaIdTicket = objListaGrisDAO.validaIdTicket(objListaGrisDTO.getIdTicket());
		
		if(validaIdTicket == 0)
			throw new BOException("not.campo.campoInvalido");

		objListaGris.setIdListaGris(uuid);
		objListaGris.setIdTicket(objListaGrisDTO.getIdTicket());
		objListaGris.setMail(objListaGrisDTO.getMail());
		objListaGris.setFechaRegistro(objListaGrisDTO.getFechaRegistro());
		objListaGris.setAsunto(objListaGrisDTO.getAsunto());
		objListaGris.setEstado(Estado.ACTIVO.getName());
		objListaGris.setObservacion(objListaGrisDTO.getObservacion());
		objListaGris.setFechaCrea(FechasHelper.dateToString(datFechaActual, FormatoFecha.YYYY_MM_DD_HH_MM_SS));
		objListaGris.setUsuarioCreacion(strUsuario);
		objListaGris.setIdUsuario(strIdUsuario);
		objListaGris.setIdEmpresa(strIdEmpresa);
		
		objListaGrisDAO.persist(objListaGris);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Observacion", objListaGrisDTO.getObservacion());

		return map;
	}

	@Override
	public RequestListaGrisDTO getListaGris(String idListaGris, String idEmpresa, String idUsuario)
			throws BOException, IOException, RestClientException, UnauthorizedException {
		ValidacionUtil.validarCampoRequeridoBO(idListaGris, "not.campo.idListaGris");
		ValidacionUtil.validarCampoRequeridoBO(idEmpresa, "not.campo.empresa");
		ValidacionUtil.validarCampoRequeridoBO(idUsuario, "not.campo.usuario");

		RequestListaGrisDTO grayList = null;
		Optional<NotListaGris> listaNegra = objListaGrisDAO.find(idListaGris);
		Long longCountListaGris = objListaGrisDAO.validarListaGrisCount(idListaGris, idEmpresa, idUsuario);
		Date datFechaActual = new Date();

		if (longCountListaGris == 0)
			throw new BOException("not.campo.campoInvalido");

		grayList = new RequestListaGrisDTO();
		if (listaNegra.isPresent())
			grayList.setMail(listaNegra.get().getMail());
		grayList.setFechaRegistro(FechasHelper.dateToString(datFechaActual, FormatoFecha.YYYY_MM_DD_HH_MM_SS));
		grayList.setAsunto(listaNegra.get().getAsunto());
		grayList.setEstado(listaNegra.get().getEstado());
		grayList.setIdTicket(listaNegra.get().getIdTicket());
		grayList.setObservacion(listaNegra.get().getObservacion());
		return grayList;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Map<String, Object> actualizarListaGris(RequestListaGrisDTO objListaGrisDTO, String idListaGris,
			String strIdUsuario, String strIdEmpresa, String strUsuario)
			throws BOException, IOException, RestClientException, UnauthorizedException {
		ValidacionUtil.validarCampoRequeridoBO(idListaGris, "not.campo.idListaGris");

		NotListaGris objListaGris = new NotListaGris();
		Date datFechaActual = new Date();
		Optional<NotListaGris> listaGris = objListaGrisDAO.find(idListaGris);

		if (listaGris.isPresent()) {
			listaGris.get().setMail(objListaGrisDTO.getMail());
			listaGris.get().setFechaRegistro(objListaGrisDTO.getFechaRegistro());
			listaGris.get().setAsunto(objListaGrisDTO.getAsunto());
			listaGris.get().setEstado(objListaGrisDTO.getEstado());
			listaGris.get().setObservacion(objListaGrisDTO.getObservacion());
			listaGris.get().setFechActualiza(FechasHelper.dateToString(datFechaActual, FormatoFecha.YYYY_MM_DD_HH_MM_SS));
			objListaGrisDAO.update(listaGris.get());

		} else if (objListaGris.getIdListaGris() != idListaGris)
			throw new BOException("not.warn.idListaNegraNoExiste", new Object[] { "not.campo.idPlantilla" });

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Lista Gris actualizada", idListaGris);
		return map;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Map<String, Object> eliminarListaGris(String idListaGris, String strIdUsuario, String strIdEmpresa,
			String strUsuario) throws BOException, IOException, RestClientException, UnauthorizedException {
		// Valido Campo requerido
		ValidacionUtil.validarCampoRequeridoBO(idListaGris, "not.campo.idListaGris");

		Date datFechaActual = new Date();
		// Valida que la plantilla exista y este en estado activo activo
		Optional<NotListaGris> objListaGris = objListaGrisDAO.ValidarListaGris(idListaGris);

		objListaGris.get().setEstado(Estado.INACTIVO.getName());
		objListaGris.get().setUsuarioInactiva(strUsuario);
		objListaGris.get().setFechaInactiva(FechasHelper.dateToString(datFechaActual, FormatoFecha.YYYY_MM_DD_HH_MM_SS));
		objListaGrisDAO.update(objListaGris.get());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Lista Gris Inactiva", idListaGris);
		return map;
	}

	@Override
	public Map<String, Object> getListaTodas()
			throws BOException, IOException, RestClientException, UnauthorizedException {
		Long countListaNegra = objListaGrisDAO.countListaGris();
		// llamamos al metodo Listar 
		List<RequestListaGrisDTO> lsConsultarListaNegra = objListaGrisDAO.listarTodas();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalFilas", countListaNegra);
		map.put("Fila", lsConsultarListaNegra);		
		return map;
	}	
}