package com.nordnet.topaze.businessprocess.packager.service;

import com.nordnet.adminpackager.ConverterException;
import com.nordnet.adminpackager.DriverException;
import com.nordnet.adminpackager.NotFoundException;
import com.nordnet.adminpackager.NullException;
import com.nordnet.adminpackager.PackagerException;
import com.nordnet.netcatalog.ws.entity.PackagerConfig;
import com.nordnet.topaze.client.rest.business.livraison.BonPreparation;
import com.nordnet.topaze.client.rest.business.livraison.ElementLivraison;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Cette classe contient les methode relative a Packager.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public interface PackagerService {

	/**
	 * traduction d'un bon de preparation vers packager.
	 * 
	 * @param bonPreparation
	 *            {@link BonPreparation}.
	 * 
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void traductionPackager(BonPreparation bonPreparation) throws TopazeException;

	/**
	 * Traduction d'un bon de preparation de livraison vers packager.
	 * 
	 * @param referenceBP
	 *            reference de bon de prepartion.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void traductionPackagerPourLivraison(String referenceBP) throws TopazeException;

	/**
	 * Traduction d'un bon de preparation de migration vers packager.
	 * 
	 * @param referenceBP
	 *            reference de bon de prepartion.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void traductionPackagerPourMigration(String referenceBP) throws TopazeException;

	/**
	 * creer un appel vers packager afin de lui communiquer le service a desactiver.
	 * 
	 * @param bonRecuperation
	 *            {@link BonPreparation}.
	 * @param isMigration
	 *            indiquer si la suspension se fait dans la fase de migration.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void suspendService(BonPreparation bonRecuperation, boolean isMigration) throws TopazeException;

	/**
	 * effectue un 'TransformPackager' au {@link ElementLivraison} de type migration.
	 * 
	 * @param bonMigration
	 *            {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void traductionPourTransformService(BonPreparation bonMigration) throws TopazeException;

	/**
	 * effectuer un 'TransformPackager' au {@link ElementLivraison} .
	 * 
	 * @param bonPreparation
	 *            {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void traductionPourCession(BonPreparation bonPreparation) throws TopazeException;

	/**
	 * effectuer un appel 'changeProperties' vers packager.
	 * 
	 * @param elementLivraison
	 *            {@link ElementLivraison} parent de l'option a desactiver.
	 * @param packagerConfig
	 *            {@link PackagerConfig}
	 * @param idClient
	 *            id du client
	 * @throws TopazeException
	 *             {@link TopazeException}
	 */
	public void changeProperties(ElementLivraison elementLivraison, PackagerConfig packagerConfig, String idClient)
			throws TopazeException;

	/**
	 * Verifier si un service est active.
	 * 
	 * @param elementLivraison
	 *            {@link ElementLivraison}.
	 * @return true si service active.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 * @throws NotFoundException
	 *             {@link NotFoundException}.
	 */
	public Boolean checkServiceActive(ElementLivraison elementLivraison) throws TopazeException, NotFoundException;

	/**
	 * Verifier si un service est suspendu.
	 * 
	 * @param elementLivraison
	 *            {@link ElementLivraison}.
	 * @return true si service suspendu.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 * @throws NotFoundException
	 *             {@link NotFoundException}.
	 */
	public boolean checkServiceSuspendu(ElementLivraison elementLivraison) throws TopazeException, NotFoundException;

	/**
	 * Resiliation definitive des services qui n'ont pas ete reactives.
	 * 
	 * @param elementLivraison
	 *            {@link ElementLivraison}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void cancelPackager(ElementLivraison elementLivraison) throws TopazeException;

	/**
	 * effectuer un 'TransformPackager' au {@link ElementLivraison} .
	 * 
	 * @param bonRenouvellement
	 *            {@link BonPreparation}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 * @throws NullException
	 *             {@link NullException}.
	 * @throws NotFoundException
	 *             {@link NotFoundException}.
	 * @throws PackagerException
	 *             {@link PackagerException}.
	 * @throws DriverException
	 *             {@link DriverException}.
	 * @throws ConverterException
	 *             {@link ConverterException}.
	 */
	public void activerPourRenouvellement(BonPreparation bonRenouvellement)
			throws TopazeException, NullException, NotFoundException, PackagerException, DriverException,
			ConverterException;

	/**
	 * Activer la service pour renouvelement.
	 * 
	 * @param referenceBP
	 *            reference de bon de prepartion.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 * @throws NullException
	 *             {@link NullException}.
	 * @throws NotFoundException
	 *             {@link NotFoundException}.
	 * @throws PackagerException
	 *             {@link PackagerException}.
	 * @throws DriverException
	 *             {@link DriverException}.
	 * @throws ConverterException
	 *             {@link ConverterException}.
	 */
	public void activerPourRenouvellement(String referenceBP)
			throws TopazeException, NullException, NotFoundException, PackagerException, DriverException,
			ConverterException;

	/**
	 * suspension de service.
	 * 
	 * @param referenceBP
	 *            reference de bon de preparation.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void packagerSuspension(String referenceBP) throws TopazeException;

	/**
	 * Traduction packager pour cession.
	 * 
	 * @param referenceBP
	 *            reference de bon de preparation.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public void traductionPourCession(String referenceBP) throws TopazeException;

}
