package com.nordnet.topaze.communication.mock;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.nordnet.adminpackager.ConverterException;
import com.nordnet.adminpackager.DeliveryException;
import com.nordnet.adminpackager.DriverException;
import com.nordnet.adminpackager.MalformedXMLException;
import com.nordnet.adminpackager.NotFoundException;
import com.nordnet.adminpackager.NotRespectedRulesException;
import com.nordnet.adminpackager.NullException;
import com.nordnet.adminpackager.PackagerException;
import com.nordnet.adminpackager.PackagerService;
import com.nordnet.packager.types.delivery.TArrayOfTDeliveryDemand;
import com.nordnet.packager.types.feasibility.TFeasibilityResult;
import com.nordnet.packager.types.packager.TArrayOfString;
import com.nordnet.packager.types.packager.TArrayOfTPackagerActionHistory;
import com.nordnet.packager.types.packager.TArrayOfTPackagerInstance;
import com.nordnet.packager.types.packager.TArrayOfTPackagerInstanceAction;
import com.nordnet.packager.types.packager.TArrayOfTPackagerInstanceHeader;
import com.nordnet.packager.types.packager.TArrayOfTPackagerModel;
import com.nordnet.packager.types.packager.TPackagerInstanceHeader;
import com.nordnet.packager.types.packager.TPackagerModel;
import com.nordnet.packager.types.packager.TState;
import com.nordnet.packager.types.product.TArrayOfTProductProperties;
import com.nordnet.packager.types.product.TProductModel;
import com.nordnet.packager.types.product.TProductProperties;
import com.nordnet.packager.types.request.TPackagerRequest;
import com.nordnet.packager.types.request.TPackagerTransformationRequest;
import com.nordnet.packager.types.request.TProductRequest;
import com.nordnet.topaze.communication.util.Constants;

/**
 * Packager mock.
 * 
 * @author Oussama Denden
 * 
 */
public class PackagerMock implements PackagerService, Serializable {

	/**
	 * Declaration du log.
	 */
	private static final Logger LOGGER = Logger.getLogger(PackagerMock.class);

	/**
	 * les packager instance seront stocké dans le database.
	 */
	private static Map<String, com.nordnet.packager.types.packager.TPackagerInstance> database =
			new HashMap<String, com.nordnet.packager.types.packager.TPackagerInstance>();

	/**
	 * constructeur par default.
	 */
	public PackagerMock() {

	}

	@Override
	public com.nordnet.packager.types.packager.TPackagerInstance getPackagerInstance(String getPackagerInstanceRequest)
			throws NullException, NotFoundException, PackagerException, DriverException, ConverterException {

		com.nordnet.packager.types.packager.TPackagerInstance instance =
				new com.nordnet.packager.types.packager.TPackagerInstance();
		instance.setRetailerPackagerId(getPackagerInstanceRequest);
		instance.setCurrentState(TState.ACTIVE);
		Long productId = 0l;
		com.nordnet.packager.types.packager.TPackagerInstance.Products products =
				new com.nordnet.packager.types.packager.TPackagerInstance.Products();
		com.nordnet.packager.types.product.TProductInstance productInstance =
				new com.nordnet.packager.types.product.TProductInstance();
		productInstance.setProductId(productId);
		productInstance.setProviderProductId("LICENSE-KEY");
		products.getProduct().add(productInstance);
		instance.setProducts(products);
		return instance;
	}

	@Override
	public void suspendPackager(TPackagerRequest suspendPackagerRequest)
			throws NullException, NotFoundException, PackagerException, ConverterException, MalformedXMLException,
			NotRespectedRulesException, DriverException {

	}

	@Override
	public void cancelPackager(TPackagerRequest cancelPackagerRequest)
			throws NullException, NotFoundException, PackagerException, ConverterException, MalformedXMLException,
			NotRespectedRulesException, DriverException {

	}

	@Override
	public com.nordnet.packager.types.packager.TPackagerInstance createPackager(TPackagerRequest createPackagerRequest)
			throws NullException, NotFoundException, PackagerException, DeliveryException, ConverterException,
			MalformedXMLException, NotRespectedRulesException, DriverException {

		return null;
	}

