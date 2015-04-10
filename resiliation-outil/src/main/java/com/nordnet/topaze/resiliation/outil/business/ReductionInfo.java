package com.nordnet.topaze.resiliation.outil.business;

import org.joda.time.LocalDate;

import com.nordnet.topaze.resiliation.outil.enums.TypeValeurReduction;
import com.nordnet.topaze.resiliation.outil.util.Constants;

/**
 * Cette class regroupe les information d'une {@link Reduction}.
 * 
 * @author Oussama Denden
 * 
 */
public class ReductionInfo {

	/**
	 * titre de la reduction.
	 */
	private String titre;

	/**
	 * date debut de la reduction.
	 */
	private LocalDate dateDebut;

	/**
	 * date fin de la reduction.
	 */
	private LocalDate dateFin;

	/**
	 * nombre maximal d'utilisation de la reduction.
	 */
	private Integer nbUtilisationMax;

	/**
	 * nombre d'utilisation en cours de la reduction.
	 */
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
	 * parametre qui indique si la reduction sera affiche dans une ligne distinct dans la facture.
	 */
	private boolean isAffichableSurFacture;

	/**
	 * ordre d'integration de la reduction.
	 */
	private Integer ordre;

	/**
	 * reference reduction.
	 */
	private String reference;

	/**
	 * constructeur par defaut.
	 */
	public ReductionInfo() {

	}

	/**
	 * 
	 * @return {@link #titre}.
	 */
	public String getTitre() {
		return titre;
	}

	/**
	 * 
	 * @param titre
	 *            {@link #titre}.
	 */
	public void setTitre(String titre) {
		this.titre = titre;
	}

	/**
	 * 
	 * @return {@link #dateDebut}
	 */
	public LocalDate getDateDebut() {
		return dateDebut;
	}

	/**
	 * 
	 * @param dateDebut
	 *            {@link #dateDebut}
	 */
	public void setDateDebut(LocalDate dateDebut) {
		this.dateDebut = dateDebut;
	}

	/**
	 * 
	 * @return {@link #dateFin}
	 */
	public LocalDate getDateFin() {
		return dateFin;
	}

	/**
	 * 
	 * @param dateFin
	 *            {@link #dateFin}
	 */
	public void setDateFin(LocalDate dateFin) {
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
	 * @return {@link #valeur}.
	 */
	public Double getValeur() {
		return valeur;
	}

	/**
	 * @param valeur
	 *            {@link #typeValeurReduction}.
	 */
	public void setValeur(Double valeur) {
		this.valeur = valeur;
	}

	/**
	 * @return {@link #TypeValeurReduction}.
	 */
	public TypeValeurReduction getTypeValeur() {
		return typeValeur;
	}

	/**
	 * @param typeValeur
	 *            {@link #typeValeur}.
	 */
	public void setTypeValeur(TypeValeurReduction typeValeur) {
		this.typeValeur = typeValeur;
	}

	/**
	 * Verifier si une reduction est eligible a une date donner.
	 * 
	 * @param dateDebutFacturation
	 *            la date de debut de facturation.
	 * @param date
	 *            date de test de l'eligibilite de raduction.
	 * @param periodicite
	 *            periodicite de l'element contractuel associe à la reduction.
	 * @return true si la reduction est eligible.
	 */
	public boolean isEligible(LocalDate dateDebutFacturation, LocalDate date, Integer periodicite) {
		boolean isEligible = true;

		if ((dateDebut == null && (getNbUtilisationRestant(dateDebutFacturation, date, periodicite) == null || getNbUtilisationRestant(
				dateDebutFacturation, date, periodicite) <= 0))
				|| (dateDebut != null && dateDebut.isAfter(date))
				|| (dateFin != null && dateFin.isBefore(date))
				|| (dateDebut != null && getNbUtilisationRestant(dateDebutFacturation, date, periodicite) != null && getNbUtilisationRestant(
						dateDebutFacturation, date, periodicite) <= 0)) {
			isEligible = false;
		}

		return isEligible;
	}

	/**
	 * Retourne le nombre d'utilisation restant pour une reduction.
	 * 
	 * @param dateDebutFacturation
	 *            la date de debut de facturation.
	 * @param date
	 *            la date du jour du calcule du nombre d'utilisation restant.
	 * @param periodicite
	 *            periodicite de l'element contractuel associe à la reduction.
	 * @return le nombre d'utilisation restant de la reduction.
	 */
	public Integer getNbUtilisationRestant(LocalDate dateDebutFacturation, LocalDate date, Integer periodicite) {
		Integer nbUtilisationRestant = null;

		if (nbUtilisationMax != null && nbUtilisationEnCours != null) {
			LocalDate dateFacture = dateDebutFacturation;
			nbUtilisationRestant = nbUtilisationMax;
			while (dateFacture.isBefore(date)) {
				if (isEnPeriodeReduction(dateFacture)) {
					nbUtilisationRestant--;

				}
				if (nbUtilisationRestant == 0) {
					return nbUtilisationRestant;
				}
				dateFacture = dateFacture.plusMonths(periodicite);
			}
		}
		return nbUtilisationRestant;
	}

	/**
	 * Verifier si une date donne est en periode de reduction.
	 * 
	 * @param date
	 *            date de verification.
	 * @return true si la date est dans la periode de reduction.
	 */
	public boolean isEnPeriodeReduction(LocalDate date) {
		if (dateDebut != null && dateFin == null && dateDebut.compareTo(date) <= Constants.ZERO) {
			return true;
		} else if (dateDebut != null && dateFin != null && dateDebut.compareTo(date) <= Constants.ZERO
				&& dateFin.compareTo(date) >= Constants.ZERO) {
			return true;
		} else if (dateDebut == null) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @return {@link #isAffichableSurFacture}.
	 */
	public Boolean getIsAffichableSurFacture() {
		return isAffichableSurFacture;
	}

	/**
	 * 
	 * @param isAffichableSurFacture
	 *            {@link #isAffichableSurFacture}.
	 */
	public void setIsAffichableSurFacture(Boolean isAffichableSurFacture) {
		this.isAffichableSurFacture = isAffichableSurFacture;
	}

	/**
	 * 
	 * @return {@link Reduction#reference}
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * 
	 * @param reference
	 *            {@link #reference}
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * 
	 * @return {@link #ordre}.
	 */
	public Integer getOrdre() {
		return ordre;
	}

	/**
	 * 
	 * @param ordre
	 *            {@link #ordre}.
	 */
	public void setOrdre(Integer ordre) {
		this.ordre = ordre;
	}

}
