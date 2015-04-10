package com.nordnet.topaze.businessprocess.facture.mock;

import static com.nordnet.common.valueObject.utils.Null.checkNotNullOrEmpty;

import org.apache.log4j.Logger;

import com.nordnet.common.alert.ws.client.SendAlert;

/**
 * {@link SendAlert} mock.
 * 
 * @author Oussama Denden
 * 
 */
public class SendAlertMock extends SendAlert {

	/**
	 * default logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(SendAlertMock.class);

	/**
	 * send an alert.
	 * 
	 * @param projectId
	 *            {@link String} the projectId to set.
	 * @param alertMessage
	 *            {@link String} the alertMessage to set.
	 * @param alertDetail
	 *            {@link String} the alertDetail to set.
	 * @param throwable
	 *            {@link Throwable} the optional throwable to set.
	 */
	@Override
	public void send(final String projectId, final String alertMessage, final String alertDetail, final String throwable) {
		// check parameters
		checkNotNullOrEmpty("projectId", projectId);
		checkNotNullOrEmpty("alertMessage", alertMessage);

		// create the Alert
		LOGGER.error(alertMessage + " " + alertDetail + " " + throwable);
	}
}