	@Override
	public TFeasibilityResult isPackagerCreationPossible(TPackagerRequest packagerRequest)
			throws NullException, NotFoundException, PackagerException, DriverException, ConverterException,
			NotRespectedRulesException {

		return null;
	}

	@Override
	public TProductModel getProductModelConfiguration(String packagerModelKey, String productModelKey)
			throws NullException, NotFoundException, PackagerException, ConverterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void translocateProductInstances(TPackagerTransformationRequest translocateProductInstancesRequest)
			throws NullException, NotFoundException, PackagerException, ConverterException, MalformedXMLException,
			NotRespectedRulesException, DriverException {
		// TODO Auto-generated method stub

	}

	@Override
	public String getVersion() throws PackagerException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TProductModel getProductModelConfigurationByPrefix(String packagerModelKey, String productModelPrefix)
			throws NullException, NotFoundException, PackagerException, ConverterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TPackagerInstanceHeader getPackagerInstanceHeader(String getPackagerInstanceHeaderRequest)
			throws NullException, NotFoundException, PackagerException, ConverterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void resetProduct(TProductRequest resetProductRequest)
			throws NullException, NotFoundException, PackagerException, ConverterException, MalformedXMLException,
			NotRespectedRulesException, DriverException {
		// TODO Auto-generated method stub

	}

	@Override
	public TFeasibilityResult isPackagerPropertiesChangePossible(
			TPackagerRequest isPackagerPropertiesChangePossibleRequest)
			throws NullException, NotFoundException, PackagerException, ConverterException, MalformedXMLException,
			NotRespectedRulesException, DriverException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void activatePackager(TPackagerRequest activatePackagerRequest)
			throws NullException, NotFoundException, PackagerException, ConverterException, MalformedXMLException,
			NotRespectedRulesException, DriverException {
		// TODO Auto-generated method stub

	}

	@Override
	public TArrayOfTProductProperties getPackagerProperties(String getPackagerPropertiesRequest)
			throws NullException, NotFoundException, PackagerException, DriverException, ConverterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void reactivatePackager(TPackagerRequest reactivatePackagerRequest)
			throws NullException, NotFoundException, PackagerException, ConverterException, MalformedXMLException,
			NotRespectedRulesException, DriverException {

	}

	@Override
	public boolean isRetailerPackagerIdFree(String isRetailerPackagerIdFreeRequest)
			throws NullException, PackagerException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void createNewDeliveryDemand(TPackagerRequest createNewDeliveryDemandRequest)
			throws NullException, NotFoundException, PackagerException, DeliveryException, DriverException,
			ConverterException, NotRespectedRulesException {
		// TODO Auto-generated method stub

	}

	@Override
	public TArrayOfTPackagerInstanceHeader searchPackagerInstancesByReference(String referenceType,
			String referenceValue, BigInteger valueMinimalSize)
			throws NullException, PackagerException, ConverterException, NotRespectedRulesException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TFeasibilityResult isProductTranslocationPossible(
			TPackagerTransformationRequest isProductTranslocationPossibleRequest)
			throws NullException, PackagerException, DriverException, ConverterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TArrayOfTPackagerModel getPackagerModels() throws PackagerException, ConverterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void timeOut(long timeOutRequest) throws PackagerException {
		// TODO Auto-generated method stub

	}

	@Override
	public TArrayOfTPackagerActionHistory getPackagerActionHistory(String retailerPackagerId)
			throws NullException, NotFoundException, PackagerException, ConverterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void changePackagerProperties(TPackagerRequest changePackagerPropertiesRequest)
			throws NullException, NotFoundException, PackagerException, ConverterException, MalformedXMLException,
			NotRespectedRulesException, DriverException {

	}

	@Override
	public TPackagerInstanceHeader updateSelfDiagnostics(String updateSelfDiagnosticsRequest)
			throws NullException, NotFoundException, PackagerException, DriverException, ConverterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TArrayOfTPackagerInstanceHeader getPackagerInstanceHeadersList(
			TArrayOfString getPackagerInstanceHeadersListRequest)
			throws NullException, NotFoundException, PackagerException, ConverterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TPackagerInstanceHeader updateReferences(String updateReferencesRequest)
			throws NullException, NotFoundException, PackagerException, DriverException, ConverterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TFeasibilityResult isMergePackagerPossible(TPackagerRequest isMergePackagerPossibleSource1,
			TPackagerRequest isMergePackagerPossibleSource2, TPackagerTransformationRequest isSplitDestination)
			throws NullException, NotFoundException, PackagerException, ConverterException, MalformedXMLException,
			NotRespectedRulesException, DriverException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void clearCache(String clearCacheRequest)
			throws NullException, NotFoundException, PackagerException, DriverException, ConverterException {
		// TODO Auto-generated method stub

	}

