package com.nordnet.topaze.communication.service;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import nordnet.client.ws.mail.NetCommMailWSImplMockService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Optional;
import com.nordnet.netcommmail.Field;
import com.nordnet.netcommmail.RecipientType;
import com.nordnet.netcommmail.schemas.NetComException;
import com.nordnet.netcommmail.schemas.NetCommMail;
import com.nordnet.netcommmail.schemas.NetCommMailWSImplService;
import com.nordnet.packager.types.packager.TPackagerInstance;
import com.nordnet.topaze.client.rest.RestClientContratCore;
import com.nordnet.topaze.client.rest.RestClientLivraison;
import com.nordnet.topaze.client.rest.business.livraison.BonPreparation;
import com.nordnet.topaze.client.rest.business.livraison.ElementLivraison;
import com.nordnet.topaze.communication.adapter.NetCustomerAdapter;
import com.nordnet.topaze.communication.adapter.PackagerAdapter;
import com.nordnet.topaze.communication.adapter.business.ClientInfo;
import com.nordnet.topaze.communication.util.Constants;
import com.nordnet.topaze.communication.util.ConstantsConnexion;
import com.nordnet.topaze.communication.util.MailObjectsFactory;
import com.nordnet.topaze.communication.util.PropertiesUtil;
import com.nordnet.topaze.exception.TopazeException;

/**
 * implementation de l'interface {@link CommunicationService}.
 * 
 * @author akram-moncer
 * 
 */
@Service("communicationService")
public class CommunicationServiceImpl implements CommunicationService {

	/**
	 * Declaration du log.
	 */
	private static final Log LOGGER = LogFactory.getLog(CommunicationServiceImpl.class);

	/**
	 * {@link NetCustomerAdapter}.
	 */
	@Autowired
	private NetCustomerAdapter netCustomerAdapter;

	/**
	 * {@link PackagerAdapter}.
	 */
	@Autowired
	private PackagerAdapter packagerAdapter;

	/**
	 * {@link RestClientLivraison}.
	 */
	@Autowired
	private RestClientLivraison restClientLivraison;

	/**
	 * {@link RestClientContratCore}.
	 */
	@Autowired
	private RestClientContratCore restClientContratCore;

	/**
	 * {@link NetCommMail}.
	 */
	private NetCommMail netCommMail;

	/**
	 * properties contenant les nom des template email du netCommunication.
	 */
	@Autowired
	private Properties templateProperties;

	/**
	 * constructeur par defaut.
	 */
	public CommunicationServiceImpl() {
		try {
			netCommMail = getService();
		} catch (MalformedURLException e) {
			LOGGER.error("Erreur lors de l'instanciation du client NetCommunication", e);
		}
	}

	@Override
	public void envoyerMail(String referenceContrat) throws TopazeException {
		try {
			BonPreparation bonPreparation = restClientLivraison.getBonPreparation(referenceContrat);
			for (ElementLivraison elementLivraison : bonPreparation.getElementLivraisons()) {

				if (Optional.fromNullable(elementLivraison.getRetailerPackagerId()).isPresent()) {

					// appel du netcustomer
					ClientInfo clientInfo = netCustomerAdapter.getHandleByKey(elementLivraison.getAddresseLivraison());

					MailObjectsFactory objectsFactory = new MailObjectsFactory();

					List<Field> fields = new ArrayList<Field>();

					// creation des fields.
					SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
					Field dateJour =
							objectsFactory.createField("DATE_JOUR",
									simpleDateFormat.format(PropertiesUtil.getInstance().getDateDuJour().toDate()));
					fields.add(dateJour);

					Field adresseEmail = objectsFactory.createField("DEST", clientInfo.getAdresseEmail());
					fields.add(adresseEmail);

					Field customerRef = objectsFactory.createField("CUSTOMER_REF", bonPreparation.getIdClient());
					fields.add(customerRef);

					Field productRef = objectsFactory.createField("PRODUCT_REF", referenceContrat);
					fields.add(productRef);

					Field name =
							objectsFactory
									.createField("CIV_PRENOM_NOM", clientInfo.getCivilite() + " " + clientInfo.getNom()
											+ " " + clientInfo.getPrenom());
					fields.add(name);

					Field nomPack =
							objectsFactory.createField("NOMPACK",
									restClientContratCore.getContratByReference(bonPreparation.getReference())
											.getTitre());
					fields.add(nomPack);

					// appel packager
					String licenseKeyString = null;

					try {

						TPackagerInstance packagerInstance = packagerAdapter.getPackagerInstance(elementLivraison);
						licenseKeyString =
								packagerInstance.getProducts().getProduct().get(Constants.ZERO).getProviderProductId();
						Field licenseKey = objectsFactory.createField("LICENSE_KEY", licenseKeyString);
						fields.add(licenseKey);

						netCommMail.sendMail(templateProperties.getProperty(elementLivraison.getReferenceProduit()),
								bonPreparation.getIdClient(), RecipientType.CUSTOMER,
								objectsFactory.createDynamicFields(fields), null,
								objectsFactory.createUserInfos("TOPAZE", null, InetAddress.getLocalHost().toString()));

						LOGGER.info("Envoie d'email pour informer le client de la livraison du contrat de reference "
								+ referenceContrat);

					} catch (TopazeException e) {
						LOGGER.error(e);
					} catch (NetComException e) {
						LOGGER.error("Error lors de l'appel vers netCommunication", e);
						throw new TopazeException("Error lors de l'appel vers netCommunication", e);
					}
				}

			}
		} catch (UnknownHostException e) {
			LOGGER.error("Error lors de la recuperation de l'adresse ip", e);
			throw new TopazeException("Error lors de la recuperation de l'adresse ip", e);
		}
	}

	/**
	 * create {@link NetCommMail} service.
	 * 
	 * @return {@link NetCommMail}.
	 * @throws MalformedURLException
	 *             {@link MalformedURLException}.
	 */
	private NetCommMail getService() throws MalformedURLException {
		if (ConstantsConnexion.USE_NETCOMM_MOCK) {
			return new NetCommMailWSImplMockService().getNetCommMailPort();
		} else {
			URL wsdlLocation = new URL(ConstantsConnexion.NETCOMM_URL);
			NetCommMailWSImplService commMailWSImplService = new NetCommMailWSImplService(wsdlLocation);
			return commMailWSImplService.getNetCommMailPort();
		}
	}
}
