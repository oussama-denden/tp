package com.nordnet.topaze.contrat.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.google.common.base.Optional;
import com.nordnet.topaze.contrat.business.ReductionInfo;
import com.nordnet.topaze.contrat.business.ReductionInterface;
import com.nordnet.topaze.contrat.util.PropertiesUtil;
import com.nordnet.topaze.contrat.util.TypeFraisDeserializer;
import com.nordnet.topaze.contrat.util.TypeReductionDeserializer;
import com.nordnet.topaze.contrat.util.TypeValeurReductionDeserializer;
import com.nordnet.topaze.exception.TopazeException;

/**
 * l'entite qui represente la promotion qui peut etre associe a un contrat.
 * 
 * @author akram-moncer
 * 
 */
@Entity
@Table(name = "reduction", uniqueConstraints = @UniqueConstraint(columnNames = { "referenceContrat", "version",
		"numEC", "contextReduction" }))
public class Reduction implements Cloneable {

	/**
	 * cle primaire.
	 */
	@Id
	@GeneratedValue
	@JsonIgnore
	private Integer id;

	/**
	 * ordre d'application de la reduction.
	 */
	private Integer ordre;

	/**
	 * reference du reduction.
	 */
	private String reference;

	/**
	 * reference contrat global.
	 */
	private String referenceContrat;

	/**
	 * reference reduction.
	 */
	private String codeCatalogueReduction;

	/**
	 * version du contrat associe à la reduction.
	 */
	private Integer version;

	/**
	 * numero element contractuel associe a la reduction.
	 */
	private Integer numEC;

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
	 * {@link ContextReduction}.
	 */
	@Enumerated(EnumType.STRING)
	private ContextReduction contextReduction;

