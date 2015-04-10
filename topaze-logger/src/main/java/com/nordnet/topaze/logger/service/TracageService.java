package com.nordnet.topaze.logger.service;

import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.logger.domain.Tracage;

/**
 * La service TracageService va contenir tous les operations en rapport avec la traçage des action topaze.
 * 
 * @author anisselmane.
 * 
 */
public interface TracageService {

	/**
	 * Ajouter trace {@link Tracage}.
	 * 
	 * @param target
	 *            target exemple: draft, commande.
	 * @param key
	 *            reference
	 * @param descr
	 *            descreption
	 * @param user
	 *            user
	 * @throws OpaleException
	 *             {@link OpaleException}
	 */
	public void ajouterTrace(String target, String key, String descr, String user) throws TopazeException;
}
