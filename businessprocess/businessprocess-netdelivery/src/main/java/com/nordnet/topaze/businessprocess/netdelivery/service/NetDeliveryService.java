package com.nordnet.topaze.businessprocess.netdelivery.service;

import com.nordnet.net_delivery.ConverterException;
import com.nordnet.net_delivery.NetDeliveryException;
import com.nordnet.net_delivery.NotFoundException;
import com.nordnet.net_delivery.NullException;
import com.nordnet.topaze.client.rest.business.livraison.ElementLivraison;
import com.nordnet.topaze.client.rest.enums.TypeBonPreparation;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Cette classe contient les methode relative a NetDelivery.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public interface NetDeliveryService {

	/**
	 * Traduction d'un bon de prepartion vers NetDelivery.
	 * 
	 * @param referenceBP
	 *            reference de bon de prepartion.
	 * @param typeBonPreparation
	 *            {@link TypeBonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public void traductionNetDelivery(String referenceBP, TypeBonPreparation typeBonPreparation) throws TopazeException;

	/**
	 * Verifier si un bien est livre.
	 * 
	 * @param elementLivraison
	 *            {@link ElementLivraison}
	 * @return true si bien livre.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 * @throws NetDeliveryException
	 *             {@link NetDeliveryException}
	 * @throws NotFoundException
	 *             {@link NotFoundException}
	 * @throws ConverterException
	 *             {@link ConverterException}
	 * @throws NullException
	 *             {@link NullException}
	 * @throws NumberFormatException
	 *             {@link NumberFormatException}
	 */
	public Boolean checkBienLivre(ElementLivraison elementLivraison)
			throws TopazeException, NotFoundException, NetDeliveryException, NumberFormatException, NullException,
			ConverterException;

}
