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
import com.goit.notify.bo.INotListaGrisTelefonoBO;
import com.goit.notify.dao.NotListaGrisTelefonoDAO;
import com.goit.notify.dto.RequestListaGrisDTO;
import com.goit.notify.dto.RequestListaGrisTlfDTO;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.exceptions.RestClientException;
import com.goit.notify.exceptions.UnauthorizedException;
import com.goit.notify.model.NotListaGrisTelefono;
import com.goit.notify.util.ValidacionUtil;
@Service
public class NotListaGrisTlfBOImpl implements INotListaGrisTelefonoBO {

	@Autowired
	private NotListaGrisTelefonoDAO objListaGrisTlfDAO;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class})
	public Map<String, Object> guardarListaGrisTlf(RequestListaGrisTlfDTO objListaGrisTlfDTO, String strIdUsuario,
			String strIdEmpresa, String strUsuario)
			throws BOException, IOException, RestClientException, UnauthorizedException {
		ValidacionUtil.validarCampoRequeridoBO(objListaGrisTlfDTO.getFechaRegistro(), "not.campo.fechaRegistro");
		ValidacionUtil.validarCampoRequeridoBO(objListaGrisTlfDTO.getTelefono(), "not.campo.telefono");


		NotListaGrisTelefono objListaGrisTlf = null;
		objListaGrisTlf = new NotListaGrisTelefono();
		Date datFechaActual = new Date();
		String uuid = null;
		uuid = UUID.randomUUID().toString();

		objListaGrisTlf.setIdListaGris(uuid);
		objListaGrisTlf.setTelefono(objListaGrisTlfDTO.getTelefono());
		objListaGrisTlf.setFechaRegistro(objListaGrisTlfDTO.getFechaRegistro());
		objListaGrisTlf.setAsunto(objListaGrisTlfDTO.getAsunto());
		objListaGrisTlf.setEstado(Estado.ACTIVO.getName());
		objListaGrisTlf.setObservacion(objListaGrisTlfDTO.getObservacion());
		objListaGrisTlf.setCanal(objListaGrisTlfDTO.getCanal());
		objListaGrisTlf.setFechaCrea(FechasHelper.dateToString(datFechaActual, FormatoFecha.YYYY_MM_DD_HH_MM_SS));
		objListaGrisTlf.setUsuarioCreacion(strUsuario);
		objListaGrisTlf.setIdUsuario(strIdUsuario);
		objListaGrisTlf.setIdEmpresa(strIdEmpresa);
		objListaGrisTlfDAO.persist(objListaGrisTlf);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Observacion", objListaGrisTlfDTO.getObservacion());

		return map;
	}

	@Override
	public RequestListaGrisTlfDTO getListaGrisTlf(String idListaGrisTlf, String idEmpresa, String idUsuario)
			throws BOException, IOException, RestClientException, UnauthorizedException {

		ValidacionUtil.validarCampoRequeridoBO(idListaGrisTlf, "not.campo.idListaGrisTlf");
		ValidacionUtil.validarCampoRequeridoBO(idEmpresa, "not.campo.empresa");
		ValidacionUtil.validarCampoRequeridoBO(idUsuario, "not.campo.usuario");

		RequestListaGrisTlfDTO listaGrisTlfDTO = null;
		listaGrisTlfDTO = new RequestListaGrisTlfDTO();
		
		Optional<NotListaGrisTelefono> listaGrisTlf = objListaGrisTlfDAO.find(idListaGrisTlf);
		/*Long longCountListaGrisTlf = objListaGrisTlfDAO.validarListaGrisTlfCount(idListaGrisTlf, idEmpresa, idUsuario);

		if (longCountListaGrisTlf == 0)
			throw new BOException("not.campo.campoInvalido");*/

		if (listaGrisTlf.isPresent())
			listaGrisTlfDTO.setTelefono(listaGrisTlf.get().getTelefono());
			listaGrisTlfDTO.setFechaRegistro(listaGrisTlf.get().getFechaRegistro());
			listaGrisTlfDTO.setAsunto(listaGrisTlf.get().getAsunto());
			listaGrisTlfDTO.setEstado(listaGrisTlf.get().getEstado());
			listaGrisTlfDTO.setObservacion(listaGrisTlf.get().getObservacion());
			listaGrisTlfDTO.setCanal(listaGrisTlf.get().getCanal());
			
		return listaGrisTlfDTO;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Map<String, Object> actualizarListaGrisTlf(RequestListaGrisTlfDTO objListaGrisTlfDTO, String idListaGrisTlf,
			String strIdUsuario, String strIdEmpresa, String strUsuario)
			throws BOException, IOException, RestClientException, UnauthorizedException {
		ValidacionUtil.validarCampoRequeridoBO(idListaGrisTlf, "not.campo.idListaGrisTlf");

		NotListaGrisTelefono objListaGrisTelefono = new NotListaGrisTelefono();
		Date datFechaActual = new Date();
		Optional<NotListaGrisTelefono> optListaGrisTelefono = objListaGrisTlfDAO.find(idListaGrisTlf);

		if (optListaGrisTelefono.isPresent()) {
			optListaGrisTelefono.get().setTelefono(objListaGrisTlfDTO.getTelefono());
			optListaGrisTelefono.get().setFechaRegistro(objListaGrisTlfDTO.getFechaRegistro());
			optListaGrisTelefono.get().setAsunto(objListaGrisTlfDTO.getAsunto());
			optListaGrisTelefono.get().setEstado(objListaGrisTlfDTO.getEstado());
			optListaGrisTelefono.get().setObservacion(objListaGrisTlfDTO.getObservacion());
			optListaGrisTelefono.get().setCanal(objListaGrisTlfDTO.getCanal());
			optListaGrisTelefono.get().setFechActualiza(FechasHelper.dateToString(datFechaActual, FormatoFecha.YYYY_MM_DD_HH_MM_SS));
			objListaGrisTlfDAO.update(optListaGrisTelefono.get());

		} else if (objListaGrisTelefono.getIdListaGris() != idListaGrisTlf)
			throw new BOException("not.warn.idListaGrisNoExiste" ,new Object[] { "not.campo.idListaGrisTlf" });

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Lista Gris Telefono actualizada", objListaGrisTlfDTO);
		return map;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Map<String, Object> eliminarListaGrisTlf(String idListaGrisTlf, String strIdUsuario, String strIdEmpresa,
			String strUsuario) throws BOException, IOException, RestClientException, UnauthorizedException {
		Date datFechaActual = new Date();
		ValidacionUtil.validarCampoRequeridoBO(idListaGrisTlf,"not.campo.idListaGrisTlf"); 
		
		Optional<NotListaGrisTelefono> optListaGrisTelefono = objListaGrisTlfDAO.ValidarListaGrisTelefono(idListaGrisTlf);
		/*Long longCountListaNegra = objListaGrisTlfDAO.validarListaGrisTlfCount(idListaGrisTlf, strIdEmpresa, strIdUsuario);
				
		if(longCountListaNegra == 0)
			throw new BOException("No existe ese campo");*/

		optListaGrisTelefono.get().setEstado(Estado.INACTIVO.getName());
		optListaGrisTelefono.get().setUsuarioInactiva(strUsuario);
		optListaGrisTelefono.get().setFechaInactiva(FechasHelper.dateToString(datFechaActual, FormatoFecha.YYYY_MM_DD_HH_MM_SS));
		objListaGrisTlfDAO.update(optListaGrisTelefono.get());
		 
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("Lista gris telefono Inactiva", idListaGrisTlf);
		return map;
	}

	@Override
	public Map<String, Object> listarTodas()
			throws BOException, IOException, RestClientException, UnauthorizedException {
		Long countListGris = objListaGrisTlfDAO.countListaGrisTLF();
		// llamamos al metodo Listar 
		List<RequestListaGrisTlfDTO> lsConsultarListaGris = objListaGrisTlfDAO.listarTodas();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalFilas", countListGris);
		map.put("Fila", lsConsultarListaGris);		
		return map;
	}

}
