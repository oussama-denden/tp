package com.nordnet.topaze.logger.business;

import java.util.Date;

/**
 * Cette classe regroupe les informations qui definissent un {@link TracageInfo}.
 * 
 * @author anisselmane.
 * 
 */
public class TracageInfo {

	/**
	 * Le produit.
	 */
	private String target;

	/**
	 * La reference.
	 */
	private String key;

	/**
	 * Action executer.
	 */
	private String description;

	/**
	 * ip qui a appliquer l'opération.
	 */
	private String ip;

	/**
	 * L'usager qui a appliquer l'opération.
	 */
	private String user;

	/**
	 * Type de log.
	 */
	private String type;

	/**
	 * constructeur par defaut.
	 */
	public TracageInfo() {

	}

	/**
	 * constructeur parametre.
	 * 
	 * @param target
	 *            target
	 * @param reference
	 *            reference
	 * @param descr
	 *            descreption
	 * @param ip
	 *            ip
	 * @param user
	 *            user
	 * @param type
	 *            type
	 * @param date
	 *            date
	 */
	public TracageInfo(String target, String key, String description, String ip, String user, String type, Date date) {
		this.target = target;
		this.key = key;
		this.description = description;
		this.ip = ip;
		this.user = user;
		this.type = type;
	}

	/* Getters & Setters */

	/**
	 * 
	 * @return {@link #target}
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * 
	 * @param target
	 *            {@link #target}
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * 
	 * @return {@link #key}
	 */
	public String getKey() {
		return key;
	}

	/**
	 * 
	 * @param key
	 *            {@link #key}
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * 
	 * @return {@link #type}
	 */
	public String getType() {
		return type;
	}

	/**
	 * 
	 * @param type
	 *            {@link #type}
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * 
	 * @return {@link #description}
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 
	 * @param description
	 *            {@link #description}
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 
	 * @return {@link #ip}
	 */
	public String getIp() {
		return ip;
	}

	/**
	 * 
	 * @param ip
	 *            {@link #ip}
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}

	/**
	 * 
	 * @return {@link #user}
	 */
	public String getUser() {
		return user;
	}

	/**
	 * 
	 * @param user
	 *            {@link #user}
	 */
	public void setUser(String user) {
		this.user = user;
	}

}