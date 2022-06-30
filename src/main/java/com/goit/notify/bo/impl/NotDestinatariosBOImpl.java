package com.goit.notify.bo.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.goit.helper.FechasHelper;
import com.goit.helper.StringHelper;
import com.goit.helper.enums.FormatoFecha;
import com.goit.helper.enums.NemonicoParametros;
import com.goit.notify.bo.INotDestinatariosBO;
import com.goit.notify.dao.NotGruposDAO;
import com.goit.notify.dto.CamposPersonalizadosDTO;
import com.goit.notify.dto.RequestDestinatariosDTO;
import com.goit.notify.enums.CamposDEnum;
import com.goit.notify.enums.TipoArchivoEnum;
import com.goit.notify.exceptions.BOException;
import com.goit.notify.exceptions.RestClientException;
import com.goit.notify.exceptions.UnauthorizedException;
import com.goit.notify.model.NotGrupos;
import com.goit.notify.util.MensajesUtil;
import com.goit.notify.util.ServiciosUtil;
import com.goit.notify.util.ValidacionUtil;

@Service
public class NotDestinatariosBOImpl implements INotDestinatariosBO{
	
	@Autowired
	private NotGruposDAO objNotGruposDAO;
	@Autowired
	private ServiciosUtil objServiciosUtil;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class}) 
	public List<RequestDestinatariosDTO> postValidaTipoArchivo(String strCanal,String strToken, Boolean booError, String strTipoArchivo, List<CamposPersonalizadosDTO> lsNombreCampos,
			MultipartFile archivoTarifario, List<RequestDestinatariosDTO> lsRequestDestinatariosDTO, Integer intIdGrupo, String idUsuario, String idEmpresa, String usuario,
			String strLanguage) throws BOException, IOException, RestClientException, UnauthorizedException {
		
		//Valida Campo requerido tipoArchivo
		ValidacionUtil.validarCampoRequeridoBO(strTipoArchivo,"not.campo.tipoArchivo");
		//Valida Campo requerido error
		ValidacionUtil.validarCampoRequeridoBO(booError,"not.campo.error");
		
		//Valida qye el tipo de archivo sea correcto es decir CSV o EXCEL
		String[] arrTipoArchivo = {TipoArchivoEnum.CSV.getName(),TipoArchivoEnum.EXCEL.getName(),TipoArchivoEnum.JSON.getName()};
		 if(!Arrays.stream(arrTipoArchivo).anyMatch(StringUtils.upperCase(strTipoArchivo)::equals)) 
			 throw new BOException("not.warn.campoInvalido",new Object[]{"not.campo.tipoArchivo"});
		 
		 //Si es tipo EXCEL entonces llama el metodo postDestinararioExcel
		 if(TipoArchivoEnum.EXCEL.getName().equalsIgnoreCase(strTipoArchivo)) 
				return postDestinararioExcel(strCanal,strToken, booError,lsNombreCampos,archivoTarifario,intIdGrupo,idUsuario,idEmpresa,usuario,strLanguage);
		 //Si es tipo CSV entonces llama el metodo postDestinararioExcel
		else if(TipoArchivoEnum.CSV.getName().equalsIgnoreCase(strTipoArchivo)) 
			return postDestinararioCsv(strCanal,strToken, booError,lsNombreCampos,archivoTarifario,intIdGrupo,idUsuario,idEmpresa,usuario,strLanguage);
		 //Si es tipo JSON entonces llama el metodo postDestinararioExcel
		else if(TipoArchivoEnum.JSON.getName().equalsIgnoreCase(strTipoArchivo)) {
			//Valida Campo requerido postFiltroDestinararios
			ValidacionUtil.validarCampoRequeridoBO(booError,"not.campo.json");
			return postFiltroDestinararios(strCanal,strToken, booError,lsRequestDestinatariosDTO, intIdGrupo, idUsuario,
					idEmpresa,usuario, strLanguage);
		}

		 return null;
		
		 
	}
	
	private List<RequestDestinatariosDTO> postDestinararioExcel(String strCanal,String strToken, Boolean booError, List<CamposPersonalizadosDTO> lsNombreCampos, MultipartFile archivoDestinarario,
			Integer intIdGrupo, String idUsuario, String idEmpresa, String usuario, String strLanguage)
			throws BOException, IOException, RestClientException, UnauthorizedException {
		
		//Declaracion de variables
		List<RequestDestinatariosDTO> lsRequestDestinatariosDTO=new ArrayList<RequestDestinatariosDTO>();
		RequestDestinatariosDTO objRequestDestinatariosDTO=null;
		List<CamposPersonalizadosDTO> lsCamposPersonalizadosDTO=null;
		CamposPersonalizadosDTO objCamposPersonalizadosDTO=null;
		XSSFWorkbook workbook = new XSSFWorkbook(archivoDestinarario.getInputStream());
		XSSFSheet sheetDetail = workbook.getSheetAt(0);
		Row row = sheetDetail.getRow(0);
		DataFormatter	formatter = new DataFormatter();
		int rows = sheetDetail.getLastRowNum();
	
		//Recorre el for segun el numero de filas
		for (int i = 0; i <= rows; ++i) {
			row = sheetDetail.getRow(i);
			lsCamposPersonalizadosDTO=new ArrayList<CamposPersonalizadosDTO>();
			objRequestDestinatariosDTO=new RequestDestinatariosDTO();
			//Recorre los for segun el numero de campos que llega en el json.
			for(int j = 0; j < lsNombreCampos.size(); ++j) {	
				if(CamposDEnum.PRIMERNOMBRE.getName().equalsIgnoreCase(lsNombreCampos.get(j).getCampo())) {
					objRequestDestinatariosDTO.setPrimerNombre(formatter.formatCellValue(row.getCell(j)));
				}else if(CamposDEnum.PRIMERAPELLIDO.getName().equalsIgnoreCase(lsNombreCampos.get(j).getCampo())) {
					objRequestDestinatariosDTO.setPrimerApellido(formatter.formatCellValue(row.getCell(j)));
				}else if(CamposDEnum.CORREO.getName().equalsIgnoreCase(lsNombreCampos.get(j).getCampo())) {
					objRequestDestinatariosDTO.setCorreo(formatter.formatCellValue(row.getCell(j)));
				}else if(CamposDEnum.GENERO.getName().equalsIgnoreCase(lsNombreCampos.get(j).getCampo())) {
					objRequestDestinatariosDTO.setGenero(formatter.formatCellValue(row.getCell(j)));
				}else if(CamposDEnum.PAIS.getName().equalsIgnoreCase(lsNombreCampos.get(j).getCampo())) {
					objRequestDestinatariosDTO.setPais(formatter.formatCellValue(row.getCell(j)));
				}else if(CamposDEnum.FECHANACIMIENTO.getName().equalsIgnoreCase(lsNombreCampos.get(j).getCampo())) {
					objRequestDestinatariosDTO.setPais(formatter.formatCellValue(row.getCell(j)));
				}else{
					//Si los campos del json son diferente a las constantes del enum entonces se entiende que es un campo personalizado
					objCamposPersonalizadosDTO=new CamposPersonalizadosDTO(); 
					objCamposPersonalizadosDTO.setCampo(lsNombreCampos.get(j).getCampo());
					objCamposPersonalizadosDTO.setValor(formatter.formatCellValue(row.getCell(j)));
					//Setea a la lista de campos personalizados
					lsCamposPersonalizadosDTO.add(objCamposPersonalizadosDTO);
				}
			}
			//Setea los campos personalizados
			objRequestDestinatariosDTO.setCamposPersonalizados(lsCamposPersonalizadosDTO);
			//Setea la lista de destinatario
			lsRequestDestinatariosDTO.add(objRequestDestinatariosDTO);		
		}
		
		//Llama a la funcion de filtro de distinatario
		return postFiltroDestinararios(strCanal,strToken,booError,lsRequestDestinatariosDTO, intIdGrupo, idUsuario,
				idEmpresa,usuario, strLanguage);
		
	}
	
	private List<RequestDestinatariosDTO> postDestinararioCsv(String strCanal,String strToken, Boolean booError, List<CamposPersonalizadosDTO> lsNombreCampos, MultipartFile archivoDestinarario,
			Integer intIdGrupo, String idUsuario, String idEmpresa, String usuario, String strLanguage)
			throws BOException, IOException, RestClientException, UnauthorizedException {
		
		//Declaracion de variables
		List<RequestDestinatariosDTO> lsRequestDestinatariosDTO=new ArrayList<RequestDestinatariosDTO>();
		RequestDestinatariosDTO objRequestDestinatariosDTO=null;
		List<CamposPersonalizadosDTO> lsCamposPersonalizadosDTO=null;
		CamposPersonalizadosDTO objCamposPersonalizadosDTO=null;

		BufferedReader fileReader = new BufferedReader(new 
		InputStreamReader(archivoDestinarario.getInputStream(), "UTF-8"));
		CSVParser csvParser = new CSVParser(fileReader, CSVFormat.DEFAULT);
		Iterable<CSVRecord> csvRecords = csvParser.getRecords();
		
		//Recorre filas csv
		for (CSVRecord csvRecord : csvRecords) {
			
			//Se inicializan variables
			lsCamposPersonalizadosDTO=new ArrayList<CamposPersonalizadosDTO>();
			objRequestDestinatariosDTO=new RequestDestinatariosDTO();
			
			//Recorre los for segun el numero de campos que llega en el json.
			for(int j = 0; j < lsNombreCampos.size(); ++j) {	
				if(CamposDEnum.PRIMERNOMBRE.getName().equalsIgnoreCase(lsNombreCampos.get(j).getCampo())) {
					objRequestDestinatariosDTO.setPrimerNombre(csvRecord.get(j));
				}else if(CamposDEnum.PRIMERAPELLIDO.getName().equalsIgnoreCase(lsNombreCampos.get(j).getCampo())) {
					objRequestDestinatariosDTO.setPrimerApellido(csvRecord.get(j));
				}else if(CamposDEnum.CORREO.getName().equalsIgnoreCase(lsNombreCampos.get(j).getCampo())) {
					objRequestDestinatariosDTO.setCorreo(csvRecord.get(j));
				}else if(CamposDEnum.GENERO.getName().equalsIgnoreCase(lsNombreCampos.get(j).getCampo())) {
					objRequestDestinatariosDTO.setGenero(csvRecord.get(j));
				}else if(CamposDEnum.PAIS.getName().equalsIgnoreCase(lsNombreCampos.get(j).getCampo())) {
					objRequestDestinatariosDTO.setPais(csvRecord.get(j));
				}else if(CamposDEnum.FECHANACIMIENTO.getName().equalsIgnoreCase(lsNombreCampos.get(j).getCampo())) {
					objRequestDestinatariosDTO.setPais(csvRecord.get(j));
				}else{
					//Si los campos del json son diferente a las constantes del enum entonces se entiende que es un campo personalizado
					objCamposPersonalizadosDTO=new CamposPersonalizadosDTO(); 
					objCamposPersonalizadosDTO.setCampo(lsNombreCampos.get(j).getCampo());
					objCamposPersonalizadosDTO.setValor(csvRecord.get(j));
					//Setea a la lista de campos personalizados
					lsCamposPersonalizadosDTO.add(objCamposPersonalizadosDTO);
				}
			}
			csvParser.close();
			
			//Setea los campos personalizados
			objRequestDestinatariosDTO.setCamposPersonalizados(lsCamposPersonalizadosDTO);
			//Setea la lista de destinatario
			lsRequestDestinatariosDTO.add(objRequestDestinatariosDTO);		
		}
		
		//Llama a la funcion de filtro de distinatario
		return postFiltroDestinararios(strCanal,strToken,booError,lsRequestDestinatariosDTO, intIdGrupo, idUsuario,
				idEmpresa,usuario, strLanguage);
				
	}
	
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = { Exception.class}) 
	public List<RequestDestinatariosDTO> postFiltroDestinararios(String strCanal,String strToken, Boolean booError, List<RequestDestinatariosDTO> lsRequestDestinatariosDTO,  Integer intIdGrupo, String strIdUsuario,
			String strIdEmpresa, String strUsuario, String strLanguaje) throws BOException, JsonMappingException, JsonProcessingException, RestClientException, UnauthorizedException{
		
		//Valida Campo requerido
		ValidacionUtil.validarCampoRequeridoBO(intIdGrupo,"not.campo.idGrupo");
		ValidacionUtil.validarCampoRequeridoBO(booError,"not.campo.error");
		
		//Valida que el id del grupo sea existente y activo
		Optional<NotGrupos> objNotGrupos=objNotGruposDAO.findYValidar(intIdGrupo);
		
		//Campos adicionales puestos en el grupo
		String strCampoOpcional = objNotGrupos.get().getCamposPersonalizados();
		String[] arrCampoOpcional = strCampoOpcional.split(",");
		
		 //Se declara la lista
		 List<RequestDestinatariosDTO> lsDestinatarioError=new ArrayList<RequestDestinatariosDTO>();
		 List<RequestDestinatariosDTO> lsDestinatarioOk=new ArrayList<RequestDestinatariosDTO>();
		 Date datFechaActual=new Date();

		 //Declara variables a utilizar
		 StringBuilder strError=null;
		 boolean booPrimerNombre = false;
		 boolean booPrimerApellido = false;
		 //boolean booCorreo = false;
		 boolean booGenero = false;
		 boolean booFechaNacimiento = false;
		 boolean booPais=false;
		 boolean booIsOK=true;
		 
		 //Recorre los destinatarios
		 for(RequestDestinatariosDTO objRequestDestinatariosDTO:lsRequestDestinatariosDTO) {
			 
			 //Entra si el primer nombre es diferente a null o vacio 
			 if (!ObjectUtils.isEmpty(objRequestDestinatariosDTO.getPrimerNombre())) 
				 booPrimerNombre = true;
			 //Entra si el primer apellido es diferente a null o vacio 
			 if (!ObjectUtils.isEmpty(objRequestDestinatariosDTO.getPrimerApellido())) 
				 booPrimerApellido = true;		 
//			 //Entra si el correo es diferente a null o vacio 
//			 if(!ObjectUtils.isEmpty(objRequestDestinatariosDTO.getCorreo())) 
//				 booCorreo = true;
			 //Entra si genero es diferente a null o vacio 
			 if(!ObjectUtils.isEmpty(objRequestDestinatariosDTO.getGenero())) 
				 booGenero = true;  
			 //Entra si fechaNacimiento es diferente a null o vacio 
			 if(!ObjectUtils.isEmpty(objRequestDestinatariosDTO.getFechaNacimiento()))
				 booFechaNacimiento = true;	 
			 //Entra si el pais es diferente a null o vacio 
			 if(!ObjectUtils.isEmpty(objRequestDestinatariosDTO.getPais()))
				 booPais = true; 	 
		 }
		 
		 RequestDestinatariosDTO objRequestDestinatariosErrorDTO=null;
		 //Recorre los destinatarios nuevamente porque ya sabemos cuales son los campos requeridos
		 for(RequestDestinatariosDTO objRequestDestinatariosDTO:lsRequestDestinatariosDTO) {
			 
			 //Inicializamos variables
			 strError = new StringBuilder();
			 booIsOK=true;
			 
			 objRequestDestinatariosErrorDTO=new RequestDestinatariosDTO();
			 
			 //Si booPrimerNombre es true entonces es requerido para todos los contactos.
			 if (booPrimerNombre && ObjectUtils.isEmpty(objRequestDestinatariosDTO.getPrimerNombre())) {
				 strError.append(", "+MensajesUtil.getMensaje("not.warn.campoObligatorio",new Object[] {"not.campo.primerNombre"}
				 ,MensajesUtil.validateSupportedLocale(strLanguaje)));
				 booIsOK = false;
				 
				 if(!booError) {
					 objRequestDestinatariosErrorDTO.setPrimerNombre(objRequestDestinatariosDTO.getPrimerNombre()); 
				 }
			 }
			 	
			//Si booPrimerApellido es true entonces es requerido para todos los contactos.
			 if (booPrimerApellido && ObjectUtils.isEmpty(objRequestDestinatariosDTO.getPrimerApellido())) {
				 strError.append(", "+MensajesUtil.getMensaje("not.warn.campoObligatorio",new Object[] {"not.campo.primerApellido"}
				 ,MensajesUtil.validateSupportedLocale(strLanguaje)));
				 booIsOK = false;
				 
				 if(!booError) {
					 objRequestDestinatariosErrorDTO.setPrimerApellido(objRequestDestinatariosDTO.getPrimerApellido()); 
				 }
			 }
	 
			//Si booCorreo es true entonces es requerido para todos los contactos.
			// if(booCorreo) {
				 
				 //Si el correo es vacio entonces ingresa
				 if(ObjectUtils.isEmpty(objRequestDestinatariosDTO.getCorreo())){
					 strError.append(", "+MensajesUtil.getMensaje("not.warn.campoObligatorio",new Object[] {"not.campo.correo"}
					 ,MensajesUtil.validateSupportedLocale(strLanguaje)));
					 booIsOK = false;
					 
					 if(!booError) {
						 objRequestDestinatariosErrorDTO.setCorreo(objRequestDestinatariosDTO.getCorreo()); 
					 }
					 
				//Si el correo no cumple con el formato adecuado entonces ingresa
				 }else if(!StringHelper.emailValido(objRequestDestinatariosDTO.getCorreo())) {
					 strError.append(", "+MensajesUtil.getMensaje("not.warn.campoInvalido",new Object[] {"not.campo.correo"}
					 ,MensajesUtil.validateSupportedLocale(strLanguaje)));
					 booIsOK = false;
					 
					 if(!booError) {
						 objRequestDestinatariosErrorDTO.setCorreo(objRequestDestinatariosDTO.getCorreo()); 
					 }
				 }
			 //}
				 
			//Si booGenero es true entonces es requerido para todos los contactos.
			 if (booGenero && ObjectUtils.isEmpty(objRequestDestinatariosDTO.getGenero())) {
				 strError.append(", "+MensajesUtil.getMensaje("not.warn.campoObligatorio",new Object[] {"not.campo.genero"}
				 ,MensajesUtil.validateSupportedLocale(strLanguaje)));
				 booIsOK = false;
				 
				 if(!booError) {
					 objRequestDestinatariosErrorDTO.setGenero(objRequestDestinatariosDTO.getGenero()); 
				 }
			 }

			 //Valida que la fecha de nacimiento sea valido
			 if(booFechaNacimiento) {
				 
				 if(ObjectUtils.isEmpty(objRequestDestinatariosDTO.getFechaNacimiento())){
					 strError.append(", "+MensajesUtil.getMensaje("not.warn.campoObligatorio",new Object[] {"not.campo.fechaNacimiento"}
					 ,MensajesUtil.validateSupportedLocale(strLanguaje)));
					 booIsOK = false;
					 
					 if(!booError) {
						 objRequestDestinatariosErrorDTO.setFechaNacimiento(objRequestDestinatariosDTO.getFechaNacimiento()); 
					 }
					 
				 }else if(!FechasHelper.formatoFechaValido(objRequestDestinatariosDTO.getFechaNacimiento(), FormatoFecha.YYYY_MM_DD)) {
					 strError.append(", "+MensajesUtil.getMensaje("not.warn.campoInvalido",new Object[] {"not.campo.fechaNacimiento"}
					 ,MensajesUtil.validateSupportedLocale(strLanguaje)));
					 booIsOK = false;
					 
					 if(!booError) {
						 objRequestDestinatariosErrorDTO.setFechaNacimiento(objRequestDestinatariosDTO.getFechaNacimiento()); 
					 }
					 
				 }
				
			 }
	 
			 //Valida que campo opcional de la tabla sea diferente a null.
			 if(!ObjectUtils.isEmpty(strCampoOpcional)){
				
				 //Valida que camposPersonalizado sea diferente a null
				 if(ObjectUtils.isEmpty(objRequestDestinatariosDTO.getCamposPersonalizados())) {
					 strError.append(", "+MensajesUtil.getMensaje("not.warn.campoObligatorio",new Object[] {"not.campo.camposPersonalizados"}
					 ,MensajesUtil.validateSupportedLocale(strLanguaje)));
					 booIsOK=false;
				 }else {
					 
					 for(CamposPersonalizadosDTO objCamposRequerido:objRequestDestinatariosDTO.getCamposPersonalizados()) {
						 //Valida campo personalizados
						 if(!Arrays.stream(arrCampoOpcional).anyMatch(StringUtils.upperCase(objCamposRequerido.getCampo())::equals)
								 || ObjectUtils.isEmpty(objCamposRequerido.getValor())) {
							 strError.append(", "+MensajesUtil.getMensaje("not.warn.campoPersonalizados",new Object[] {
									 strCampoOpcional	 
							 }
							 ,MensajesUtil.validateSupportedLocale(strLanguaje)));
							 							 
							 booIsOK=false;
						 }
					 }
				 }
			 }
			 
			//Si booPais es true entonces es requerido para todos los contactos.
			 if (booPais && ObjectUtils.isEmpty(objRequestDestinatariosDTO.getPais())) {
				 strError.append(", "+MensajesUtil.getMensaje("not.warn.campoObligatorio",new Object[] {"not.campo.pais"}
				 ,MensajesUtil.validateSupportedLocale(strLanguaje)));
				 
				 if(!booError) {
					 objRequestDestinatariosErrorDTO.setPais(objRequestDestinatariosDTO.getPais()); 
				 }
				 
				 booIsOK = false;
			 }
	 
			  
			 //Distribuye en listas ok o con error.
			 if(!booError && !booIsOK) {
				 
				 //Si error es diferente a null ingresa
				 if(!ObjectUtils.isEmpty(strError)) 
					 objRequestDestinatariosErrorDTO.setError(strError.toString().substring(1,strError.length()));
				
				// objRequestDestinatariosErrorDTO.setCamposPersonalizados(objRequestDestinatariosDTO.getCamposPersonalizados());
				 lsDestinatarioError.add(objRequestDestinatariosErrorDTO);
				 
			 }else if(!booIsOK) {
				 
				 //Si error es diferente a null ingresa
				 if(!ObjectUtils.isEmpty(strError)) 
					 objRequestDestinatariosDTO.setError(strError.toString().substring(1,strError.length()));
				
				 lsDestinatarioError.add(objRequestDestinatariosDTO);
			 }else 
				 lsDestinatarioOk.add(objRequestDestinatariosDTO); 
		 }
		 
		 
		//Entra si la lista lsDestinatarioOk es diferente a null o vacio
		 if(!ObjectUtils.isEmpty(lsDestinatarioOk)) {
			 //Total de data enviada
			 objNotGrupos.get().setCantidadParcial(lsRequestDestinatariosDTO.stream().count());
			 //Total de data filtrada
			 objNotGrupos.get().setCantidadDestinatarios(lsDestinatarioOk.stream().count());
			 objNotGrupos.get().setFechActualiza(datFechaActual);
			 objNotGrupos.get().setUsuarioActualiza(strUsuario.toUpperCase());
			 objNotGrupos.get().setIdEmpresa(strIdEmpresa);
			 objNotGrupos.get().setIdUsuario(strIdUsuario);
			 objNotGruposDAO.update(objNotGrupos.get());
		 }
		 //Guarda la data filtrada 
		 guardarDestinatario(strCanal,strToken, strLanguaje,intIdGrupo,lsDestinatarioOk);
		 
		return lsDestinatarioError;
	
	}

	private void guardarDestinatario(String strCanal,String strToken, String strLanguaje, Integer intIdGrupo, List<RequestDestinatariosDTO> lsDestinatarioOk) throws JsonMappingException, JsonProcessingException, RestClientException, BOException, UnauthorizedException {
				
		Integer intCantidadParcial=new Integer(NemonicoParametros.CANTIDAD_PARCIAL.getName());
		 do {
			 //Envio de datos a guardar
			 if(lsDestinatarioOk.stream().count()<=intCantidadParcial.intValue()) {
				 
				 for(int i=0;i<lsDestinatarioOk.stream().count();i++) 
					 lsDestinatarioOk.get(i).setState("N");
				 	
			 }else {
				 
				 //Agrega los destinatario
				 for(int i=0;i<intCantidadParcial.intValue();i++) 
					 lsDestinatarioOk.get(i).setState("N");
				 		 
			 }
			 
			 //Guarda los destinatarios
			 objServiciosUtil.guardarDestinatario( strCanal,strToken, strLanguaje, intIdGrupo, lsDestinatarioOk);
			 lsDestinatarioOk.removeIf(a->a.getState().equals("N"));
	
			 
		 }while(lsDestinatarioOk.stream().count()!=0);
			
	}
}
