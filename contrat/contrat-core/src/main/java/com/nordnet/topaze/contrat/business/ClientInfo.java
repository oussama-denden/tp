package com.nordnet.topaze.contrat.business;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nordnet.topaze.contrat.domain.Contrat;

/**
 * Contient les info de client lors de la cession de {@link Contrat}.
 * 
 * @author akram-moncer
 * 
 */
public class ClientInfo {

	/**
	 * id client.
	 */
	private String idClient;

	/**
	 * address de facturation.
	 */
	private String idAdrFacturation;

	/**
	 * constructeur par defaut.
	 */
	public ClientInfo() {

	}

	@Override
	public String toString() {
		return "ClientInfo [idClient=" + idClient + ", idAdrFacturation=" + idAdrFacturation + "]";
	}

	/**
	 * 
	 * @return {@link #idClient}.
	 */
	public String getIdClient() {
		return idClient;
	}

	/**
	 * 
	 * @param idClient
	 *            {@link #idClient}.
	 */
	public void setIdClient(String idClient) {
		this.idClient = idClient;
	}

	/**
	 * 
	 * @return {@link #idAdrFacturation}.
	 */
	public String getIdAdrFacturation() {
		return idAdrFacturation;
	}

	/**
	 * 
	 * @param idAdrFacturation
	 *            {@link #idAdrFacturation}.
	 */
	public void setIdAdrFacturation(String idAdrFacturation) {
		this.idAdrFacturation = idAdrFacturation;
	}

	/**
	 * convertir json en {@link ClientInfo}.
	 * 
	 * @param json
	 *            json.
	 * @return {@link ClientInfo}.
	 * @throws IOException
	 *             {@link IOException}.
	 */
	public static ClientInfo fromJsonToClientInfo(String json) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.readValue(json, ClientInfo.class);
	}

	/**
	 * convertir un {@link ClientInfo} en un Json.
	 * 
	 * @return json.
	 * @throws JsonProcessingException
	 *             {@link JsonProcessingException}.
	 */
	public String toJson() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(this);
	}

}
