package com.nordnet.topaze.catalogue.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nordnet.topaze.catalogue.domain.FraisProduit;
import com.nordnet.topaze.catalogue.domain.Produit;
import com.nordnet.topaze.catalogue.domain.ProduitBillingInfo;
import com.nordnet.topaze.catalogue.domain.TypeTVA;
import com.nordnet.topaze.catalogue.repository.ProduitRepository;
import com.nordnet.topaze.catalogue.util.TemplatePathProperties;
import com.nordnet.topaze.catalogue.validator.ProduitValidator;
import com.nordnet.topaze.exception.TopazeException;

/**
 * L'implementation de service {@link ProduitService}.
 * 
 * @author Denden-OUSSAMA
 * @author akram-moncer
 * 
 */
@Service("produitService")
public class ProduitServiceImpl implements ProduitService {

	/**
	 * Declaration du log.
	 */
	private static final Log LOGGER = LogFactory.getLog(ProduitServiceImpl.class);

	/**
	 * {@link ProduitRepository}.
	 */
	@Autowired
	private ProduitRepository produitRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public void addProduit(Produit produit) throws TopazeException {

		ProduitValidator.validationTypeFrais(produit);
		ProduitValidator.validateAddProduit(produit);

		ProduitValidator.checkNotExist(produitRepository.findByReference(produit.getReference()));

		produit.getPrix().setProduit(produit);

		for (FraisProduit fraisProduit : produit.getFrais())
			fraisProduit.setProduit(produit);

		produitRepository.save(produit);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public TypeTVA getTypeTVAProduit(String reference) throws TopazeException {

		Produit produit = produitRepository.findByReference(reference);
		ProduitValidator.checkExist(reference, produit);

		return produit.getTypeTVA();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProduitBillingInfo getProduitBillingInformation(String reference) throws TopazeException {

		Produit produit = produitRepository.findByReference(reference);
		ProduitValidator.checkExist(reference, produit);

		ProduitBillingInfo produitBillingInfo = new ProduitBillingInfo();
		produitBillingInfo.setProduitId(produit.getId());
		produitBillingInfo.setLabel(produit.getLabel());
		produitBillingInfo.setOutilLivraison(produit.getOutilsLivraison());
		produitBillingInfo.setPrix(produit.getPrix().getMontant());
		produitBillingInfo.setTypeTVA(produit.getTypeTVA());
		List<Double> frais = new ArrayList<>();
		for (FraisProduit fraisProduit : produit.getFrais()) {
			frais.add(fraisProduit.getMontant());
		}
		produitBillingInfo.setFrais(frais);
		return produitBillingInfo;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Produit getProduit(String reference) throws TopazeException {

		Produit produit = produitRepository.findByReference(reference);
		ProduitValidator.checkExist(reference, produit);

		return produit;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Transactional
	public Produit associerTemplate(String referenceProduit, String referenceTemplate) throws TopazeException {

		Produit produit = produitRepository.findByReference(referenceProduit);
		ProduitValidator.checkExist(referenceProduit, produit);
		ProduitValidator.validateAssociationTemplateXML(produit);
		ProduitValidator.checkXMLTemplateNOTExist(referenceTemplate);
		ProduitValidator.checkXSDTemplateNOTExist(referenceTemplate);
		produit.setXmlTemplatePath(referenceTemplate);
		produit.setXsdTemplatePath(referenceTemplate);
		produitRepository.save(produit);

		return produit;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTemplateXML(String referenceProduit) throws TopazeException {

		TemplatePathProperties templatePathProperties = TemplatePathProperties.getInstance();

		Produit produit = produitRepository.findByReference(referenceProduit);
		ProduitValidator.checkExist(referenceProduit, produit);
		ProduitValidator.checkXMLAssocier(produit);
		ProduitValidator.checkXMLTemplateNOTExist(produit.getXmlTemplatePath());
		ClassPathResource resource =
				new ClassPathResource(templatePathProperties.getXMLTemplatePath() + produit.getXmlTemplatePath()
						+ ".xml");

		String contentTemplate = "";

		try (BufferedReader br = new BufferedReader(new FileReader(resource.getFile()))) {

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				contentTemplate += sCurrentLine;
			}

		} catch (IOException e) {
			LOGGER.error("exception catched during the getTemplateXML method", e);
		}

		return contentTemplate;

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTemplateXSD(String referenceProduit) throws TopazeException {

		TemplatePathProperties templatePathProperties = TemplatePathProperties.getInstance();

		Produit produit = produitRepository.findByReference(referenceProduit);
		ProduitValidator.checkExist(referenceProduit, produit);
		ProduitValidator.checkXSDAssocier(produit);
		ProduitValidator.checkXSDTemplateNOTExist(produit.getXsdTemplatePath());
		ClassPathResource resource =
				new ClassPathResource(templatePathProperties.getXSDTemplatePath() + produit.getXsdTemplatePath()
						+ ".xsd");

		String contentTemplate = "";

		try (BufferedReader br = new BufferedReader(new FileReader(resource.getFile()))) {

			String sCurrentLine;

			while ((sCurrentLine = br.readLine()) != null) {
				contentTemplate += sCurrentLine;
			}

		} catch (IOException e) {
			LOGGER.error("exception catched during the getTemplateXSD method", e);
		}

		return contentTemplate;

	}
}
