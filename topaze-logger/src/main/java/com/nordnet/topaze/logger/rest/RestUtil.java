package com.nordnet.topaze.logger.rest;

import org.springframework.http.HttpStatus;

/**
 * classe contient des methodes utile pour le {@link RestClient}.
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
		return (HttpStatus.Series.CLIENT_ERROR.equals(series) || HttpStatus.Series.SERVER_ERROR.equals(series));
	}

}
