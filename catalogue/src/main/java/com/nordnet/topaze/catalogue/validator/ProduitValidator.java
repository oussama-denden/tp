package com.nordnet.topaze.catalogue.validator;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import com.nordnet.topaze.catalogue.domain.FraisProduit;
import com.nordnet.topaze.catalogue.domain.OutilLivraison;
import com.nordnet.topaze.catalogue.domain.Produit;
import com.nordnet.topaze.catalogue.domain.TypeFrais;
import com.nordnet.topaze.catalogue.domain.TypePrix;
import com.nordnet.topaze.catalogue.domain.TypeProduit;
import com.nordnet.topaze.catalogue.util.PropertiesUtil;
import com.nordnet.topaze.catalogue.util.TemplatePathProperties;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Valider un produit.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class ProduitValidator {

	/**
	 * {@link PropertiesUtil}.
	 */
	private static PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();

	/**
	 * Declaration du log.
	 */
	private final static Logger LOGGER = Logger.getLogger(ProduitValidator.class);

	/**
	 * {@link TemplatePathProperties}.
	 */
	private static TemplatePathProperties templatePathProperties = TemplatePathProperties.getInstance();

	/**
	 * 
	 * @param produit
	 *            {@link Produit}
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public static void validateAddProduit(Produit produit) throws TopazeException {

		PrixValidator.validatePrix(produit.getPrix());
		FraisProduitValidator.validateFraisPrix(produit);

		if (produit.getReference() == null || produit.getReference().equals(""))
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "Produit.reference"), "0.1.1");

		if (produit.getLabel() == null || produit.getLabel().equals(""))
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "Produit.label"), "0.1.1");
		if (produit.getTypeProduit() == null)
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.1", "Produit.typeProduit"), "0.1.1");

		if (produit.getTypeTVA() == null)
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.4", "Produit.typeTVA"), "0.1.4");

		if (produit.getOutilsLivraison() == null)
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.4", "Produit.outilsLivraison"), "0.1.4");

		if ((produit.getTypeProduit().equals(TypeProduit.BIEN) && produit.getOutilsLivraison().equals(
				OutilLivraison.PACKAGER))
				|| (produit.getTypeProduit().equals(TypeProduit.SERVICE) && produit.getOutilsLivraison().equals(
						OutilLivraison.NETDELIVERY)))
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.5", "Produit", produit.getTypeProduit()
					.toString(), "Produit.outilsLivraison", produit.getOutilsLivraison().toString()), "0.1.5");
		if ((produit.getTypeProduit().equals(TypeProduit.SERVICE) && (produit.getPrix().getTypePrix()
				.equals(TypePrix.ONE_SHOT) || produit.getPrix().getTypePrix().equals(TypePrix.TROIS_FOIS_SANS_FRAIS))))
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.5", "Produit", produit.getTypeProduit()
					.toString(), "Produit.Prix.typePrix", produit.getPrix().getTypePrix().toString()), "0.1.5");

	}

	/**
	 * Verifier si le produit n'est pas existant dans la base.
	 * 
	 * @param produit
	 *            {@link Produit}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void checkNotExist(Produit produit) throws TopazeException {
		if (produit != null)
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.3", produit.getReference()), "0.1.3");
	}

	/**
	 * Verifier si le produit exist dans la base.
	 * 
	 * @param reference
	 *            reference du produit.
	 * @param produit
	 *            {@link Produit}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void checkExist(String reference, Produit produit) throws TopazeException {
		if (produit == null)
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.2", reference), "0.1.2");
	}

	/**
	 * 
	 * @param produit
	 *            produit.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public static void checkXMLAssocier(Produit produit) throws TopazeException {
		if (produit.getXmlTemplatePath() == null || produit.getXmlTemplatePath().equals(""))
			throw new TopazeException(propertiesUtil.getErrorMessage("2.1.4", produit.getReference()), "2.1.4");
	}

	/**
	 * 
	 * @param produit
	 *            produit.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public static void checkXSDAssocier(Produit produit) throws TopazeException {
		if (produit.getXsdTemplatePath() == null || produit.getXsdTemplatePath().equals(""))
			throw new TopazeException(propertiesUtil.getErrorMessage("2.1.5", produit.getReference()), "2.1.5");
	}

	/**
	 * 
	 * @param referenceTemplate
	 *            la reference du template.
	 * @return la template xml.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public static File checkXMLTemplateNOTExist(String referenceTemplate) throws TopazeException {

		ClassPathResource resource =
				new ClassPathResource(templatePathProperties.getXMLTemplatePath() + referenceTemplate + ".xml");

		try {
			return resource.getFile();
		} catch (IOException e) {
			LOGGER.error("Error occurs during call of  ProduitValidator.checkXMLTemplateNOTExist()", e);
			throw new TopazeException(propertiesUtil.getErrorMessage("2.1.1", referenceTemplate), "2.1.1");
		}
	}

	/**
	 * 
	 * @param referenceTemplate
	 *            la reference du template.
	 * @return la template xsd.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public static File checkXSDTemplateNOTExist(String referenceTemplate) throws TopazeException {

		ClassPathResource resource =
				new ClassPathResource(templatePathProperties.getXSDTemplatePath() + referenceTemplate + ".xsd");

		try {
			return resource.getFile();
		} catch (IOException e) {
			LOGGER.error("Error occurs during call of  ProduitValidator.checkXSDTemplateNOTExist()", e);
			throw new TopazeException(propertiesUtil.getErrorMessage("2.1.2", referenceTemplate), "2.1.2");
		}
	}

	/**
	 * 
	 * @param produit
	 *            produit.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void validateAssociationTemplateXML(Produit produit) throws TopazeException {
		if (produit.getOutilsLivraison() != OutilLivraison.PACKAGER) {
			throw new TopazeException(propertiesUtil.getErrorMessage("2.1.3"), "2.1.3");
		}
	}

	public static void validationTypeFrais(Produit produit) throws TopazeException {

		for (FraisProduit typeFrais : produit.getFrais()) {

			if (typeFrais.getTypeFrais() != TypeFrais.CREATION && typeFrais.getTypeFrais() != TypeFrais.RESILIATION) {
				throw new TopazeException(propertiesUtil.getErrorMessage("2.1.6"), "2.1.6");
			}

		}

	}
}
