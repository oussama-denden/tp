package com.nordnet.topaze.communication.mock;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import nordnet.client.ws.netequipment.WebServiceNetEquipmentMethods;
import nordnet.client.ws.netequipment.faults.NotFoundFault;
import nordnet.client.ws.netequipment.faults.NotRespectedRulesFault;
import nordnet.client.ws.netequipment.faults.NullFault;
import nordnet.client.ws.netequipment.faults.ServiceFault;
import nordnet.client.ws.netequipment.requests.REquipment;
import nordnet.client.ws.netequipment.requests.RReturnAuthorization;
import nordnet.client.ws.netequipment.requests.RShippingInformation;
import nordnet.client.ws.netequipment.types.TCustomerInformation;
import nordnet.client.ws.netequipment.types.TEquipment;
import nordnet.client.ws.netequipment.types.TEquipmentReturnCause;
import nordnet.client.ws.netequipment.types.TEquipmentSpecificity;
import nordnet.client.ws.netequipment.types.TEquipmentType;
import nordnet.client.ws.netequipment.types.TEquipmentTypeProperty;
import nordnet.client.ws.netequipment.types.TEquipmentTypeSpecificity;
import nordnet.client.ws.netequipment.types.TFailedEquipmentShipping;
import nordnet.client.ws.netequipment.types.TReturnAuthorization;
import nordnet.client.ws.netequipment.types.TReturnCause;
import nordnet.client.ws.netequipment.types.TShippingEquipmentData;
import nordnet.client.ws.netequipment.types.TShippingInformation;

import org.apache.log4j.Logger;

public class NetEquipmentMock implements WebServiceNetEquipmentMethods {

	/**
	 * default logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(NetEquipmentMock.class.getName());

	/**
	 * les equipements instance seront stockés dans le database.
	 */
	private static Map<String, List<TEquipment>> databaseEquipementClient = new HashMap<String, List<TEquipment>>();

	/**
	 * constructeur par default.
	 */
	public NetEquipmentMock() {

	}

	/**
	 * recuperer la liste des equippements par rapport a l'id du client
	 * 
	 */
	@Override
	public nordnet.client.ws.netequipment.types.TEquipment[] searchEquipments(
			nordnet.client.ws.netequipment.requests.REquipment requestEquipment,
			nordnet.client.ws.netequipment.requests.RReturnAuthorization requestReturnAuthorization,
			java.lang.Integer nbResult) {

		LOGGER.info("Debut  methode searchEquipments");

		// recuperer la liste des equipements en se basant sur l'id du client
		List<TEquipment> equipmentList = databaseEquipementClient.get(requestEquipment.getCustomerId());
		TEquipment[] equipmentArray = new TEquipment[nbResult];
		if (equipmentList == null) {
			equipmentList = new ArrayList<TEquipment>();
			TEquipment equipment;
			TShippingInformation shippingInformation;
			for (int i = 0; i <= nbResult; i++) {
				shippingInformation = new TShippingInformation();
				shippingInformation.setSubscriptionId(requestEquipment.getSubscriptionId());
				shippingInformation.setCustomerId(requestEquipment.getCustomerId());
				TShippingInformation[] shippingInformationList = new TShippingInformation[]{ shippingInformation };
				equipment = new TEquipment();
				equipment.setSerialNumber(UUID.randomUUID().toString().substring(0, 8));
				TEquipmentType equipmentType = new TEquipmentType();
				equipmentType.setProductRef(requestEquipment.getSubscriptionId() + i);
				equipment.setEquipmentType(equipmentType);
				Calendar cl = Calendar.getInstance();
				cl.setTime(new Date());
				equipment.setCreationDate(cl);
				equipment.setShippingInformations(shippingInformationList);
				equipmentList.add(equipment);
			}
		}
		return equipmentList.toArray(equipmentArray);

	}

	@Override
	public void changeEquipmentReturnCause(Long equipmentReturnCauseId, Long equipmentId, Boolean deleteOldEquipment)
			throws RemoteException, ServiceFault, NotFoundFault, NotRespectedRulesFault, NullFault {
		// TODO Auto-generated method stub

	}

	@Override
	public void changeEquipment(TEquipment equipment)
			throws RemoteException, ServiceFault, NotFoundFault, NotRespectedRulesFault, NullFault {
		// TODO Auto-generated method stub

	}

