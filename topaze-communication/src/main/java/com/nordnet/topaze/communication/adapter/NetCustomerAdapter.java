package com.nordnet.topaze.communication.adapter;

import nordnet.customer.NetCustomerClient;

import org.springframework.stereotype.Component;

import com.nordnet.backoffice.ws.CustomerException;
import com.nordnet.backoffice.ws.NetCustomer;
import com.nordnet.backoffice.ws.types.customer.Handle;
import com.nordnet.topaze.communication.adapter.business.ClientInfo;
import com.nordnet.topaze.communication.mock.NetCustomerMock;
import com.nordnet.topaze.communication.util.ConstantsConnexion;
import com.nordnet.topaze.exception.TopazeException;

/**
 * Adapter pour le client netCustomer.
 * 
 * @author akram-moncer
 * 
 */
@Component("netCustomerAdapter")
public class NetCustomerAdapter {

	/**
	 * {@link NetCustomer}.
	 */
	private NetCustomer netCustomerService;

	/**
	 * constructeur par defaut.
	 */
	public NetCustomerAdapter() {
		getNetCustomerClient();
	}

	/**
	 * appel vers le netCustomer pour recuperer les info clients.
	 * 
	 * @param idAdresse
	 *            id adresse.
	 * @return {@link ClientInfo}.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public ClientInfo getHandleByKey(String idAdresse) throws TopazeException {
		try {
			Handle handle = netCustomerService.getHandleByKey(idAdresse);
			ClientInfo clientInfo = new ClientInfo();
			clientInfo.setAdresseEmail(handle.getEmail().getValue());
			clientInfo.setCivilite(handle.getCivility().getValue());
			clientInfo.setCompany(handle.getCompany().getValue());
			clientInfo.setNom(handle.getName().getValue());
			clientInfo.setPrenom(handle.getFirstName().getValue());

			return clientInfo;
		} catch (CustomerException e) {
			throw new TopazeException("Error lors de l'appel vers netCustomer", e);
		}
	}

	/**
	 * Effectue la connexion avec NetCustomer et retourn le service contenant les operations fournit par netCustomer.
	 * 
	 * @return {@link NetCustomer}.
	 */
	public NetCustomer getNetCustomerClient() {
		if (netCustomerService == null) {
			NetCustomerClient netCustomerClient = null;
			if (ConstantsConnexion.USE_NETCUSTOMER_MOCK) {
				netCustomerClient =
						NetCustomerClient.builder().withMockedPort(new NetCustomerMock()).enableMocking()
								.build(NetCustomerClient.class);
			} else {
				netCustomerClient =
						NetCustomerClient
								.builder()
								.withWsseCredentials(ConstantsConnexion.NETCUSTOMER_USER,
										ConstantsConnexion.NETCUSTOMER_PWD)
								.withConnectionTimeout(ConstantsConnexion.NETCUSTOMER_CONNECTION_TIME_OUT)
								.withReceiveTimeout(ConstantsConnexion.NETCUSTOMER_RECEIVE_TIME_OUT)
								.withLogger(ConstantsConnexion.NETCUSTOMER_LOGGER)
								.withEndpoint(ConstantsConnexion.NETCUSTOMER_ENDPOINT)
								.withServers(ConstantsConnexion.NETCUSTOMER_SERVERS).disableMocking()
								.build(NetCustomerClient.class);
			}
			netCustomerService = netCustomerClient.service();
		}
		return netCustomerService;
	}

}
