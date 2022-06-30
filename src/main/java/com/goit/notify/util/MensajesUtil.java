package com.goit.notify.util;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.ResourceBundle;

public class MensajesUtil {

	private static ResourceBundle bundle;
	private static String ficheroMensajes = "mensajes_notify";
	public static final Locale Locale = new Locale("es", "EC");
	private static final String LOCALE_ES="es-EC";
	private static final String LOCALE_ING="en-US";

	public static String getMensaje(String strKey, Locale locale) {
		if(isKey(strKey)) {
			bundle = ResourceBundle.getBundle(ficheroMensajes, locale);
			return new String(bundle.getString(strKey).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
		}else
			return strKey;
	}

	public static String getMensaje(String strKey, Object[] arrParametros, Locale locale) {
		bundle = ResourceBundle.getBundle(ficheroMensajes, locale);
		String strMensaje = new String(bundle.getString(strKey).getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
		if (arrParametros != null && arrParametros.length > 0) {
			for (int i = 0; i < arrParametros.length; i++) {
				String param = String.valueOf(arrParametros[i]);
				strMensaje = strMensaje.replace("{" + i + "}",
						(isKey(param)
								? new String(bundle.getString(param)
										.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8)
								: param));
			}
		}
		return strMensaje;
	}
	
	private static boolean isKey(String strKey) {
		if (strKey != null && (strKey.contains(".warn.") || strKey.contains(".error.") || strKey.contains(".info.")
				|| strKey.contains(".campo.") || strKey.contains(".response.") || strKey.contains(".etiquetas.")))
			return true;
		else
			return false;
	}
	
	public static String getMensajeCampoObligatorio(String strKeyCampo, Locale locale) {
		return MensajesUtil.getMensaje("com.warn.campoObligatorio",
				new Object[] { MensajesUtil.getMensaje(strKeyCampo, locale) }, locale);
	}
	
	public static Locale validateSupportedLocale(String strLanguage) {
		if(strLanguage == null || (!LOCALE_ES.equals(strLanguage) && !LOCALE_ING.equals(strLanguage)))
			strLanguage=LOCALE_ES;
		String[] arrLang = strLanguage.split("-");
    	return new Locale(arrLang[0], arrLang[1]);
	}

}