package com.goit.notify.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.goit.notify.exceptions.BOException;


public class StringUtil {
	
	private static final String EMAIL_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
	private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

	
	/**
	 * Permite descomponer una cadena de cararteres separado por un caracter en
	 * especifico
	 * 
	 * @author Bryan Zamora
	 * @param strCadena
	 * @param strCaracter
	 * @return parts
	 */
	public static String[] descomponerCadenaSeparadoPorCaracter(String strCadena, String strCaracter) throws BOException {
		String[] parts = null;
		try {
			parts = strCadena.split(strCaracter);
		} catch (Exception e) {
			throw new BOException("con.error.descomponerCadena");
		}
		return parts;
	}
	
		/**
		 * Valida si solamente existen letras en una cadena.
		 * 
		 * @author Bryan Zamora
		 * @param input
		 * @return
		 */
		public static boolean soloLetras(String input) {
			String regx = "^[\\p{L}]+$";
			Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(input);
			return matcher.find();
		}
		
		/**
		 * Valida si el formato de un email es correcto.
		 * 
		 * @author Ivan Marriott
		 * @param email
		 */
		public static boolean emailValido(String email) {
			if (email == null) {
				return false;
			}
			Matcher matcher = EMAIL_PATTERN.matcher(email);
			return matcher.matches();
		}
		
}
