package com.nordnet.topaze.livraison.core.validator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.nordnet.topaze.exception.TopazeException;
import com.nordnet.topaze.livraison.core.domain.BonPreparation;
import com.nordnet.topaze.livraison.core.domain.StatusBonPreparation;
import com.nordnet.topaze.livraison.core.domain.TypeBonPreparation;
import com.nordnet.topaze.livraison.core.util.PropertiesUtil;
import com.nordnet.topaze.livraison.core.util.Utils;

/**
 * Valider un {@link BonPreparation}.
 * 
 * @author Denden-OUSSAMA
 * @author anisselmane.
 * 
 */
public class BonPreparationValidator {

	/**
	 * default logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(BonPreparationValidator.class.getName());

	/**
	 * {@link PropertiesUtil}.
	 */
	private static PropertiesUtil propertiesUtil = PropertiesUtil.getInstance();

	/**
	 * Alert BP non livrer.
	 * 
	 * @param bonPreparationGlobal
	 *            the bon preparation global
	 * @throws TopazeException
	 *             the livraison exception
	 */
	public static void alertBPNonLivrer(BonPreparation bonPreparationGlobal) throws TopazeException {
		throw new TopazeException(propertiesUtil.getErrorMessage("3.1.2", bonPreparationGlobal.getReference(),
				bonPreparationGlobal.getCauseNonlivraison()), "3.1.2");
	}

	/**
	 * 
	 * @param reference
	 *            reference bon du preparation.
	 * @param bonGlobal
	 *            {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void checkExist(String reference, BonPreparation bonGlobal) throws TopazeException {
		if (bonGlobal == null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.2", reference), "0.1.2");
		}
	}

	/**
	 * verifie si le bon de preparation a deja ete initie.
	 * 
	 * @param reference
	 *            reference bon du preparation.
	 * @param bonGlobal
	 *            {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void checkNotExist(String reference, BonPreparation bonGlobal) throws TopazeException {
		if (bonGlobal != null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.3", reference), "0.1.3");
		}
	}

	/**
	 * Verifier reference bon de préparation.
	 * 
	 * @param reference
	 *            the reference
	 * @throws TopazeException
	 *             the livraison exception
	 */
	public static void verifierReferenceBP(String reference) throws TopazeException {
		if (Utils.isStringNullOrEmpty(reference)) {
			throw new TopazeException(propertiesUtil.getErrorMessage("0.1.4", "la référence du bon de préparation"),
					"0.1.4");
		}

	}

	/**
	 * Tester si un bon de preparation global est livree ou pas.
	 * 
	 * @param reference
	 *            reference du contrat associe au bon de preparation.
	 * @param bonPreparation
	 *            {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException} si le bon du preparation n'est pas livrer.
	 */
	public static void checkLivrer(String reference, BonPreparation bonPreparation) throws TopazeException {
		checkExist(reference, bonPreparation);

		if (!bonPreparation.getStatut().equals(StatusBonPreparation.TERMINER)) {
			throw new TopazeException(propertiesUtil.getErrorMessage("3.1.4", bonPreparation.getReference()), "3.1.4");
		}

	}

	/**
	 * Verifier si le bon de preparation est pour un retour.
	 * 
	 * @param referenceBP
	 *            reference du BP.
	 * @param bonPreparation
	 *            {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 * 
	 */
	public static void checkRecuperer(String referenceBP, BonPreparation bonPreparation) throws TopazeException {
		checkExist(referenceBP, bonPreparation);
		if (!bonPreparation.getTypeBonPreparation().equals(TypeBonPreparation.RETOUR)) {
			throw new TopazeException(propertiesUtil.getErrorMessage("3.1.5", bonPreparation.getReference()), "3.1.5");
		}
	}

	/**
	 * Test si un BP exist et preparer.
	 * 
	 * @param referenceBP
	 *            reference du BP.
	 * @param bonPreparation
	 *            {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void checkPreparer(String referenceBP, BonPreparation bonPreparation) throws TopazeException {
		checkExist(referenceBP, bonPreparation);
		if (!bonPreparation.isPreparer()) {
			throw new TopazeException(propertiesUtil.getErrorMessage("3.1.6", bonPreparation.getReference()), "3.1.6");
		}

	}

	/**
	 * Tester si le un BR est en cours de recuperation ou non.
	 * 
	 * @param bonRecuperation
	 *            {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void checkNonPrepare(BonPreparation bonRecuperation) throws TopazeException {
		if (bonRecuperation.getDateInitiation() != null && bonRecuperation.getDatePreparation() != null) {
			throw new TopazeException(propertiesUtil.getErrorMessage("3.1.9", bonRecuperation.getReference()), "3.1.9");
		}

	}

	/**
	 * Tester si le proecessus de recuperation des biens et des services a commencer ou pas.
	 * 
	 * @param bonPreparationRetour
	 *            bon de retour.
	 * @param bonPreparation
	 *            bon de preparation associe au bon de retour.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void checkNonRecuperer(BonPreparation bonPreparationRetour, BonPreparation bonPreparation)
			throws TopazeException {
		if (bonPreparationRetour != null
				&& bonPreparationRetour.getElementLivraisons().size() == bonPreparation.getElementRetours().size()) {
			throw new TopazeException(propertiesUtil.getErrorMessage("3.1.7", bonPreparationRetour.getReference()),
					"3.1.7");
		}
	}

	/**
	 * valider l'annulation du {@link BonPreparation}.
	 * 
	 * @param bonPreparation
	 *            {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void validerAnnulerBonPreparation(final BonPreparation bonPreparation) throws TopazeException {
		if (bonPreparation.isEnCoursPreparation() || bonPreparation.isPreparer()) {
			throw new TopazeException(propertiesUtil.getErrorMessage("3.1.11", bonPreparation.getStatut()), "3.1.11");
		}
	}

	/**
	 * 
	 * @param xmlContent
	 *            la template xml complete.
	 * @param xsdTemplate
	 *            la template xsd associer a l'xml.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static void validateXML(String xmlContent, String xsdTemplate) throws TopazeException {

		try (BufferedWriter outXml = new BufferedWriter(new FileWriter(File.createTempFile("xmlTemplate", ".xml")));
				BufferedWriter outXsd = new BufferedWriter(new FileWriter(File.createTempFile("xsdTemplate", ".xml")))) {
			outXml.write(xmlContent);
			outXsd.write(xsdTemplate);
			SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = factory.newSchema(File.createTempFile("xsdTemplate", ".xml"));
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(File.createTempFile("xmlTemplate", ".xml")));
		} catch (IOException | SAXException e) {
			LOGGER.error("Error occurs during call of  BonPreparationValidator.validateXML()", e);
			throw new TopazeException(propertiesUtil.getErrorMessage("3.1.8"), "3.1.8");
		}
	}
}