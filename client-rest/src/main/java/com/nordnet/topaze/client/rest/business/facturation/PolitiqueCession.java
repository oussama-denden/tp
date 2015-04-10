package com.nordnet.topaze.client.rest.business.facturation;

import java.util.Date;

import com.nordnet.topaze.client.rest.business.contrat.Contrat;
import com.nordnet.topaze.client.rest.enums.Duree;
import com.nordnet.topaze.client.rest.enums.Engagement;

/**
 * politique de cession d'un {@link Contrat}.
 * 
 * @author akram-moncer
 * 
 */
public class PolitiqueCession {

	/**
	 * Engement.
	 */
	private Engagement engagement;

	/**
	 * Duree.
	 */
	private Duree duree;

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
	 * le commentaire .
	 */
	private String commentaire;

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
		return "PolitiqueCession [engagement=" + engagement + ", duree=" + duree + ", remboursement=" + remboursement
				+ ", montantRemboursement=" + montantRemboursement + ", fraisCession=" + fraisCession + "]";
	}

	/**
	 * 
	 * @return {@link #engagement}
	 */
	public Engagement getEngagement() {
		return engagement;
	}

	/**
	 * 
	 * @param engagement
	 *            {@link #engagement}.
	 */
	public void setEngagement(Engagement engagement) {
		this.engagement = engagement;
	}

	/**
	 * 
	 * @return {@link #duree}.
	 */
	public Duree getDuree() {
		return duree;
	}

	/**
	 * 
	 * @param duree
	 *            {@link #duree}.
	 */
	public void setDuree(Duree duree) {
		this.duree = duree;
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
	 * 
	 * @return {@link #commentaire}
	 */
	public String getCommentaire() {
		if (commentaire == null)
			return "";
		return commentaire;
	}

	/**
	 * 
	 * @param commentaire
	 *            {@link #commentaire}
	 */
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}

}
