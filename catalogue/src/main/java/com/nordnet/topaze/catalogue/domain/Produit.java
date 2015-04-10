package com.nordnet.topaze.catalogue.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.nordnet.topaze.catalogue.util.OutilLivraisonDeserializer;
import com.nordnet.topaze.catalogue.util.TypeProduitDeserializer;
import com.nordnet.topaze.catalogue.util.TypeTVADeserializer;

/**
 * Cette classe regroupe les informations qui definissent un {@link Produit}.
 * 
 * @author Denden-OUSSAMA
 * @author Ahmed-Mehdi-Laabidi
 * @author anisselmane.
 * 
 */
@Entity
@Table(name = "produit")
@JsonIgnoreProperties({ "id" })
public class Produit {

	/**
	 * cle primaire.
	 */
	@Id
	@GeneratedValue
	private Integer id;

	/**
	 * nom court de produit par exemple: SatMax10.
	 */
	@NotNull
	private String reference;

	/**
	 * le nom de {@link Produit} par exemple: Pack Sat Max 10Go.
	 */
	@NotNull
	private String label;

	/**
	 * {@link TypeProduit}.
	 */
	@JsonDeserialize(using = TypeProduitDeserializer.class)
	@Enumerated(EnumType.STRING)
	@NotNull
	private TypeProduit typeProduit;

	/**
	 * {@link TypeTVA}.
	 */
	@JsonDeserialize(using = TypeTVADeserializer.class)
	@Enumerated(EnumType.STRING)
	@NotNull
	private TypeTVA typeTVA;

	/**
	 * {@link OutilLivraison}.
	 */
	@JsonDeserialize(using = OutilLivraisonDeserializer.class)
	@Enumerated(EnumType.STRING)
	@NotNull
	private OutilLivraison outilsLivraison;

	/**
	 * le template xml associe au produit pour effectuer l'appel vers packager.
	 */
	private String xmlTemplatePath;

	/**
	 * le path du template xsd pour valider le xml du produit.
	 */
	private String xsdTemplatePath;

	/**
	 * Délais de préparation.
	 */
	private Integer delaisPreparation;

	/**
	 * {@link Prix}.
	 */
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "produit")
	@NotNull
	private Prix prix;

	/**
	 * liste des {@link FraisProduit}.
	 */
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "produit")
	private Set<FraisProduit> frais = new HashSet<>();

	/**
	 * constructeur par defaut.
	 */
	public Produit() {

	}

	@Override
	public String toString() {
		return "Produit [id=" + id + ", reference=" + reference + ", label=" + label + ", typeProduit=" + typeProduit
				+ ", typeTVA=" + typeTVA + ", outilsLivraison=" + outilsLivraison + ", xmlTemplatePath="
				+ xmlTemplatePath + ", prix=" + prix + ", frais=" + frais + ", delaisPreparation=" + delaisPreparation
				+ "]";
	}

	/* Getters & Setters */

	/**
	 * @return id.
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            id.
	 */
	public void setId(Integer id) {
		this.id = id;
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
	 * @return {@link #prix}.
	 */
	public Prix getPrix() {
		return prix;
	}

	/**
	 * @param prix
	 *            {@link #prix}.
	 */
	public void setPrix(Prix prix) {
		this.prix = prix;
	}

	/**
	 * @return {@link #typeProduit}.
	 */
	public TypeProduit getTypeProduit() {
		return typeProduit;
	}

	/**
	 * @param typeProduit
	 *            {@link #typeProduit}.
	 */
	public void setTypeProduit(TypeProduit typeProduit) {
		this.typeProduit = typeProduit;
	}

	/**
	 * @return {@link #typeTVA}.
	 */
	public TypeTVA getTypeTVA() {
		return typeTVA;
	}

	/**
	 * @param typeTVA
	 *            {@link #typeTVA}.
	 */
	public void setTypeTVA(TypeTVA typeTVA) {
		this.typeTVA = typeTVA;
	}

	/**
	 * @return {@link #frais}.
	 */
	public Set<FraisProduit> getFrais() {
		return frais;
	}

	/**
	 * @param frais
	 *            {@link #frais}.
	 */
	public void setFrais(Set<FraisProduit> frais) {
		this.frais = frais;
	}

	/**
	 * @return {@link #outilsLivraison}.
	 */
	public OutilLivraison getOutilsLivraison() {
		return outilsLivraison;
	}

	/**
	 * @param outilsLivraison
	 *            {@link #outilsLivraison}.
	 */
	public void setOutilsLivraison(OutilLivraison outilsLivraison) {
		this.outilsLivraison = outilsLivraison;
	}

	/**
	 * @return {@link #xmlTemplatePath}.
	 */
	public String getXmlTemplatePath() {
		return xmlTemplatePath;
	}

	/**
	 * @param xmlTemplatePath
	 *            {@link #xmlTemplatePath}.
	 */
	public void setXmlTemplatePath(String xmlTemplatePath) {
		this.xmlTemplatePath = xmlTemplatePath;
	}

	/**
	 * @return {@link #xsdTemplatePath}.
	 */
	public String getXsdTemplatePath() {
		return xsdTemplatePath;
	}

	/**
	 * @param xsdTemplatePath
	 *            {@link #xsdTemplatePath}.
	 */
	public void setXsdTemplatePath(String xsdTemplatePath) {
		this.xsdTemplatePath = xsdTemplatePath;
	}

	/**
	 * @return {@link #delaisPreparation}.
	 */
	public Integer getDelaisPreparation() {
		return delaisPreparation;
	}

	/**
	 * @param delaisPreparation
	 *            {@link #delaisPreparation}.
	 */
	public void setDelaisPreparation(Integer delaisPreparation) {
		this.delaisPreparation = delaisPreparation;

	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof Produit)) {
			return false;
		}
		Produit rhs = (Produit) obj;
		return new EqualsBuilder().append(id, rhs.id).append(reference, rhs.reference).append(label, rhs.label)
				.isEquals();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(43, 11).append(id).append(reference).append(label).toHashCode();
	}

}