	@Override
	public void resetPackager(TPackagerRequest resetPackagerRequest)
			throws NullException, NotFoundException, PackagerException, ConverterException, MalformedXMLException,
			NotRespectedRulesException, DriverException {
		// TODO Auto-generated method stub

	}

	@Override
	public TProductProperties getProductProperties(long getProductPropertiesRequest)
			throws NullException, NotFoundException, PackagerException, DriverException, ConverterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public com.nordnet.packager.types.product.TProductInstance getProductInstance(long getProductInstanceRequest)
			throws NullException, NotFoundException, PackagerException, DriverException, ConverterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TArrayOfTPackagerActionHistory getPackagerActionHistoryByActions(String retailerPackagerId,
			TArrayOfTPackagerInstanceAction includeActions, TArrayOfTPackagerInstanceAction excludeActions)
			throws NullException, PackagerException, ConverterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TFeasibilityResult isPackagerTransformationPossible(
			TPackagerTransformationRequest isPackagerTransformationPossibleRequest)
			throws NullException, PackagerException, ConverterException, MalformedXMLException,
			NotRespectedRulesException, DriverException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void transformPackager(TPackagerTransformationRequest transformPackagerRequest)
			throws NullException, NotFoundException, PackagerException, ConverterException, MalformedXMLException,
			NotRespectedRulesException, DriverException {

		if (transformPackagerRequest.getProducts() != null)
			LOGGER.info(transformPackagerRequest.getProducts().getProduct().get(Constants.ZERO).getProperties());

	}

	@Override
	public TFeasibilityResult isSplitPackagerPossible(TPackagerRequest isSplitSource,
			TPackagerTransformationRequest isSplitDestination1, TPackagerTransformationRequest isSplitDestination2)
			throws NullException, NotFoundException, PackagerException, DriverException, ConverterException,
			MalformedXMLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void splitPackager(TPackagerRequest splitPackagerSource,
			TPackagerTransformationRequest splitPackagerDestination1,
			TPackagerTransformationRequest splitPackagerDestination2)
			throws NullException, NotFoundException, PackagerException, ConverterException, MalformedXMLException,
			NotRespectedRulesException, DriverException {
		// TODO Auto-generated method stub

	}

	@Override
	public com.nordnet.packager.types.packager.TPackagerInstance mergePackagers(TPackagerRequest mergePackagersSource1,
			TPackagerRequest mergePackagersSource2, TPackagerTransformationRequest mergePackagersDestination)
			throws NullException, NotFoundException, PackagerException, ConverterException, MalformedXMLException,
			NotRespectedRulesException, DriverException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TArrayOfTPackagerInstance getPackagerInstancesList(TArrayOfString getPackagerInstancesListRequest)
			throws NullException, NotFoundException, PackagerException, DriverException, ConverterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsageProductProperties(long getUsageProductPropertiesRequest)
			throws NullException, NotFoundException, PackagerException, DriverException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsageProductPropertiesWithFilter(TProductRequest getUsageProductPropertiesWithFilterRequest)
			throws NullException, NotFoundException, PackagerException, DriverException, ConverterException,
			MalformedXMLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TPackagerModel getPackagerModel(String packagerModelKey)
			throws NullException, NotFoundException, PackagerException, ConverterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TArrayOfTDeliveryDemand getDeliveryDemands(String getDeliveryDemandsRequest)
			throws NullException, NotFoundException, PackagerException, ConverterException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cancelProduct(TProductRequest cancelProductRequest)
			throws NullException, NotFoundException, PackagerException, ConverterException, MalformedXMLException,
			NotRespectedRulesException, DriverException {
		// TODO Auto-generated method stub

	}

	@Override
	public void throwException(String throwExceptionRequest)
			throws NullException, NotFoundException, PackagerException, ConverterException, MalformedXMLException,
			NotRespectedRulesException, DriverException {
		// TODO Auto-generated method stub

	}

}