	@Override
	public void changeEquipmentStatus(Long equipmentId, String equipmentStatus, String equipmentCommercialStatus,
			String comments, String commercialComments)
			throws RemoteException, ServiceFault, NotFoundFault, NotRespectedRulesFault, NullFault {
		// TODO Auto-generated method stub

	}

	@Override
	public void changeEquipmentSpecificityValue(Long equipmentSpecificityId, String value)
			throws RemoteException, ServiceFault, NotFoundFault, NullFault {
		// TODO Auto-generated method stub

	}

	@Override
	public void changeReturnCause(Long returnCauseId, String label, Boolean active)
			throws RemoteException, ServiceFault, NotFoundFault, NotRespectedRulesFault, NullFault {
		// TODO Auto-generated method stub

	}

	@Override
	public void changeReturnAuthorization(TReturnAuthorization returnAuthorization)
			throws RemoteException, ServiceFault, NotFoundFault, NullFault {
		// TODO Auto-generated method stub

	}

	@Override
	public void changeReturnAuthorizationStatus(Long returnAuthorizationId, String returnAuthorizationStatus,
			String comments) throws RemoteException, ServiceFault, NotFoundFault, NullFault, NotRespectedRulesFault {
		// TODO Auto-generated method stub

	}

	@Override
	public void changeEquipmentType(Long equipmentTypeId, String tradeProcess, String label, Float billing)
			throws RemoteException, ServiceFault, NotFoundFault, NotRespectedRulesFault, NullFault {
		// TODO Auto-generated method stub

	}

	@Override
	public void changeEquipmentTypeProperties(TEquipmentTypeProperty[] equipmentTypeProperties)
			throws RemoteException, ServiceFault, NotFoundFault, NotRespectedRulesFault, NullFault {
		// TODO Auto-generated method stub

	}

