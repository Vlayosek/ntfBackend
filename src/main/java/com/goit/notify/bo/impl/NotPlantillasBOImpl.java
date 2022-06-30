package com.goit.notify.bo.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.goit.helper.FechasHelper;
import com.goit.helper.enums.Estado;
import com.goit.helper.enums.FormatoFecha;
import com.goit.notify.bo.INotPlantillasBO;
import com.goit.notify.dao.NotAdjuntoPlantillaDAO;
import com.goit.notify.dao.NotDetallePlantillaDAO;
import com.goit.notify.dao.NotPlantillasDAO;
import com.goit.notify.dto.ListaPlantillasDTO;
import com.goit.notify.dto.RequestListaNegraDTO;
import com.goit.notify.dto.RequestPlantillasDTO;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.exceptions.RestClientException;
import com.goit.notify.exceptions.UnauthorizedException;
import com.goit.notify.model.NotAdjuntoPlantilla;
import com.goit.notify.model.NotDetallesPlantilla;
import com.goit.notify.model.NotPlantilla;
import com.goit.notify.util.ValidacionUtil;

@Service
public class NotPlantillasBOImpl implements INotPlantillasBO {

	@Autowired
	private NotPlantillasDAO objPlantillasDAO;
	@Autowired
	private NotDetallePlantillaDAO objDetallePlantillaDAO;
	@Autowired
	private NotAdjuntoPlantillaDAO objAdjuntoPlantillaDAO;

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Map<String, Object> postCrearPlantillas(RequestPlantillasDTO objPlantillasDTO, String strIdUsuario,
			String strIdEmpresa, String strUsuario) throws BOException {

		ValidacionUtil.validarCampoRequeridoBO(objPlantillasDTO.getNombre(), "not.campo.nombre");
		ValidacionUtil.validarCampoRequeridoBO(objPlantillasDTO.getTipo(), "not.campo.tipo");
		
		if(objPlantillasDTO.getTipo().equalsIgnoreCase("H"))
			throw new BOException("not.warn.CampoJson");

		// Valida que el tipo Plantilla solo sea (H, T y E)
		String[] arrTipoPlantillas = { "H", "T", "E" };

		if (!Arrays.stream(arrTipoPlantillas).anyMatch(StringUtils.upperCase(objPlantillasDTO.getTipo())::equals))
			throw new BOException("not.warn.paramNoValidTipo", new Object[] { "not.campo.tipo" });
		
		Date datFechaActual = new Date();
		NotPlantilla objPlantilla = new NotPlantilla();

		objPlantilla.setNombre(objPlantillasDTO.getNombre());
		objPlantilla.setDescripcion(objPlantillasDTO.getDescripcion());
		objPlantilla.setTipo(objPlantillasDTO.getTipo());
		objPlantilla.setEstado(Estado.ACTIVO.getName());
		objPlantilla.setIdUsuario(strIdUsuario);
		objPlantilla.setIdEmpresa(strIdEmpresa);
		objPlantilla.setUsuarioCreacion(strUsuario);
		objPlantilla.setFechaCreacion(FechasHelper.dateToString(datFechaActual, FormatoFecha.YYYY_MM_DD_HH_MM_SS));
		objPlantilla.setEsPublica("S");
		objPlantilla.setContenidoJson(objPlantillasDTO.getContenidoJson());
		objPlantillasDAO.persist(objPlantilla);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idPlantilla", objPlantilla.getIdPlantilla());

		return map;
	}

