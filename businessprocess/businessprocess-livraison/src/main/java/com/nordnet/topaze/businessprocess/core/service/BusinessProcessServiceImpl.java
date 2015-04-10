package com.nordnet.topaze.businessprocess.core.service;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Optional;
import com.nordnet.topaze.businessprocess.core.mock.NetCatalogueMock;
import com.nordnet.topaze.businessprocess.core.mock.PackagerConfig;
import com.nordnet.topaze.businessprocess.core.util.PropertiesUtil;
import com.nordnet.topaze.businessprocess.core.util.Utils;
import com.nordnet.topaze.client.rest.RestClientLivraison;
import com.nordnet.topaze.client.rest.business.livraison.Avenant;
import com.nordnet.topaze.client.rest.business.livraison.BonPreparation;
import com.nordnet.topaze.client.rest.business.livraison.ContratBP;
import com.nordnet.topaze.client.rest.business.livraison.ContratMigrationInfo;
import com.nordnet.topaze.client.rest.business.livraison.ElementLivraison;
import com.nordnet.topaze.client.rest.enums.OutilLivraison;
import com.nordnet.topaze.client.rest.enums.TypeBonPreparation;
import com.nordnet.topaze.client.rest.enums.TypeProduit;
import com.nordnet.topaze.exception.TopazeException;

/**
 * implementation du {@link BusinessProcessService}.
 * 
 * @author Oussama Denden
 * 
 */
@Service("businessProcessService")
public class BusinessProcessServiceImpl implements BusinessProcessService {

	/**
	 * {@link RestClientLivraison}.
	 */
	@Autowired
	private RestClientLivraison restClientLivraison;

	/**
	 * {@link NetCatalogueMock}.
	 */
	@Autowired
	private NetCatalogueMock netCatalogueMock;

