package com.nordnet.topaze.contrat.business;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nordnet.topaze.contrat.domain.Contrat;
import com.nordnet.topaze.contrat.util.PropertiesUtil;
import com.nordnet.topaze.contrat.util.Utils;
import com.nordnet.topaze.exception.TopazeException;

/**
 * politique de cession d'un {@link Contrat}.
 * 
 * @author akram-moncer
 * 
 */
@JsonIgnoreProperties({ "cessionFuture" })
public class PolitiqueCession {

	/**
	 * Flag remboursement.
	 */
	private Boolean remboursement;

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
	 * le commentaire .
	 */
	private String commentaire;

	/**
	 * Constructeur par default.
	 */
	public PolitiqueCession() {

	}

	@Override
	public String toString() {
		return "PolitiqueCession [remboursement=" + remboursement + ", montantRemboursement=" + montantRemboursement
				+ ", fraisCession=" + fraisCession + "]";
	}

	/**
	 * 
	 * @return {@link #remboursement}.
	 */
	public Boolean isRemboursement() {
		return remboursement;
	}

	/**
	 * 
	 * @param remboursement
	 *            {@link #remboursement}.
	 */
	public void setRemboursement(Boolean remboursement) {
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

	/**
	 * Tester si c'est une cession future.
	 * 
	 * @return true si c'est une cession dans le future.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public boolean isCessionFuture() throws TopazeException {
		if (dateAction == null) {
			return false;
		}
		return Utils.compareDate(dateAction, PropertiesUtil.getInstance().getDateDuJour().toDate()) > 0;
	}

	/**
	 * convertir {@link PolitiqueCession} vers {@link com.nordnet.topaze.contrat.domain.PolitiqueCession}.
	 * 
	 * @return {@link com.nordnet.topaze.contrat.domain.PolitiqueCession}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public com.nordnet.topaze.contrat.domain.PolitiqueCession toDomain() throws TopazeException {
		com.nordnet.topaze.contrat.domain.PolitiqueCession politiqueCession =
				new com.nordnet.topaze.contrat.domain.PolitiqueCession();
		if (dateAction != null) {
			politiqueCession.setDateAction(dateAction);
		} else {
			politiqueCession.setDateAction(PropertiesUtil.getInstance().getDateDuJour().toDate());
		}
		politiqueCession.setFraisCession(fraisCession);
		politiqueCession.setMontantRemboursement(montantRemboursement);
		politiqueCession.setRemboursement(remboursement);
		politiqueCession.setConserverAncienneReduction(conserverAncienneReduction);

		return politiqueCession;
	}

}
