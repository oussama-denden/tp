package com.nordnet.topaze.businessprocess.facture.calcule;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nordnet.common.alert.ws.client.SendAlert;
import com.nordnet.common.valueObject.constants.CurrencyCode;
import com.nordnet.common.valueObject.constants.PaymentType;
import com.nordnet.common.valueObject.constants.TransactionType;
import com.nordnet.common.valueObject.constants.VatType;
import com.nordnet.common.valueObject.date.TimePeriod;
import com.nordnet.common.valueObject.identifier.Identifier;
import com.nordnet.common.valueObject.money.Discount;
import com.nordnet.common.valueObject.money.Price;
import com.nordnet.common.valueObject.money.RUM;
import com.nordnet.common.valueObject.money.Transaction;
import com.nordnet.netcatalog.exception.NetCatalogException;
import com.nordnet.netcatalog.ws.client.NetCatalogClient;
import com.nordnet.netcatalog.ws.entity.OffreCatalogue;
import com.nordnet.saphir.ws.client.SaphirTechnical;
import com.nordnet.saphir.ws.entities.MovementAppendixItem;
import com.nordnet.saphir.ws.entities.constants.BillType;
import com.nordnet.topaze.businessprocess.facture.business.DiscountInfo;
import com.nordnet.topaze.businessprocess.facture.calcule.utils.CalculeUtils;
import com.nordnet.topaze.businessprocess.facture.calcule.utils.ReductionUtils;
import com.nordnet.topaze.businessprocess.facture.mock.MouvementMock;
import com.nordnet.topaze.businessprocess.facture.mock.SaveMouvements;
import com.nordnet.topaze.businessprocess.facture.service.FactureServiceImpl;
import com.nordnet.topaze.businessprocess.facture.util.Constants;
import com.nordnet.topaze.businessprocess.facture.util.PropertiesUtil;
import com.nordnet.topaze.businessprocess.facture.util.Utils;
import com.nordnet.topaze.businessprocess.facture.utils.spring.ApplicationContextHolder;
import com.nordnet.topaze.client.rest.business.facturation.ContratBillingInfo;
import com.nordnet.topaze.client.rest.business.facturation.Frais;
import com.nordnet.topaze.client.rest.business.facturation.ReductionInfo;
import com.nordnet.topaze.client.rest.enums.ModePaiement;
import com.nordnet.topaze.client.rest.enums.TypeContrat;
import com.nordnet.topaze.client.rest.enums.TypeProduit;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Gere les mouvement vers saphir.
 * 
 * @author Oussama Denden
 * 
 */
@Component("mouvementService")
public class MouvementService {

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(FactureServiceImpl.class);

	/**
	 * Separateur de l'arborescence.
	 */
	private final String separator = System.getProperty("separator");

	/**
	 * Alert service.
	 */
	@Autowired
	private SendAlert sendAlert;

	/**
	 * {@link SaveMouvements}.
	 */
	@Autowired
	private SaveMouvements saveMouvements;

	/**
	 * {@link NetCatalogClient}.
	 */
	private NetCatalogClient netCatalogClient;

	/**
	 * creation du client saphir.
	 * 
	 * @return {@link SaphirTechnical}.
	 */
	private SaphirTechnical getSaphirTechnical() {
		SaphirTechnical saphirTechnical = null;
		if (System.getProperty("ws.saphir.useMock").equals("true")) {
			saphirTechnical = new MouvementMock(saveMouvements);
		} else {
			saphirTechnical = new SaphirTechnical();
			saphirTechnical.setUrl(System.getProperty("saphir.url"));
		}

		return saphirTechnical;
	}

