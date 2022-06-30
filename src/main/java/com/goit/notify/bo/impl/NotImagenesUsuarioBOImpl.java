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
import com.goit.notify.bo.INotImagenesUsuarioBO;
import com.goit.notify.dao.NotImagenesUsuarioDAO;
import com.goit.notify.dto.GeneralImagenesUsuarioDTO;
import com.goit.notify.dto.GetImagenesUsuarioDTO;
import com.goit.notify.dto.RequestImagenesUsuarioDTO;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.model.NotImagenesUsuario;
import com.goit.notify.util.ValidacionUtil;


@Service
public class NotImagenesUsuarioBOImpl implements INotImagenesUsuarioBO {
    
    @Autowired
    private NotImagenesUsuarioDAO objNotImagenesUsuarioDAO;

    @Override
    public Map<String, Object> consultarImagenesUsuario() throws BOException , ClassNotFoundException, IOException{
    	Long longConsultarImagenesUsuario = objNotImagenesUsuarioDAO.consultarImagenesUsuarioCount();
    	List<GeneralImagenesUsuarioDTO> lsGeneralImagenesUsuarioDTO= objNotImagenesUsuarioDAO.consultarImagenesUsuario();
    	Map<String, Object> mapResult = new HashMap<String, Object>();
    	mapResult.put("totalRows", longConsultarImagenesUsuario);
    	mapResult.put("row", lsGeneralImagenesUsuarioDTO);
    	
    	return mapResult;
    }

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Map<String, Object> postImagenUsuario(List<RequestImagenesUsuarioDTO> lsRequestImagenesUsuarioDTO,
			String strIdUsuario, String idUsuario, String idEmpresa, String usuario)
			throws BOException, ClassNotFoundException, IOException {
		
		NotImagenesUsuario objNotImagenesUsuario = null;
		
		for (RequestImagenesUsuarioDTO requestImagenesUsuarioDTO : lsRequestImagenesUsuarioDTO) {
			objNotImagenesUsuario = new NotImagenesUsuario();
			objNotImagenesUsuario.setIdEmpresa(requestImagenesUsuarioDTO.getIdEmpresa());
			objNotImagenesUsuario.setIdUsuario(requestImagenesUsuarioDTO.getIdUsuario());
			objNotImagenesUsuario.setNombre(requestImagenesUsuarioDTO.getNombre().trim());
			objNotImagenesUsuario.setRutaLocal(requestImagenesUsuarioDTO.getRutaLocal().trim());
			objNotImagenesUsuario.setUrlPublica(requestImagenesUsuarioDTO.getUrlPublica().trim());
			objNotImagenesUsuario.setEsPublica(requestImagenesUsuarioDTO.getEsPublica());
			objNotImagenesUsuario.setFechaCrea(FechasHelper.dateToString(new Date(), FormatoFecha.YYYY_MM_DD));
			objNotImagenesUsuario.setEstado("A");
			objNotImagenesUsuario.setUsuarioCrea(usuario);
			
			objNotImagenesUsuarioDAO.persist(objNotImagenesUsuario);
		}
		Map<String,Object> map=new HashMap<String,Object>() ;
        map.put("idUsuario", strIdUsuario);
        return map;
	}

	@Override
	public Map<String, Object> consultarImagenXId(Integer intImagen)
			throws BOException, ClassNotFoundException, IOException {
		//Valida que el ID Imagen sera requerido
        ValidacionUtil.validarCampoRequeridoBO(intImagen,"not.campo.idUsuario");

        Optional<NotImagenesUsuario> optNotImagenesUsuario= objNotImagenesUsuarioDAO.find(intImagen);

        NotImagenesUsuario objNotImagenesUsuario = null;
        GetImagenesUsuarioDTO objGetImagenesUsuarioDTO = null;

        if(optNotImagenesUsuario.isPresent()){
        	objNotImagenesUsuario = optNotImagenesUsuario.get();
        	objGetImagenesUsuarioDTO = new GetImagenesUsuarioDTO();
        	objGetImagenesUsuarioDTO.setIdImagenUsuario(objNotImagenesUsuario.getIdImagenUsuario());
        	objGetImagenesUsuarioDTO.setIdEmpresa(objNotImagenesUsuario.getIdEmpresa());
        	objGetImagenesUsuarioDTO.setIdUsuario(objNotImagenesUsuario.getIdUsuario());
        	objGetImagenesUsuarioDTO.setNombre(objNotImagenesUsuario.getNombre());
        	objGetImagenesUsuarioDTO.setRutaLocal(objNotImagenesUsuario.getRutaLocal());
        	objGetImagenesUsuarioDTO.setUrlPublica(objNotImagenesUsuario.getUrlPublica());
        	objGetImagenesUsuarioDTO.setEsPublica(objNotImagenesUsuario.getEsPublica());
        	objGetImagenesUsuarioDTO.setEstado(objNotImagenesUsuario.getEstado());
        }
        
        Map<String, Object> mapResult = new HashMap<String, Object>();
    	mapResult.put("row", objGetImagenesUsuarioDTO);
    	
    	return mapResult;

	}