	@Override
	public BonPreparation genereBM(ContratMigrationInfo migrationInfo) throws TopazeException, JSONException {

		/*
		 * l'avenant contient les informations de contrat après migration.
		 */
		Avenant avenant = migrationInfo.getAvenant();

		/*
		 * Le bon de preparation contient les informations du contrat avant migration.
		 */
		BonPreparation bonPreparation = migrationInfo.getBonPreparation();

		/*
		 * On crée le bon de migration.
		 */
		BonPreparation bonMigration = new BonPreparation();
		bonMigration.setTypeBonPreparation(TypeBonPreparation.MIGRATION);
		bonMigration.setReference(bonPreparation.getReference());
		bonMigration.setIdClient(bonPreparation.getIdClient());

		/*
		 * On cherche les elements de livraison. Ces sont les elements qui existe dans l'avenant et n'existe pas dans le
		 * bon de preparation de l'ancien contrat.
		 */
		for (ElementLivraison contratModification : avenant.getContratModifications()) {
			boolean estPresent = false;
			ChercherEMLivraison: for (ElementLivraison elementLivraison : bonPreparation.getElementLivraisons()) {
				if (contratModification.getNumEC() == elementLivraison.getNumEC()) {
					estPresent = true;
					break ChercherEMLivraison;
				}
				contratModification.setAddresseLivraison(elementLivraison.getAddresseLivraison());
			}

			/*
			 * S’il y a un element de livraison, alors copier le comportement de la livraison.
			 */
			if (!estPresent) {
				contratModification.setTypeBonPreparation(TypeBonPreparation.LIVRAISON);
				bonMigration.getElementLivraisons().add(contratModification);
			}
		}

		/*
		 * On cherche les elements qui existent dans le nouveau contrat et l'ancien contrat. Pour identifier les
		 * elements de migration.
		 */
		for (ElementLivraison contratModification : avenant.getContratModifications()) {
			for (ElementLivraison elementLivraison : bonPreparation.getElementLivraisons()) {

				/*
				 * S’il n’y a aucune modification Alors je ne crée pas d’élément de migration. S’il y a modification, et
				 * que c’est une migration.
				 */
				if (contratModification.getNumEC() == elementLivraison.getNumEC()
						&& !contratModification.getReferenceProduit().equals(elementLivraison.getReferenceProduit())) {
					/*
					 * si ça concerne des biens. On effectue l’échange de matériel (SWAP).
					 */
					if (contratModification.getTypeElement().equals(TypeProduit.BIEN)) {
						contratModification.setActeur(OutilLivraison.SWAP);
						contratModification.setTypeBonPreparation(TypeBonPreparation.MIGRATION);
						contratModification.setReferenceAncienProduit(elementLivraison.getReferenceProduit());

						bonMigration.getElementLivraisons().add(contratModification);
					}

					/*
					 * Si ça concerne des services.
					 */
					else if (contratModification.getTypeElement().equals(TypeProduit.SERVICE)) {

						/*
						 * On test si on a un « option+ » ou « freezone ».On peut savoire si on a un « option+ » ou «
						 * freezone » à l'aide de PackagerConfig de l'element.
						 */
						PackagerConfig packagerConfig =
								netCatalogueMock.getPackagerConfig(contratModification.getReferenceProduit());

						/*
						 * Si c'est une migration dans la même gamme.
						 */
						if (contratModification.getReferenceGammeDestination().equals(
								contratModification.getReferenceGammeSource())) {

							/*
							 * Si on n'a pas un template alors le produit est « option+ » ou « freezone ».
							 */
							if (packagerConfig.getTemplate() == null) {

								/*
								 * Si je n’ai pas déjà un EM pour le produit de l’EC parent Alors je crée 2 éléments de
								 * migration : le 1er avec le produit de l’EC parent, le 2ème avec le produit « Option+
								 * » ou « Freezone »
								 */
								ElementLivraison elementLivraisonParentAvenant = null;
								ElementLivraison elementLivraisonParentAncien = null;

								/*
								 * On cherche l'element de livraison parent dans l'avenant.
								 */
								ChercheELParentAvenant: for (ElementLivraison elAvenant : avenant
										.getContratModifications()) {
									if (elAvenant.getNumEC().equals(
											contratModification.getElementLivraisonParent().getNumEC())
											&& elAvenant.getReferenceProduit().equals(
													contratModification.getElementLivraisonParent()
															.getReferenceProduit())) {
										elementLivraisonParentAvenant = elAvenant;
										break ChercheELParentAvenant;
									}
								}

								/*
								 * On cherche l'element de livraison parent dans l'ancien contrat.
								 */
								ChercheELParentAncien: for (ElementLivraison elAncien : bonPreparation
										.getElementLivraisons()) {
									if (elAncien.getNumEC().equals(
											contratModification.getElementLivraisonParent().getNumEC())) {
										elementLivraisonParentAncien = elAncien;
										break ChercheELParentAncien;
									}
								}

								/*
								 * Pour l'EL parent il faut comparer les numEC, les references des produits et le gamme
								 * source et distination. S'ils sont egaux donc on n'a pas crée un element de migration
								 * pour le parent de l'option.
								 */
								if (elementLivraisonParentAvenant != null
										&& elementLivraisonParentAncien != null
										&& elementLivraisonParentAvenant.getReferenceGammeDestination().equals(
												elementLivraisonParentAvenant.getReferenceGammeSource())
										&& elementLivraisonParentAncien.getReferenceProduit().equals(
												elementLivraisonParentAvenant.getReferenceProduit())) {
									/*
									 * bon de migration de produit de l’EC parent.
									 */
									contratModification.getElementLivraisonParent().setTypeBonPreparation(
											TypeBonPreparation.MIGRATION);
									contratModification.getElementLivraisonParent().setReferenceAncienProduit(
											elementLivraisonParentAncien.getReferenceProduit());

									bonMigration.getElementLivraisons().add(
											contratModification.getElementLivraisonParent());
								}

							}

							/*
							 * bon de migration de produit de l’EC.
							 */
							contratModification.setTypeBonPreparation(TypeBonPreparation.MIGRATION);
							contratModification.setReferenceAncienProduit(elementLivraison.getReferenceProduit());

							bonMigration.getElementLivraisons().add(contratModification);
						}

						/*
						 * Si c'est une migration dans une autre gamme.
						 */
						else {

							/*
							 * Si on n'a pas un template alors le produit est « option+ » ou « freezone ».
							 */
							if (packagerConfig.getTemplate() == null) {

								/*
								 * Si je n’ai pas déjà un EM pour le produit de l’EC parent Lever une erreur.
								 */
								ElementLivraison elementLivraisonParentAvenant = null;
								ElementLivraison elementLivraisonParentAncien = null;

								/*
								 * On cherche l'element de livraison parent dans l'avenant.
								 */
								ChercheELParent: for (ElementLivraison elAvenant : avenant.getContratModifications()) {
									if (elAvenant.getNumEC().equals(
											contratModification.getElementLivraisonParent().getNumEC())
											&& elAvenant.getReferenceProduit().equals(
													contratModification.getElementLivraisonParent()
															.getReferenceProduit())) {
										elementLivraisonParentAvenant = elAvenant;
										break ChercheELParent;
									}
								}

								/*
								 * On cherche l'element de livraison parent dans l'ancien contrat.
								 */
								ChercheELParentAncien: for (ElementLivraison elAncien : bonPreparation
										.getElementLivraisons()) {
									if (elAncien.getNumEC().equals(
											contratModification.getElementLivraisonParent().getNumEC())) {
										elementLivraisonParentAncien = elAncien;
										break ChercheELParentAncien;
									}
								}

								/*
								 * Pour l'EL parent il faut comparer les numEC, les references des produits et le gamme
								 * source et distination. S'ils sont egaux donc on n'a pas crée un element de migration
								 * pour le parent de l'option.
								 */
								if (elementLivraisonParentAvenant != null
										&& elementLivraisonParentAncien != null
										&& elementLivraisonParentAvenant.getReferenceGammeDestination().equals(
												elementLivraisonParentAvenant.getReferenceGammeSource())
										&& elementLivraisonParentAncien.getReferenceProduit().equals(
												elementLivraisonParentAvenant.getReferenceProduit())) {
									throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("3.1.10"),
											"3.1.10");
								}
							}

							/*
							 * On crée 2 éléments de livraison : le 1er de type « RETOUR », le 2ème de type « LIVRAISON
							 * »
							 */
							elementLivraison.setTypeBonPreparation(TypeBonPreparation.RETOUR);
							bonMigration.getElementLivraisons().add(elementLivraison);
							contratModification.setTypeBonPreparation(TypeBonPreparation.LIVRAISON);
							contratModification.setAddresseLivraison(elementLivraison.getAddresseLivraison());
							bonMigration.getElementLivraisons().add(contratModification);
						}

					}

				}
			}
		}

