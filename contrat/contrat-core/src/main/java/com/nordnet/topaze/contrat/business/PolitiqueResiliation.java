package com.nordnet.topaze.contrat.business;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nordnet.topaze.contrat.domain.MotifResiliation;
import com.nordnet.topaze.contrat.domain.TypeResiliation;
import com.nordnet.topaze.contrat.util.MotifResiliationDeserializer;
import com.nordnet.topaze.contrat.util.TypeResiliationDeserializer;
import com.nordnet.topaze.contrat.validator.ContratValidator;
import com.nordnet.topaze.exception.TopazeException;

/**
 * contient tout les informations necessaire pour la politique de resiliation.
 */

public class PolitiqueResiliation {

	/**
	 * Flag remboursement.
	 */
	private Boolean remboursement;

	/**
	 * frais de resiliation.
	 */
	private Boolean fraisResiliation;

	/**
	 * Penalite de resiliation.
	 */
	private Boolean penalite;

	/**
	 * Le montant defini pour le remboursement.
	 */
	private Double montantRemboursement;

	/**
	 * Le montant de frais resiliation.
	 */
	private Double montantResiliation;

	/**
	 * Type de résiliation RIN ou RIC.
	 */
	@JsonDeserialize(using = TypeResiliationDeserializer.class)
	private TypeResiliation typeResiliation;

	/**
	 * Boolean résiliation en cascade.
	 */
	private Boolean enCascade = true;

	/**
	 * commentaire de resiliation.
	 */
	private String commentaire;

	/**
	 * La Date de résiliation.
	 */
	private String dateResiliation;

	/**
	 * motif de resiliation.
	 */
	@JsonDeserialize(using = MotifResiliationDeserializer.class)
	private MotifResiliation motif;

	/**
	 * La Date de remboursement.
	 */
	private String dateRemboursement;

	/**
	 * delai de securite.
	 */
	private Boolean delaiDeSecurite = true;

	/**
	 * Instantiates a new politique resiliation.
	 */
	public PolitiqueResiliation() {
		super();
	}

	/* Getters & Setters */
	/**
	 * Checks if is remboursement.
	 * 
	 * @return true, if is remboursement
	 */
	public Boolean isRemboursement() {
		return remboursement;
	}

	/**
	 * Checks if is frais resiliation.
	 * 
	 * @return true, if is frais resiliation
	 */
	public Boolean isFraisResiliation() {
		return fraisResiliation;
	}

	/**
	 * Sets the frais resiliation.
	 * 
	 * @param fraisResiliation
	 *            the new frais resiliation
	 */
	public void setFraisResiliation(Boolean fraisResiliation) {
		this.fraisResiliation = fraisResiliation;
	}

	/**
	 * Checks if is penalite.
	 * 
	 * @return true, if is penalite
	 */
	public Boolean isPenalite() {
		return penalite;
	}

	/**
	 * Sets the penalite.
	 * 
	 * @param penalite
	 *            the new penalite
	 */
	public void setPenalite(Boolean penalite) {
		this.penalite = penalite;
	}

	/**
	 * 
	 * @return {@link PolitiqueResiliation#montantRemboursement}.
	 */
	public Double getMontantRemboursement() {
		return montantRemboursement;
	}

	/**
	 * 
	 * @param montantRemboursement
	 *            {@link PolitiqueResiliation#montantRemboursement}.
	 */
	public void setMontantRemboursement(Double montantRemboursement) {
		this.montantRemboursement = montantRemboursement;
	}

	/**
	 * Sets the remboursement.
	 * 
	 * @param remboursement
	 *            the new remboursement
	 */
	public void setRemboursement(Boolean remboursement) {
		this.remboursement = remboursement;
	}

	/**
	 * Gets the montant resiliation.
	 * 
	 * @return {@link #montantResiliation}.
	 */
	public Double getMontantResiliation() {
		return montantResiliation;
	}

