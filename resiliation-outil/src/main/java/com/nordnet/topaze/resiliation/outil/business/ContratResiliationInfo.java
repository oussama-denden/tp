package com.nordnet.topaze.resiliation.outil.business;

import java.util.ArrayList;
import java.util.List;

/**
 * Information de resiliation d'un contrat global.
 * 
 * @author Denden Oussama
 */
public class ContratResiliationInfo {

	/**
	 * reference contrat global.
	 */
	private String referenceContrat;

	/**
	 * Frais a montant definit.
	 */
	private Frais frais;

	/**
	 * Remboursement a montant definit.
	 */
	private RemboursementBillingInfo remboursementBillingInfo;

	/**
	 * {@link ECResiliationInfo}.
	 */
	private List<ECResiliationInfo> paymentInfos = new ArrayList<>();

	/**
	 * liste des biens et des services a retourner.
	 */
	private List<RetourInfo> retourInfos = new ArrayList<>();

	/**
	 * constructeur par defaut.
	 */
	public ContratResiliationInfo() {

	}

	@Override
	public String toString() {
		return "ContratResiliationInfo [referenceContrat=" + referenceContrat + ", frais=" + frais + ", paymentInfos="
				+ paymentInfos + "]";
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
	public Frais getFrais() {
		return frais;
	}

	/**
	 * @param frais
	 *            {@link #frais}.
	 */
	public void setFrais(Frais frais) {
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
	 * @return {@link #paymentInfos}.
	 */
	public List<ECResiliationInfo> getPaymentInfos() {
		return paymentInfos;
	}

	/**
	 * @param paymentInfos
	 *            {@link #paymentInfos}.
	 */
	public void setPaymentInfos(List<ECResiliationInfo> paymentInfos) {
		this.paymentInfos = paymentInfos;
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

}