	/**
	 * parametre qui indique si la reduction sera affiche dans une ligne distinct dans la facture.
	 */
	private Boolean isAffichableSurFacture;

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
	 * @return {@link #id}
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 *            {@link #id}
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 
	 * @return {@link #reference}.
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * 
	 * @param reference
	 *            {@link #reference}.
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
	 * @return {@link #referenceContrat}.
	 */
	public String getReferenceContrat() {
		return referenceContrat;
	}

	/**
	 * 
	 * @param referenceContrat
	 *            {@link #referenceContrat}.
	 */
	public void setReferenceContrat(String referenceContrat) {
		this.referenceContrat = referenceContrat;
	}

	/**
	 * 
	 * @return {@link Reduction#reference}
	 */
	public String getCodeCatalogueReduction() {
		return codeCatalogueReduction;
	}

	/**
	 * 
	 * @param reference
	 *            {@link #reference}
	 */
	public void setCodeCatalogueReduction(String codeCatalogueReduction) {
		this.codeCatalogueReduction = codeCatalogueReduction;
	}

	/**
	 * 
	 * @return {@link #version}.
	 */
	public Integer getVersion() {
		return version;
	}

	/**
	 * 
	 * @param version
	 *            {@link #version}.
	 */
	public void setVersion(Integer version) {
		this.version = version;
	}

	/**
	 * 
	 * @return {@link #numEC}.
	 */
	public Integer getNumEC() {
		return numEC;
	}

	/**
	 * 
	 * @param numEC
	 *            {@link #numEC}.
	 */
	public void setNumEC(Integer numEC) {
		this.numEC = numEC;
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
	 * @return {@link #typeReduction}
	 */
	public TypeReduction getTypeReduction() {
		return typeReduction;
	}

	/**
	 * 
	 * @param typeReduction
	 *            {@link #typeReduction}
	 */
	public void setTypeReduction(TypeReduction typeReduction) {
		this.typeReduction = typeReduction;
	}

	/**
	 * 
	 * @return {@link #TypeFrais}
	 */
	public TypeFrais getTypeFrais() {
		return typeFrais;
	}

	/**
	 * 
	 * @param typeFrais
	 *            {@link TypeFrais}
	 */
	public void setTypeFrais(TypeFrais typeFrais) {
		this.typeFrais = typeFrais;
	}

	/**
	 * 
	 * @return {@link #contextReduction}.
	 */
	public ContextReduction getContextReduction() {
		return contextReduction;
	}

	/**
	 * 
	 * @param contextReduction
	 *            {@link #contextReduction}.
	 */
	public void setContextReduction(ContextReduction contextReduction) {
		this.contextReduction = contextReduction;
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

		if (isReductionPourVente()) {
			return isEligible;
		}

		if ((dateDebut == null && (getNbUtilisationRestant() == null || getNbUtilisationRestant() <= 0))
				|| (dateDebut != null && dateDebut.after(PropertiesUtil.getInstance().getDateDuJour().toDate()))
				|| (dateFin != null && dateFin.before(PropertiesUtil.getInstance().getDateDuJour().toDate()))
				|| (dateDebut != null && getNbUtilisationRestant() != null && getNbUtilisationRestant() <= 0)) {
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
	 * cloner la reduction.
	 * 
	 * @return {@link Reduction}.
	 */
	@Override
	public Reduction clone() throws CloneNotSupportedException {
		return (Reduction) super.clone();
	}

	/**
	 * Creer une {@link ReductionInfo} a partir d'une {@link Reduction}.
	 * 
	 * @return {@link ReductionInfo}.
	 */
	public ReductionInfo toReductionInfo() {
		ReductionInfo reductionInfo = new ReductionInfo();
		reductionInfo.setOrdre(ordre);
		reductionInfo.setTitre(titre);
		reductionInfo.setDateDebut(dateDebut);
		reductionInfo.setDateFin(dateFin);
		reductionInfo.setNbUtilisationEnCours(nbUtilisationEnCours);
		reductionInfo.setNbUtilisationMax(nbUtilisationMax);
		reductionInfo.setTypeValeur(typeValeur);
		reductionInfo.setValeur(valeur);
		reductionInfo.setIsAffichableSurFacture(isAffichableSurFacture);
		reductionInfo.setCodeCatalogueReduction(codeCatalogueReduction);
		reductionInfo.setReference(reference);
		return reductionInfo;
	}

	/**
	 * creer une copie avec id = null.
	 * 
	 * @return {@link Reduction}.
	 */
	public Reduction copy() {
		Reduction reduction = new Reduction();
		reduction.setReferenceContrat(referenceContrat);
		reduction.setVersion(version);
		reduction.setNumEC(numEC);
		reduction.setTitre(titre);
		reduction.setDateDebut(dateDebut);
		reduction.setDateFin(dateFin);
		reduction.setNbUtilisationEnCours(nbUtilisationEnCours);
		reduction.setNbUtilisationMax(nbUtilisationMax);
		reduction.setTypeValeur(typeValeur);
		reduction.setValeur(valeur);
		reduction.setTypeReduction(typeReduction);
		reduction.setTypeFrais(typeFrais);
		reduction.setIsAffichableSurFacture(isAffichableSurFacture);
		reduction.setContextReduction(contextReduction);
		return reduction;
	}

	/**
	 * Verifer si la reduction fait partie du catalogue.
	 * 
	 * @return true si la reduction est du catalogue.
	 */
	public boolean isReductionCatalogue() {
		return codeCatalogueReduction != null;
	}

	/**
	 * verifer si la reduction est annulee.
	 * 
	 * @return true si la reduction est annulee.
	 */
	public boolean isAnnule() {
		return dateAnnulation != null;
	}

	/**
	 * verifier si la reduction n'est pas destine a un element de vente, si les champs [dateDebut, dateFin,
	 * nbUtilisationMax] alors la reduction est destine a un element de vente.
	 * 
	 * @return true si la reduction est destine a un element de vente
	 */
	@JsonIgnore
	public Boolean isReductionPourVente() {
		if (dateDebut == null && dateFin == null && nbUtilisationMax == null) {
			return true;
		}

		return false;
	}

	/**
	 * Creer une {@link ReductionInterface} a partir d'une {@link Reduction}.
	 * 
	 * @return {@link ReductionInterface}.
	 */
	public ReductionInterface toReductionInterface() {
		ReductionInterface reductionInterface = new ReductionInterface();
		reductionInterface.setDateDebut(dateDebut);
		reductionInterface.setDateFin(dateFin);
		reductionInterface.setLabel(titre);
		reductionInterface.setNbUtilisationRestante(getNbUtilisationRestant());
		reductionInterface.setNumEC(numEC);
		reductionInterface.setOrdre(ordre);
		reductionInterface.setReference(reference);
		reductionInterface.setReferenceCatalogue(codeCatalogueReduction);
		reductionInterface.setTypeValeur(typeValeur);
		reductionInterface.setValeur(valeur);
		return reductionInterface;
	}

	/**
	 * indique si la reduction est automatique ou non.
	 * 
	 * @return true si la reduction est automatique.
	 */
	public boolean isReductionAutomatique() {
		if (Optional.fromNullable(codeCatalogueReduction).isPresent())
			return true;

		return false;
	}

}