	@Override
	public Map<String, Object> consultarImagenesXUsuario(String strIdUsuario)
			throws BOException, ClassNotFoundException, IOException {
		//Valida que el ID Usuario sera requerido
        ValidacionUtil.validarCampoRequeridoBO(strIdUsuario,"not.campo.idUsuario");
        
        List<GeneralImagenesUsuarioDTO> lsGeneralImagenesUsuarioDTO = objNotImagenesUsuarioDAO.consultarImagenesXUsuario(strIdUsuario);
        
        Map<String,Object> map=new HashMap<String,Object>() ;
        map.put("idUsuario", strIdUsuario);
        map.put("row", lsGeneralImagenesUsuarioDTO);
        return map;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Map<String, Object> deleteImagen(Integer idImagen, String idUsuario, String idEmpresa,
			String usuario) throws BOException, ClassNotFoundException, IOException {
				//Valido Campo requerido
				ValidacionUtil.validarCampoRequeridoBO(idImagen, "not.campo.idImagen");
			
				//Vlaido que exista y que este activo
				Optional<NotImagenesUsuario> optNotImagenesUsuario = objNotImagenesUsuarioDAO.find(idImagen);
				
				//Le cambio por estado Inactivo (Eliminado Logico)
				optNotImagenesUsuario.get().setEstado("I");
				optNotImagenesUsuario.get().setUsuarioInactiva(usuario);
				optNotImagenesUsuario.get().setFechaInactiva(FechasHelper.dateToString(new Date(), FormatoFecha.YYYY_MM_DD));
				
				//Envio a Actualizar
				objNotImagenesUsuarioDAO.update(optNotImagenesUsuario.get());
				
				
				Map<String,Object> map=new HashMap<String,Object>() ;
		        map.put("idImagen", idImagen);
		        return map;
			
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class })
	public Map<String, Object> putImagenUsuario(RequestImagenesUsuarioDTO objRequestImagenesUsuarioDTO,
			Integer intIdImagen, String idUsuario, String idEmpresa, String usuario)
			throws BOException, ClassNotFoundException, IOException {
		
				//Valido Campo requerido
				ValidacionUtil.validarCampoRequeridoBO(intIdImagen, "not.campo.idImagen");
				
				Optional<NotImagenesUsuario> optNotImagenesUsuario = objNotImagenesUsuarioDAO.findWithValidacionCampo(intIdImagen);

				if(optNotImagenesUsuario.isPresent()) {
					optNotImagenesUsuario.get().setIdEmpresa(objRequestImagenesUsuarioDTO.getIdEmpresa()); 
					optNotImagenesUsuario.get().setIdUsuario(objRequestImagenesUsuarioDTO.getIdUsuario()); 
					optNotImagenesUsuario.get().setNombre(objRequestImagenesUsuarioDTO.getNombre().trim());
					optNotImagenesUsuario.get().setRutaLocal(objRequestImagenesUsuarioDTO.getRutaLocal().trim());
					optNotImagenesUsuario.get().setUrlPublica(objRequestImagenesUsuarioDTO.getUrlPublica().trim());
					optNotImagenesUsuario.get().setEsPublica(objRequestImagenesUsuarioDTO.getEsPublica());
					optNotImagenesUsuario.get().setUsuarioActualiza(usuario);
					optNotImagenesUsuario.get().setFechaActualiza(FechasHelper.dateToString(new Date(), FormatoFecha.YYYY_MM_DD));
					
				}
				
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("idImagen", optNotImagenesUsuario.get().getIdImagenUsuario());
				
				return map;
	}
}
