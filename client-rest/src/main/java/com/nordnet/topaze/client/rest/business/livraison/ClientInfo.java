package com.nordnet.topaze.client.rest.business.livraison;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nordnet.topaze.client.rest.enums.ModePaiement;

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
	 * {@link ModePaiement}.
	 */
	private ModePaiement modePaiement;

	/**
	 * reference mode paiment.
	 */
	private String referenceModePaiement;

	/**
	 * constructeur par defaut.
	 */
	public ClientInfo() {

	}

	@Override
	public String toString() {
		return "ClientInfo [idClient=" + idClient + ", idAdrFacturation=" + idAdrFacturation + ", modePaiement="
				+ modePaiement + ", referenceModePaiement=" + referenceModePaiement + "]";
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
	 * 
	 * @return {@link #modePaiement}.
	 */
	public ModePaiement getModePaiement() {
		return modePaiement;
	}

	/**
	 * 
	 * @param modePaiement
	 *            {@link #modePaiement}.
	 */
	public void setModePaiement(ModePaiement modePaiement) {
		this.modePaiement = modePaiement;
	}

	/**
	 * 
	 * @return {@link #referenceModePaiement}.
	 */
	public String getReferenceModePaiement() {
		return referenceModePaiement;
	}

	/**
	 * 
	 * @param referenceModePaiement
	 *            {@link #referenceModePaiement}.
	 */
	public void setReferenceModePaiement(String referenceModePaiement) {
		this.referenceModePaiement = referenceModePaiement;
	}

	/**
	 * convert json to {@link ClientInfo}.
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

}
