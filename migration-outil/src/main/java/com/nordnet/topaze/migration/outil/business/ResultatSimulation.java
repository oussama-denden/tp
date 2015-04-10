package com.nordnet.topaze.migration.outil.business;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Le resultat du simulation.
 * 
 * @author Oussama Denden
 * 
 */
public class ResultatSimulation {

	/**
	 * reference contrat global.
	 */
	private String referenceContrat;

	/**
	 * Frais a montant definit.
	 */
	private List<FraisMigrationSimulation> frais = new ArrayList<>();

	/**
	 * Remboursement a montant definit.
	 */
	private RemboursementBillingInfo remboursementBillingInfo;

	/**
	 * {@link ECMigrationSimulationInfo}.
	 */
	private Set<ResultatSimulationEC> paymentInfosLivraison = new HashSet<>();

	/**
	 * {@link ECMigrationSimulationInfo}.
	 */
	private Set<ResultatSimulationEC> paymentInfosMigration = new HashSet<>();

	/**
	 * {@link ECMigrationSimulationInfo}.
	 */
	private Set<ResultatSimulationEC> paymentInfosMigrationAdministrative = new HashSet<>();

	/**
	 * {@link ECMigrationSimulationInfo}.
	 */
	private Set<ResultatSimulationEC> paymentInfosResiliation = new HashSet<>();

	/**
	 * liste des biens et des services a retourner.
	 */
	private List<RetourInfo> livraisonInfos = new ArrayList<>();

	/**
	 * liste des biens et des services a retourner.
	 */
	private List<RetourInfo> retourInfos = new ArrayList<>();

	/**
	 * liste des biens et services a echanges.
	 */
	private List<EchangeInfo> echangeInfos = new ArrayList<>();

	/**
	 * constructeur par defaut.
	 */
	public ResultatSimulation() {

	}

	@Override
	public String toString() {
		return "ResultatSimulation [referenceContrat=" + referenceContrat + ", frais=" + frais
				+ ", remboursementBillingInfo=" + remboursementBillingInfo + ", paymentInfosLivraison="
				+ paymentInfosLivraison + ", paymentInfosMigration=" + paymentInfosMigration
				+ ", paymentInfosMigrationAdministrative=" + paymentInfosMigrationAdministrative
				+ ", paymentInfosResiliation=" + paymentInfosResiliation + ", livraisonInfos=" + livraisonInfos
				+ ", retourInfos=" + retourInfos + ", echangeInfos=" + echangeInfos + "]";
	}

	/**
	 * @return {@link #referenceContrat}.
	 */
	public String getReferenceContrat() {
		return referenceContrat;
	}

	/**
	 * @param referenceContrat
	 *            {@link #referenceContrat}.
	 */
	public void setReferenceContrat(String referenceContrat) {
		this.referenceContrat = referenceContrat;
	}

	/**
	 * @return {@link #frais}.
	 */
	public List<FraisMigrationSimulation> getFrais() {
		return frais;
	}

	/**
	 * @param frais
	 *            {@link #frais}.
	 */
	public void setFrais(List<FraisMigrationSimulation> frais) {
		this.frais = frais;
	}

	/**
	 * @return {@link #remboursementBillingInfo}.
	 */
	public RemboursementBillingInfo getRemboursementBillingInfo() {
		return remboursementBillingInfo;
	}

	/**
	 * @param remboursementBillingInfo
	 *            {@link #remboursementBillingInfo}.
	 */
	public void setRemboursementBillingInfo(RemboursementBillingInfo remboursementBillingInfo) {
		this.remboursementBillingInfo = remboursementBillingInfo;
	}

	/**
	 * @return {@link #paymentInfosLivraison}.
	 */
	public Set<ResultatSimulationEC> getPaymentInfosLivraison() {
		return paymentInfosLivraison;
	}

	/**
	 * @param paymentInfosLivraison
	 *            {@link #paymentInfosLivraison}.
	 */
	public void setPaymentInfosLivraison(Set<ResultatSimulationEC> paymentInfosLivraison) {
		this.paymentInfosLivraison = paymentInfosLivraison;
	}

	/**
	 * @return {@link #paymentInfosMigration}.
	 */
	public Set<ResultatSimulationEC> getPaymentInfosMigration() {
		return paymentInfosMigration;
	}

	/**
	 * @param paymentInfosMigration
	 *            {@link #paymentInfosMigration}.
	 */
	public void setPaymentInfosMigration(Set<ResultatSimulationEC> paymentInfosMigration) {
		this.paymentInfosMigration = paymentInfosMigration;
	}

	/**
	 * @return {@link #paymentInfosMigrationAdministrative}.
	 */
	public Set<ResultatSimulationEC> getPaymentInfosMigrationAdministrative() {
		return paymentInfosMigrationAdministrative;
	}

	/**
	 * @param paymentInfosMigrationAdministrative
	 *            {@link #paymentInfosMigrationAdministrative}.
	 */
	public void setPaymentInfosMigrationAdministrative(Set<ResultatSimulationEC> paymentInfosMigrationAdministrative) {
		this.paymentInfosMigrationAdministrative = paymentInfosMigrationAdministrative;
	}

	/**
	 * @return {@link #paymentInfosResiliation}.
	 */
	public Set<ResultatSimulationEC> getPaymentInfosResiliation() {
		return paymentInfosResiliation;
	}

	/**
	 * @param paymentInfosResiliation
	 *            {@link #paymentInfosResiliation}.
	 */
	public void setPaymentInfosResiliation(Set<ResultatSimulationEC> paymentInfosResiliation) {
		this.paymentInfosResiliation = paymentInfosResiliation;
	}

	/**
	 * @return {@link #livraisonInfos}.
	 */
	public List<RetourInfo> getLivraisonInfos() {
		return livraisonInfos;
	}

	/**
	 * @param livraisonInfos
	 *            {@link #livraisonInfos}.
	 */
	public void setLivraisonInfos(List<RetourInfo> livraisonInfos) {
		this.livraisonInfos = livraisonInfos;
	}

	/**
	 * @return {@link #retourInfos}.
	 */
	public List<RetourInfo> getRetourInfos() {
		return retourInfos;
	}

	/**
	 * @param retourInfos
	 *            {@link #retourInfos}.
	 */
	public void setRetourInfos(List<RetourInfo> retourInfos) {
		this.retourInfos = retourInfos;
	}

	/**
	 * @return {@link #echangeInfos}.
	 */
	public List<EchangeInfo> getEchangeInfos() {
		return echangeInfos;
	}

	/**
	 * @param echangeInfos
	 *            {@link #echangeInfos}.
	 */
	public void setEchangeInfos(List<EchangeInfo> echangeInfos) {
		this.echangeInfos = echangeInfos;
	}

}
