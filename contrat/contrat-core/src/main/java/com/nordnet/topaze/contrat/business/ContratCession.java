package com.nordnet.topaze.contrat.business;

import java.util.ArrayList;
import java.util.List;

import com.nordnet.topaze.contrat.domain.Contrat;

/**
 * Contient les informations de cession d'un {@link Contrat}.
 * 
 * @author akram-moncer
 * 
 */
public class ContratCession {

	/**
	 * address de livraison.
	 */
	private String idAdrLivraison;

	/**
	 * le propriétaire courant du contrat.
	 */
	private ClientInfo clientSource;

	/**
	 * le nouveau propriétaire du contrat.
	 */
	private ClientInfo clientDestination;

	/**
	 * {@link PolitiqueCession}.
	 */
	private PolitiqueCession politiqueCession;

	/**
	 * {@link ProduitCession}.
	 */
	private List<ProduitCession> produitsCession = new ArrayList<>();

	/**
	 * user qui a effectue la cession.
	 */
	private String user;

	/**
	 * constructeur par defaut.
	 */
	public ContratCession() {

	}

	@Override
	public String toString() {
		return "ContratCession [idAdrLivraison=" + idAdrLivraison + ", clientSource=" + clientSource
				+ ", clientDestination=" + clientDestination + ", politiqueCession=" + politiqueCession + ", user="
				+ user + "]";
	}

	/**
	 * 
	 * @return {@link #idAdrLivraison}.
	 */
	public String getIdAdrLivraison() {
		return idAdrLivraison;
	}

	/**
	 * 
	 * @param idAdrLivraison
	 *            {@link #idAdrLivraison}.
	 */
	public void setIdAdrLivraison(String idAdrLivraison) {
		this.idAdrLivraison = idAdrLivraison;
	}

	/**
	 * 
	 * @return {@link #clientSource}.
	 */
	public ClientInfo getClientSource() {
		return clientSource;
	}

	/**
	 * 
	 * @param clientSource
	 *            {@link #clientSource}.
	 */
	public void setClientSource(ClientInfo clientSource) {
		this.clientSource = clientSource;
	}

	/**
	 * 
	 * @return {@link #clientDestination}.
	 */
	public ClientInfo getClientDestination() {
		return clientDestination;
	}

	/**
	 * 
	 * @param clientDestination
	 *            {@link #clientDestination}.
	 */
	public void setClientDestination(ClientInfo clientDestination) {
		this.clientDestination = clientDestination;
	}

	/**
	 * 
	 * @return {@link #politiqueCession}.
	 */
	public PolitiqueCession getPolitiqueCession() {
		return politiqueCession;
	}

	/**
	 * 
	 * @param politiqueCession
	 *            {@link #politiqueCession}.
	 */
	public void setPolitiqueCession(PolitiqueCession politiqueCession) {
		this.politiqueCession = politiqueCession;
	}

	/**
	 * 
	 * @return {@link #cessionInfos}.
	 */
	public List<ProduitCession> getProduitsCession() {
		return produitsCession;
	}

	/**
	 * 
	 * @param produitsCession
	 *            {@link #produitsCession}.
	 */
	public void setProduitsCession(List<ProduitCession> produitsCession) {
		this.produitsCession = produitsCession;
	}

	/**
	 * 
	 * @return {@link #user}.
	 */
	public String getUser() {
		return user;
	}

	/**
	 * 
	 * @param user
	 *            {@link #user}.
	 */
	public void setUser(String user) {
		this.user = user;
	}

}
