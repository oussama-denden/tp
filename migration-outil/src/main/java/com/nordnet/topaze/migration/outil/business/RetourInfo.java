package com.nordnet.topaze.migration.outil.business;

import javax.lang.model.element.TypeElement;

import com.nordnet.topaze.migration.outil.enums.TypeOperation;
import com.nordnet.topaze.migration.outil.enums.TypeProduit;

/**
 * Informations sur les biens a retourner et les services a suspendu.
 * 
 * @author Oussama Denden
 * 
 */
public class RetourInfo {

	/**
	 * reference du produit.
	 */
	private String referenceProduit;

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
	public RetourInfo() {

	}

	/**
	 * @return {@link #referenceProduit}.
	 */
	public String getReferenceProduit() {
		return referenceProduit;
	}

	/**
	 * @param referenceProduit
	 *            {@link #referenceProduit}.
	 */
	public void setReferenceProduit(String referenceProduit) {
		this.referenceProduit = referenceProduit;
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
