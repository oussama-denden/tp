package com.nordnet.topaze.resiliation.outil.business;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * Remboursement.
 * 
 * @author Oussama Denden
 * 
 */
public class Remboursement {

	/**
	 * montant.
	 */
	private Double montant;

	/**
	 * date debut.
	 */
	private Date dateDebut;

	/**
	 * date fin.
	 */
	private Date dateFin;

	/**
	 * information de reduction definit dans {@link DiscountInfo}.
	 */
	private DiscountInfo discountInfo;

	/**
	 * constructeur par defaut.
	 */
	public Remboursement() {

	}

	@Override
	public String toString() {
		return "Remboursement [montant=" + montant + ", dateDebut=" + dateDebut + ", dateFin=" + dateFin + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Remboursement)) {
			return false;
		}
		Remboursement rhs = (Remboursement) obj;
		return new EqualsBuilder().append(montant, rhs.montant).append(dateDebut, rhs.dateDebut)
				.append(dateFin, rhs.dateFin).isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(43, 11).append(montant).append(dateDebut).append(dateFin).toHashCode();
	}

	/* Getters & Setters */

	/**
	 * @return {@link #montant}.
	 */
	public Double getMontant() {
		return montant;
	}

	/**
	 * @param montant
	 *            {@link #montant}.
	 */
	public void setMontant(Double montant) {
		this.montant = montant;
	}

	/**
	 * @return {@link #dateDebut}.
	 */
	public Date getDateDebut() {
		return dateDebut;
	}

	/**
	 * @param dateDebut
	 *            {@link #dateDebut}.
	 */
	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	/**
	 * @return {@link #dateFin}.
	 */
	public Date getDateFin() {
		return dateFin;
	}

	/**
	 * @param dateFin
	 *            {@link #dateFin}.
	 */
	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	/**
	 * @return {@link #discountInfo}.
	 */
	public DiscountInfo getDiscountInfo() {
		return discountInfo;
	}

	/**
	 * @param discountInfo
	 *            {@link #discountInfo}.
	 */
	public void setDiscountInfo(DiscountInfo discountInfo) {
		this.discountInfo = discountInfo;
	}
}
