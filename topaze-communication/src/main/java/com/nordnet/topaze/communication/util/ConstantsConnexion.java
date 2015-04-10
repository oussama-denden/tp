package com.nordnet.topaze.communication.util;

/**
 * contient les constantes de connexion ajoute dans le fichier env.properties.
 * 
 * @author akram-moncer
 * 
 */
public final class ConstantsConnexion {

	/**
	 * private constructor.
	 */
	private ConstantsConnexion() {
		super();
	}

	/**
	 * constante contient l'info pour utiliser ou non le mock du NetCustomer.
	 */
	public static final boolean USE_NETCUSTOMER_MOCK = (System.getProperty("ws.netCustomer.useMock") == null || !System
			.getProperty("ws.netCustomer.useMock").trim().equals("true")) ? false : true;

	/**
	 * l'url d'appel vers NetCustomer.
	 */
	public static final String NETCUSTOMER_ENDPOINT = System.getProperty("ws.netCustomer.endpoint") != null ? System
			.getProperty("ws.netCustomer.endpoint").trim() : null;

	/**
	 * liste des serveur de rebond du netCustomer.
	 */
	public static final String[] NETCUSTOMER_SERVERS = System.getProperty("ws.netCustomer.servers") != null ? System
			.getProperty("ws.netCustomer.servers").trim().split(",") : new String[]{ "" };

	/**
	 * l'utilisateur de connexion au netCustomer.
	 */
	public static final String NETCUSTOMER_USER = System.getProperty("ws.netCustomer.wsseUser") != null ? System
			.getProperty("ws.netCustomer.wsseUser").trim() : null;

	/**
	 * le mot de passe de connexion au netCustomer.
	 */
	public static final String NETCUSTOMER_PWD = System.getProperty("ws.netCustomer.wssePwd") != null ? System
			.getProperty("ws.netCustomer.wssePwd").trim() : null;

	/**
	 * connection time out du netCustomer.
	 */
	public static final Long NETCUSTOMER_CONNECTION_TIME_OUT =
			System.getProperty("ws.netCustomer.connectionTimeout") != null
					&& !System.getProperty("ws.netCustomer.connectionTimeout").trim().equals("") ? Long.valueOf(System
					.getProperty("ws.netCustomer.connectionTimeout").trim()) : 0;

	/**
	 * receive time out du netCustomer.
	 */
	public static final Long NETCUSTOMER_RECEIVE_TIME_OUT = System.getProperty("ws.netCustomer.receiveTimeout") != null
			&& !System.getProperty("ws.netCustomer.receiveTimeout").trim().equals("") ? Long.valueOf(System
			.getProperty("ws.netCustomer.receiveTimeout").trim()) : 0;

	/**
	 * logger du netCustomer.
	 */
	public static final String NETCUSTOMER_LOGGER = System.getProperty("ws.netCustomer.Logger") != null ? System
			.getProperty("ws.netCustomer.Logger").trim() : "";

	/**
	 * constante contient l'info pour utiliser ou non le mock du Packager.
	 */
	public static final boolean USE_PACKAGER_MOCK = (System.getProperty("ws.packager.useMock") == null || !System
			.getProperty("ws.packager.useMock").trim().equals("true")) ? false : true;

	/**
	 * logger du netCustomer.
	 */
	public static final String PACKAGER_LOGGER = System.getProperty("ws.packager.Logger") != null ? System.getProperty(
			"ws.packager.Logger").trim() : "";

	/**
	 * l'utilisateur de connexion au packager.
	 */
	public static final String PACKAGER_USER = System.getProperty("ws.packager.wsseUser") != null ? System.getProperty(
			"ws.packager.wsseUser").trim() : null;

	/**
	 * le mot de passe de connexion au packager.
	 */
	public static final String PACKAGER_PWD = System.getProperty("ws.packager.wssePwd") != null ? System.getProperty(
			"ws.packager.wssePwd").trim() : null;

	/**
	 * connection time out du packager.
	 */
	public static final Long PACKAGER_CONNECTION_TIME_OUT = System.getProperty("ws.packager.connectionTimeout") != null
			&& !System.getProperty("ws.packager.connectionTimeout").trim().equals("") ? Long.valueOf(System
			.getProperty("ws.packager.connectionTimeout").trim()) : 0;

	/**
	 * receive time out du packager.
	 */
	public static final Long PACKAGER_RECEIVE_TIME_OUT = System.getProperty("ws.packager.receiveTimeout") != null
			&& !System.getProperty("ws.packager.receiveTimeout").trim().equals("") ? Long.valueOf(System.getProperty(
			"ws.packager.receiveTimeout").trim()) : 0;

	/**
	 * l'url d'appel vers packager.
	 */
	public static final String PACKAGER_ENDPOINT = System.getProperty("ws.packager.endpoint") != null ? System
			.getProperty("ws.packager.endpoint").trim() : null;

	/**
	 * liste des serveur de rebond du packager.
	 */
	public static final String[] PACKAGER_SERVERS = System.getProperty("ws.packager.servers") != null ? System
			.getProperty("ws.packager.servers").trim().split(",") : new String[]{ "" };

	/**
	 * l'url d'appel vers netCatalogue.
	 */
	public static final String NETCATALOGUE_ENDPOINT = System.getProperty("ws.netCatalogue.endpoint") != null ? System
			.getProperty("ws.netCatalogue.endpoint").trim() : null;

	/**
	 * constante contient l'info pour utiliser ou non le mock du NetCatalogue.
	 */
	public static final boolean USE_NETCATALOGUE_MOCK =
			(System.getProperty("ws.netCatalogue.useMock") == null || !System.getProperty("ws.netCatalogue.useMock")
					.trim().equals("true")) ? false : true;

	/**
	 * constante contient l'info pour utiliser ou non le mock du NetCommunication.
	 */
	public static final boolean USE_NETCOMM_MOCK = (System.getProperty("ws.netComm.useMock") == null || !System
			.getProperty("ws.netComm.useMock").trim().equals("true")) ? false : true;

	/**
	 * constante contient le url du netcommunication.
	 */
	public static final String NETCOMM_URL = System.getProperty("ws.netComm.url") != null ? System.getProperty(
			"ws.netComm.url").trim() : null;

}
