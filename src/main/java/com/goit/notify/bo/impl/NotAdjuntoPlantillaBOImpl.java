package com.goit.notify.bo.impl;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.goit.helper.FechasHelper;
import com.goit.helper.enums.FormatoFecha;
import com.goit.notify.bo.INotAdjuntoPlantillaBO;
import com.goit.notify.dao.NotAdjuntoPlantillaDAO;
import com.goit.notify.dao.NotPlantillasDAO;
import com.goit.notify.dto.GeneralAdjuntoPlantillaDTO;
import com.goit.notify.dto.GetAdjuntoPlantillaDTO;
import com.goit.notify.dto.RequestAdjuntoPlantillaDTO;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.model.NotAdjuntoPlantilla;
import com.goit.notify.model.NotPlantilla;
import com.goit.notify.util.ValidacionUtil;


@Service
public class NotAdjuntoPlantillaBOImpl implements INotAdjuntoPlantillaBO {

    @Autowired
    private NotAdjuntoPlantillaDAO objNotAdjuntoPlantillaDAO;
    
    @Autowired
    private NotPlantillasDAO objNotPlantillasDAO;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
    public Map<String, Object> postAdjuntoPlantilla(List<RequestAdjuntoPlantillaDTO> lsRequestAdjuntoPlantilla, Integer idPlantilla,String usuario) throws BOException , ClassNotFoundException, IOException {
        ValidacionUtil.validarCampoRequeridoBO(idPlantilla,"not.campo.idPlantilla");

        //Busca la plantilla y valido que exista
        Optional<NotPlantilla> objNotPlantilla =objNotPlantillasDAO.ValidacionPlantilla(idPlantilla);
        
        NotAdjuntoPlantilla objNotAdjuntoPlantilla = null;

        //Recorro el objeto recibido en el request
        for (RequestAdjuntoPlantillaDTO objRequestAdjuntoPlantillaDTO:
             lsRequestAdjuntoPlantilla) {
        	
     		String[] arrTipoExtensiones = {"HTML", "TXT", "ENRIQUECIDO"};
     		
     		 //Valida las estensiones
     		 if (!Arrays.stream(arrTipoExtensiones).anyMatch(StringUtils.upperCase(objRequestAdjuntoPlantillaDTO.getExtension().toUpperCase())::equals))
 				throw new BOException("not.warn.campoInvalido", new Object[]{"not.campo.extension"});
     		
 
            objNotAdjuntoPlantilla = new NotAdjuntoPlantilla();
            objNotAdjuntoPlantilla.setIdPlantilla(objNotPlantilla.get().getIdPlantilla());
            objNotAdjuntoPlantilla.setNombreArchivo(objRequestAdjuntoPlantillaDTO.getNombreArchivo().trim());
            objNotAdjuntoPlantilla.setRutaLocal(objRequestAdjuntoPlantillaDTO.getRutaLocal().trim());
            objNotAdjuntoPlantilla.setExtension(objRequestAdjuntoPlantillaDTO.getExtension().trim());
            objNotAdjuntoPlantilla.setEstado("A");
            objNotAdjuntoPlantilla.setUsuarioCrea(usuario);
            objNotAdjuntoPlantilla.setFechaCrea(FechasHelper.dateToString(new Date(), FormatoFecha.YYYY_MM_DD));

            objNotAdjuntoPlantillaDAO.persist(objNotAdjuntoPlantilla);
        }
        
        Map<String,Object> map=new HashMap<String,Object>() ;
        map.put("msg", "Adjunto Plantillas creadas correctamente");
        
        return map;
    }

    @Override
    public Map<String, Object> consultarAdjuntoPlantillaXId(Integer intAdjuntoPlantilla) throws BOException , ClassNotFoundException, IOException{
        //Valida que el ID Adjunto Plantilla sera requerido
        ValidacionUtil.validarCampoRequeridoBO(intAdjuntoPlantilla,"not.campo.idAdjuntoPlantilla");

        Optional<NotAdjuntoPlantilla> optNotAdjuntoPlantilla = objNotAdjuntoPlantillaDAO.find(intAdjuntoPlantilla);

        NotAdjuntoPlantilla objNotAdjuntoPlantilla = null;
        GetAdjuntoPlantillaDTO objGeneralAdjuntoPlantillaDTO = null;

        if(optNotAdjuntoPlantilla.isPresent()){
            objNotAdjuntoPlantilla = optNotAdjuntoPlantilla.get();
            objGeneralAdjuntoPlantillaDTO = new GetAdjuntoPlantillaDTO();
            objGeneralAdjuntoPlantillaDTO.setIdAdjuntoPlantilla(objNotAdjuntoPlantilla.getIdAdjuntoPlantilla());
            objGeneralAdjuntoPlantillaDTO.setIdPlantilla(objNotAdjuntoPlantilla.getIdPlantilla());
            objGeneralAdjuntoPlantillaDTO.setNombreArchivo(objNotAdjuntoPlantilla.getNombreArchivo());
            objGeneralAdjuntoPlantillaDTO.setRutaLocal(objNotAdjuntoPlantilla.getRutaLocal());
            objGeneralAdjuntoPlantillaDTO.setExtension(objNotAdjuntoPlantilla.getExtension());
        }

        Map<String,Object> map=new HashMap<String,Object>() ;
        map.put("row", objGeneralAdjuntoPlantillaDTO);
        return map;
    }

