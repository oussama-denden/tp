package com.nordnet.topaze.businessprocess.packager.jms;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.nordnet.adminpackager.ConverterException;
import com.nordnet.adminpackager.DriverException;
import com.nordnet.adminpackager.NotFoundException;
import com.nordnet.adminpackager.NullException;
import com.nordnet.adminpackager.PackagerException;
import com.nordnet.topaze.businessprocess.packager.service.PackagerService;
import com.nordnet.topaze.exception.TopazeException;

/**
 * listener de l'evenement 'PrepareServiceRenewal' pour effectuer le renouvellement d'un service.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class PrepareServiceRenewalListener implements MessageListener {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(PrepareServiceRenewalListener.class);

	/**
	 * {@link PackagerService}.
	 */
	@Autowired
	private PackagerService packagerService;

	@Override
	public void onMessage(Message message) {
		LOGGER.info("Reception de l'evenement: PrepareServiceRenewal");
		if (message instanceof MapMessage) {
			final MapMessage mapMessage = (MapMessage) message;
			try {
				// recuperation reference contrat resilier par le client de la brique contrat
				String referenceBP = mapMessage.getString("referenceBP");
				if (referenceBP != null) {
					packagerService.activerPourRenouvellement(referenceBP);
				}

			} catch (JMSException | TopazeException | NullException | NotFoundException | PackagerException
					| DriverException | ConverterException exception) {
				LOGGER.error("Error occurs during call of  PrepareServiceRenewalListener.onMessage()", exception);
			}
		}
	}
}