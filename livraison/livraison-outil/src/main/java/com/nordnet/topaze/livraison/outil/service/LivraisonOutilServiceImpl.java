package com.nordnet.topaze.livraison.outil.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nordnet.backoffice.ws.NetCustomer;
import com.nordnet.topaze.client.rest.RestClientLivraisonOutil;
import com.nordnet.topaze.client.rest.business.livraison.Avenant;
import com.nordnet.topaze.client.rest.business.livraison.BonPreparation;
import com.nordnet.topaze.client.rest.business.livraison.ContratAvenantInfo;
import com.nordnet.topaze.client.rest.business.livraison.ContratBP;
import com.nordnet.topaze.client.rest.business.livraison.ContratMigrationInfo;
import com.nordnet.topaze.client.rest.business.livraison.ElementLivraison;
import com.nordnet.topaze.client.rest.enums.OutilLivraison;
import com.nordnet.topaze.client.rest.enums.TypeBonPreparation;
import com.nordnet.topaze.client.rest.enums.TypeProduit;
import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.livraison.outil.mock.NetCustomerMock;
import com.nordnet.topaze.livraison.outil.util.PropertiesUtil;

/**
 * implementation du {@link LivraisonOutilService}.
 * 
 * @author Denden-OUSSAMA
 * 
 */
@Service("livraisonOutilService")
public class LivraisonOutilServiceImpl implements LivraisonOutilService {

	/**
	 * {@link RestClientLivraisonOutil}.
	 */
	@Autowired
	private RestClientLivraisonOutil restClientLivraisonOutil;

	/**
	 * {@link NetCustomer}.
	 */
	private NetCustomer netCustomer;

	/**
	 * {@inheritDoc}
	 * 
	 * @throws TopazeException
	 */
	@Override
	public BonPreparation getBonPreparation(String referenceBP) throws TopazeException {
		// ERR_TODO Appel vers NetCatalog pour l'outil de livraison
		// ERR_TODO Appel vers NetCustomer pour le client
		ContratBP contratBP = restClientLivraisonOutil.getContratBP(referenceBP);

		return creeBonPreparation(contratBP);
	}

	/**
	 * Cree un {@link BonPreparation} a partir d'un {@link ContratBP}.
	 * 
	 * @param contratBP
	 *            {@link ContratBP}.
	 * @return {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	private BonPreparation creeBonPreparation(ContratBP contratBP) throws TopazeException {
		BonPreparation bonPreparation = new BonPreparation();
		bonPreparation.setReference(contratBP.getReferenceContrat());
		bonPreparation.setIdClient(contratBP.getIdClient());
		bonPreparation.setDateInitiation(PropertiesUtil.getInstance().getDateDuJour().toDate());
		bonPreparation.setTypeBonPreparation(TypeBonPreparation.LIVRAISON);

		Set<ElementLivraison> elementLivraisons = new HashSet<>();
		if (contratBP.getSousContratsBP() != null) {
			for (ContratBP sousContratBP : contratBP.getSousContratsBP()) {
				ElementLivraison elementLivraison = creeElementLivraison(sousContratBP);
				elementLivraison.setBonPreparationParent(bonPreparation);
				elementLivraisons.add(elementLivraison);
			}
		}

		bonPreparation.setElementLivraison(elementLivraisons);

		return bonPreparation;
	}

	private ElementLivraison creeElementLivraison(ContratBP sousContratBP) throws TopazeException {
		ElementLivraison elementLivraison = new ElementLivraison();
		elementLivraison.setReference(sousContratBP.getReferenceContrat() + "-" + sousContratBP.getNumEC());
		elementLivraison.setReferenceProduit(sousContratBP.getReferenceProduit());
		elementLivraison.setReferenceGammeDestination(sousContratBP.getReferenceGammeDestination());
		elementLivraison.setReferenceGammeSource(sousContratBP.getReferenceGammeSource());
		elementLivraison.setNumEC(sousContratBP.getNumEC());
		elementLivraison.setAddresseLivraison(sousContratBP.getIdAdrLivraison());
		elementLivraison.setDateInitiation(PropertiesUtil.getInstance().getDateDuJour().toDate());
		if (sousContratBP.getTypeProduit() == TypeProduit.SERVICE) {
			elementLivraison.setActeur(OutilLivraison.PACKAGER);
		} else {
			elementLivraison.setActeur(OutilLivraison.NETDELIVERY);
		}
		elementLivraison.setTypeContrat(sousContratBP.getTypeContrat());
		elementLivraison.setTypeElement(sousContratBP.getTypeProduit());
		elementLivraison.setTypeBonPreparation(TypeBonPreparation.LIVRAISON);

		if (sousContratBP.getContratBPParent() != null) {
			elementLivraison.setElementLivraisonParent(creeElementLivraison(sousContratBP.getContratBPParent()));
		}
		return elementLivraison;
	}

	@Override
	public ContratMigrationInfo getContratMigrationInfo(String referenceContrat) throws TopazeException {
		ContratAvenantInfo avenantInfo = restClientLivraisonOutil.getContratAvenant(referenceContrat);
		BonPreparation bonPreparation = getBonPreparationAncienContrat(referenceContrat);
		ContratMigrationInfo migrationInfo = new ContratMigrationInfo();
		List<ElementLivraison> elementLivraisons = new ArrayList<>();
		for (ContratBP contratBP : avenantInfo.getContratModifications()) {
			elementLivraisons.add(creeElementLivraison(contratBP));
		}
		Avenant avenant = new Avenant();
		avenant.setDateAction(avenantInfo.getDateAction());
		avenant.setUser(avenantInfo.getUser());
		avenant.setContratModifications(elementLivraisons);
		migrationInfo.setAvenant(avenant);
		migrationInfo.setBonPreparation(bonPreparation);
		return migrationInfo;
	}

	/**
	 * cree le bon de preparation de l'ancient contrat apres migration.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @return {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	private BonPreparation getBonPreparationAncienContrat(String referenceContrat) throws TopazeException {
		ContratBP contratBP = restClientLivraisonOutil.getContratBPHistorique(referenceContrat);

		return creeBonPreparation(contratBP);
	}

	/**
	 * @return {@link #netCustomer}.
	 */
	public NetCustomer getNetCustomer() {
		if (netCustomer == null) {
			// netCustomer =
			// NetCustomerClient.builder()
			// .withProperties("ws.customer",
			// PropertiesUtil.getInstance().getNetCustomerProperties()).withMockedPort(NetCustomerMock.getInstance())
			// .build(NetCustomerClient.class);
			netCustomer = NetCustomerMock.getInstance();
		}

		return netCustomer;
	}

}
