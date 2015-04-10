package com.nordnet.topaze.contrat.business;

import java.util.ArrayList;
import java.util.List;

/**
 * Les Information d'un contrat.
 * 
 * @author Oussama Denden
 * 
 */
public class ContratMigrationSimulationInfo {

	/**
	 * reference contrat global.
	 */
	private String referenceContrat;

	/**
	 * {@link ECMigrationSimulationInfo}.
	 */
	private List<ECMigrationSimulationInfo> paymentInfos = new ArrayList<>();

	/**
	 * constructeur par defaut.
	 */
	public ContratMigrationSimulationInfo() {

	}

	@Override
	public String toString() {
		return "ContratResiliationInfo [referenceContrat=" + referenceContrat + ", paymentInfos=" + paymentInfos + "]";
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
	 * @return {@link #paymentInfos}.
	 */
	public List<ECMigrationSimulationInfo> getPaymentInfos() {
		return paymentInfos;
	}

	/**
	 * @param paymentInfos
	 *            {@link #paymentInfos}.
	 */
	public void setPaymentInfos(List<ECMigrationSimulationInfo> paymentInfos) {
		this.paymentInfos = paymentInfos;
	}

}