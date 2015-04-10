package com.nordnet.topaze.contrat.business;

import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.base.Optional;
import com.nordnet.topaze.contrat.domain.Reduction;
import com.nordnet.topaze.contrat.domain.TypeValeurReduction;
import com.nordnet.topaze.contrat.util.DateSerializer;
import com.nordnet.topaze.contrat.util.TypeValeurReductionDeserializer;

/**
 * Cette class regroupe les information d'une {@link Reduction}.
 * 
 * @author Oussama Denden
 * 
 */
public class ReductionInfo {

	/**
	 * reference reduction.
	 */
	private String reference;

	/**
	 * ordre d'integration de la reduction.
	 */
	private Integer ordre;

	/**
	 * titre de la reduction.
	 */
	private String titre;

	/**
	 * date debut de la reduction.
	 */
	@JsonSerialize(using = DateSerializer.class)
	private Date dateDebut;

	/**
	 * date fin de la reduction.
	 */
	@JsonSerialize(using = DateSerializer.class)
	private Date dateFin;

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
	@JsonDeserialize(using = TypeValeurReductionDeserializer.class)
	@Enumerated(EnumType.STRING)
	private TypeValeurReduction typeValeur;

	/**
	 * parametre qui indique si la reduction sera affiche dans une ligne distinct dans la facture.
	 */
	private boolean isAffichableSurFacture;

	/**
	 * reference reduction.
	 */
	private String codeCatalogueReduction;

	/**
	 * constructeur par defaut.
	 */
	public ReductionInfo() {

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
	 * @return {@link #codeCatalogueReduction}.
	 */
	public String getCodeCatalogueReduction() {
		return codeCatalogueReduction;
	}

	/**
	 * 
	 * @param codeCatalogueReduction
	 *            {@link #codeCatalogueReduction}.
	 */
	public void setCodeCatalogueReduction(String codeCatalogueReduction) {
		this.codeCatalogueReduction = codeCatalogueReduction;
	}

	/**
	 * indique si la reduction est automatique ou non.
	 * 
	 * @return true si la reduction est automatique.
	 */
	@JsonIgnore
	public boolean isReductionAutomatique() {
		if (Optional.fromNullable(codeCatalogueReduction).isPresent())
			return true;

		return false;
	}

}
