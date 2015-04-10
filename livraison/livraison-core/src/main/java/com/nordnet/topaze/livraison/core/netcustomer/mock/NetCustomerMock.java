package com.nordnet.topaze.livraison.core.netcustomer.mock;

import java.util.UUID;

/**
 * 
 * @author akram-moncer
 * 
 */
public class NetCustomerMock {

	/**
	 * get client by key.
	 * 
	 * @param customerKey
	 *            client key.
	 * @return {@link Client}.
	 */
	public Client getByKey(String customerKey) {

		Client client = new Client();
		client.setCustomerKey(customerKey);
		Handle handle = new Handle();
		handle.setHandleKey(UUID.randomUUID().toString().substring(0, 8));
		handle.setCivility("civil");
		handle.setName("doe");
		handle.setFirstName("john");
		handle.setAddress1("14 rue germain");
		handle.setZipCode("5170");
		handle.setCity("paris");
		handle.setCountry("france");
		handle.setPhone("066523154");
		handle.setCompany("nordNet");
		handle.setEmail("john.doe@nordnet.com");
		client.setDefaultHandle(handle);
		return client;

	}

}
