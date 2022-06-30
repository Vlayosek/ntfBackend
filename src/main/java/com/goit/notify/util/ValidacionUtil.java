package com.goit.notify.util;

import org.apache.commons.lang3.ObjectUtils;

import com.goit.notify.exceptions.BOException;

public class ValidacionUtil {

	private ValidacionUtil() {
		throw new IllegalStateException("Utility class");
	}

	/**
	 * Valida campo requerido 
	 * @param <T>
	 * @param objCampoRequerido
	 * @param strNombreCampo
	 * @throws BOException
	 */
	public static <T> void validarCampoRequeridoBO(T objCampoRequerido, String strNombreCampo) throws BOException {
	 	
		if (ObjectUtils.isEmpty(objCampoRequerido)) 
			throw new BOException("not.warn.campoObligatorio", new Object[] { strNombreCampo });
		
	}
	
	/**
	 * Valida canal requerido 
	 * @param <T>
	 * @param objCampoRequerido
	 * @param strNombreCampo
	 * @throws BOException
	 */
	public static <T> void validarCanalRequeridoBO(T objCampoRequerido, String strNombreCampo) throws BOException {
	 	
		if (ObjectUtils.isEmpty(strNombreCampo)) 
			throw new BOException("not.warn.headerObligatorio", new Object[] { objCampoRequerido });
	}
	
	/**
	 * Valida campo nombre 
	 * @param <T>
	 * @param objCampoRequerido
	 * @param strNombreCampo
	 * @throws BOException
	 */
	public static <T> void validarNombre(T objCampoRequerido) throws BOException{
		if(objCampoRequerido == null)
			throw new BOException("not.warn.headerObligatorio", new Object[] { objCampoRequerido });
	}
}