	/**
	 * Sets the montant resiliation.
	 * 
	 * @param montantResiliation
	 *            the new montant resiliation {@link #montantResiliation}.
	 */
	public void setMontantResiliation(Double montantResiliation) {
		this.montantResiliation = montantResiliation;
	}

	/**
	 * Gets the type resiliation.
	 * 
	 * @return the type resiliation
	 */
	public TypeResiliation getTypeResiliation() {
		return typeResiliation;
	}

	/**
	 * Sets the type resiliation.
	 * 
	 * @param typeResiliation
	 *            the new type resiliation
	 */
	public void setTypeResiliation(TypeResiliation typeResiliation) {
		this.typeResiliation = typeResiliation;
	}

	/**
	 * Checks if is en cascade.
	 * 
	 * @return true, if is en cascade
	 */
	public Boolean isEnCascade() {
		return enCascade;
	}

	/**
	 * Sets the en cascade.
	 * 
	 * @param enCascade
	 *            the new en cascade
	 */
	public void setEnCascade(Boolean enCascade) {
		this.enCascade = enCascade;
	}

	/**
	 * Gets the date resiliation.
	 * 
	 * @return the date resiliation
	 */
	public String getDateResiliation() {
		return dateResiliation;
	}

	/**
	 * Sets the date resiliation.
	 * 
	 * @param dateResiliation
	 *            the new date resiliation
	 */
	public void setDateResiliation(String dateResiliation) {
		this.dateResiliation = dateResiliation;
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
	 * get the motif .
	 * 
	 * @return {@link MotifResiliation}
	 */
	public MotifResiliation getMotif() {
		return motif;
	}

	/**
	 * set the motif.
	 * 
	 * @param motif
	 *            the new {@link MotifResiliation}
	 */
	public void setMotif(MotifResiliation motif) {
		this.motif = motif;
	}

	/**
	 * 
	 * @return {@link #dateRemboursement}
	 */
	public String getDateRemboursement() {
		return dateRemboursement;
	}

	/**
	 * 
	 * @param dateRembourcement
	 *            {@link #dateRembourcement}
	 */
	public void setDateRemboursement(String dateRemboursement) {
		this.dateRemboursement = dateRemboursement;
	}

	/**
	 * get delai de securite.
	 * 
	 * @return {@link #delaiDeSecurite}
	 */
	public Boolean getDelaiDeSecurite() {
		return delaiDeSecurite;
	}

	/**
	 * set delai de securite.
	 * 
	 * @param delaiDeSecurite
	 */
	public void setDelaiDeSecurite(Boolean delaiDeSecurite) {
		this.delaiDeSecurite = delaiDeSecurite;
	}

	/**
	 * Mapping politique validation domain à business.
	 * 
	 * @return the politique validation domain
	 * @throws TopazeException
	 *             the contrat exception
	 */
	public com.nordnet.topaze.contrat.domain.PolitiqueResiliation toDomain() throws TopazeException {
		com.nordnet.topaze.contrat.domain.PolitiqueResiliation politiqueResiliation =
				new com.nordnet.topaze.contrat.domain.PolitiqueResiliation();

		politiqueResiliation.setFraisResiliation(fraisResiliation);
		politiqueResiliation.setMontantRemboursement(montantRemboursement);
		politiqueResiliation.setMontantResiliation(montantResiliation);
		politiqueResiliation.setPenalite(penalite);
		politiqueResiliation.setRemboursement(remboursement);
		politiqueResiliation.setEnCascade(enCascade);
		politiqueResiliation.setTypeResiliation(typeResiliation);
		politiqueResiliation.setMotif(motif);
		if (dateRemboursement != null) {
			politiqueResiliation
					.setDateRemboursement(ContratValidator.verifierDateWithoutTimeValide(dateRemboursement));
		}
		if (dateResiliation != null) {
			politiqueResiliation.setDateResiliation(ContratValidator.verifierDateValide(dateResiliation));
		}
		politiqueResiliation.setDelaiDeSecurite(delaiDeSecurite);
		politiqueResiliation.setCommentaire(commentaire);
		return politiqueResiliation;
	}

}
