package com.nordnet.topaze.businessprocess.netdelivery.mock;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.nordnet.net_delivery.ConverterException;
import com.nordnet.net_delivery.NetDeliveryException;
import com.nordnet.net_delivery.NotFoundException;
import com.nordnet.net_delivery.NotRespectedRulesException;
import com.nordnet.net_delivery.NullException;
import com.nordnet.net_delivery.types.TArrayOfString;
import com.nordnet.net_delivery.types.TArrayOfTProperty;
import com.nordnet.net_delivery.types.TArrayOfTShippingAlert;
import com.nordnet.net_delivery.types.TArrayOfTShippingDemand;
import com.nordnet.net_delivery.types.TArrayOfTShippingPack;
import com.nordnet.net_delivery.types.TEquipment;
import com.nordnet.net_delivery.types.TProperty;
import com.nordnet.net_delivery.types.TReceiverContact;
import com.nordnet.net_delivery.types.TShippingAlertStatus;
import com.nordnet.net_delivery.types.TShippingDemand;
import com.nordnet.net_delivery.types.TShippingProperties;
import com.nordnet.net_delivery.types.TShippingStatus;
import com.nordnet.net_delivery.types.TTemplate;
import com.nordnet.topaze.businessprocess.netdelivery.util.Constants;

/**
 * NetDelivery service mock.
 * 
 * @author Denden-OUSSAMA
 * 
 */
public class NetDeliveryMock implements com.nordnet.net_delivery.NetDeliveryService {

	/**
	 * Declaration du log.
	 */
	private static final Log LOGGER = LogFactory.getLog(NetDeliveryMock.class);

	/**
	 * This attribute contains the current shipping demand id which can be used to create new shipping demand.
	 */
	private static Long currentShippingDemandId = 1L;

	/**
	 * {@link NetDeliveryMock}.
	 */
	private static NetDeliveryMock instance;

	/**
	 * contient tous les shippin demand.
	 */
	private static Map<Long, TShippingDemand> database = new HashMap<>();

	/**
	 * @return {@link #instance}.
	 */
	public static NetDeliveryMock getInstance() {
		if (instance == null) {
			instance = new NetDeliveryMock();
		}
		return instance;
	}

	/**
	 * The method {@link #generateNewShippingDemandId()} generates a new shipping demand identifier which does not exist
	 * into the database.
	 * 
	 * @return identifier.
	 * 
	 */
	private static synchronized Long generateNewShippingDemandId() {
		while (database.keySet().contains(currentShippingDemandId)) {
			currentShippingDemandId++;
		}
		return currentShippingDemandId;
	}

	@Override
	public TShippingDemand getShippingDemand(long pShippingDemandId) throws NotFoundException {
		if (database.keySet().contains(pShippingDemandId)) {
			TShippingDemand demand = database.get(pShippingDemandId);
			Calendar c = Calendar.getInstance();

			switch (Constants.EST_DELAI) {
			case 0:
				c.add(Calendar.DAY_OF_YEAR, -1 * Constants.DELAI_LIVRAISON);
				break;
			case 1:
				c.add(Calendar.DAY_OF_YEAR, Constants.DELAI_LIVRAISON);
				break;
			default:
				c.setTime(demand.getCreationDate().toGregorianCalendar().getTime());
				int isDelai = (int) (Math.random() * 2);
				if (isDelai == 0) {
					c.add(Calendar.DAY_OF_YEAR, Constants.DELAI_LIVRAISON);
				} else {
					c.add(Calendar.DAY_OF_YEAR, -1 * Constants.DELAI_LIVRAISON);
				}
				break;
			}
			if (c.getTime().before(new Date())) {
				demand.setCurrentStatus(TShippingStatus.PROBLEM);
			} else {
				demand.setCurrentStatus(TShippingStatus.DELIVERED);
			}
			return demand;
		}
		throw new NotFoundException("No shipping demand has been found id [" + pShippingDemandId + "].");

	}