	@Override
	public Optional<NotPlantilla> getPlantilla(Integer idPlantilla, String idEmpresa, String idUsuario)
			throws BOException {
		
		ValidacionUtil.validarCampoRequeridoBO(idPlantilla, "not.campo.idPlantilla");
		ValidacionUtil.validarCampoRequeridoBO(idEmpresa, "not.campo.empresa");
		ValidacionUtil.validarCampoRequeridoBO(idUsuario, "not.campo.usuario");

		// Valida que exista id Empresa y usuario
		Long longConsultarPlantillas = objPlantillasDAO.validarPlantillasCount(idEmpresa, idUsuario);

		if (longConsultarPlantillas == 0)
			throw new BOException("not.campo.campoInvalido");
		
		Optional<NotPlantilla> objPlantilla = objPlantillasDAO.ValidacionPlantilla(idPlantilla);
		///Mostar las plantillas publicas y privadas
		return objPlantilla;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Map<String, Object> actualizarPlantillas(RequestPlantillasDTO objPlantillasDTO, Integer strIdPlantilla,
			String strIdUsuario, String strIdEmpresa, String strUsuario)
			throws BOException, IOException, RestClientException, UnauthorizedException {
		NotPlantilla objPlantilla = new NotPlantilla();
		Date datFechaActual = new Date();
		// Validar Campo Requerido ID_PLANTILLA
		ValidacionUtil.validarCampoRequeridoBO(strIdPlantilla, "not.campo.idPlantilla");

		// Busca la plantilla
		Optional<NotPlantilla> optPlantilla = objPlantillasDAO.find(strIdPlantilla);

		// Actualiza los campos
		if (optPlantilla.isPresent()) {
			optPlantilla.get().setNombre(objPlantillasDTO.getNombre());
			optPlantilla.get().setDescripcion(objPlantillasDTO.getDescripcion());
			optPlantilla.get().setTipo(objPlantillasDTO.getTipo());
			optPlantilla.get().setUsuarioActualiza(strUsuario);
			optPlantilla.get().setFechActualiza(FechasHelper.dateToString(datFechaActual, FormatoFecha.YYYY_MM_DD_HH_MM_SS));
			optPlantilla.get().setContenidoJson(objPlantillasDTO.getContenidoJson());

			objPlantillasDAO.update(optPlantilla.get());
		} else if (objPlantilla.getIdPlantilla() != strIdPlantilla)
			// valida que laplantilla exista
			throw new BOException("not.warn.idPlantillaNoExiste");

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idPlantilla", strIdPlantilla);

		return map;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Map<String, Object> eliminarPlantilla(Integer strIdPlantilla, String strIdUsuario, String strIdEmpresa,
			String strUsuario) throws BOException, IOException, RestClientException, UnauthorizedException {
		Date datFechaActual = new Date();
		// Valido Campo requerido
		ValidacionUtil.validarCampoRequeridoBO(strIdPlantilla, "not.campo.idPlantilla");
		// Valida que la plantilla exista y este en estado activo activo
		Optional<NotPlantilla> objPlantilla = objPlantillasDAO.ValidacionPlantilla(strIdPlantilla);
		objPlantilla.get().setEstado("I");
		objPlantilla.get().setUsuarioInactiva(strUsuario);
		objPlantilla.get().setFechaInactiva(FechasHelper.dateToString(datFechaActual, FormatoFecha.YYYY_MM_DD_HH_MM_SS));
		objPlantillasDAO.update(objPlantilla.get());

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Plantilla Inactiva", strIdPlantilla);
		return map;
	}

	// Listar plantillas publ y privadas q sea 'A'
	@Override
	public Map<String, Object> getPlantillas() throws BOException {
		Long countPlantilla = objPlantillasDAO.countPlantilla();
		// llamamos al metodo ListarPlantillas
		List<RequestPlantillasDTO> lsConsultarPlantillas = objPlantillasDAO.listarPlantillas();

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalFilas", countPlantilla);
		map.put("Fila", lsConsultarPlantillas);

		return map;
	}

	@Override
	public Map<String, Object> listarPlantillasPorUsuario(String idEmpresa, String idUsuario) throws BOException {
		ValidacionUtil.validarCampoRequeridoBO(idEmpresa, "not.campo.idEmpresa");
		ValidacionUtil.validarCampoRequeridoBO(idUsuario, "not.campo.usuario");
		Long countPlantilla = objPlantillasDAO.countPlantilla();

		Long longConsultarPlantillas = objPlantillasDAO.validarPlantillasCount(idEmpresa, idUsuario);

		if (longConsultarPlantillas == 0)
			throw new BOException("not.campo.campoInvalido");
		
		List<RequestPlantillasDTO> lsConsultarPlantillas = objPlantillasDAO.listarPlantillasPorUsuario(idEmpresa, idUsuario);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalFilas", countPlantilla);
		map.put("Fila", lsConsultarPlantillas);

		return map;
	}

	@Override
	public Map<String, Object> getTipo(String idUsuario, String idEmpresa, String tipo) throws BOException {
		ValidacionUtil.validarCampoRequeridoBO(tipo, "not.campo.tipo");
		ValidacionUtil.validarCampoRequeridoBO(idUsuario, "not.campo.idUsuario");
		ValidacionUtil.validarCampoRequeridoBO(idEmpresa, "not.campo.idEmpresa");

		// Busca y valida qie exista usuario y empresa
		Long longConsultarPlantillas = objPlantillasDAO.validarPlantillasCount(idEmpresa, idUsuario);

		if (longConsultarPlantillas == 0)
			throw new BOException("campo invalido");

		// Valida que solo acepte los tres tipos de plantilla (H, T, E)
		if (!"E".equalsIgnoreCase(tipo) && !"T".equalsIgnoreCase(tipo) && !"H".equalsIgnoreCase(tipo))
			throw new BOException("not.warn.paramNoValidTipo", new Object[] { "not.campo.tipo" });
		// Cuenta la cantidad de Plantillas
		Long countPlantilla = objPlantillasDAO.countPlantillaPorTipo(tipo);
		// Lista las plantillas por tipo
		List<RequestPlantillasDTO> lsConsultarPlantillaPorTipo = objPlantillasDAO.listarPlantillasPorTipo(idUsuario,
				idEmpresa, tipo);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("totalFilas", countPlantilla);
		map.put("Fila", lsConsultarPlantillaPorTipo);

		return map;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Map<String, Object> copyPlantilla(Integer idPlantilla, String strIdUsuario, String strIdEmpresa,
			String strUsuario) throws BOException {

		ValidacionUtil.validarCampoRequeridoBO(idPlantilla, "not.campo.idPlantilla");
		// Instancio una nueva plantilla
		NotDetallesPlantilla detalleP = new NotDetallesPlantilla();
		NotAdjuntoPlantilla adjuntP = null;
		NotPlantilla plantilla = new NotPlantilla();

		// Busca la plantilla por su id valida si existe y si esta activo
		NotPlantilla objPlantilla = objPlantillasDAO.ValidacionPlantilla(idPlantilla).get();
		System.out.println("IDDDD " + objPlantilla.getIdPlantilla());
		// Valida que las plantillas existan
		List<NotDetallesPlantilla> lsNotDetallesPlantilla = objDetallePlantillaDAO.consultarListDetaPlant(idPlantilla);
		List<NotAdjuntoPlantilla> lsNotAdjuntoPlantilla = objAdjuntoPlantillaDAO.consultarListAdjuntPlant(idPlantilla);
		System.out.println("IDDDDlsNotDetallesPlantilla: " + lsNotDetallesPlantilla);
		System.out.println("IDDDDlsNotAdjuntoPlantilla: " + lsNotAdjuntoPlantilla);

		// Declarar variable
		Date datFechaActual = new Date();

		// Si no existe la plantilla no se puede duplicar

		if (ObjectUtils.isEmpty(lsNotDetallesPlantilla) || ObjectUtils.isEmpty(lsNotAdjuntoPlantilla)) {
			throw new BOException("not.warn.campoInvalido", new Object[] { " este id Plantilla" });

		} else if (ObjectUtils.isNotEmpty(lsNotDetallesPlantilla) && ObjectUtils.isNotEmpty(lsNotAdjuntoPlantilla)) {
			Long lonPlantilla = objPlantillasDAO.contadorPlantillaCopia(objPlantilla.getNombre()) + 1;

			if (lonPlantilla == 1) {
				plantilla.setNombre(objPlantilla.getNombre() + " - copia");

			} else if (lonPlantilla > 1) {
				plantilla.setNombre(objPlantilla.getNombre() + " - copia" + "(" + lonPlantilla + ")");
			}
			// Persistir Plantilla
			plantilla.setDescripcion(objPlantilla.getDescripcion());
			plantilla.setTipo(objPlantilla.getTipo());
			plantilla.setEstado(objPlantilla.getEstado());
			plantilla.setFechaCreacion(FechasHelper.dateToString(datFechaActual, FormatoFecha.YYYY_MM_DD_HH_MM_SS));
			plantilla.setFechActualiza(null);
			plantilla.setFechaInactiva(null);
			plantilla.setUsuarioCreacion(strUsuario);
			plantilla.setUsuarioInactiva(null);
			plantilla.setIdUsuario(strIdUsuario);
			plantilla.setIdEmpresa(strIdEmpresa);
			plantilla.setEsPublica("S");
			plantilla.setContenidoJson(objPlantilla.getContenidoJson());
			objPlantillasDAO.persist(plantilla);

			// Persistir el cuerpo de la plantilla
			for (NotDetallesPlantilla objNotDetallesPlantilla : lsNotDetallesPlantilla) {
				detalleP.setNotPlantilla(plantilla);
				detalleP.setEstado(objNotDetallesPlantilla.getEstado());
				detalleP.setFechaCrea(FechasHelper.dateToString(datFechaActual, FormatoFecha.YYYY_MM_DD_HH_MM_SS));
				detalleP.setNombre_remitente(objNotDetallesPlantilla.getNombre_remitente());
				detalleP.setUsuarioCrea(objNotDetallesPlantilla.getUsuarioCrea());
				detalleP.setAsunto(objNotDetallesPlantilla.getAsunto());
				detalleP.setCabecera(objNotDetallesPlantilla.getCabecera());
				detalleP.setContenido(objNotDetallesPlantilla.getContenido());
				detalleP.setPie(objNotDetallesPlantilla.getPie());
				detalleP.setMail_remitente(objNotDetallesPlantilla.getMail_remitente());
				detalleP.setNombre_remitente(objNotDetallesPlantilla.getNombre_remitente());
				detalleP.setIdFirma(objNotDetallesPlantilla.getIdFirma());
				objDetallePlantillaDAO.persist(detalleP);
			}

			for (NotAdjuntoPlantilla objNotAdjuntoPlantilla : lsNotAdjuntoPlantilla) {
				adjuntP = new NotAdjuntoPlantilla();
				adjuntP.setNotPlantilla(plantilla);
				adjuntP.setNombreArchivo(objNotAdjuntoPlantilla.getNombreArchivo());
				adjuntP.setRutaLocal(objNotAdjuntoPlantilla.getRutaLocal());
				adjuntP.setExtension(objNotAdjuntoPlantilla.getExtension());
				adjuntP.setEstado(objNotAdjuntoPlantilla.getEstado());
				adjuntP.setUsuarioCrea(strUsuario);
				adjuntP.setFechaCrea(FechasHelper.dateToString(datFechaActual, FormatoFecha.YYYY_MM_DD_HH_MM_SS));
				objAdjuntoPlantillaDAO.persist(adjuntP);
			}
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idPlantillaNueva", plantilla.getNombre());

		return map;
	}

	@Override
	public Map<String, Object> consultarPorNombre(String nombre, String strIdUsuario, String strIdEmpresa)
			throws BOException, IOException, RestClientException, UnauthorizedException {
		ValidacionUtil.validarCampoRequeridoBO(strIdUsuario, "not.campo.usuario");
		ValidacionUtil.validarCampoRequeridoBO(strIdEmpresa, "not.campo.idEmpresa");
		
		List<RequestPlantillasDTO> listPlantilla = objPlantillasDAO.listarPorNombre(nombre, strIdUsuario, strIdEmpresa);

		if(listPlantilla.isEmpty()) {
			throw new BOException ("not.warn.inactivo", new Object[] {"not.campo.idPlantilla"});
		}
		
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Plantilla", listPlantilla);
		return map;
	}

	@Override
	public Map<String, Object> consultarMasRecientes(String strIdEmpresa, String strIdUsuario)
			throws BOException, IOException, RestClientException, UnauthorizedException {
		
		ValidacionUtil.validarCampoRequeridoBO(strIdEmpresa, "not.campo.empresa");
		ValidacionUtil.validarCampoRequeridoBO(strIdUsuario, "not.campo.usuario");
		// Valida que exista id Empresa y usuario
		Long longConsultarPlantillas = objPlantillasDAO.validarPlantillasCount(strIdEmpresa, strIdUsuario);
		System.out.println("COUNT" + longConsultarPlantillas);

		if (longConsultarPlantillas == 0)		
			throw new BOException("not.campo.campoInvalido");

		List<RequestPlantillasDTO> listPlantilla = objPlantillasDAO.listPlantillaDesc(strIdEmpresa, strIdUsuario);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Plantilla", listPlantilla);
		System.out.println("Listar Plantillas mas recientes...");
		return map;
	}
}
