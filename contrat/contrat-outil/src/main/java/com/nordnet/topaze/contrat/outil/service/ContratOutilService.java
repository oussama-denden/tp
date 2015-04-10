package com.nordnet.topaze.contrat.outil.service;

import com.nordnet.adminpackager.ConverterException;
import com.nordnet.adminpackager.DriverException;
import com.nordnet.adminpackager.NotFoundException;
import com.nordnet.adminpackager.NullException;
import com.nordnet.adminpackager.PackagerException;
import com.nordnet.topaze.client.rest.business.contrat.ElementsRenouvellemtnInfo;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Cette classe regroupe l'ensemble des methodes de l'outil livraison.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public interface ContratOutilService {

	/**
	 * valider via 'netEquipment' s'il n'y a pas un equipement qui a plusieurs numero de serie.
	 * 
	 * @param referenceContrat
	 *            reference contrat.
	 * @param idClient
	 *            id du client.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public Boolean validerSerialNumber(String referenceContrat, String idClient) throws TopazeException;

	/**
	 * recuperation de l'etat actuelle de l'element contractuelle
	 * 
	 * @param codeproduit
	 * @return
	 */
	public Object[] getElementsState(ElementsRenouvellemtnInfo elementsRenouvellemtnInfo)
			throws NullException, NotFoundException, PackagerException, DriverException, ConverterException,
			TopazeException;

}
