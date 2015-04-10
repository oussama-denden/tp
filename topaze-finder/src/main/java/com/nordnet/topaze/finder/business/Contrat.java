package com.nordnet.topaze.finder.business;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Cette classe regroupe les informations qui definissent un {@link Contrat}.
 * 
 * @author anisselmane.
 * 
 */

@JsonIgnoreProperties({ "reference" })
public class Contrat {

	/**
	 * titre(titre de contrat par exemple: Contrat d'abonnement pour le produit Kit satellite 2Giga).
	 */
	private String titre;

	/**
	 * liste de {@link ElementContractuel}.
	 */
	private List<ElementContractuel> elementContractuels = new ArrayList<>();

	/**
	 * constructeur par defaut.
	 */
	public Contrat() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Contrat [, titre=" + titre + "]";
	}

	/* Getters & Setters */

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
	 *            the new titre {@link #titre}.
	 */
	public void setTitre(String titre) {
		this.titre = titre;
	}

	/**
	 * 
	 * @return liste de {@link ElementContractuel}
	 */
	public List<ElementContractuel> getElementContractuels() {
		return elementContractuels;
	}

	/**
	 * 
	 * @param elementContractuels
	 *            liste de {@link ElementContractuel}
	 */
	public void setElementContractuels(List<ElementContractuel> elementContractuels) {
		this.elementContractuels = elementContractuels;
	}

	/**
	 * Associer un EC a un contrat.
	 * 
	 * @param elementContractuel
	 *            {@link ElementContractuel}
	 */
	public void addEC(ElementContractuel elementContractuel) {
		this.elementContractuels.add(elementContractuel);
	}
}