	@Override
	public TShippingDemand createShippingDemand(String templateName, String customerId,
			TReceiverContact receiverContact, TArrayOfString equipments, TArrayOfString shippers,
			TArrayOfTProperty shippingProperties) throws NullException, NotFoundException {
		if (templateName == null || templateName.trim().length() == 0) {
			throw new NullException("The template name cannot be null or empty.");
		}

		if (receiverContact == null) {
			throw new NullException("The receiverContact cannot be null.");
		}

		if (equipments == null || equipments.getItems() == null || equipments.getItems().getItem().size() == 0) {
			throw new NullException("The equipments array cannot be null or empty.");
		}

		if (shippers == null) {
			throw new NullException("The shippers cannot be null.");
		}

		TShippingDemand shippingDemand = new TShippingDemand();
		shippingDemand.setId(generateNewShippingDemandId());
		GregorianCalendar gCalendar = new GregorianCalendar();
		gCalendar.setTime(new Date());
		XMLGregorianCalendar xmlCalendar;
		try {
			xmlCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gCalendar);
			shippingDemand.setCreationDate(xmlCalendar);
		} catch (DatatypeConfigurationException e) {
			LOGGER.error("exception catched during the createShippingDemand method", e);
		}
		shippingDemand.setCurrentStatus(TShippingStatus.DELIVERING);
		TShippingDemand.Properties properties = new TShippingDemand.Properties();
		properties.getProperty().addAll(shippingProperties.getProperties().getProperty());
		shippingDemand.setReceiver(receiverContact);
		shippingDemand.setCustomerId(customerId);

		TShippingDemand.Equipments shippingEquipments = new TShippingDemand.Equipments();
		for (int i = 0; i < equipments.getItems().getItem().size(); i++) {
			TEquipment newTEquipment = new TEquipment();
			newTEquipment.setEquipmentModelName(equipments.getItems().getItem().get(i));
			newTEquipment.setId(i);
			shippingEquipments.getEquipment().add(newTEquipment);
		}
		shippingDemand.setEquipments(shippingEquipments);

		TTemplate finalTemplate = new TTemplate();
		finalTemplate.setId(Long.valueOf(currentShippingDemandId));
		finalTemplate.setName(templateName);
		shippingDemand.setTemplate(finalTemplate);

		if (shippingDemand.getTemplate() == null) {
			throw new NotFoundException(
					"The template ["
							+ templateName
							+ "] has not been found (only 'valid_template1' and 'valid_template2' and 'blank-template' are valid).");
		}

		database.put(shippingDemand.getId(), shippingDemand);

		return shippingDemand;
	}

	@Override
	public void deleteShippingDemand(long pShippingDemandId)
			throws NullException, NotFoundException, NotRespectedRulesException, NetDeliveryException {
		// TODO Auto-generated method stub

	}

	@Override
	public void throwException(String throwExceptionRequest)
			throws NullException, NotFoundException, NotRespectedRulesException, NetDeliveryException,
			ConverterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void changeShippingAlert(long changeShippingAlertShippingAlertIdParamRequest,
			TShippingAlertStatus changeShippingAlertShippingAlertStatusParamRequest)
			throws NullException, NotFoundException, NotRespectedRulesException, NetDeliveryException {
		// TODO Auto-generated method stub

	}

	@Override
	public TShippingProperties getShippingProperties(String shippingKey)
			throws NullException, NotFoundException, ConverterException, NetDeliveryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TArrayOfTShippingAlert findShippingAlertsByShippingDemand(long findShippingAlertsByShippingDemandRequest)
			throws NullException, NotFoundException, ConverterException, NetDeliveryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TArrayOfTShippingDemand findByCustomerId(String findByCustomerIdRequest)
			throws NullException, NotFoundException, ConverterException, NetDeliveryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changeShippingDemand(long pShippingDemandId, TReceiverContact pReceiverContact,
			TArrayOfString pEquipments, TArrayOfString pShippers, TArrayOfTProperty pShippingProperties)
			throws NullException, NotFoundException, NotRespectedRulesException, NetDeliveryException,
			ConverterException {
		// TODO Auto-generated method stub

	}

	@Override
	public TArrayOfTShippingAlert findShippingAlertsByShippingAlertStatus(
			TShippingAlertStatus findShippingAlertsByShippingAlertStatusRequest)
			throws NullException, NotFoundException, ConverterException, NetDeliveryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TArrayOfTShippingDemand findByShippingDemandProperty(TProperty findByShippingDemandPropertyRequest)
			throws NullException, NotFoundException, ConverterException, NetDeliveryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getVersion() throws NetDeliveryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TArrayOfTShippingPack getShippingPacks(long pShippingDemandId)
			throws NullException, NotFoundException, ConverterException, NetDeliveryException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void timeOut(long minTimeOutSec) throws NetDeliveryException {
		// TODO Auto-generated method stub

	}

}