    @Override
    public Map<String, Object> consultarAdjuntoPlantilla() throws BOException , ClassNotFoundException, IOException{
    	Long longConsultarAdjuntosPlantillas = objNotAdjuntoPlantillaDAO.consultarAdjuntosPlantillasCount();
    	List<GeneralAdjuntoPlantillaDTO> lsGeneralAdjuntoPlantillaDTO = objNotAdjuntoPlantillaDAO.consultarAdjuntosPlantillas();
    	Map<String, Object> mapResult = new HashMap<String, Object>();
    	mapResult.put("totalRows", longConsultarAdjuntosPlantillas);
    	mapResult.put("row", lsGeneralAdjuntoPlantillaDTO);
    	
    	return mapResult;
    }

    @Override
    public Map<String, Object> consultarAdjuntoPlantillaXIdPlantilla(Integer idPlantilla) throws BOException, ClassNotFoundException, IOException {
        //Valida que el ID Plantilla sera requerido
        ValidacionUtil.validarCampoRequeridoBO(idPlantilla,"not.campo.idPlantilla");

        List<GeneralAdjuntoPlantillaDTO> lsGeneralAdjuntoPlantillaDTO = objNotAdjuntoPlantillaDAO.consultarAdjuntoPlantillaXIdPlantilla(idPlantilla);

        Map<String,Object> map=new HashMap<String,Object>() ;
        map.put("idPlantilla", idPlantilla);
        map.put("row", lsGeneralAdjuntoPlantillaDTO);
        return map;
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Map<String, Object> deleteAdjuntoPlantilla(Integer idAdjuntoPlantilla,String usuario)
			throws BOException, ClassNotFoundException, IOException {
		//Valido Campo requerido
		ValidacionUtil.validarCampoRequeridoBO(idAdjuntoPlantilla, "not.campo.idAdjuntoPlantilla");
	
		//Vlaido que exista y que este activo
		Optional<NotAdjuntoPlantilla> optNotAdjuntoPlantilla = objNotAdjuntoPlantillaDAO.findWithValidacionCampo(idAdjuntoPlantilla);
		
		//Le cambio por estado Inactivo (Eliminado Logico)
		optNotAdjuntoPlantilla.get().setEstado("I");
		optNotAdjuntoPlantilla.get().setUsuarioInactiva(usuario);
		optNotAdjuntoPlantilla.get().setFechaInactiva(FechasHelper.dateToString(new Date(), FormatoFecha.YYYY_MM_DD));
		
		//Envio a Actualizar
		objNotAdjuntoPlantillaDAO.update(optNotAdjuntoPlantilla.get());
		
		 Map<String,Object> map=new HashMap<String,Object>() ;
		
		 map.put("IdAdjuntoPlantilla", idAdjuntoPlantilla);
		
		return map;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Map<String, Object> putAdjuntoPlantilla(RequestAdjuntoPlantillaDTO objRequestAdjuntoPlantilla,
			Integer idPlantilla, String usuario)
			throws BOException, ClassNotFoundException, IOException {
		
		//Valido Campo requerido
		ValidacionUtil.validarCampoRequeridoBO(idPlantilla, "not.campo.idAdjuntoPlantilla");
		
		//Valido que exista y que este activo
		Optional<NotAdjuntoPlantilla> optNotAdjuntoPlantilla = objNotAdjuntoPlantillaDAO.findWithValidacionCampo(idPlantilla);
		
		if(optNotAdjuntoPlantilla.isPresent()) {
			optNotAdjuntoPlantilla.get().setNombreArchivo(objRequestAdjuntoPlantilla.getNombreArchivo().trim()); 
			optNotAdjuntoPlantilla.get().setRutaLocal(objRequestAdjuntoPlantilla.getRutaLocal().trim()); 
			optNotAdjuntoPlantilla.get().setExtension(objRequestAdjuntoPlantilla.getExtension().trim()); 
			optNotAdjuntoPlantilla.get().setUsuarioActualiza(usuario);
			optNotAdjuntoPlantilla.get().setFechaActualiza(FechasHelper.dateToString(new Date(), FormatoFecha.YYYY_MM_DD));
			
		}
		
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("nombreArchivo", objRequestAdjuntoPlantilla.getNombreArchivo());
		
		return map;
	}
}
