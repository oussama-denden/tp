package com.nordnet.topaze.businessprocess.facture.mock;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.nordnet.topaze.businessprocess.facture.util.Constants;

/**
 * enregister les mvts dans la base de donnees.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
@Component("saveMouvements")
public class SaveMouvements {

	/**
	 * default logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(SaveMouvements.class);

	/**
	 * connection jdbc.
	 */
	private Connection connect;

	/**
	 * ajouter un mouvement.
	 * 
	 * @param referenceContrat
	 *            reference du contrat.
	 * @param idClient
	 *            id du client.
	 * @param numeroCommande
	 *            numero du commande.
	 * @param productid
	 *            id du produit.
	 * @param valeur
	 *            valeur de mouvement.
	 * @param discount
	 *            reduction.
	 */
	public void ajouterMouvement(String referenceContrat, String idClient, String numeroCommande, String productid,
			Double valeur, Double discount) {
		String sql = null;
		try (Statement stmt =
				DriverManager.getConnection(System.getProperty("DbUrl"), "test", "test").createStatement()) {

			LOGGER.info("create statement pour inserer des mouvement lies aux contrat " + referenceContrat);

			sql =
					"INSERT INTO movement (`amount`,`discount`,`productReferenceId`,`accountId`,`billinggroup`,`productid`) VALUES('"
							+ valeur + "','" + (discount == null ? Constants.ZERO : discount) + "','"
							+ referenceContrat + "','" + idClient + "','" + numeroCommande + "','" + productid + "')";

			LOGGER.info("preparation du requette sql pour inserer les mouvement lies au contrat " + referenceContrat);

			stmt.execute(sql);

			LOGGER.info(" fin execution du requette sql  ");

		} catch (Exception e) {

			LOGGER.error("erreur peandant l'ajout du mouvement " + e.getMessage());

		}
	}

	/**
	 * @return {@link #connect}.
	 */
	@PostConstruct
	public Connection createConnection() {

		String sql = null;
		if (System.getProperty("ws.saphir.useMock").equals("true")) {

			try {
				Class.forName("com.mysql.jdbc.Driver");
				Statement stmt =
						DriverManager.getConnection(System.getProperty("DbUrl"), "test", "test").createStatement();
				sql =
						"CREATE TABLE IF NOT EXISTS movement (`id` int(11) AUTO_INCREMENT,`amount` double,`discount` double,`productReferenceId` varchar(255),`accountId` varchar(255),`billinggroup` varchar(255),`productid` varchar(255) , PRIMARY KEY (`id`) );";

				LOGGER.info("preparation du requette sql  ");

				stmt.execute(sql);

				LOGGER.info(" fin execution du requette sql  ");

			} catch (Exception e) {
				LOGGER.error("error connection " + e.getMessage());

			}

		}
		return connect;

	}

	/**
	 * 
	 * @return {@link #connect}.
	 */
	public Connection getConnect() {
		return connect;
	}

	/**
	 * 
	 * @param connect
	 *            {@link #connect}
	 */
	public void setConnect(Connection connect) {
		this.connect = connect;
	}

}
