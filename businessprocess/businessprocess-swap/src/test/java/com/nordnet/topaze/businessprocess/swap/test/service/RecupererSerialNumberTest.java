package com.nordnet.topaze.businessprocess.swap.test.service;

import static org.junit.Assert.assertEquals;

import java.rmi.RemoteException;
import java.util.Calendar;

import nordnet.client.ws.netequipment.faults.NotFoundFault;
import nordnet.client.ws.netequipment.faults.NotRespectedRulesFault;
import nordnet.client.ws.netequipment.faults.NullFault;
import nordnet.client.ws.netequipment.faults.ServiceFault;
import nordnet.client.ws.netequipment.types.TEquipment;
import nordnet.client.ws.netequipment.types.TShippingInformation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.nordnet.topaze.businessprocess.swap.mock.NetEquipmentMock;
import com.nordnet.topaze.businessprocess.swap.service.SwapService;
import com.nordnet.topaze.businessprocess.swap.service.SwapServiceImpl;
import com.nordnet.topaze.businessprocess.swap.test.GlobalTestCase;
import com.nordnet.topaze.client.rest.business.livraison.BonPreparation;
import com.nordnet.topaze.client.rest.business.livraison.ElementLivraison;
import com.nordnet.topaze.exception.TopazeException;

/**
 * 
 * This class tests {@link SwapServiceImpl#recupererSerialNumber(ElementLivraison)}.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
public class RecupererSerialNumberTest extends GlobalTestCase {

	/**
	 * The log of the class.
	 */
	@SuppressWarnings("unused")
	private static final Log LOGGER = LogFactory.getLog(RecupererSerialNumberTest.class);

	/**
	 * un instance de classe mock pour simuler les testes.
	 */
	private NetEquipmentMock netEquipmentMock;

	/**
	 * The service used for tests.
	 */
	@Autowired
	private SwapService swapService;

	/**
	 * cette methode teste le cas ou on a un seul equipement pour un client donné.
	 * 
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 * @throws ServiceFault
	 *             {@link ServiceFault}.
	 * @throws NotFoundFault
	 *             {@link NotFoundFault}.
	 * @throws NotRespectedRulesFault
	 *             {@link NotRespectedRulesFault}.
	 * @throws NullFault
	 *             {@link NullFault}.
	 * @throws RemoteException
	 *             {@link RemoteException}.
	 */
	@Test
	public void recupererSerialNumberPourSeulEquipement()
			throws TopazeException, ServiceFault, NotFoundFault, NotRespectedRulesFault, NullFault, RemoteException {
		ElementLivraison elementLivraison = new ElementLivraison();
		BonPreparation bonPreparation = new BonPreparation();
		bonPreparation.setIdClient("Client1");
		elementLivraison.setBonPreparationParent(bonPreparation);
		elementLivraison.getBonPreparationParent().setReference("refProd1");
		getNetEquipement(true, false, false);
		assertEquals("000111", swapService.recupererSerialNumber(elementLivraison.getBonPreparationParent()
				.getIdClient(), elementLivraison.getBonPreparationParent().getReference()));

	}

	/**
	 * cette methode teste le cas ou on a plusieurs equipements pour un client donné qui ont la meme SubscriptionId don
	 * on doit passer filtrage par date.
	 * 
	 * @throws TopazeException
	 * @throws RemoteException
	 * @throws NullFault
	 * @throws NotRespectedRulesFault
	 * @throws NotFoundFault
	 * @throws ServiceFault
	 * 
	 */
	@Test
	public void recupererSerialNumberParFiltrageDate()
			throws TopazeException, ServiceFault, NotFoundFault, NotRespectedRulesFault, NullFault, RemoteException {
		ElementLivraison elementLivraison = new ElementLivraison();
		BonPreparation bonPreparation = new BonPreparation();
		bonPreparation.setIdClient("Client1");
		elementLivraison.setBonPreparationParent(bonPreparation);
		elementLivraison.getBonPreparationParent().setReference("refProd2");
		getNetEquipement(false, false, true);
		assertEquals("000333", swapService.recupererSerialNumber(elementLivraison.getBonPreparationParent()
				.getIdClient(), elementLivraison.getBonPreparationParent().getReference()));

	}

	/**
	 * cette methode est conçus our simuler un comportement reele en ajoutant des données dans la database de
	 * netEquipement mock!!
	 * 
	 * @param onePerClient
	 * @param multiplePerCLient
	 * @param multiplePerSubscriptionId
	 * @return
	 * @throws RemoteException
	 * @throws NullFault
	 * @throws NotRespectedRulesFault
	 * @throws NotFoundFault
	 * @throws ServiceFault
	 */
	private NetEquipmentMock getNetEquipement(boolean onePerClient, boolean multiplePerCLient,
			boolean multiplePerSubscriptionId)
			throws ServiceFault, NotFoundFault, NotRespectedRulesFault, NullFault, RemoteException {
		netEquipmentMock = new NetEquipmentMock();

		TShippingInformation tShippingInformation1 = new TShippingInformation();
		tShippingInformation1.setSubscriptionId("refProd1");
		tShippingInformation1.setCustomerId("Client1");

		TShippingInformation[] tShippingInformationList1 = new TShippingInformation[1];
		tShippingInformationList1[0] = tShippingInformation1;

		TShippingInformation tShippingInformation2 = new TShippingInformation();
		tShippingInformation2.setSubscriptionId("refProd2");
		tShippingInformation2.setCustomerId("Client1");

		TShippingInformation[] tShippingInformationList2 = new TShippingInformation[1];
		tShippingInformationList2[0] = tShippingInformation2;

		TShippingInformation tShippingInformation3 = new TShippingInformation();
		tShippingInformation3.setSubscriptionId("refProd2");
		tShippingInformation3.setCustomerId("Client1");

		TShippingInformation[] tShippingInformationList3 = new TShippingInformation[1];
		tShippingInformationList3[0] = tShippingInformation3;

		TEquipment tEquipment1 = new TEquipment();
		tEquipment1.setSerialNumber("000111");
		tEquipment1.setCreationDate(Calendar.getInstance());
		tEquipment1.setShippingInformations(tShippingInformationList1);

		TEquipment tEquipment2 = new TEquipment();
		tEquipment2.setSerialNumber("000222");
		Calendar c = Calendar.getInstance();
		c.add(Calendar.MONTH, 1);
		tEquipment2.setCreationDate(c);
		tEquipment2.setShippingInformations(tShippingInformationList2);

		TEquipment tEquipment3 = new TEquipment();
		tEquipment3.setSerialNumber("000333");
		Calendar cl = Calendar.getInstance();
		cl.add(Calendar.MONTH, 2);
		tEquipment3.setCreationDate(cl);
		tEquipment3.setShippingInformations(tShippingInformationList3);

		if (onePerClient) {
			netEquipmentMock.createEquipment(tEquipment1);
		}

		if (multiplePerCLient) {
			netEquipmentMock.createEquipment(tEquipment1);
			netEquipmentMock.createEquipment(tEquipment2);
		}

		if (multiplePerSubscriptionId) {
			netEquipmentMock.createEquipment(tEquipment1);
			netEquipmentMock.createEquipment(tEquipment2);
			netEquipmentMock.createEquipment(tEquipment3);
		}

		return netEquipmentMock;
	}

}
