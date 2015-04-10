package com.nordnet.topaze.businessprocess.facture.calcule.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.joda.time.LocalDate;

import com.nordnet.common.valueObject.constants.CurrencyCode;
import com.nordnet.common.valueObject.constants.PaymentType;
import com.nordnet.common.valueObject.constants.TransactionType;
import com.nordnet.common.valueObject.constants.VatType;
import com.nordnet.common.valueObject.date.TimePeriod;
import com.nordnet.common.valueObject.identifier.Identifier;
import com.nordnet.common.valueObject.money.Price;
import com.nordnet.common.valueObject.money.Transaction;
import com.nordnet.saphir.ws.entities.MovementAppendixItem;
import com.nordnet.saphir.ws.entities.constants.AppendixItemType;
import com.nordnet.topaze.businessprocess.facture.business.DiscountInfo;
import com.nordnet.topaze.businessprocess.facture.util.Constants;
import com.nordnet.topaze.businessprocess.facture.util.PropertiesUtil;
import com.nordnet.topaze.client.rest.business.facturation.ContratBillingInfo;
import com.nordnet.topaze.client.rest.business.facturation.Frais;
import com.nordnet.topaze.client.rest.enums.ModePaiement;
import com.nordnet.topaze.client.rest.enums.TypeTVA;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Une classe qui regroupe des methodes utiles a la facturation.
 * 
 * @author Oussama Denden
 * 
 */
public final class CalculeUtils {

	/**
	 * Separateur de l'arborescence.
	 */
	private final static String SEPARATOR = System.getProperty("separator");

	/**
	 * Changer le TypeTVA de Topaze vers le VatType de saphir.
	 * 
	 * @param typeTVA
	 *            {@link TypeTVA}.
	 * @return {@link VatType}.
	 */
	public static final VatType getVatType(TypeTVA typeTVA) {
		switch (typeTVA) {
		case P:
			return VatType.P;
		case S:
			return VatType.S;
		case R:
			return VatType.R;
		case SR:
			return VatType.SR;
		default:
			return null;
		}
	}

	/**
	 * Changer le {@link ModePaiement} de Topaze vers le {@link PaymentType} de saphir.
	 * 
	 * @param modePaiement
	 *            {@link ModePaiement}.
	 * @return {@link PaymentType}.
	 */
	public static final PaymentType getTPayementType(ModePaiement modePaiement) {
		switch (modePaiement) {
		case CB:
			return PaymentType.CB;
		case CHEQUE:
			return PaymentType.CHECK;
		case SEPA:
			return PaymentType.SEPA;
		case TROIS_FOIS_SANS_FRAIS:
			return PaymentType.INSTALLMENTS;
		case FACTURE:
		case FACTURE_FIN_DE_MOIS:
			return PaymentType.INVOICE;
		case VIREMENT:
			return PaymentType.OTHER;
		default:
			return null;
		}
	}

	/**
	 * Creer les AppendixItem associe au mouvement.
	 * 
	 * @param label
	 *            le nom du produit
	 * @param montant
	 *            le montant sans reduction
	 * @param productReferenceId
	 *            reference du contrat
	 * @return liste des appendixItem
	 */
	public static final Collection<MovementAppendixItem> creerAppendixItem(String label, double montant,
			String productReferenceId) {
		Collection<MovementAppendixItem> appendixItems = new ArrayList<>();
		Price price = new Price(montant, CurrencyCode.EUR);
		Identifier productReferenceIDenIdentifier = Identifier.build(productReferenceId);

		com.nordnet.saphir.ws.entities.MovementAppendixItem.Builder

		movementAppendixItem = MovementAppendixItem.builder();
		movementAppendixItem.productReferenceId(productReferenceIDenIdentifier);
		movementAppendixItem.label(label);
		movementAppendixItem.price(price);
		movementAppendixItem.type(AppendixItemType.OTHER);

		appendixItems.add(movementAppendixItem.build());
		return appendixItems;
	}

	/**
	 * Creer une transaction.
	 * 
	 * @param prix
	 *            montant.
	 * @param eur
	 *            Currency.
	 * @param tt
	 *            {@link TransactionType}.
	 * @return {@link Transaction}.
	 */
	public static final Transaction<Price> createTransaction(Double prix, CurrencyCode eur, TransactionType tt) {
		Price p = new Price(prix, eur);
		return new Transaction<>(p, tt);
	}

	/**
	 * Le productLabel du mouvement.
	 * 
	 * @param contratBillingInfo
	 *            {@link ContratBillingInfo}.
	 * @return productLabel.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static String construireProductLabel(ContratBillingInfo contratBillingInfo) throws TopazeException {
		if (contratBillingInfo.getPeriodicite() != null) {
			return contratBillingInfo.getTitre() + " du " + PropertiesUtil.getInstance().getDateDuJour() + " au "
					+ PropertiesUtil.getInstance().getDateDuJour().plusMonths(contratBillingInfo.getPeriodicite())
					+ " (Dossier produit: " + contratBillingInfo.getReferenceContrat() + ")";
		}
		return contratBillingInfo.getTitre();

	}

	/**
	 * creation du productId selon le type de la reduction.
	 * 
	 * @param discountInfo
	 *            {@link DiscountInfo}.
	 * @param refProduit
	 *            reference complete du produit.
	 * @return productId pour le mouvement de la reduction.
	 */
	public static String construireProductIdReduction(DiscountInfo discountInfo, String refProduit) {
		if (discountInfo.isReductionAutomatique()) {
			return refProduit + SEPARATOR + "Reduction" + SEPARATOR + "auto" + SEPARATOR
					+ discountInfo.getCodeCatalogueReduction();
		}
		return refProduit + SEPARATOR + "Reduction" + SEPARATOR + "gesteco";
	}

	/**
	 * retourne le frais a applique lors de la resiliation.
	 * 
	 * @param contratBillingInfo
	 *            {@link ContratBillingInfo}
	 * @return {@link Frais}
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public static Frais getFraisResiliationActif(ContratBillingInfo contratBillingInfo) throws TopazeException {
		Collections.sort(contratBillingInfo.getFraisResiliations());
		LocalDate dateDebut = LocalDate.fromDateFields(contratBillingInfo.getDateDebutFacturation());
		LocalDate dateJour = PropertiesUtil.getInstance().getDateDuJour();
		for (Frais frais : contratBillingInfo.getFraisResiliations()) {
			if (frais.getNombreMois() != null) {
				LocalDate dateFin = dateDebut.plusMonths(frais.getNombreMois()).minusDays(Constants.UN);
				TimePeriod period = new TimePeriod(dateDebut, dateFin);
				if (period.contains(dateJour)) {
					return frais;
				}
				dateDebut = dateFin.plusDays(Constants.UN);
			} else {
				return frais;
			}
		}
		return null;
	}

}
