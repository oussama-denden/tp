package com.nordnet.topaze.migration.outil.business;

import java.util.HashSet;
import java.util.Set;

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
	private Set<ECMigrationSimulationInfo> paymentInfos = new HashSet<>();

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
	public Set<ECMigrationSimulationInfo> getPaymentInfos() {
		return paymentInfos;
	}

	/**
	 * @param paymentInfos
	 *            {@link #paymentInfos}.
	 */
	public void setPaymentInfos(Set<ECMigrationSimulationInfo> paymentInfos) {
		this.paymentInfos = paymentInfos;
	}

}