		/*
		 * On cherche les elements de retour. Ces sont les elements qui existe dans l'ancien contrat et n'existe pas
		 * dans l'avenant.
		 */
		for (ElementLivraison elementLivraison : bonPreparation.getElementLivraisons()) {
			boolean estPresent = false;
			ChercherEMRetour: for (ElementLivraison contratModification : avenant.getContratModifications()) {
				if (elementLivraison.getNumEC() == contratModification.getNumEC()) {
					estPresent = true;
					break ChercherEMRetour;
				}
			}

			/*
			 * S’il y a un element de retour, alors copier le comportement de la retour.
			 */
			if (!estPresent) {
				elementLivraison.setTypeBonPreparation(TypeBonPreparation.RETOUR);
				if (elementLivraison.getTypeElement().equals(TypeProduit.BIEN)) {
					elementLivraison.setActeur(OutilLivraison.NETRETOUR);
				}
				bonMigration.getElementLivraisons().add(elementLivraison);
			}
		}

		return bonMigration;

	}

	@Override
	public void initierBP(String referenceContrat) throws TopazeException {
		// Get BonPreparation from Livraison-Outil
		BonPreparation bonPreparation = restClientLivraison.getBonPreparationInfo(referenceContrat);
		// Appel vers Livraison-Core pour Initier le Bon de preparation.
		restClientLivraison.initierBP(bonPreparation);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initierBM(String referenceContrat) throws TopazeException, JSONException {
		ContratMigrationInfo migrationInfo = restClientLivraison.getContratMigrationInfo(referenceContrat);
		// initiation du Bon de Migration.
		BonPreparation bonMigration = genereBM(migrationInfo);
		if (!Utils.isListNullOrEmpty(bonMigration.getElementLivraisons())) {
			restClientLivraison.initierBM(bonMigration);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initierBRE(String referenceContrat) throws TopazeException {
		// Get BonPreparation from Livraison-Outil
		BonPreparation bonPreparation = restClientLivraison.getBonPreparationInfo(referenceContrat);
		bonPreparation.setTypeBonPreparation(TypeBonPreparation.RENOUVELLEMENT);
		for (ElementLivraison elementLivraison : bonPreparation.getElementLivraisons()) {
			elementLivraison.setTypeBonPreparation(TypeBonPreparation.RENOUVELLEMENT);
		}
		// Appel vers Livraison-Core pour Initier le Bon de renouvellement.
		restClientLivraison.initierBRE(bonPreparation);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initiationRecuperation(String referenceContrat, Integer numEC, boolean isResiliationPartiel)
			throws TopazeException {
		if (!isResiliationPartiel) {

			/*
			 * s'il n'existe pas un BP associe au contrat alors lors de la resiliation il ne faut pas instancier un BR.
			 */
			Optional<BonPreparation> bonPreparation =
					Optional.fromNullable(restClientLivraison.getBonPreparation(referenceContrat));
			if (bonPreparation.isPresent()) {
				/*
				 * si le BP est instancie sans etre prepare, il suffit de blocker (annuler) la livraison du BP, sans
				 * instancier un BR.
				 */
				if (bonPreparation.get().isInitier()) {

					/*
					 * si le BP passe a l'état preparer lors de l'annulation alors on initie un bon de retour.
					 */
					try {
						restClientLivraison.annuler(referenceContrat);
					} catch (TopazeException ex) {
						if (ex.getErrorCode().equals("3.1.11")) {
							restClientLivraison.initierBR(referenceContrat);
						} else {
							throw new TopazeException(ex.getMessage(), ex.getErrorCode());
						}
					}

				} else if (bonPreparation.get().isNonLivrer()) {
					/*
					 * si BP est non livre alors pour chaque EL livre il faut initier un ER.
					 */
					for (ElementLivraison elementLivraison : bonPreparation.get().getElementLivraisons()) {
						/*
						 * l'ER ne sera initier que pour une EL prepare/livre/enCoursLivraison
						 */
						if (!(elementLivraison.isInitier() || elementLivraison.isNonLivrer())) {
							restClientLivraison.initierER(elementLivraison.getReference());
						}
					}
				} else if (bonPreparation.get().isPreparer() || bonPreparation.get().isTermine()) {

					/*
					 * si le BP est prepare/en cours preparation/livre alors la resiliation se fait en instanciant in
					 * BR.
					 */
					restClientLivraison.initierBR(referenceContrat);
				}

			}

		} else {
			restClientLivraison.initierER(referenceContrat + "-" + numEC);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initierCession(String referenceContrat) throws TopazeException {
		ContratBP contratBP = restClientLivraison.getContratBP(referenceContrat);
		BonPreparation bonPreparation = restClientLivraison.getBonPreparation(referenceContrat);
		bonPreparation.setDateDebutPreparation(null);
		bonPreparation.setDatePreparation(null);
		bonPreparation.setDateLivraisonTermine(null);
		bonPreparation.setTypeBonPreparation(TypeBonPreparation.CESSION);
		bonPreparation.setIdClient(contratBP.getIdClient());
		for (ElementLivraison elementLivraison : bonPreparation.getElementLivraisons()) {
			elementLivraison.setDateDebutPreparation(null);
			elementLivraison.setDatePreparation(null);
			elementLivraison.setDateLivraisonTermine(null);
			elementLivraison.setTypeBonPreparation(TypeBonPreparation.CESSION);
		}
		restClientLivraison.initierCession(bonPreparation);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void changerDateDebutFacturation(String referenceContrat) throws TopazeException {
		restClientLivraison.changerDateDebutFacturation(referenceContrat, PropertiesUtil.getInstance().getDateDuJour()
				.toDate());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void preparerBP(String referenceBP) throws TopazeException {

		/*
		 * Appel vers la brique Livraison Core pour preparer le bon de Preparation de reference indique, la preparation
		 * ne s'effectuer que pour un element qui n'est pas annule.
		 */
		BonPreparation bonPreparation = restClientLivraison.getBonPreparation(referenceBP);
		if (!bonPreparation.isAnnule()) {
			restClientLivraison.preparerBP(referenceBP);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void preparerBM(String referenceBM) throws TopazeException {
		restClientLivraison.preparerBM(referenceBM);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void marquerBPGlobalLivre(String referenceBP) throws TopazeException {
		BonPreparation bonPreparationGlobal = restClientLivraison.getBonPreparation(referenceBP);
		if (!bonPreparationGlobal.isAnnule()) {
			boolean isLivre = true;
			for (ElementLivraison elementLivraison : bonPreparationGlobal.getElementLivraisons()) {
				if (!elementLivraison.isTermine()) {
					isLivre = false;
				}
			}
			if (isLivre) {
				// Appel vers la brique Livraison Core pour marquer un BP global livre
				restClientLivraison.marquerBPGlobalLivre(bonPreparationGlobal.getReference());
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void marquerBMLivre(String referenceBM) throws TopazeException {
		BonPreparation bonPreparationGlobal = restClientLivraison.getBonMigration(referenceBM);
		boolean isLivre = true;
		for (ElementLivraison elementLivraison : bonPreparationGlobal.getElementLivraisons()) {
			if ((elementLivraison.getTypeBonPreparation().equals(TypeBonPreparation.MIGRATION))
					|| (elementLivraison.getTypeBonPreparation().equals(TypeBonPreparation.LIVRAISON))) {
				if (elementLivraison.getDateLivraisonTermine() == null) {
					isLivre = false;
				}
			}
		}
		if (isLivre) {
			// Appel vers la brique Livraison Core pour marquer un BM global livre
			restClientLivraison.marquerBMLivre(bonPreparationGlobal.getReference());
		}

	}

	@Override
	public void marquerBMRetourne(String referenceBM) throws TopazeException {
		BonPreparation bonPreparationGlobal = restClientLivraison.getBonMigration(referenceBM);
		boolean isRetourne = true;
		for (ElementLivraison elementLivraison : bonPreparationGlobal.getElementLivraisons()) {
			if (elementLivraison.getTypeBonPreparation().equals(TypeBonPreparation.MIGRATION)
					|| (elementLivraison.getTypeBonPreparation().equals(TypeBonPreparation.RETOUR))) {
				if (elementLivraison.getDateRetourTermine() == null) {
					isRetourne = false;
				}

			}
		}
		if (isRetourne) {
			// Appel vers la brique Livraison Core pour marquer un BM global livre
			restClientLivraison.marquerBMRetourne(bonPreparationGlobal.getReference());
		}

	}

	@Override
	public void marquerBMGlobalPreparer(String referenceBP, TypeBonPreparation typeBonPreparation)
			throws TopazeException {

		BonPreparation bonPreparationGlobal = null;
		if (typeBonPreparation.equals(TypeBonPreparation.LIVRAISON)) {
			bonPreparationGlobal = restClientLivraison.getBonPreparation(referenceBP);
		} else if (typeBonPreparation.equals(TypeBonPreparation.MIGRATION)) {
			bonPreparationGlobal = restClientLivraison.getBonMigration(referenceBP);
		} else {
			throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("0.1.1", "TypeBonPreparation"),
					"1.1.93");
		}
		boolean isPrepare = true;
		TesteSiTousELPreparer: for (ElementLivraison elementLivraison : bonPreparationGlobal.getElementLivraisons()) {
			if (!(elementLivraison.isPreparer() || elementLivraison.isTermine())) {
				isPrepare = false;
				break TesteSiTousELPreparer;
			}
		}
		if (isPrepare) {
			if (typeBonPreparation.equals(TypeBonPreparation.LIVRAISON)) {
				// Appel vers la brique Livraison Core pour marquer un BP global livre
				restClientLivraison.marquerBPGlobalPreparer(bonPreparationGlobal.getReference());
			} else if (typeBonPreparation.equals(TypeBonPreparation.MIGRATION)) {
				// Appel vers la brique Livraison Core pour marquer un BM global comme prepare
				restClientLivraison.marquerBMGlobalPreparer(bonPreparationGlobal.getReference());
			}
		}
	}

	@Override
	public void marquerBPGlobalRenouvele(String referenceBP) throws TopazeException {
		// recuperer le bon de renouvellement
		BonPreparation bonRenouvellement = restClientLivraison.getBonRenouvellement(referenceBP);
		boolean renouvellementTotal = true;
		for (ElementLivraison elementLivraison : bonRenouvellement.getElementLivraisons()) {
			// tester si la date de livraison est terminee
			if (!elementLivraison.isCede()) {
				renouvellementTotal = false;
			}
		}

		/*
		 * si tout les element sont renouvele alors le bon global est renouvele.
		 */
		if (renouvellementTotal) {
			restClientLivraison.marquerBPGlobalRenouvele(bonRenouvellement);
		}
	}

	@Override
	public void marquerBPGlobalCede(String referenceBP) throws TopazeException {
		BonPreparation bonCession = restClientLivraison.getBonCession(referenceBP);
		boolean cedeTotal = true;
		for (ElementLivraison elementLivraison : bonCession.getElementLivraisons()) {
			if (!elementLivraison.isCede()) {
				cedeTotal = false;
			}
		}

		/*
		 * si tout les element sont cede alors le bon global est cede.
		 */
		if (cedeTotal) {
			restClientLivraison.marquerBPGlobalCede(bonCession);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void preparerBR(String referenceBR) throws TopazeException {
		BonPreparation bonRetour = restClientLivraison.getBonRecuperation(referenceBR);
		restClientLivraison.preparerBR(bonRetour);
	}

	@Override
	public void preparerER(String referenceBR) throws TopazeException {
		BonPreparation bonRetour = restClientLivraison.getBonRecuperation(referenceBR);
		restClientLivraison.preparerER(bonRetour);
	}
}