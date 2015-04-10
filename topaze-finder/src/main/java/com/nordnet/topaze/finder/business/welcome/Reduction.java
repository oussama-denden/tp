package com.nordnet.topaze.finder.business.welcome;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.finder.util.PropertiesUtil;

/**
 * l'entite qui represente la promotion qui peut etre associe a un contrat.
 * 
 * @author akram-moncer
 * 
 */
public class Reduction {

	/**
	 * date debut de la reduction.
	 */
	private Date dateDebut;

	/**
	 * date fin de la reduction.
	 */
	private Date dateFin;

	/**
	 * nombre maximal d'utilisation de la reduction.
	 */
	private Integer nbUtilisationMax;

	/**
	 * nombre d'utilisation en cours de la reduction.
	 */
	@JsonIgnore
	private Integer nbUtilisationEnCours = 0;

	/**
	 * valeur de la reduction.
	 */
	private Double valeur;

	/**
	 * type de {@link #valeur}.
	 */
	private TypeValeurReduction typeValeur;

	/**
	 * date annulation du reduction.
	 */
	private Date dateAnnulation;

	/**
	 * constructeur par defaut.
	 */
	public Reduction() {

	}

	/**
	 * 
	 * @return {@link #dateDebut}
	 */
	public Date getDateDebut() {
		return dateDebut;
	}

	/**
	 * 
	 * @param dateDebut
	 *            {@link #dateDebut}
	 */
	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	/**
	 * 
	 * @return {@link #dateFin}
	 */
	public Date getDateFin() {
		return dateFin;
	}

	/**
	 * 
	 * @param dateFin
	 *            {@link #dateFin}
	 */
	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	/**
	 * @return {@link #nbUtilisationMax}.
	 */
	public Integer getNbUtilisationMax() {
		return nbUtilisationMax;
	}

	/**
	 * @param nbUtilisationMax
	 *            {@link #nbUtilisationMax}.
	 */
	public void setNbUtilisationMax(Integer nbUtilisationMax) {
		this.nbUtilisationMax = nbUtilisationMax;
	}

	/**
	 * @return {@link #nbUtilisationEnCours}.
	 */
	public Integer getNbUtilisationEnCours() {
		return nbUtilisationEnCours;
	}

	/**
	 * @param nbUtilisationEnCours
	 *            {@link #nbUtilisationEnCours}.
	 */
	public void setNbUtilisationEnCours(Integer nbUtilisationEnCours) {
		this.nbUtilisationEnCours = nbUtilisationEnCours;
	}

	/**
	 * 
	 * @return {@link #valeur}
	 */
	public Double getValeur() {
		return valeur;
	}

	/**
	 * 
	 * @param valeur
	 *            {@link #valeur}
	 */
	public void setValeur(Double valeur) {
		this.valeur = valeur;
	}

	/**
	 * 
	 * @return {@link TypeValeurReduction}
	 */
	public TypeValeurReduction getTypeValeur() {
		return typeValeur;
	}

	/**
	 * 
	 * @param typeValeur
	 *            {@link TypeValeurReduction}
	 */
	public void setTypeValeur(TypeValeurReduction typeValeur) {
		this.typeValeur = typeValeur;
	}

	/**
	 * 
	 * @return {@link #dateAnnulation}
	 */
	public Date getDateAnnulation() {
		return dateAnnulation;
	}

	/**
	 * 
	 * @param dateAnnulation
	 *            {@link #dateAnnulation}
	 */
	public void setDateAnnulation(Date dateAnnulation) {
		this.dateAnnulation = dateAnnulation;
	}

	/**
	 * Tester si une reduction est ligible ou non.
	 * 
	 * @return true si le reduction est ligible.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@JsonIgnore
	public boolean isEligible() throws TopazeException {
		boolean isEligible = true;
		if ((dateDebut == null && (getNbUtilisationRestant() == null || getNbUtilisationRestant() <= 0))
				|| (dateDebut != null && dateDebut.after(PropertiesUtil.getInstance().getDateDuJour().toDate()))
				|| (dateFin != null && dateFin.before(PropertiesUtil.getInstance().getDateDuJour().toDate()))
				|| (dateDebut != null && getNbUtilisationRestant() != null && getNbUtilisationRestant() <= 0)
				|| dateAnnulation != null) {
			isEligible = false;
		}
		return isEligible;
	}

	/**
	 * calculer nombre d'utilisation restant.
	 * 
	 * @return nombre d'utilisation restant.
	 */
	@JsonIgnore
	public Integer getNbUtilisationRestant() {
		Integer nbUtilisationRestant = null;

		if (nbUtilisationMax != null && nbUtilisationEnCours != null) {
			nbUtilisationRestant = nbUtilisationMax - nbUtilisationEnCours;
		}
		return nbUtilisationRestant;
	}

	/**
	 * verifer si la reduction est annulee.
	 * 
	 * @return true si la reduction est annulee.
	 */
	public boolean isAnnule() {
		return dateAnnulation != null;
	}
}
