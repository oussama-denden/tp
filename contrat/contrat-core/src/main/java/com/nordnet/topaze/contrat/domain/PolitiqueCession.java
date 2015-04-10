package com.nordnet.topaze.contrat.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.nordnet.topaze.contrat.business.PolitiqueResiliation;
import com.nordnet.topaze.contrat.util.PropertiesUtil;
import com.nordnet.topaze.contrat.util.Utils;
import com.nordnet.topaze.exception.TopazeException;

/**
 * politique de cession d'un {@link Contrat}.
 * 
 * @author akram-moncer
 * 
 */
@Entity
@Table(name = "politiquecession")
public class PolitiqueCession {

	/**
	 * cle primaire.
	 */
	@Id
	@GeneratedValue
	private Integer id;

	/**
	 * Flag remboursement.
	 */
	private boolean remboursement;

	/**
	 * Le montant defini pour le remboursement.
	 */
	private Double montantRemboursement;

	/**
	 * frais de cession.
	 */
	private double fraisCession;

	/**
	 * date de cession.
	 */
	private Date dateAction;

	/**
	 * true/false on garde les ancienne reduction.
	 */
	private Boolean conserverAncienneReduction;

	/**
	 * Constructeur par default.
	 */
	public PolitiqueCession() {

	}

	@Override
	public String toString() {
		return "PolitiqueCession [id=" + id + ", remboursement=" + remboursement + ", montantRemboursement="
				+ montantRemboursement + ", fraisCession=" + fraisCession + ", dateAction=" + dateAction
				+ ", conserverAncienneReduction=" + conserverAncienneReduction + "]";
	}

	/**
	 * 
	 * @return {@link #id}.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 *            {@link #id}.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 
	 * @return {@link #remboursement}.
	 */
	public boolean isRemboursement() {
		return remboursement;
	}

	/**
	 * 
	 * @param remboursement
	 *            {@link #remboursement}.
	 */
	public void setRemboursement(boolean remboursement) {
		this.remboursement = remboursement;
	}

	/**
	 * 
	 * @return {@link #montantRemboursement}.
	 */
	public Double getMontantRemboursement() {
		return montantRemboursement;
	}

	/**
	 * 
	 * @param montantRemboursement
	 *            {@link #montantRemboursement}.
	 */
	public void setMontantRemboursement(Double montantRemboursement) {
		this.montantRemboursement = montantRemboursement;
	}

	/**
	 * 
	 * @return {@link #fraisCession}.
	 */
	public double getFraisCession() {
		return fraisCession;
	}

	/**
	 * 
	 * @param fraisCession
	 *            {@link #fraisCession}.
	 */
	public void setFraisCession(double fraisCession) {
		this.fraisCession = fraisCession;
	}

	/**
	 * 
	 * @return {@link #dateAction}.
	 */
	public Date getDateAction() {
		return dateAction;
	}

	/**
	 * 
	 * @param dateAction
	 *            {@link #dateAction}.
	 */
	public void setDateAction(Date dateAction) {
		this.dateAction = dateAction;
	}

	/**
	 * 
	 * @return {@link #conserverAncienneReduction}.
	 */
	public Boolean isConserverAncienneReduction() {
		return conserverAncienneReduction;
	}

	/**
	 * 
	 * @param conserverAncienneReduction
	 *            {@link #conserverAncienneReduction}.
	 */
	public void setConserverAncienneReduction(Boolean conserverAncienneReduction) {
		this.conserverAncienneReduction = conserverAncienneReduction;
	}

	/**
	 * Tester si c'est une cession future.
	 * 
	 * @return true si c'est une cession dans le future.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public boolean isCessionFuture() throws TopazeException {
		return Utils.compareDate(dateAction, PropertiesUtil.getInstance().getDateDuJour().toDate()) < 1;
	}

	/**
	 * convertir {@link PolitiqueCession} vers {@link com.nordnet.topaze.contrat.business.PolitiqueCession}.
	 * 
	 * @return {@link com.nordnet.topaze.contrat.business.PolitiqueCession}.
	 */
	public com.nordnet.topaze.contrat.business.PolitiqueCession toBusiness() {
		com.nordnet.topaze.contrat.business.PolitiqueCession politiqueCession =
				new com.nordnet.topaze.contrat.business.PolitiqueCession();
		politiqueCession.setFraisCession(fraisCession);
		politiqueCession.setDateAction(dateAction);
		politiqueCession.setRemboursement(remboursement);
		politiqueCession.setMontantRemboursement(montantRemboursement);
		politiqueCession.setConserverAncienneReduction(conserverAncienneReduction);

		return politiqueCession;
	}

	/**
	 * retourner la politique de resiliation à partir de la politique de cession.
	 * 
	 * @param politiqueCession
	 *            {@link PolitiqueCession}.
	 * @return {@link PolitiqueResiliation}.
	 */
	public PolitiqueResiliation getPRFromPC() {
		PolitiqueResiliation politiqueResiliation = new PolitiqueResiliation();
		politiqueResiliation.setFraisResiliation(false);
		politiqueResiliation.setMontantRemboursement(montantRemboursement);
		politiqueResiliation.setPenalite(false);
		politiqueResiliation.setRemboursement(remboursement);
		politiqueResiliation.setTypeResiliation(TypeResiliation.RIC);
		politiqueResiliation.setMotif(MotifResiliation.AUTRE);

		return politiqueResiliation;
	}

}
