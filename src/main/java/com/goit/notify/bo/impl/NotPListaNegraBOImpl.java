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
import com.goit.notify.bo.INotListaNegraBO;
import com.goit.notify.dao.NotListaNegraDAO;
import com.goit.notify.dto.RequestListaNegraDTO;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.exceptions.RestClientException;
import com.goit.notify.exceptions.UnauthorizedException;
import com.goit.notify.model.NotListaNegra;
import com.goit.notify.util.ValidacionUtil;

@Service
public class NotPListaNegraBOImpl implements INotListaNegraBO {

	@Autowired
	private NotListaNegraDAO objListaNegraDAO;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Map<String, Object> guardarListaNegra(RequestListaNegraDTO listaNegraDTO, String strIdUsuario,
			String strIdEmpresa, String strUsuario)
			throws BOException, IOException, RestClientException, UnauthorizedException {

		ValidacionUtil.validarCampoRequeridoBO(listaNegraDTO.getIdTicket(), "not.campo.idTicket");

		NotListaNegra objListaNegra = new NotListaNegra();
		Date datFechaActual = new Date();
		String uuid = null;
		uuid = UUID.randomUUID().toString();
		
		Long validaIdTicket = objListaNegraDAO.validaIdTicket(listaNegraDTO.getIdTicket());
		
		if(validaIdTicket == 0)
			throw new BOException("not.campo.campoInvalido");

		objListaNegra.setIdListaNegra(uuid);
		objListaNegra.setIdTicket(listaNegraDTO.getIdTicket());
		objListaNegra.setMail(listaNegraDTO.getMail());
		objListaNegra.setAsunto(listaNegraDTO.getAsunto());
		objListaNegra.setEstado(Estado.ACTIVO.getName());
		objListaNegra.setObservacion(listaNegraDTO.getObservacion());
		objListaNegra.setContenidoRebote(listaNegraDTO.getContenidoRebote());
		objListaNegra.setFechaRegistro(listaNegraDTO.getFechaRegistro());
		objListaNegra.setIdUsuario(strIdUsuario);
		objListaNegra.setIdEmpresa(strIdEmpresa);
		objListaNegra.setFechaCrea(FechasHelper.dateToString(datFechaActual, FormatoFecha.YYYY_MM_DD_HH_MM_SS));
		objListaNegra.setUsuarioCreacion(strUsuario);
		objListaNegraDAO.persist(objListaNegra);
		
		System.out.println("IDS " + strIdUsuario + " " + strIdEmpresa);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Contenido Rebote", listaNegraDTO.getContenidoRebote());

		return map;
	}

	@Override
	public RequestListaNegraDTO getListaNegra(String idListaNegra, String strIdUsuario, String strIdEmpresa,
			String strUsuario) throws BOException, IOException, RestClientException, UnauthorizedException {
		ValidacionUtil.validarCampoRequeridoBO(idListaNegra, "not.campo.idListaNegra");
		ValidacionUtil.validarCampoRequeridoBO(strIdEmpresa, "not.campo.empresa");
		ValidacionUtil.validarCampoRequeridoBO(strIdUsuario, "not.campo.usuario");

		RequestListaNegraDTO blackList = null;
		Optional<NotListaNegra> listaNegra = objListaNegraDAO.find(idListaNegra);
		Long longCountListaNegra = objListaNegraDAO.validarListaNegraCount(strIdEmpresa, strIdUsuario);
		Date datFechaActual = new Date();

		if (longCountListaNegra == 0)
			throw new BOException("not.campo.campoInvalido");

		blackList = new RequestListaNegraDTO();
		if (listaNegra.isPresent())
			blackList.setMail(listaNegra.get().getMail());
			blackList.setFechaRegistro(listaNegra.get().getFechaRegistro());
			blackList.setAsunto(listaNegra.get().getAsunto());
			blackList.setEstado(listaNegra.get().getEstado());
			blackList.setObservacion(listaNegra.get().getObservacion());
			blackList.setIdTicket(listaNegra.get().getIdTicket());
			blackList.setContenidoRebote(listaNegra.get().getContenidoRebote());
		return blackList;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Map<String, Object> actualizarListaNegra(RequestListaNegraDTO objListaNegraDTO, String idListaNegra, String strIdUsuario, String strIdEmpresa,
			String strUsuario)
			throws BOException, IOException, RestClientException, UnauthorizedException {
		ValidacionUtil.validarCampoRequeridoBO(idListaNegra, "not.campo.idListaNegra");

		NotListaNegra objListaNegra = new NotListaNegra();
		Date datFechaActual = new Date();
		Optional<NotListaNegra> listaNegra = objListaNegraDAO.find(idListaNegra);

		if (listaNegra.isPresent()) {
			listaNegra.get().setMail(objListaNegraDTO.getMail());
			listaNegra.get().setFechaRegistro(objListaNegraDTO.getFechaRegistro());
			listaNegra.get().setAsunto(objListaNegraDTO.getAsunto());
			listaNegra.get().setEstado(objListaNegraDTO.getEstado());
			listaNegra.get().setObservacion(objListaNegraDTO.getObservacion());
			listaNegra.get().setContenidoRebote(objListaNegraDTO.getContenidoRebote());
			listaNegra.get().setFechActualiza(FechasHelper.dateToString(datFechaActual, FormatoFecha.YYYY_MM_DD_HH_MM_SS));
			objListaNegraDAO.update(listaNegra.get());

		} else if (objListaNegra.getIdListaNegra() != idListaNegra)
			throw new BOException("not.warn.idListaNegraNoExiste",new Object[]{"not.campo.idPlantilla"});

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Lista Negra actualizada", idListaNegra);
		return map;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Map<String, Object> eliminarListaNegra(String idListaNegra, String strIdEmpresa, String strIdUsuario,
			String strUsuario) throws BOException, IOException, RestClientException, UnauthorizedException {
		ValidacionUtil.validarCampoRequeridoBO(idListaNegra, "not.campo.idListaNegra");
		
		Date datFechaActual = new Date();

		Optional<NotListaNegra> objListaNegra = objListaNegraDAO.Valida(idListaNegra);
		objListaNegra.get().setEstado(Estado.INACTIVO.getName());
		objListaNegra.get().setUsuarioInactiva(strUsuario);
		objListaNegra.get().setFechaInactiva(FechasHelper.dateToString(datFechaActual, FormatoFecha.YYYY_MM_DD_HH_MM_SS));
		
		objListaNegraDAO.update(objListaNegra.get());
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Plantilla Inactiva", idListaNegra);
		return map;
	}
	
	@Override
	public Map<String, Object> getListaTodas() throws BOException {
		Long countListaNegra = objListaNegraDAO.countListaNegra();
		// llamamos al metodo Listar 
		List<RequestListaNegraDTO> lsConsultarListaNegra = objListaNegraDAO.listarListaNegra();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalFilas", countListaNegra);
		map.put("Fila", lsConsultarListaNegra);

		return map;
	}
}
