package com.nordnet.topaze.livraison.outil.mock;

import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;

import nordnet.java.util.Datas;
import nordnet.java.util.HandleType;
import nordnet.java.util.Reasons;

import com.nordnet.backoffice.ws.ArrayOfString;
import com.nordnet.backoffice.ws.CustomerException;
import com.nordnet.backoffice.ws.NetCustomer;
import com.nordnet.backoffice.ws.types.ArrayOfLog;
import com.nordnet.backoffice.ws.types.Cursor;
import com.nordnet.backoffice.ws.types.customer.ArrayOfHandle;
import com.nordnet.backoffice.ws.types.customer.Customer;
import com.nordnet.backoffice.ws.types.customer.CustomerLight;
import com.nordnet.backoffice.ws.types.customer.Handle;
import com.nordnet.backoffice.ws.types.customer.Identifier;
import com.nordnet.backoffice.ws.types.customer.LENInformation;
import com.nordnet.backoffice.ws.types.customer.LenInfo;
import com.nordnet.backoffice.ws.types.customer.MapResultCustomer;
import com.nordnet.backoffice.ws.types.customer.MapResultProductSumUp;
import com.nordnet.backoffice.ws.types.customer.NPAI;
import com.nordnet.backoffice.ws.types.order.ArrayOfInvoice;
import com.nordnet.backoffice.ws.types.order.Iban;
import com.nordnet.backoffice.ws.types.product.ArrayOfProductTypeNumber;
import com.nordnet.common.ws.types.UserInfos;

/**
 * NetCustomer mock.
 * 
 * @author Oussama Denden
 * 
 */
public class NetCustomerMock implements NetCustomer {

	/**
	 * NetCustomer mock instance.
	 */
	private static NetCustomerMock instance;

	/**
	 * constructeur par default.
	 */
	private NetCustomerMock() {

	}

	/**
	 * @return NetCustomer mock instance.
	 */
	public static NetCustomerMock getInstance() {
		if (instance == null) {
			instance = new NetCustomerMock();
		}
		return instance;
	}

	@Override
	public Customer getByKey(String key) throws CustomerException {
		Customer customer = new Customer();
		JAXBElement<String> customerKeyElement = new JAXBElement<>(new QName("customerKey"), String.class, key);
		customer.setCustomerKey(customerKeyElement);
		ArrayOfString handleKeys = new ArrayOfString();
		handleKeys.getString().add("handle-1");
		handleKeys.getString().add("handle-2");
		JAXBElement<ArrayOfString> handleKeysElement =
				new JAXBElement<>(new QName("handleKeys"), ArrayOfString.class, handleKeys);
		customer.setHandleKeys(handleKeysElement);
		return customer;
	}

	@Override
	public Handle getHandleByKey(String handleKey) throws CustomerException {
		Handle handle = new Handle();
		JAXBElement<String> handleKeyElement = new JAXBElement<>(new QName("handleKey"), String.class, "handleKey");
		handle.setHandleKey(handleKeyElement);
		JAXBElement<String> address1Element = new JAXBElement<>(new QName("address1"), String.class, "address 1");
		handle.setAddress1(address1Element);
		JAXBElement<String> address2Element = new JAXBElement<>(new QName("address2"), String.class, "address 2");
		handle.setAddress2(address2Element);
		JAXBElement<String> cityElement = new JAXBElement<>(new QName("city"), String.class, "city");
		handle.setCity(cityElement);
		JAXBElement<String> companyElement = new JAXBElement<>(new QName("company"), String.class, "company");
		handle.setCompany(companyElement);
		JAXBElement<String> countryElement = new JAXBElement<>(new QName("country"), String.class, "country");
		handle.setCountry(countryElement);
		JAXBElement<String> firstNameElement = new JAXBElement<>(new QName("firstName"), String.class, "firstName");
		handle.setFirstName(firstNameElement);
		JAXBElement<String> nameElement = new JAXBElement<>(new QName("name"), String.class, "name");
		handle.setName(nameElement);
		JAXBElement<String> zipCodeElement = new JAXBElement<>(new QName("zipCode"), String.class, "2240");
		handle.setZipCode(zipCodeElement);

		return handle;
	}

	@Override
	public void addComment(String arg0, String arg1, UserInfos arg2) throws CustomerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void addIBANRecorder(Iban arg0, String arg1, String arg2, UserInfos arg3) throws CustomerException {
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean checkPassword(String arg0, String arg1) throws CustomerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Customer create(Customer arg0, UserInfos arg1) throws CustomerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Customer createByLogin(CustomerLight arg0, UserInfos arg1) throws CustomerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void errorUpdateEmail(String arg0, Reasons arg1, String arg2, String arg3, String arg4, String arg5,
			String arg6, UserInfos arg7) throws CustomerException {
		// TODO Auto-generated method stub

	}

	@Override
	public Identifier getIdentifiers(String arg0) throws CustomerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LENInformation getLENInformation(String arg0) throws CustomerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayOfProductTypeNumber getProductListByCustomerKey(String arg0, boolean arg1, UserInfos arg2)
			throws CustomerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean isLoginAlreadyExist(String arg0) throws CustomerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayOfHandle listHandles(String arg0, HandleType arg1) throws CustomerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayOfInvoice listInvoices(String arg0) throws CustomerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayOfLog listLogs(String arg0, String arg1) throws CustomerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public NPAI processNPAILetter(String arg0, String arg1, Datas arg2, UserInfos arg3) throws CustomerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void recoverIdentifiers(String arg0, String arg1, UserInfos arg2) throws CustomerException {
		// TODO Auto-generated method stub

	}

	@Override
	public MapResultCustomer search(String arg0, String arg1, Cursor arg2) throws CustomerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MapResultProductSumUp searchListProductsByProductKey(String arg0, boolean arg1, String arg2, Cursor arg3,
			UserInfos arg4) throws CustomerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void sendActivationKeyByMail(String arg0, UserInfos arg1) throws CustomerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLENInformation(String arg0, LenInfo arg1, UserInfos arg2) throws CustomerException {
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean setMarguerite(String arg0, UserInfos arg1) throws CustomerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateCustomerType(String arg0, String arg1, UserInfos arg2) throws CustomerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateHandle(Handle arg0, UserInfos arg1) throws CustomerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateLoginAndPassword(String arg0, String arg1, String arg2, boolean arg3, UserInfos arg4)
			throws CustomerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updatePassword(String arg0, String arg1, boolean arg2, UserInfos arg3) throws CustomerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateRateBand(String arg0, String arg1, UserInfos arg2) throws CustomerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateSAARICode(String arg0, String arg1, UserInfos arg2) throws CustomerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateMailbyActivationKey(String arg0, UserInfos arg1) throws CustomerException {
		// TODO Auto-generated method stub

	}

	@Override
	public void validateSMSbyActivationKey(String arg0, String arg1, UserInfos arg2) throws CustomerException {
		// TODO Auto-generated method stub

	}
}