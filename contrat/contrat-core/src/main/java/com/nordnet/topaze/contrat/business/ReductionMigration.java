package com.nordnet.topaze.contrat.business;

import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nordnet.topaze.contrat.domain.FraisContrat;
import com.nordnet.topaze.contrat.domain.Reduction;
import com.nordnet.topaze.contrat.domain.TypeFrais;
import com.nordnet.topaze.contrat.domain.TypeReduction;
import com.nordnet.topaze.contrat.domain.TypeValeurReduction;
import com.nordnet.topaze.contrat.util.TypeFraisDeserializer;
import com.nordnet.topaze.contrat.util.TypeReductionDeserializer;
import com.nordnet.topaze.contrat.util.TypeValeurReductionDeserializer;

/**
 * Cette class regroupe les information d'une {@link Reduction}.
 * 
 * @author anisselmane.
 * 
 */
public class ReductionMigration {

	/**
	 * titre de la reduction.
	 */
	private String titre;

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
	 * type de {@link Reduction}.
	 */
	@JsonDeserialize(using = TypeReductionDeserializer.class)
	@Enumerated(EnumType.STRING)
	private TypeReduction typeReduction;

	/**
	 * type de {@link FraisContrat}.
	 */
	@JsonDeserialize(using = TypeFraisDeserializer.class)
	@Enumerated(EnumType.STRING)
	private TypeFrais typeFrais;

	/**
	 * parametre qui indique si la reduction sera affiche dans une ligne distinct dans la facture.
	 */
	private boolean isAffichableSurFacture;

	/**
	 * constructeur par defaut.
	 */
	public ReductionMigration() {

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
	 * @return {@link ReductionMigration#typeReduction}.
	 */

	public TypeReduction getTypeReduction() {
		return typeReduction;
	}

	/**
	 * 
	 * @param typeReduction
	 *            {@link ReductionMigration#typeReduction}.
	 */
	public void setTypeReduction(TypeReduction typeReduction) {
		this.typeReduction = typeReduction;
	}

	/**
	 * 
	 * @return {@link #typeFrais}.
	 */
	public TypeFrais getTypeFrais() {
		return typeFrais;
	}

	/**
	 * 
	 * @param typeFrais
	 *            {@link #typeFrais}.
	 */
	public void setTypeFrais(TypeFrais typeFrais) {
		this.typeFrais = typeFrais;
	}

	/**
	 * Mappe {@link ReductionMigration} to {@link Reduction}.
	 * 
	 * @return {@link Reduction}.
	 */
	public Reduction toReduction() {
		Reduction reduction = new Reduction();
		reduction.setDateDebut(dateDebut);
		reduction.setDateFin(dateFin);
		reduction.setIsAffichableSurFacture(isAffichableSurFacture);
		reduction.setNbUtilisationMax(nbUtilisationMax);
		reduction.setTitre(titre);
		reduction.setTypeFrais(typeFrais);
		reduction.setTypeReduction(typeReduction);
		reduction.setTypeValeur(typeValeur);
		reduction.setValeur(valeur);
		return reduction;

	}

}