	/**
	 * Construire arboriscence produits.
	 * 
	 * @param contratBillingInfo
	 *            the contrat billing info
	 * @return the string
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	private String construireRefProduit(ContratBillingInfo contratBillingInfo) throws TopazeException {
		OffreCatalogue catalogue;
		try {
			catalogue = getNetCatalogClient().getOffre(contratBillingInfo.getOffre());
		} catch (NetCatalogException e) {
			LOGGER.error("failed to send REST request to NetCatalogue", e);
			throw new TopazeException(e.getMessage(), e.getErrorCode());
		}
		return StringUtils.stripAccents(catalogue.getSecteur() + separator + catalogue.getGamme() + separator
				+ catalogue.getReference());
	}

	/**
	 * Envoyer Mouvement pour billing recurrent.
	 * 
	 * @param contratBillingInfo
	 *            information du contrat pour le billing {@link ContratBillingInfo}.
	 * @param montant
	 *            montant a payer.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void sendMouvement(ContratBillingInfo contratBillingInfo, Double montant) throws TopazeException {
		String refProduit = construireRefProduit(contratBillingInfo);
		String label = contratBillingInfo.getTitre();
		Price p = null;
		PaymentType tpaymentType = null;
		Transaction<Price> t = null;
		VatType typeVatType = null;

		/**
		 * envoyer un mouvement recurrent dans le cas ou le contrat est de type Abonnement ou service.
		 */
		switch (contratBillingInfo.getTypeContrat()) {
		case VENTE:
			break;
		default:
			typeVatType = CalculeUtils.getVatType(contratBillingInfo.getTypeTVA());
			tpaymentType = CalculeUtils.getTPayementType(contratBillingInfo.getModePaiement());

			p = new Price(montant, CurrencyCode.EUR);

			t = new Transaction<>(p, TransactionType.DEBIT);

			RUM rum = null;
			if (contratBillingInfo.isRumRequired()) {
				rum = RUM.build(contratBillingInfo.getReferenceModePaiement());
			}
			String billingGroup = null;
			Boolean canBeGroup = null;
			if (contratBillingInfo.getDateDerniereFacture() == null) {
				billingGroup = contratBillingInfo.getNumeroCommande();
				canBeGroup = true;
			}
			LOGGER.info("Création du Billing recurrent pour le produit " + label);

			Collection<MovementAppendixItem> appendixItems =
					CalculeUtils.creerAppendixItem(label, contratBillingInfo.getMontant(),
							contratBillingInfo.getReferenceContrat());

			try {
				LOGGER.info("dateJour: " + PropertiesUtil.getInstance().getDateDuJour());
				List<DiscountInfo> discountInfos =
						ReductionUtils.calculerReduction(contratBillingInfo.getPeriodicite(), montant,
								contratBillingInfo.getToutReductions());
				Discount discount = null;
				for (DiscountInfo discountInfo : discountInfos) {
					if (discountInfo.getDiscount().getAmount().getAmount().doubleValue() > Constants.ZERO) {
						if (discountInfo.isAffichableSurFacture()) {
							double montantDiscount =
									ReductionUtils.calculerMontantDiscount(contratBillingInfo.getMontant(),
											discountInfo.getDiscount());
							Transaction<Price> discountTransaction =
									CalculeUtils.createTransaction(montantDiscount, CurrencyCode.EUR,
											TransactionType.CREDIT);
							String titre =
									Utils.isStringNullOrEmpty(discountInfo.getTitre()) ? "Reduction pour" + label
											: discountInfo.getTitre();

							Collection<MovementAppendixItem> appendixItemsDiscount =
									CalculeUtils.creerAppendixItem(titre, montantDiscount,
											contratBillingInfo.getReferenceContrat());

							String productId = CalculeUtils.construireProductIdReduction(discountInfo, refProduit);

							getSaphirTechnical().addMovement(
									Identifier.build(contratBillingInfo.getIdClient()),
									BillType.MONTHLY,
									new TimePeriod(PropertiesUtil.getInstance().getDateDuJour(), (PropertiesUtil
											.getInstance().getDateDuJour()).plusMonths(contratBillingInfo
											.getPeriodicite())), true, contratBillingInfo.getNumeroCommande(),
									Identifier.build(productId), titre, typeVatType, rum, tpaymentType, null,
									discountTransaction, appendixItemsDiscount);

						} else {
							discount =
									new Discount(discount != null ? discount.getAmount().add(
											discountInfo.getDiscount().getAmount()) : discountInfo.getDiscount()
											.getAmount(), discountInfo.getDiscount().getDiscountType());
						}
					}
				}
				getSaphirTechnical().addMovement(
						Identifier.build(contratBillingInfo.getIdClient()),
						BillType.MONTHLY,
						new TimePeriod(PropertiesUtil.getInstance().getDateDuJour(), (PropertiesUtil.getInstance()
								.getDateDuJour()).plusMonths(contratBillingInfo.getPeriodicite())), canBeGroup,
						billingGroup, Identifier.build(refProduit),
						CalculeUtils.construireProductLabel(contratBillingInfo), typeVatType, rum, tpaymentType,
						discount, t, appendixItems);
			} catch (Exception e) {
				LOGGER.error("Erreur dans l'appel vers saphir: ", e);
				try {
					sendAlert.send(System.getProperty(Constants.PRODUCT_ID), "Unable to perform request",
							"Error occurs during call of FactureServiceImpl.sendMouvement()", e.getMessage());
				} catch (Exception ex) {
					LOGGER.error("fail to send alert", ex);
					throw new TopazeException("fail to send alert", ex);
				}
				LOGGER.error("Erreur dans l'appel vers saphir: " + e.getMessage());
				throw new TopazeException("Erreur dans l'appel vers saphir", e);
			}

			break;

		}
	}

	/**
	 * 
	 * @param contratBillingInfo
	 *            information du contrat pour le billing {@link ContratBillingInfo}
	 * @param montant
	 *            montant a payer.
	 * @param periodStart
	 *            date debut periode du mouvement.
	 * @param periodEnd
	 *            date debut periode du mouvement.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void sendMouvementProrata(ContratBillingInfo contratBillingInfo, Double montant, LocalDate periodStart,
			LocalDate periodEnd) throws TopazeException {
		String productId = construireRefProduit(contratBillingInfo);
		String label = contratBillingInfo.getTitre();
		Price p = null;
		PaymentType tpaymentType = null;
		Transaction<Price> t = null;
		VatType typeVatType = null;
		/**
		 * envoyer un mouvement recurrent dans le cas ou le contrat est de type Abonnement ou service.
		 */
		switch (contratBillingInfo.getTypeContrat()) {
		case VENTE:
			break;
		default:
			typeVatType = CalculeUtils.getVatType(contratBillingInfo.getTypeTVA());
			tpaymentType = CalculeUtils.getTPayementType(contratBillingInfo.getModePaiement());
			p = new Price(montant, CurrencyCode.EUR);

			t = new Transaction<>(p, TransactionType.DEBIT);
			RUM rum = null;
			if (contratBillingInfo.isRumRequired()) {
				rum = RUM.build(contratBillingInfo.getReferenceModePaiement());
			}
			LOGGER.info("Création du Billing recurrent pour le produit " + label);
			Collection<MovementAppendixItem> appendixItems =
					CalculeUtils.creerAppendixItem(label, montant, contratBillingInfo.getReferenceContrat());
			try {
				getSaphirTechnical().addMovement(Identifier.build(contratBillingInfo.getIdClient()), BillType.MONTHLY,
						new TimePeriod(periodStart, periodEnd), null, null, Identifier.build(productId),
						CalculeUtils.construireProductLabel(contratBillingInfo), typeVatType, rum, tpaymentType, null,
						t, appendixItems);
			} catch (Exception e) {
				LOGGER.error("Erreur dans l'appel vers saphir: ", e);
				try {
					sendAlert.send(System.getProperty(Constants.PRODUCT_ID), "Unable to perform request",
							"error occurs during call of FactureServiceImpl.sendMouvementProrata()", e.getMessage());
				} catch (Exception ex) {
					LOGGER.error("fail to send alert", ex);
					throw new TopazeException("fail to send alert", ex);
				}
				LOGGER.error("Erreur dans l'appel vers saphir: " + e.getMessage());
				throw new TopazeException("Erreur dans l'appel vers saphir", e);
			}

			break;

		}
	}

	/**
	 * 
	 * 
	 * @param contratBillingInfo
	 *            {@link ContratBillingInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void sendMouvementPourVente(ContratBillingInfo contratBillingInfo) throws TopazeException {
		LOGGER.info("Vente de " + contratBillingInfo.getTypeProduit());
		String label = contratBillingInfo.getTitre();
		VatType vatType = CalculeUtils.getVatType(contratBillingInfo.getTypeTVA());
		PaymentType tpaymentType = CalculeUtils.getTPayementType(contratBillingInfo.getModePaiement());
		BillType billType =
				contratBillingInfo.getModePaiement() == ModePaiement.FACTURE_FIN_DE_MOIS ? BillType.MONTHLY
						: BillType.DAILY;
		String refProduit = construireRefProduit(contratBillingInfo);
		LOGGER.info("Création du Billing de Livraison pour le produit " + label + " de prix: "
				+ contratBillingInfo.getMontant());

		Transaction<Price> t =
				CalculeUtils
						.createTransaction(contratBillingInfo.getMontant(), CurrencyCode.EUR, TransactionType.DEBIT);
		RUM rum = null;
		if (contratBillingInfo.isRumRequired()) {
			rum = RUM.build(contratBillingInfo.getReferenceModePaiement());
		}
		if (contratBillingInfo.getTypeProduit() == TypeProduit.SERVICE) {
			// Periode = la période sera celle de le vente de service
			// Ex: s’il a acheté un Antivirus pour un an, la période sera [17-06-2014,17-06-2015]
			// ERR_TODO a verifier avec Emna, la periode du mouvement lors du vente de bien/service.
		}
		try {
			LOGGER.info("dateJour: " + PropertiesUtil.getInstance().getDateDuJour());
			Collection<MovementAppendixItem> appendixItems =
					CalculeUtils.creerAppendixItem(label, contratBillingInfo.getMontant(),
							contratBillingInfo.getReferenceContrat());
			// Periode = [Date Courant,Date Courant]

			List<DiscountInfo> discountInfos =
					ReductionUtils.calculerReduction(contratBillingInfo.getPeriodicite(),
							contratBillingInfo.getMontant(), contratBillingInfo.getToutReductions());
			Discount discount = null;
			for (DiscountInfo discountInfo : discountInfos) {
				if (discountInfo.getDiscount().getAmount().getAmount().doubleValue() > Constants.ZERO) {
					if (discountInfo.isAffichableSurFacture()) {
						double montantDiscount =
								ReductionUtils.calculerMontantDiscount(contratBillingInfo.getMontant(),
										discountInfo.getDiscount());
						Transaction<Price> discountTransaction =
								CalculeUtils.createTransaction(montantDiscount, CurrencyCode.EUR,
										TransactionType.CREDIT);
						String titre =
								Utils.isStringNullOrEmpty(discountInfo.getTitre()) ? "Reduction pour" + label
										: discountInfo.getTitre();

						Collection<MovementAppendixItem> appendixItemsDiscount =
								CalculeUtils.creerAppendixItem(titre, montantDiscount,
										contratBillingInfo.getReferenceContrat());

						String productId = CalculeUtils.construireProductIdReduction(discountInfo, refProduit);

						getSaphirTechnical().addMovement(
								Identifier.build(contratBillingInfo.getIdClient()),
								BillType.DAILY,
								new TimePeriod(PropertiesUtil.getInstance().getDateDuJour(), PropertiesUtil
										.getInstance().getDateDuJour()), true, contratBillingInfo.getNumeroCommande(),
								Identifier.build(productId), titre, vatType, rum, tpaymentType, null,
								discountTransaction, appendixItemsDiscount);
					} else {
						discount =
								new Discount(discount != null ? discount.getAmount().add(
										discountInfo.getDiscount().getAmount()) : discountInfo.getDiscount()
										.getAmount(), discountInfo.getDiscount().getDiscountType());
					}
				}
			}

			getSaphirTechnical().addMovement(
					Identifier.build(contratBillingInfo.getIdClient()),
					billType,
					new TimePeriod(PropertiesUtil.getInstance().getDateDuJour(), PropertiesUtil.getInstance()
							.getDateDuJour()), true, contratBillingInfo.getNumeroCommande(),
					Identifier.build(refProduit), CalculeUtils.construireProductLabel(contratBillingInfo), vatType,
					rum, tpaymentType, discount, t, appendixItems);
		} catch (Exception e) {
			LOGGER.error("Erreur dans l'appel vers saphir: ", e);
			try {
				sendAlert.send(System.getProperty(Constants.PRODUCT_ID), "Unable to perform request",
						"error occurs during call of FactureServiceImpl.sendMouvementPourVente()", e.getMessage());
			} catch (Exception ex) {
				LOGGER.error("fail to send alert", ex);
				throw new TopazeException("fail to send alert", ex);
			}
			LOGGER.error("Erreur dans l'appel vers saphir: " + e.getMessage());
			throw new TopazeException("Erreur dans l'appel vers saphir", e);
		}

	}

	/**
	 * Envoyer Mouvement pour les frais de penalite.
	 * 
	 * @param contratBillingInfo
	 *            the contrat billing info {@link ContratBillingInfo}.
	 * @param montant
	 *            montant a payer.
	 * @param discountPanalite
	 *            le discount associe a la penalite.
	 * @param periodFacturation
	 *            la periode de facturation a envoyer vers saphir.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void sendMouvementFraisPenalite(ContratBillingInfo contratBillingInfo, Double montant,
			Discount discountPanalite, TimePeriod periodFacturation) throws TopazeException {

		String refProduit = construireRefProduit(contratBillingInfo) + separator + "Penalites";
		String label = contratBillingInfo.getTitre();
		String productId = refProduit;
		Price p = null;
		PaymentType tpaymentType = null;
		Transaction<Price> t = null;
		VatType typeVatType = null;
		/**
		 * envoyer un mouvement recurrent dans le cas ou le contrat est de type Abonnement ou service.
		 */
		switch (contratBillingInfo.getTypeContrat()) {
		case VENTE:
			break;
		default:
			typeVatType = CalculeUtils.getVatType(contratBillingInfo.getTypeTVA());
			tpaymentType = CalculeUtils.getTPayementType(contratBillingInfo.getModePaiement());
			BillType billType =
					contratBillingInfo.getModePaiement() == ModePaiement.FACTURE_FIN_DE_MOIS ? BillType.MONTHLY
							: BillType.DAILY;
			p = new Price(montant, CurrencyCode.EUR);

			t = new Transaction<>(p, TransactionType.DEBIT);
			RUM rum = null;
			if (contratBillingInfo.isRumRequired()) {
				rum = RUM.build(contratBillingInfo.getReferenceModePaiement());
			}
			LOGGER.info("Création du Frais de penalite pour pour le produit " + label);

			Collection<MovementAppendixItem> appendixItems =
					CalculeUtils.creerAppendixItem(label, montant, contratBillingInfo.getReferenceContrat());

			try {
				getSaphirTechnical().addMovement(Identifier.build(contratBillingInfo.getIdClient()), billType,
						periodFacturation, true, contratBillingInfo.getNumeroCommande(), Identifier.build(productId),
						"Frais penalite pour cause de resiliation", typeVatType, rum, tpaymentType, discountPanalite,
						t, appendixItems);
			} catch (Exception e) {
				LOGGER.error("Erreur dans l'appel vers saphir: ", e);
				try {
					sendAlert.send(System.getProperty(Constants.PRODUCT_ID), "Unable to perform request",
							"error occurs during call of FactureServiceImpl.sendMouvementFraisPenalite()",
							e.getMessage());
				} catch (Exception ex) {
					LOGGER.error("fail to send alert", ex);
					throw new TopazeException("fail to send alert", ex);
				}
				LOGGER.error("Erreur dans l'appel vers saphir: " + e.getMessage());
				throw new TopazeException("Erreur dans l'appel vers saphir", e);
			}

			break;

		}
	}

	/**
	 * Envoyer Mouvement pour remboursement.
	 * 
	 * @param contratBillingInfo
	 *            the contrat billing info {@link ContratBillingInfo}.
	 * @param remboursement
	 *            montant a payer.
	 * @param discountRemboursement
	 *            la reduction pour le remboursement.
	 * @param periodFacturation
	 *            la periode de facturation a envoyer vers saphir.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 * 
	 */
	public void sendMouvementRemboursement(ContratBillingInfo contratBillingInfo, Double remboursement,
			Discount discountRemboursement, TimePeriod periodFacturation) throws TopazeException {
		String refProduit = construireRefProduit(contratBillingInfo) + separator + "Remboursement";
		String productId = refProduit;
		String productLabel = contratBillingInfo.getTitre();
		String label = productLabel;

		Price p = null;
		PaymentType tpaymentType = null;
		Transaction<Price> t = null;
		VatType typeVatType = null;
		/**
		 * envoyer un mouvement recurrent dans le cas ou le contrat est de type Abonnement ou service.
		 */
		switch (contratBillingInfo.getTypeContrat()) {
		case VENTE:
			break;
		default:
			typeVatType = CalculeUtils.getVatType(contratBillingInfo.getTypeTVA());
			tpaymentType = CalculeUtils.getTPayementType(contratBillingInfo.getModePaiement());

			p = new Price(remboursement, CurrencyCode.EUR);

			t = new Transaction<>(p, TransactionType.CREDIT);
			RUM rum = null;
			if (contratBillingInfo.isRumRequired()) {
				rum = RUM.build(contratBillingInfo.getReferenceModePaiement());
			}

			LOGGER.info("Création du Billing remboursement pour le produit" + productLabel);

			Collection<MovementAppendixItem> appendixItems =
					CalculeUtils.creerAppendixItem(label, remboursement, contratBillingInfo.getReferenceContrat());

			try {
				getSaphirTechnical().addMovement(Identifier.build(contratBillingInfo.getIdClient()), BillType.MONTHLY,
						periodFacturation, true, null, Identifier.build(String.valueOf(productId)),
						CalculeUtils.construireProductLabel(contratBillingInfo), typeVatType, rum, tpaymentType,
						discountRemboursement, t, appendixItems);
			} catch (Exception e) {
				LOGGER.error("Erreur dans l'appel vers saphir: ", e);
				try {
					sendAlert.send(System.getProperty(Constants.PRODUCT_ID), "Unable to perform request",
							"error occurs during call of FactureServiceImpl.sendMouvementRemboursement()",
							e.getMessage());
				} catch (Exception ex) {
					LOGGER.error("fail to send alert", ex);
					throw new TopazeException("fail to send alert", ex);
				}
				LOGGER.error("Erreur dans l'appel vers saphir: " + e.getMessage());
				throw new TopazeException("Erreur dans l'appel vers saphir", e);
			}

			break;

		}
	}

	/**
	 * Envoyer des mouvement vers saphir pour les frais de creation.
	 * 
	 * @param contratBillingInfo
	 *            {@link ContratBillingInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void sendMouvementPourFraisCreation(ContratBillingInfo contratBillingInfo) throws TopazeException {

		if (contratBillingInfo.getTypeTVA() != null) {
			VatType vatType = CalculeUtils.getVatType(contratBillingInfo.getTypeTVA());
			PaymentType tpaymentType = CalculeUtils.getTPayementType(contratBillingInfo.getModePaiement());
			BillType billType =
					contratBillingInfo.getModePaiement() == ModePaiement.FACTURE_FIN_DE_MOIS ? BillType.MONTHLY
							: BillType.DAILY;
			String refProduit = construireRefProduit(contratBillingInfo) + separator + "Frais" + separator + "Creation";

			for (Frais frais : contratBillingInfo.getFraisCreations()) {
				Double montantpayment = frais.getMontant();
				LOGGER.info(frais.getTitre() + " : " + frais.getMontant());

				Transaction<Price> t =
						CalculeUtils.createTransaction(montantpayment, CurrencyCode.EUR, TransactionType.DEBIT);
				RUM rum = null;
				if (contratBillingInfo.isRumRequired()) {
					rum = RUM.build(contratBillingInfo.getReferenceModePaiement());
				}

				try {
					LOGGER.info("dateJour: " + PropertiesUtil.getInstance().getDateDuJour());
					Collection<MovementAppendixItem> appendixItems =
							CalculeUtils.creerAppendixItem(frais.getTitre(), frais.getMontant(),
									contratBillingInfo.getReferenceContrat());
					List<DiscountInfo> discountInfos =
							ReductionUtils.calculerReduction(null, montantpayment, frais.getReductionsFrais());
					Discount discount = null;
					for (DiscountInfo discountInfo : discountInfos) {
						if (discountInfo.getDiscount().getAmount().getAmount().doubleValue() > Constants.ZERO) {
							if (discountInfo.isAffichableSurFacture()) {
								double montantDiscount =
										ReductionUtils.calculerMontantDiscount(frais.getMontant(),
												discountInfo.getDiscount());
								Transaction<Price> discountTransaction =
										CalculeUtils.createTransaction(montantDiscount, CurrencyCode.EUR,
												TransactionType.CREDIT);
								String titre =
										Utils.isStringNullOrEmpty(discountInfo.getTitre()) ? "Reduction pour"
												+ frais.getTitre() : discountInfo.getTitre();
								Collection<MovementAppendixItem> discountAppendixItems =
										CalculeUtils.creerAppendixItem(titre, montantDiscount,
												contratBillingInfo.getReferenceContrat());

								String productId = CalculeUtils.construireProductIdReduction(discountInfo, refProduit);

								getSaphirTechnical().addMovement(
										Identifier.build(contratBillingInfo.getIdClient()),
										billType,
										new TimePeriod(PropertiesUtil.getInstance().getDateDuJour(), PropertiesUtil
												.getInstance().getDateDuJour()), true,
										contratBillingInfo.getNumeroCommande(), Identifier.build(productId), titre,
										vatType, rum, tpaymentType, null, discountTransaction, discountAppendixItems);
							} else {
								discount =
										new Discount(discount != null ? discount.getAmount().add(
												discountInfo.getDiscount().getAmount()) : discountInfo.getDiscount()
												.getAmount(), discountInfo.getDiscount().getDiscountType());
							}
						}
					}

					getSaphirTechnical().addMovement(
							Identifier.build(contratBillingInfo.getIdClient()),
							billType,
							new TimePeriod(PropertiesUtil.getInstance().getDateDuJour(), PropertiesUtil.getInstance()
									.getDateDuJour()), true, contratBillingInfo.getNumeroCommande(),
							Identifier.build(refProduit), frais.getTitre(), vatType, rum, tpaymentType, discount, t,
							appendixItems);
				} catch (Exception e) {
					LOGGER.error("Erreur dans l'appel vers saphir: ", e);
					try {
						sendAlert.send(System.getProperty(Constants.PRODUCT_ID), "Unable to perform request",
								"error occurs during call of FactureServiceImpl.sendMouvementPourFraisCreation()",
								e.getMessage());
					} catch (Exception ex) {
						LOGGER.error("fail to send alert", ex);
						throw new TopazeException("fail to send alert", ex);
					}
					LOGGER.error("Erreur dans l'appel vers saphir: " + e.getMessage());
					throw new TopazeException("Erreur dans l'appel vers saphir", e);
				}
			}
		}
	}

	/**
	 * Envoyer des mouvement vers saphir pour les frais de cession.
	 * 
	 * @param contratBillingInfo
	 *            {@link ContratBillingInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void sendMouvementPourFraisCession(ContratBillingInfo contratBillingInfo) throws TopazeException {
		double fraisCession = contratBillingInfo.getPolitiqueCession().getFraisCession();
		String refProduit = construireRefProduit(contratBillingInfo) + separator + "Frais" + separator + "Cession";
		String label = contratBillingInfo.getTitre();
		if ((contratBillingInfo.getTypeContrat().equals(TypeContrat.ABONNEMENT) || contratBillingInfo.getTypeContrat()
				.equals(TypeContrat.LOCATION))) {
			VatType vatType = CalculeUtils.getVatType(contratBillingInfo.getTypeTVA());
			PaymentType tpaymentType = CalculeUtils.getTPayementType(contratBillingInfo.getModePaiement());
			BillType billType =
					contratBillingInfo.getModePaiement() == ModePaiement.FACTURE_FIN_DE_MOIS ? BillType.MONTHLY
							: BillType.DAILY;
			LOGGER.info("Création de facture frais cession pour le produit " + label + " de frais: " + fraisCession);
			Transaction<Price> t =
					CalculeUtils.createTransaction(fraisCession, CurrencyCode.EUR, TransactionType.DEBIT);
			RUM rum = null;
			if (contratBillingInfo.isRumRequired()) {
				rum = RUM.build(contratBillingInfo.getReferenceModePaiement());
			}
			Collection<MovementAppendixItem> appendixItems =
					CalculeUtils.creerAppendixItem(label, fraisCession, contratBillingInfo.getReferenceContrat());
			try {
				LOGGER.info("dateJour: " + PropertiesUtil.getInstance().getDateDuJour());
				getSaphirTechnical().addMovement(
						Identifier.build(contratBillingInfo.getIdClient()),
						billType,
						new TimePeriod(PropertiesUtil.getInstance().getDateDuJour(), PropertiesUtil.getInstance()
								.getDateDuJour()), true, contratBillingInfo.getNumeroCommande(),
						Identifier.build(refProduit), label, vatType, rum, tpaymentType, null, t, appendixItems);
			} catch (Exception e) {
				LOGGER.error("Erreur dans l'appel vers saphir: ", e);
				try {
					sendAlert.send(System.getProperty(Constants.PRODUCT_ID), "Unable to perform request",
							"error occurs during call of FactureServiceImpl.fraisCession()", e.getMessage());
				} catch (Exception ex) {
					LOGGER.error("fail to send alert", ex);
					throw new TopazeException("fail to send alert", ex);
				}
				LOGGER.error("Erreur dans l'appel vers saphir: " + e.getMessage());
				throw new TopazeException("Erreur dans l'appel vers saphir", e);
			}
		}
	}

	/**
	 * Envoyer des mouvement vers saphir pour les frais de migration.
	 * 
	 * @param contratBillingInfo
	 *            {@link ContratBillingInfo}.
	 * @param montantMigration
	 *            le montant du frais de migration.
	 * @param reductionSurFraisMigration
	 *            la reduction associe au frais de migration.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void sendMouvementPourFraisMigration(ContratBillingInfo contratBillingInfo, Double montantMigration,
			ReductionInfo reductionSurFraisMigration) throws TopazeException {
		String refProduit = construireRefProduit(contratBillingInfo) + separator + "Frais" + separator + "Migration";
		String label = contratBillingInfo.getTitre();
		if ((contratBillingInfo.getTypeContrat().equals(TypeContrat.ABONNEMENT) || contratBillingInfo.getTypeContrat()
				.equals(TypeContrat.LOCATION))) {
			VatType vatType = CalculeUtils.getVatType(contratBillingInfo.getTypeTVA());
			PaymentType tpaymentType = CalculeUtils.getTPayementType(contratBillingInfo.getModePaiement());
			BillType billType =
					contratBillingInfo.getModePaiement() == ModePaiement.FACTURE_FIN_DE_MOIS ? BillType.MONTHLY
							: BillType.DAILY;
			LOGGER.info("Création de facture frais migration pour le produit " + label + " de frais: "
					+ montantMigration);
			Transaction<Price> t =
					CalculeUtils.createTransaction(montantMigration, CurrencyCode.EUR, TransactionType.DEBIT);
			RUM rum = null;
			if (contratBillingInfo.isRumRequired()) {
				rum = RUM.build(contratBillingInfo.getReferenceModePaiement());
			}
			try {
				Collection<MovementAppendixItem> appendixItems =
						CalculeUtils.creerAppendixItem(label, montantMigration,
								contratBillingInfo.getReferenceContrat());
				Discount discount = null;
				if (reductionSurFraisMigration != null) {
					List<DiscountInfo> discountInfos =
							ReductionUtils.calculerReduction(null, montantMigration,
									Arrays.asList(reductionSurFraisMigration));
					for (DiscountInfo discountInfo : discountInfos) {
						if (discountInfo.getDiscount().getAmount().getAmount().doubleValue() > Constants.ZERO) {
							if (discountInfo.isAffichableSurFacture()) {
								double montantDiscount =
										ReductionUtils.calculerMontantDiscount(montantMigration,
												discountInfo.getDiscount());
								Transaction<Price> discountTransaction =
										CalculeUtils.createTransaction(montantDiscount, CurrencyCode.EUR,
												TransactionType.CREDIT);
								String titre =
										Utils.isStringNullOrEmpty(reductionSurFraisMigration.getTitre())
												? "Reduction pour le frais de migration" : reductionSurFraisMigration
														.getTitre();
								Collection<MovementAppendixItem> discountAppendixItems =
										CalculeUtils.creerAppendixItem(titre, montantDiscount,
												contratBillingInfo.getReferenceContrat());
								String productId = CalculeUtils.construireProductIdReduction(discountInfo, refProduit);

								getSaphirTechnical().addMovement(
										Identifier.build(contratBillingInfo.getIdClient()),
										billType,
										new TimePeriod(PropertiesUtil.getInstance().getDateDuJour(), PropertiesUtil
												.getInstance().getDateDuJour()), true,
										contratBillingInfo.getNumeroCommande(), Identifier.build(productId), titre,
										vatType, rum, tpaymentType, null, discountTransaction, discountAppendixItems);
							} else {
								discount =
										new Discount(discount != null ? discount.getAmount().add(
												discountInfo.getDiscount().getAmount()) : discountInfo.getDiscount()
												.getAmount(), discountInfo.getDiscount().getDiscountType());
							}
						}
					}
				}

				LOGGER.info("dateJour: " + PropertiesUtil.getInstance().getDateDuJour());
				getSaphirTechnical().addMovement(
						Identifier.build(contratBillingInfo.getIdClient()),
						billType,
						new TimePeriod(PropertiesUtil.getInstance().getDateDuJour(), PropertiesUtil.getInstance()
								.getDateDuJour()), true, contratBillingInfo.getNumeroCommande(),
						Identifier.build(refProduit), label, vatType, rum, tpaymentType, discount, t, appendixItems);
			} catch (Exception e) {
				LOGGER.error("Erreur dans l'appel vers saphir: ", e);
				try {
					sendAlert.send(System.getProperty(Constants.PRODUCT_ID), "Unable to perform request",
							"error occurs during call of FactureServiceImpl.fraisMigration()", e.getMessage());
				} catch (Exception ex) {
					LOGGER.error("fail to send alert", ex);
					throw new TopazeException("fail to send alert", ex);
				}
				LOGGER.error("Erreur dans l'appel vers saphir: " + e.getMessage());
				throw new TopazeException("Erreur dans l'appel vers saphir", e);
			}
		}
	}

	/**
	 * Envoyer un mouvement vers saphir pour un frais de resiliation definit.
	 * 
	 * @param contratBillingInfo
	 *            {@link ContratBillingInfo}.
	 * @param montantResiliation
	 *            montant de frais de resiliation.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void sendMouvementPourMontantResiliation(ContratBillingInfo contratBillingInfo, Double montantResiliation)
			throws TopazeException {
		String refProduit = construireRefProduit(contratBillingInfo) + separator + "Frais" + separator + "Resiliation";
		String label = contratBillingInfo.getTitre();
		if ((contratBillingInfo.getTypeContrat().equals(TypeContrat.ABONNEMENT) || contratBillingInfo.getTypeContrat()
				.equals(TypeContrat.LOCATION))) {
			VatType vatType = CalculeUtils.getVatType(contratBillingInfo.getTypeTVA());
			PaymentType tpaymentType = CalculeUtils.getTPayementType(contratBillingInfo.getModePaiement());
			BillType billType =
					contratBillingInfo.getModePaiement() == ModePaiement.FACTURE_FIN_DE_MOIS ? BillType.MONTHLY
							: BillType.DAILY;
			LOGGER.info("Création de facture frais résiliation pour le produit " + label + " de frais: "
					+ montantResiliation);
			Transaction<Price> t =
					CalculeUtils.createTransaction(montantResiliation, CurrencyCode.EUR, TransactionType.DEBIT);
			RUM rum = null;
			if (contratBillingInfo.isRumRequired()) {
				rum = RUM.build(contratBillingInfo.getReferenceModePaiement());
			}
			Collection<MovementAppendixItem> appendixItems =
					CalculeUtils.creerAppendixItem(label, montantResiliation, contratBillingInfo.getReferenceContrat());
			try {
				getSaphirTechnical().addMovement(
						Identifier.build(contratBillingInfo.getIdClient()),
						billType,
						new TimePeriod(PropertiesUtil.getInstance().getDateDuJour(), PropertiesUtil.getInstance()
								.getDateDuJour()), true, contratBillingInfo.getNumeroCommande(),
						Identifier.build(refProduit), CalculeUtils.construireProductLabel(contratBillingInfo), vatType,
						rum, tpaymentType, null, t, appendixItems);
			} catch (Exception e) {
				LOGGER.error("Erreur dans l'appel vers saphir: ", e);
				try {
					sendAlert.send(System.getProperty(Constants.PRODUCT_ID), "Unable to perform request",
							"error occurs during call of FactureServiceImpl.montantResiliation()", e.getMessage());
				} catch (Exception ex) {
					LOGGER.error("fail to send alert", ex);
					throw new TopazeException("fail to send alert", ex);
				}
				LOGGER.error("Erreur dans l'appel vers saphir: " + e.getMessage());
				throw new TopazeException("Erreur dans l'appel vers saphir", e);
			}
		}
	}

	/**
	 * Envoyer un mouvement vers saphir pour un frais de resiliation.
	 * 
	 * @param contratBillingInfo
	 *            the contrat billing info.
	 * @param frais
	 *            le frais.
	 * @throws TopazeException
	 *             the topaze exception {@link ContratBillingInfo} {@link TopazeException}.
	 */
	public void sendMouvementPourFraisResiliation(ContratBillingInfo contratBillingInfo, Frais frais)
			throws TopazeException {
		LOGGER.info(frais.getTitre() + " : " + frais.getMontant());
		String refProduit = construireRefProduit(contratBillingInfo) + separator + "Frais" + separator + "Resiliation";
		double montantpayment = frais.getMontant();
		VatType vatType = CalculeUtils.getVatType(contratBillingInfo.getTypeTVA());
		PaymentType tpaymentType = CalculeUtils.getTPayementType(contratBillingInfo.getModePaiement());
		BillType billType =
				contratBillingInfo.getModePaiement() == ModePaiement.FACTURE_FIN_DE_MOIS ? BillType.MONTHLY
						: BillType.DAILY;
		Transaction<Price> t =
				CalculeUtils.createTransaction(frais.getMontant(), CurrencyCode.EUR, TransactionType.DEBIT);
		RUM rum = null;
		if (contratBillingInfo.isRumRequired()) {
			rum = RUM.build(contratBillingInfo.getReferenceModePaiement());
		}
		Collection<MovementAppendixItem> appendixItems =
				CalculeUtils.creerAppendixItem(frais.getTitre(), frais.getMontant(),
						contratBillingInfo.getReferenceContrat());

		// appliquer la reduction.
		List<DiscountInfo> discountInfos =
				ReductionUtils.calculerReduction(null, montantpayment, frais.getReductionsFrais());
		Discount discount = null;
		for (DiscountInfo discountInfo : discountInfos) {
			discount =
					new Discount(discount != null ? discount.getAmount().add(discountInfo.getDiscount().getAmount())
							: discountInfo.getDiscount().getAmount(), discountInfo.getDiscount().getDiscountType());
		}
		try {
			getSaphirTechnical().addMovement(
					Identifier.build(contratBillingInfo.getIdClient()),
					billType,
					new TimePeriod(PropertiesUtil.getInstance().getDateDuJour(), PropertiesUtil.getInstance()
							.getDateDuJour()), true, contratBillingInfo.getNumeroCommande(),
					Identifier.build(refProduit), frais.getTitre(), vatType, rum, tpaymentType, discount, t,
					appendixItems);

		} catch (Exception e) {
			LOGGER.error("Erreur dans l'appel vers saphir: ", e);
			try {
				sendAlert.send(System.getProperty(Constants.PRODUCT_ID), "Unable to perform request",
						"error occurs during call of FactureServiceImpl.fraisResiliation()", e.getMessage());
			} catch (Exception ex) {
				LOGGER.error("fail to send alert", ex);
				throw new TopazeException("fail to send alert", ex);
			}
			LOGGER.error("Erreur dans l'appel vers saphir: " + e.getMessage());
			throw new TopazeException("Erreur dans l'appel vers saphir", e);
		}
	}

	/**
	 * Retourn le {@link NetCatalogClient}.
	 * 
	 * @return {@link NetCatalogClient}
	 */
	public NetCatalogClient getNetCatalogClient() {
		if (netCatalogClient == null) {
			if (System.getProperty("netcatalog.useMock").equals("true")) {
				netCatalogClient = (NetCatalogClient) ApplicationContextHolder.getBean("netCatalogClientFake");
			} else {
				netCatalogClient = (NetCatalogClient) ApplicationContextHolder.getBean("netCatalogClient");
			}
		}
		return netCatalogClient;
	}

}