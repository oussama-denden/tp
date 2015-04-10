package com.nordnet.topaze.logger.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * Cette classe regroupe les informations qui definissent un {@link Tracage}.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
@Entity
@Table(name = "tracage")
public class Tracage {

	/**
	 * cle primaire.
	 */
	@Id
	@GeneratedValue
	private Integer id;

	/**
	 * Le produit.
	 */
	private String target;

	/**
	 * La reference.
	 */
	private String reference;
	/**
	 * Action executer.
	 */
	@Type(type = "text")
	private String descr;

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
	 * Date de l'opération.
	 */
	private Date date;

	/**
	 * constructeur par defaut.
	 */
	public Tracage() {

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
	public Tracage(String target, String reference, String descr, String ip, String user, String type, Date date) {
		this.target = target;
		this.reference = reference;
		this.descr = descr;
		this.ip = ip;
		this.user = user;
		this.type = type;
		this.date = date;
	}

	/* Getters & Setters */

	/**
	 * 
	 * @return {@link #id}
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 *            {@link #id}
	 */
	public void setId(Integer id) {
		this.id = id;
	}

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
	 * @return {@link #reference}
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * 
	 * @param reference
	 *            {@link #reference}
	 */
	public void setReference(String reference) {
		this.reference = reference;
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
	 * @return {@link #descr}
	 */
	public String getDescr() {
		return descr;
	}

	/**
	 * 
	 * @param descr
	 *            {@link #descr}
	 */
	public void setDescr(String descr) {
		this.descr = descr;
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

	/**
	 * 
	 * @return {@link #date}
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * 
	 * @param date
	 *            {@link #date}
	 */
	public void setDate(Date date) {
		this.date = date;
	}

}