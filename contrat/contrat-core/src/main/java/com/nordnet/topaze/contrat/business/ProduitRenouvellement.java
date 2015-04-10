package com.nordnet.topaze.contrat.business;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nordnet.topaze.client.rest.enums.TypeProduit;
import com.nordnet.topaze.contrat.util.TypeProduitDeserializer;

/**
 * Cette classe regroupe les informations qui definissent un {@link produitRenouvellement}.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class ProduitRenouvellement {

	/**
	 * reference du produit.
	 */
	private String referenceProduit;

	/**
	 * reference du tarif associe au Ec dans le catalogue.
	 */
	private String referenceTarif;

	/**
	 * numero element contractuelle.
	 */
	private Integer numEC;

	/**
	 * prix de renouvellemnt.
	 */
	private PrixRenouvellemnt prix;

	/**
	 * le nom de {@link Produit} par exemple: Pack Sat Max 10Go.
	 */
	private String label;

	/**
	 * Le champ "BillingGroup", lorsqu'il doit être rempli, le sera avec le numéro de la commande.
	 */
	private String numeroCommande;

	/** The type produit. {@link TypeProduit} . */
	@JsonDeserialize(using = TypeProduitDeserializer.class)
	private TypeProduit typeProduit;

	/**
	 * le numero de element contractuel parent.
	 */
	private Integer numECParent;

	/**
	 * un EL peut etre remboursable ou non.
	 */
	private Boolean remboursable;

	/**
	 * constructeur par defaut.
	 */
	public ProduitRenouvellement() {

	}

	/* Getters & Setters */

	/**
	 * gets the referenceProduit.
	 * 
	 * @return {@link #referenceProduit}
	 */
	public String getReferenceProduit() {
		return referenceProduit;
	}

	/**
	 * sets the referenceProduit.
	 * 
	 * @param referenceProduit
	 *            the new reference produit {@link #referenceProduit}
	 */
	public void setReferenceProduit(String referenceProduit) {
		this.referenceProduit = referenceProduit;
	}

	/**
	 * 
	 * @return {@link #referenceTarif}
	 */
	public String getReferenceTarif() {
		return referenceTarif;
	}

	/**
	 * 
	 * @param referenceTarif
	 *            {@link #referenceTarif}.
	 */
	public void setReferenceTarif(final String referenceTarif) {
		this.referenceTarif = referenceTarif;
	}

	/**
	 * gets the numEC
	 * 
	 * @return {@link #numEC}
	 */
	public Integer getNumEC() {
		return numEC;
	}

	/**
	 * sets the numEC
	 * 
	 * @param numEC
	 *            the new numEC {@link #numEC}
	 */
	public void setNumEC(Integer numEC) {
		this.numEC = numEC;
	}

	/**
	 * gets the prix
	 * 
	 * @return {@link #prix}
	 */
	public PrixRenouvellemnt getPrix() {
		return prix;
	}

	/**
	 * sets the prix
	 * 
	 * @param prix
	 *            the new prix {@link #prix}
	 */
	public void setPrix(PrixRenouvellemnt prix) {
		this.prix = prix;
	}

	/**
	 * get the label
	 * 
	 * @return {@link #label}
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * set the label
	 * 
	 * @param label
	 *            the new {@link #label}
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * get the new numero commande
	 * 
	 * @return {@link #numeroCommande}
	 */
	public String getNumeroCommande() {
		return numeroCommande;
	}

	/**
	 * set the numero commande
	 * 
	 * @param numeroCommande
	 *            the new {@link #numeroCommande}
	 */
	public void setNumeroCommande(String numeroCommande) {
		this.numeroCommande = numeroCommande;
	}

	/**
	 * get the type produit
	 * 
	 * @return {@link #typeProduit}
	 */
	public TypeProduit getTypeProduit() {
		return typeProduit;
	}

	/**
	 * set the type produit
	 * 
	 * @param typeProduit
	 *            the new {@link #typeProduit}
	 */
	public void setTypeProduit(TypeProduit typeProduit) {
		this.typeProduit = typeProduit;
	}

	/**
	 * get the numEc parent
	 * 
	 * @return {@link #numECParent}
	 */
	public Integer getNumECParent() {
		return numECParent;
	}

	/**
	 * set the numEC parent
	 * 
	 * @param numECParent
	 *            the new {@link #numECParent}
	 */
	public void setNumECParent(Integer numECParent) {
		this.numECParent = numECParent;
	}

	/**
	 * 
	 * @return indique que l'element est remboursable ou non.
	 */
	public Boolean isRemboursable() {
		return remboursable;
	}

	/**
	 * 
	 * @param remboursable
	 *            remboursable.
	 */
	public void setRemboursable(Boolean remboursable) {
		this.remboursable = remboursable;
	}

	/**
	 * From json to {@link ProduitMigration}.
	 * 
	 * @param json
	 *            l'objet {@link ProduitMigration} en format json.
	 * @return {@link ProduitMigration}
	 * @throws IOException
	 *             {@link IOException}
	 */
	public static ProduitRenouvellement fromJsonToProduitRenouvellement(String json) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, ProduitRenouvellement.class);
	}

	/**
	 * Mapping produit de renouvellement à un produit.
	 * 
	 * @param produitRenouvellements
	 * @param referenceContrat
	 * @return {@link Produit}
	 */
	public Produit toProduit() {
		Produit produit = new Produit();
		produit.setReference(referenceProduit);
		produit.setReferenceTarif(referenceTarif);
		produit.setNumEC(numEC);
		produit.setLabel(label);
		produit.setNumeroCommande(numeroCommande);
		produit.setTypeProduit(typeProduit);
		produit.setNumECParent(numECParent);

		if (prix != null) {

			produit.setPrix(this.prix.toPrix());

		}

		return produit;
	}

}
