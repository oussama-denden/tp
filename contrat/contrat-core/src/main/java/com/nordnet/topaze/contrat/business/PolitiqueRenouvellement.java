package com.nordnet.topaze.contrat.business;


/**
 * contient tout les informations necessaire pour la politique de renouvellemnt {@link politiqueRenouvellement}.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class PolitiqueRenouvellement {

	/**
	 * force pour forcer le renouvellement des biens.
	 */

	private Boolean force;

	/**
	 * indique qu'on garde les anciennes reductions.
	 */
	private Boolean conserverAncienneReduction;

	/**
	 * le commentaire .
	 */
	private String commentaire;

	/**
	 * constructeur par defaut.
	 * 
	 */
	public PolitiqueRenouvellement() {

	}

	/* Gettes and Setters */

	/**
	 * 
	 * @return {@link #force}
	 */
	public Boolean getForce() {
		return force;
	}

	/**
	 * 
	 * @param force
	 *            {@link #force}
	 */
	public void setForce(Boolean force) {
		this.force = force;
	}

	/**
	 * 
	 * @return {@link #conserverAncienneReduction}
	 */
	public Boolean getConserverAncienneReduction() {
		return conserverAncienneReduction;
	}

	/**
	 * 
	 * @param conserverAncienneReduction
	 *            {@link #conserverAncienneReduction}
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
	 * Mapping politique de migration buisness to domain.
	 * 
	 * @return the politique migration
	 */
	public com.nordnet.topaze.contrat.domain.PolitiqueRenouvellement toDomain() {
		com.nordnet.topaze.contrat.domain.PolitiqueRenouvellement politiqueRenouvellement =
				new com.nordnet.topaze.contrat.domain.PolitiqueRenouvellement();
		politiqueRenouvellement.setForceRenouvellement(force);
		politiqueRenouvellement.setConserverAncienneReduction(conserverAncienneReduction);

		return politiqueRenouvellement;

	}

}
