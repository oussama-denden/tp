package com.nordnet.topaze.resiliation.outil.rest;

import org.springframework.http.HttpStatus;

/**
 * classe contient des methodes utile pour le restClient.
 * 
 * @author akram-moncer
 * 
 */
public class RestUtil {

	/**
	 * verifier si le status de la reponse contient une erreur.
	 * 
	 * @param status
	 *            {@link HttpStatus}.
	 * @return true si la reponse contient une erreur.
	 */
	public static boolean isError(HttpStatus status) {
		HttpStatus.Series series = status.series();
		return (!status.equals(HttpStatus.NOT_FOUND) && HttpStatus.Series.CLIENT_ERROR.equals(series) || HttpStatus.Series.SERVER_ERROR
				.equals(series));
	}

	/**
	 * verifier si le status de la reponse est 404 Not Found.
	 * 
	 * @param status
	 *            {@link HttpStatus}.
	 * @return true si le status de la reponse est 404 Not Found.
	 */
	public static boolean isNotFound(HttpStatus status) {
		return (status.equals(HttpStatus.NOT_FOUND));
	}

}
