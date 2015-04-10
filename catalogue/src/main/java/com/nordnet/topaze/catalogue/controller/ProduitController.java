package com.nordnet.topaze.catalogue.controller;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.nordnet.topaze.catalogue.domain.Produit;
import com.nordnet.topaze.catalogue.domain.ProduitBillingInfo;
import com.nordnet.topaze.catalogue.domain.TypeTVA;
import com.nordnet.topaze.catalogue.service.ProduitService;
import com.nordnet.topaze.exception.InfoErreur;
import com.nordnet.topaze.exception.TopazeException;
import com.wordnik.swagger.annotations.Api;

/**
 * Gerer l'ensemble des requetes qui ont en rapport avec les produits.
 * 
 * @author Denden-OUSSAMA
 * 
 */
@Api(value = "produit", description = "produit")
@Controller
@RequestMapping("/produit")
public class ProduitController {

	/**
	 * {@link ProduitService} service.
	 */
	@Autowired
	private ProduitService produitService;

	/**
	 * Creer et persister l'entite {@link Produit}.
	 * 
	 * @param produit
	 *            {@link Produit} a ajouter.
	 * @return Le {@link produit} ajoutee.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */

	@RequestMapping(value = "/ajouter", method = RequestMethod.POST, headers = "Accept=application/json")
	@ResponseBody
	public Produit addProduit(@RequestBody Produit produit) throws TopazeException {
		produitService.addProduit(produit);
		return produit;
	}

	/**
	 * cherecher le type de TVA de produit appartir de son reference.
	 * 
	 * @param referenceProduit
	 *            reference du produit.
	 * @return {@link TypeTVA}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceProduit:.+}/typeTVA", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public TypeTVA getTypeTVAProduit(@PathVariable String referenceProduit) throws TopazeException {
		return produitService.getTypeTVAProduit(referenceProduit);
	}

	/**
	 * chercher le produit avec le reference donnee et retourner son {@link ProduitBillingInfo}.
	 * 
	 * @param referenceProduit
	 *            reference d'un produit.
	 * @return Liste des references des produits.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/{referenceProduit:.+}/billingInformation", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public ProduitBillingInfo getProduitBillingInformation(@PathVariable String referenceProduit)
			throws TopazeException {
		return produitService.getProduitBillingInformation(referenceProduit);
	}

	/**
	 * get produit.
	 * 
	 * @param referenceProduit
	 *            reference du produit.
	 * @return {@link Produit}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	@RequestMapping(value = "/search/{referenceProduit:.+}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Produit getProduit(@PathVariable String referenceProduit) throws TopazeException {
		return produitService.getProduit(referenceProduit);
	}

	/**
	 * 
	 * @param referenceProduit
	 *            reference du produit.
	 * @param referenceTemplate
	 *            reference du template xml qu'on associe au produit.
	 * @return {@link Produit}.
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	@RequestMapping(value = "/{referenceProduit:.+}/associerXMLTemplate/{referenceTemplate:.+}", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public Produit associerTemplate(@PathVariable String referenceProduit, @PathVariable String referenceTemplate)
			throws TopazeException {
		return produitService.associerTemplate(referenceProduit, referenceTemplate);
	}

	/**
	 * 
	 * @param referenceProduit
	 *            reference du produit.
	 * @return le contenu de la template xsd du produit.
	 * @throws JSONException
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	@RequestMapping(value = "/{referenceProduit:.+}/xsd", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getTemplateXSD(@PathVariable String referenceProduit) throws TopazeException {
		return produitService.getTemplateXSD(referenceProduit);
	}

	/**
	 * 
	 * @param referenceProduit
	 *            reference du produit.
	 * @return le contenu de la template xml du produit.
	 * @throws JSONException
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	@RequestMapping(value = "/{referenceProduit:.+}/xml", method = RequestMethod.GET, produces = "application/json")
	@ResponseBody
	public String getTemplateXML(@PathVariable String referenceProduit) throws TopazeException {
		return produitService.getTemplateXML(referenceProduit);
	}

	/**
	 * Gerer le cas ou on a une exception.
	 * 
	 * @param req
	 *            requete HttpServletRequest.
	 * @param ex
	 *            exception
	 * @return {@link InfoErreur}
	 */
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler({ TopazeException.class })
	@ResponseBody
	InfoErreur handleTopazeException(HttpServletRequest req, Exception ex) {
		return new InfoErreur(req.getRequestURI(), ((TopazeException) ex).getErrorCode(), ex.getLocalizedMessage());
	}

}
