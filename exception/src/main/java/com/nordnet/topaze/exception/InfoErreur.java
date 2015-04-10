package com.nordnet.topaze.exception;

/**
 * Message customis�e pour les methodes de controlle annot�e avec @ExceptionHandler qui g�rent les cas d'erreur.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class InfoErreur {

	/**
	 * l'url de service REST.
	 */
	private String url;

	/**
	 * code d'erreur.
	 */
	private String errorCode;

	/**
	 * message d'erreur.
	 */
	private String errorMessage;

	/**
	 * constructeur par defaut.
	 */
	public InfoErreur() {

	}

	/**
	 * constructeur.
	 * 
	 * @param url
	 *            l'url de service REST.
	 * @param errorCode
	 *            code d'erreur.
	 * @param errorMessage
	 *            message d'erreur.
	 */
	public InfoErreur(String url, String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.url = url;
		this.errorMessage = errorMessage;
	}

	/**
	 * @return {@link #url}.
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url
	 *            {@link #url}.
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return {@link #errorCode}.
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * @param errorCode
	 *            {@link #errorCode}.
	 */
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	/**
	 * @return {@link #errorMessage}.
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	/**
	 * @param errorMessage
	 *            {@link #errorMessage}.
	 */
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
