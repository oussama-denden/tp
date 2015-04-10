package com.nordnet.topaze.contrat.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * contient tout les informations necessaire pour la politique de Renouvellement {@link PolitiqueRenouvellement}.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
@Entity
@Table(name = "politiquerenouvellement")
@JsonIgnoreProperties({ "id" })
public class PolitiqueRenouvellement {

	/**
	 * cle primaire.
	 */
	@Id
	@GeneratedValue
	private Integer id;

	/**
	 * utilisateur acteur dans le processus
	 */
	private String user;

	/**
	 * force pour forcer le renouvellement des biens
	 */

	private Boolean forceRenouvellement;

	/**
	 * indique qu'on garde les anciennes reductions.
	 */
	private Boolean conserverAncienneReduction;

	/**
	 * constructeur par defaut
	 * 
	 */
	public PolitiqueRenouvellement() {

	}

	/* Gettes and Setters */

	/**
	 * 
	 * @return {@link user}
	 */
	public String getUser() {
		return user;
	}

	/**
	 * 
	 * @param user
	 *            {@link String}
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * get the force
	 * 
	 * @return {@link #force}
	 */
	public Boolean getForceRenouvellement() {
		return forceRenouvellement;
	}

	/**
	 * set the force
	 * 
	 * @param force
	 *            the new {@link #force}
	 */
	public void setForceRenouvellement(Boolean forceRenouvellement) {
		this.forceRenouvellement = forceRenouvellement;
	}

	/**
	 * the conserverAncienneReduction
	 * 
	 * @return {@link #conserverAncienneReduction}
	 */
	public Boolean getConserverAncienneReduction() {
		return conserverAncienneReduction;
	}

	/**
	 * sets the conserverAncienneReduction
	 * 
	 * @param conserverAncienneReduction
	 *            the new {@link #conserverAncienneReduction}
	 */
	public void setConserverAncienneReduction(Boolean conserverAncienneReduction) {
		this.conserverAncienneReduction = conserverAncienneReduction;
	}

	/**
	 * Mapping politique de migration buisness to domain.
	 * 
	 * @param politiqueMigrationBuis
	 *            the politique migration buis
	 * @return the politique migration
	 */
	public com.nordnet.topaze.contrat.business.PolitiqueRenouvellement toBuisness() {
		com.nordnet.topaze.contrat.business.PolitiqueRenouvellement politiqueRenouvellement =
				new com.nordnet.topaze.contrat.business.PolitiqueRenouvellement();

		politiqueRenouvellement.setForce(forceRenouvellement);

		return politiqueRenouvellement;

	}

}
