package com.nordnet.topaze.businessprocess.packager.adapter;

import nordnet.packager.PackagerClient;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.base.Optional;
import com.nordnet.adminpackager.ConverterException;
import com.nordnet.adminpackager.DriverException;
import com.nordnet.adminpackager.NotFoundException;
import com.nordnet.adminpackager.NullException;
import com.nordnet.adminpackager.PackagerException;
import com.nordnet.adminpackager.PackagerService;
import com.nordnet.common.alert.ws.client.SendAlert;
import com.nordnet.packager.types.packager.TPackagerInstance;
import com.nordnet.packager.types.packager.TState;
import com.nordnet.topaze.businessprocess.packager.mock.PackagerMock;
import com.nordnet.topaze.businessprocess.packager.util.ConstantsConnexion;
import com.nordnet.topaze.client.rest.business.livraison.ElementLivraison;
import com.nordnet.topaze.client.rest.enums.TypeBonPreparation;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Adapter du client packager.
 * 
 * @author akram-moncer
 * 
 */
@Component("packagerAdapter")
public class PackagerAdapter {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(PackagerAdapter.class);

	/**
	 * Alert service.
	 */
	@Autowired
	SendAlert sendAlert;

	/**
	 * {@link PackagerService}.
	 */
	private PackagerService packagerService;

	/**
	 * constructeur par defaut.
	 */
	public PackagerAdapter() {

	}

	/**
	 * appel de la methode 'getPackagerInstance' du packager.
	 * 
	 * @param elementLivraison
	 *            {@link ElementLivraison}.
	 * @return {@link TPackagerInstance}.
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
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public TPackagerInstance getPackagerInstance(ElementLivraison elementLivraison)
			throws NullException, NotFoundException, PackagerException, DriverException, ConverterException,
			TopazeException {
		if (!ConstantsConnexion.USE_PACKAGER_MOCK) {
			LOGGER.info("Appel Vers Le Serveur du Packager");
		}
		TPackagerInstance packagerInstance =
				getPackagerService().getPackagerInstance(elementLivraison.getRetailerPackagerId());
		if (ConstantsConnexion.USE_PACKAGER_MOCK) {
			if (elementLivraison.getTypeBonPreparation() == TypeBonPreparation.LIVRAISON)
				packagerInstance.setCurrentState(TState.ACTIVE);
			else
				packagerInstance.setCurrentState(TState.SUSPENDED);
		}

		return packagerInstance;
	}

	/**
	 * @return {@link #packagerService}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public com.nordnet.adminpackager.PackagerService getPackagerService() throws TopazeException {
		if (packagerService == null) {
			PackagerClient packagerClient = null;
			if (ConstantsConnexion.USE_PACKAGER_MOCK) {
				packagerClient =
						PackagerClient.builder().withLogger(ConstantsConnexion.PACKAGER_LOGGER)
								.withMockedPort(new PackagerMock()).enableMocking().build(PackagerClient.class);
			} else {
				packagerClient =
						PackagerClient.builder()
								.withWsseCredentials(ConstantsConnexion.PACKAGER_USER, ConstantsConnexion.PACKAGER_PWD)
								.withConnectionTimeout(ConstantsConnexion.PACKAGER_CONNECTION_TIME_OUT)
								.withReceiveTimeout(ConstantsConnexion.PACKAGER_RECEIVE_TIME_OUT)
								.withLogger(ConstantsConnexion.PACKAGER_LOGGER)
								.withEndpoint(ConstantsConnexion.PACKAGER_ENDPOINT)
								.withServers(ConstantsConnexion.PACKAGER_SERVERS).disableMocking()
								.build(PackagerClient.class);
			}

			packagerService = packagerClient.service();

			if (!Optional.fromNullable(packagerService).isPresent()) {
				throw new TopazeException("erreur lors d'etablissement de la connexion avec packager", "404");
			}
		}

		return packagerService;
	}

}
