package com.nordnet.topaze.exception;

/**
 * des exceptions specifiques Topaze.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class TopazeException extends Exception {

	/**
	 * Serialization token.
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Code d'erreur.
	 */
	private final String errorCode;

	/**
	 * constructeur avec message d'erreur et code d'erreur..
	 * 
	 * @param s
	 *            message d'erreur.
	 * @param errorCode
	 *            code d'erreur.
	 */
	public TopazeException(String s, String errorCode) {
		super(s);
		this.errorCode = errorCode;
	}

	/**
	 * constructeur avec message d'erreur et l'erreur lui meme.
	 * 
	 * @param message
	 *            message de l'erreur
	 * @param cause
	 *            {@link Throwable}
	 */
	public TopazeException(String message, Throwable cause) {
		super(message, cause);
		this.errorCode = null;
	}

	/**
	 * @return code d'erreur.
	 */
	public String getErrorCode() {
		return errorCode;
	}

}