package com.nordnet.topaze.migration.outil.business;

import javax.lang.model.element.TypeElement;

import com.nordnet.topaze.migration.outil.enums.TypeOperation;
import com.nordnet.topaze.migration.outil.enums.TypeProduit;

/**
 * Informations sur les biens et services a echange.
 * 
 * @author Oussama Denden
 * 
 */
public class EchangeInfo {

	/**
	 * reference du produit source.
	 */
	private String referenceProduitSource;

	/**
	 * reference du produit source.
	 */
	private String referenceProduitDestination;

	/**
	 * {@link TypeElement}.
	 */
	private TypeProduit typeElement;

	/**
	 * {@link TypeOperation}.
	 */
	private TypeOperation typeOperation;

	/**
	 * Constructeur par defaut.
	 */
	public EchangeInfo() {

	}

	/**
	 * @return {@link #referenceProduitSource}.
	 */
	public String getReferenceProduitSource() {
		return referenceProduitSource;
	}

	/**
	 * @param referenceProduitSource
	 *            {@link #referenceProduitSource}.
	 */
	public void setReferenceProduitSource(String referenceProduitSource) {
		this.referenceProduitSource = referenceProduitSource;
	}

	/**
	 * @return {@link #referenceProduitDestination}.
	 */
	public String getReferenceProduitDestination() {
		return referenceProduitDestination;
	}

	/**
	 * @param referenceProduitDestination
	 *            {@link #referenceProduitDestination}.
	 */
	public void setReferenceProduitDestination(String referenceProduitDestination) {
		this.referenceProduitDestination = referenceProduitDestination;
	}

	/**
	 * @return {@link #typeElement}.
	 */
	public TypeProduit getTypeElement() {
		return typeElement;
	}

	/**
	 * @param typeElement
	 *            {@link #typeElement}.
	 */
	public void setTypeElement(TypeProduit typeElement) {
		this.typeElement = typeElement;
	}

	/**
	 * @return {@link #typeOperation}.
	 */
	public TypeOperation getTypeOperation() {
		return typeOperation;
	}

	/**
	 * @param typeOperation
	 *            {@link #typeOperation}.
	 */
	public void setTypeOperation(TypeOperation typeOperation) {
		this.typeOperation = typeOperation;
	}

}