	@Override
	public TReturnCause[] createReturnCauses(TReturnCause[] returnCauses, Long[] equipmentTypeIds)
			throws RemoteException, ServiceFault, NotFoundFault, NotRespectedRulesFault, NullFault {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TEquipmentTypeProperty[] createEquipmentTypeProperties(TEquipmentTypeProperty[] equipmentTypeProperties,
			Long equipmentTypeId)
			throws RemoteException, ServiceFault, NotFoundFault, NotRespectedRulesFault, NullFault {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TEquipmentTypeSpecificity createEquipmentTypeSpecificity(Long equipmentTypeId, String name)
			throws RemoteException, ServiceFault, NotFoundFault, NullFault {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TEquipmentType createEquipmentType(TEquipmentType equipmentType)
			throws RemoteException, ServiceFault, NotFoundFault, NotRespectedRulesFault, NullFault {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TEquipment createEquipment(TEquipment equipment)
			throws RemoteException, ServiceFault, NotFoundFault, NotRespectedRulesFault, NullFault {

		LOGGER.info("debut methode createEquipment");

		String idClient = equipment.getShippingInformations()[0].getCustomerId();
		List<TEquipment> tEquipments = databaseEquipementClient.get(idClient);

		if (tEquipments != null) {
			tEquipments.add(equipment);

		} else {

			tEquipments = new ArrayList<TEquipment>();
			tEquipments.add(equipment);
		}
		databaseEquipementClient.put(idClient, tEquipments);
		return equipment;
	}

	@Override
	public TEquipmentReturnCause createEquipmentReturnCause(TEquipmentReturnCause equipmentReturnCause)
			throws RemoteException, ServiceFault, NotFoundFault, NullFault {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TReturnAuthorization createReturnAuthorization(TReturnAuthorization returnAuthorization,
			TEquipment[] equipments, Boolean generatePDF)
			throws RemoteException, ServiceFault, NotFoundFault, NotRespectedRulesFault, NullFault {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TReturnAuthorization reloadWaitingSendingReturnAuthorization(Long returnAuthorizationId)
			throws RemoteException, ServiceFault, NotFoundFault, NotRespectedRulesFault, NullFault {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteEquipmentReturnCause(Long equipmentReturnCauseId)
			throws RemoteException, ServiceFault, NotFoundFault, NullFault {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteEquipmentTypeProperties(Long[] equipmentTypePropertiesId)
			throws RemoteException, ServiceFault, NotFoundFault, NullFault {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteEquipmentTypeSpecificity(Long equipmentTypeSpecificityId)
			throws RemoteException, ServiceFault, NotFoundFault, NullFault {
		// TODO Auto-generated method stub

	}

	@Override
	public TCustomerInformation getCustomerInformationByReturnAuthorizationId(Long returnAuthorizationId)
			throws RemoteException, ServiceFault, NotFoundFault, NullFault {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TEquipment[] getEquipmentsBySerialNumber(String serialNumber)
			throws RemoteException, ServiceFault, NullFault {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getVersion() throws RemoteException, ServiceFault {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TEquipment getEquipment(Long equipmentId) throws RemoteException, ServiceFault, NotFoundFault, NullFault {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TFailedEquipmentShipping getFailedEquipmentShipping(Long failedEquipmentShippingId)
			throws RemoteException, ServiceFault, NotFoundFault, NullFault {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TReturnAuthorization[] getReturnAuthorizationsForEquipment(Long equipmentId)
			throws RemoteException, ServiceFault, NotFoundFault, NullFault {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TReturnAuthorization getReturnAuthorization(Long returnAuthorizationId)
			throws RemoteException, ServiceFault, NotFoundFault, NullFault {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TEquipmentSpecificity[] getEquipmentSpecificities(Long equipmentId)
			throws RemoteException, ServiceFault, NotFoundFault, NullFault {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TEquipmentType getEquipmentType(Long equipmentTypeId)
			throws RemoteException, ServiceFault, NotFoundFault, NullFault {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TEquipmentType getEquipmentTypeByProductRef(String productRef)
			throws RemoteException, ServiceFault, NotFoundFault, NullFault {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TEquipment[] getEquipmentsBySpecificity(Long equipmentTypeSpecificityId, String specificityValue)
			throws RemoteException, ServiceFault, NotFoundFault, NullFault {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TEquipmentType[] getEquipmentTypes() throws RemoteException, ServiceFault {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getTradeProcesses() throws RemoteException, ServiceFault {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getEquipmentStatuses() throws RemoteException, ServiceFault {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getReturnAuthorizationCauses() throws RemoteException, ServiceFault {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getReturnAuthorizationStatuses() throws RemoteException, ServiceFault {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void receiveEquipments(Long[] equipmentsId, Long returnAuthorizationId, String comments)
			throws RemoteException, ServiceFault, NotFoundFault, NotRespectedRulesFault, NullFault {
		// TODO Auto-generated method stub

	}

	@Override
	public void resolveFailedEquipmentShipping(Long failedEquipmentShippingId)
			throws RemoteException, ServiceFault, NotFoundFault, NotRespectedRulesFault, NullFault {
		// TODO Auto-generated method stub

	}

	@Override
	public TEquipmentType[] searchEquipmentTypes(String type, Boolean wildcard, Integer nbResult)
			throws RemoteException, ServiceFault, NullFault {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TReturnAuthorization[] searchReturnAuthorizations(REquipment requestEquipment,
			RReturnAuthorization requestReturnAuthorization, Integer nbResult)
			throws RemoteException, ServiceFault, NotFoundFault, NullFault {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TShippingInformation[] searchShippingInformations(RShippingInformation requestShippingInformation,
			Integer nbResult) throws RemoteException, ServiceFault, NotFoundFault, NullFault {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendReturnAuthorization(Long returnAuthorizationId, String[] sendTypes,
			TCustomerInformation customerInformation)
			throws RemoteException, ServiceFault, NotFoundFault, NullFault, NotRespectedRulesFault {
		// TODO Auto-generated method stub

	}

	@Override
	public byte[] retrieveReturnAuthorization(Long returnAuthorizationId)
			throws RemoteException, ServiceFault, NotFoundFault, NullFault, NotRespectedRulesFault {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TShippingInformation shipEquipments(TShippingInformation shippingInformation, REquipment[] requestEquipments)
			throws RemoteException, ServiceFault, NotFoundFault, NullFault, NotRespectedRulesFault {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void throwException(String exceptionName)
			throws RemoteException, ServiceFault, NotFoundFault, NotRespectedRulesFault, NullFault {
		// TODO Auto-generated method stub

	}

	@Override
	public void timeOut(Integer minTimeOutSec) throws RemoteException, ServiceFault {
		// TODO Auto-generated method stub

	}

	@Override
	public void createShipEquipments(TShippingEquipmentData[] shippingData)
			throws RemoteException, ServiceFault, NotFoundFault, NullFault, NotRespectedRulesFault {
		// TODO Auto-generated method stub

	}

	@Override
	public TShippingInformation getShippingInformation(String shippingDemandId, String customerId,
			String subscriptionId, Calendar sendDate) throws RemoteException, ServiceFault, NullFault {
		// TODO Auto-generated method stub
		return null;
	}

}
