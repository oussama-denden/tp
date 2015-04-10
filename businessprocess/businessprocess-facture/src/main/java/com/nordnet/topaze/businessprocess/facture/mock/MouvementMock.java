package com.nordnet.topaze.businessprocess.facture.mock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.nordnet.common.valueObject.constants.PaymentType;
import com.nordnet.common.valueObject.constants.VatType;
import com.nordnet.common.valueObject.date.TimePeriod;
import com.nordnet.common.valueObject.identifier.Identifier;
import com.nordnet.common.valueObject.money.Discount;
import com.nordnet.common.valueObject.money.Price;
import com.nordnet.common.valueObject.money.RUM;
import com.nordnet.common.valueObject.money.Transaction;
import com.nordnet.saphir.ws.client.SaphirTechnical;
import com.nordnet.saphir.ws.entities.MovementAppendixItem;
import com.nordnet.saphir.ws.entities.constants.BillType;
import com.nordnet.topaze.businessprocess.facture.util.Constants;

/**
 * 
 * @author Ahmed-Mehdi-Laabidi
 * 
 */
public class MouvementMock extends SaphirTechnical {

	/**
	 * Loggeur sur la Classe MouvementMock.
	 */
	private static final Logger LOGGER = Logger.getLogger(MouvementMock.class);

	/**
	 * {@link SaveMouvements}.
	 */
	@Autowired
	private final SaveMouvements saveMouvements;

	/**
	 * constructeur avec params.
	 * 
	 * @param saveMouvements
	 *            {@link SaveMouvements}.
	 */
	public MouvementMock(SaveMouvements saveMouvements) {
		this.saveMouvements = saveMouvements;
	}

	@Override
	public void addMovement(Identifier accountId, BillType billType, TimePeriod period, Boolean canBeGroup,
			String billingGroup, Identifier productId, String productLabel, VatType vatType, RUM rum,
			PaymentType paymentType, Discount discount, Transaction<Price> unitPrice,
			Collection<MovementAppendixItem> appendixItems) {
		String appendixItemLogMessage = Constants.CHAINE_VIDE;
		if (appendixItems != null) {
			Iterator<MovementAppendixItem> appendixItemIterator = appendixItems.iterator();
			while (appendixItemIterator.hasNext()) {
				MovementAppendixItem movementAppendixItem = appendixItemIterator.next();
				appendixItemLogMessage +=
						"\n***Appendix Item*** \n" + "Item detail: "
								+ movementAppendixItem.getMovementAppendixItemsDetails() + "\nAppendixItem Type: "
								+ movementAppendixItem.getType() + "\nPrice: " + movementAppendixItem.getPrice()
								+ "\nLabel: " + movementAppendixItem.getLabel() + "\nProductReferenceId: "
								+ movementAppendixItem.getProductReferenceId();
			}
		}
		LOGGER.info(" \n*******************Mouvement Ajouté******************* " + "\n Account ID:" + accountId
				+ "\n Type Billing:" + billType + "\n periode:" + period + "\n can be Group:" + canBeGroup + "\n "
				+ "Billing group:" + billingGroup + "\n product Id:" + productId + "\n product Label:" + productLabel
				+ "\n" + " Type TVA:" + vatType + "\n Rum:" + rum + "\n Type Paiement:" + paymentType + "\n Discount:"
				+ discount + "\n Prix unitaire:" + unitPrice + "\n" + appendixItemLogMessage);

		ArrayList<MovementAppendixItem> movementAppendixItems = (ArrayList<MovementAppendixItem>) appendixItems;
		String referenceContrat = null;
		if (movementAppendixItems != null && movementAppendixItems.size() > Constants.ZERO) {
			referenceContrat = movementAppendixItems.get(0).getProductReferenceId().getIdentifier();
		}
		String idClient = accountId.getIdentifier();
		String numeroCommande = billingGroup;
		String productid = productId.getIdentifier();
		double valeur = unitPrice.getTransactionAmount().getPrice().getAmount().doubleValue();
		Double discountVal = null;
		if (discount != null) {
			discountVal = discount.getAmount().getAmount().doubleValue();
		}

		saveMouvements.ajouterMouvement(referenceContrat, idClient, numeroCommande, productid, valeur, discountVal);
	}

}
