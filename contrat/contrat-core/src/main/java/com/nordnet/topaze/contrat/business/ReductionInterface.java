package com.nordnet.topaze.contrat.business;

import java.util.Date;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
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
public class ReductionInterface {

	/**
	 * reference reduction.
	 */
	private String reference;

	/**
	 * reference reduction dans le catalogue.
	 */
	private String referenceCatalogue;

	/**
	 * titre de la reduction.
	 */
	private String label;

	/**
	 * type de {@link #valeur}.
	 */
	@JsonDeserialize(using = TypeValeurReductionDeserializer.class)
	@Enumerated(EnumType.STRING)
	private TypeValeurReduction typeValeur;

	/**
	 * valeur de la reduction.
	 */
	private Double valeur;

	/**
	 * numero element contractuel associe a la reduction.
	 */
	private Integer numEC;

	/**
	 * ordre d'application de la reduction.
	 */
	private Integer ordre;

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
	 * nombre d'utilisation restant.
	 */
	private Integer nbUtilisationRestante;

	/**
	 * constructeur par defaut.
	 */
	public ReductionInterface() {

	}

	/**
	 * @return {@link #reference}.
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference
	 *            {@link #reference}.
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return {@link #referenceCatalogue}.
	 */
	public String getReferenceCatalogue() {
		return referenceCatalogue;
	}

	/**
	 * @param referenceCatalogue
	 *            {@link #referenceCatalogue}.
	 */
	public void setReferenceCatalogue(String referenceCatalogue) {
		this.referenceCatalogue = referenceCatalogue;
	}

	/**
	 * @return {@link #label}.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            {@link #label}.
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return {@link #typeValeur}.
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
	 * @return {@link #valeur}.
	 */
	public Double getValeur() {
		return valeur;
	}

	/**
	 * @param valeur
	 *            {@link #valeur}.
	 */
	public void setValeur(Double valeur) {
		this.valeur = valeur;
	}

	/**
	 * @return {@link #numEC}.
	 */
	public Integer getNumEC() {
		return numEC;
	}

	/**
	 * @param numEC
	 *            {@link #numEC}.
	 */
	public void setNumEC(Integer numEC) {
		this.numEC = numEC;
	}

	/**
	 * @return {@link #ordre}.
	 */
	public Integer getOrdre() {
		return ordre;
	}

	/**
	 * @param ordre
	 *            {@link #ordre}.
	 */
	public void setOrdre(Integer ordre) {
		this.ordre = ordre;
	}

	/**
	 * @return {@link #dateDebut}.
	 */
	public Date getDateDebut() {
		return dateDebut;
	}

	/**
	 * @param dateDebut
	 *            {@link #dateDebut}.
	 */
	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	/**
	 * @return {@link #dateFin}.
	 */
	public Date getDateFin() {
		return dateFin;
	}

	/**
	 * @param dateFin
	 *            {@link #dateFin}.
	 */
	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	/**
	 * @return {@link #nbUtilisationRestante}.
	 */
	public Integer getNbUtilisationRestante() {
		return nbUtilisationRestante;
	}

	/**
	 * @param nbUtilisationRestante
	 *            {@link #nbUtilisationRestante}.
	 */
	public void setNbUtilisationRestante(Integer nbUtilisationRestante) {
		this.nbUtilisationRestante = nbUtilisationRestante;
	}

}