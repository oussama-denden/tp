package com.nordnet.topaze.communication.util;

import java.util.List;

import com.nordnet.netcommmail.DynamicFields;
import com.nordnet.netcommmail.Field;
import com.nordnet.netcommmail.UserInfos;

/**
 * creation des object pour l'envoi des emails.
 * 
 * @author akram-moncer
 * 
 */
public class MailObjectsFactory {

	/**
	 * creation de {@link Field}.
	 * 
	 * @param name
	 *            nom du champs.
	 * @param value
	 *            vaeur du champs
	 * @return {@link Field}.
	 */
	public Field createField(String name, String value) {
		Field field = new Field();
		field.setName(name);
		field.setValue(value);

		return field;
	}

	/**
	 * create {@link DynamicFields}.
	 * 
	 * @param fields
	 *            liste des {@link Field}.
	 * @return {@link DynamicFields}.
	 */
	public DynamicFields createDynamicFields(List<Field> fields) {
		DynamicFields dynamicFields = new DynamicFields();
		dynamicFields.setField(fields);
		return dynamicFields;
	}

	/**
	 * create {@link UserInfos}.
	 * 
	 * @param userName
	 *            userName.
	 * @param httpUserAgent
	 *            agent http.
	 * @param ip
	 *            adresse ip.
	 * @return {@link UserInfos}.
	 */
	public UserInfos createUserInfos(String userName, String httpUserAgent, String ip) {
		UserInfos userInfos = new UserInfos();
		userInfos.setUserName(userName);
		userInfos.setHttpUserAgent(httpUserAgent);
		userInfos.setIp(ip);

		return userInfos;
	}
}
