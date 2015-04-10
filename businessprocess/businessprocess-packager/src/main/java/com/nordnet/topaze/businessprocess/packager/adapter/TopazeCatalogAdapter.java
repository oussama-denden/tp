package com.nordnet.topaze.businessprocess.packager.adapter;

import org.springframework.stereotype.Component;

import com.nordnet.netcatalog.exception.NetCatalogException;
import com.nordnet.netcatalog.ws.client.NetCatalogClient;
import com.nordnet.netcatalog.ws.client.fake.NetCatalogClientFake;
import com.nordnet.netcatalog.ws.entity.PackagerConfig;
import com.nordnet.topaze.businessprocess.packager.util.ConstantsConnexion;
import com.nordnet.topaze.exception.TopazeException;

/**
 * adapter pour le client du netCatalog.
 * 
 * @author akram-moncer
 * 
 */
@Component("topazeCatalogAdapter")
public class TopazeCatalogAdapter {

	/**
	 * {@link NetCatalogClient}.
	 */
	private NetCatalogClient netCatalogClient;

	/**
	 * constructeur par defaut.
	 */
	public TopazeCatalogAdapter() {
		if (ConstantsConnexion.USE_NETCATALOGUE_MOCK) {
			netCatalogClient = new NetCatalogClientFake();
		} else {
			netCatalogClient = new NetCatalogClient();
		}
	}

	/**
	 * recuperation des configs nacessaire pour l'envoi de l'appel vers packager.
	 * 
	 * @param referenceService
	 *            reference produit.
	 * @return {@link PackagerConfig}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public PackagerConfig getPackagerConfig(String referenceService) throws TopazeException {
		try {
			PackagerConfig packagerConfig = netCatalogClient.getService(referenceService);
			PackagerConfig packagerConfigFake = new NetCatalogClientFake().getService(referenceService);
			packagerConfig.setTemplate(packagerConfigFake.getTemplate());
			packagerConfig.setConfig(packagerConfigFake.getConfig());

			return packagerConfig;
		} catch (NetCatalogException e) {
			throw new TopazeException(e.getMessage(), e.getErrorCode());
		}
	}

	/**
	 * recuperation de la valeur de l'option plus.
	 * 
	 * @param referenceProduit
	 *            reference produit
	 * @return valeur option plus.
	 */
	public String getValeurOptionPlus(String referenceProduit) {
		return netCatalogClient.getValeurOptionPlus(referenceProduit);
	}
}
