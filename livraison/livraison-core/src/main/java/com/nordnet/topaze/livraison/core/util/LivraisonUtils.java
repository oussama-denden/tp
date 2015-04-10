package com.nordnet.topaze.livraison.core.util;

import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.livraison.core.domain.BonPreparation;
import com.nordnet.topaze.livraison.core.domain.ElementLivraison;
import com.nordnet.topaze.livraison.core.domain.OutilLivraison;
import com.nordnet.topaze.livraison.core.domain.TypeBonPreparation;

/**
 * Des methodes utils pour la livraison.
 * 
 * @author Oussama Denden
 * 
 */
public class LivraisonUtils {

	/**
	 * Creer element de retour.
	 * 
	 * @param elementLivraison
	 *            l'element de livraison necaissare à la creation de l'element de retour.
	 * @param bonRecuperation
	 *            {@link BonPreparation} de recuperation parent.
	 * @param isResiliationPartiel
	 *            true si resiliation partiel.
	 * @return l'element de retour.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 * 
	 */
	public static ElementLivraison creerElementRetour(ElementLivraison elementLivraison,
			BonPreparation bonRecuperation, boolean isResiliationPartiel) throws TopazeException {
		ElementLivraison elementRetour = new ElementLivraison();
		elementRetour.setReference(elementLivraison.getReference());
		elementRetour.setAddresseLivraison(elementLivraison.getAddresseLivraison());
		elementRetour.setTypeContrat(elementLivraison.getTypeContrat());
		elementRetour.setTypeBonPreparation(TypeBonPreparation.RETOUR);
		elementRetour.setNumEC(elementLivraison.getNumEC());
		elementRetour.setReferenceProduit(elementLivraison.getReferenceProduit());
		elementRetour.setActeur(elementLivraison.getActeur().equals(OutilLivraison.NETDELIVERY)
				? OutilLivraison.NETRETOUR : OutilLivraison.PACKAGER);
		elementRetour.setRetailerPackagerId(elementLivraison.getRetailerPackagerId());
		elementRetour.setCodeColis(elementLivraison.getCodeColis());
		elementRetour.setDateInitiation(PropertiesUtil.getInstance().getDateDuJour().toDate());
		elementRetour.setResiliationPartiel(isResiliationPartiel);
		elementRetour.setBonPreparationParent(bonRecuperation);
		return elementRetour;

	}

}
