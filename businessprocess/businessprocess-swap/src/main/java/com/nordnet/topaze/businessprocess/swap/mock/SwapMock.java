package com.nordnet.topaze.businessprocess.swap.mock;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.nordnet.swap.schemas.Swap;
import com.nordnet.swap.schemas.SwapException;
import com.nordnet.swap.ws.types.CreateSwapDemandRequest;
import com.nordnet.swap.ws.types.CreateSwapDemandResponse;
import com.nordnet.swap.ws.types.TArrayOfShippingEquipment;
import com.nordnet.swap.ws.types.TReturnEquipmentDemand;
import com.nordnet.swap.ws.types.TShippingEquipmentDemand;
import com.nordnet.swap.ws.types.TSwapDemand;
import com.nordnet.swap.ws.types.UserInfos;
import com.nordnet.topaze.businessprocess.swap.util.Constants;

/**
 * swap mock.
 * 
 * @author mahjoub-MARZOUGUI
 * 
 */
@Component("swapMock")
public class SwapMock implements Swap {

	/**
	 * default logger.
	 */
	private static final Logger LOGGER = Logger.getLogger(SwapMock.class.getName());

	/**
	 * les EM instance seront stockés dans le database.
	 */
	private static Map<String, CreateSwapDemandRequest> database = new HashMap<String, CreateSwapDemandRequest>();

	/**
	 * les status des EM instance seront stockés dans le database.
	 */
	private static Map<String, TRequestStatus> databaseStatus = new HashMap<String, TRequestStatus>();

	/**
	 * constructeur par default.
	 */
	public SwapMock() {

	}

	/**
	 * @param requestReference
	 * @return
	 */
	public TRequestStatus getRequestStatus(String requestReference) {

		// de materiel sera effectue au bout de 15 secondes

		waiting(Constants.QUINZE);
		if (databaseStatus.get(requestReference) != null) {
			if (databaseStatus.get(requestReference).equals(TRequestStatus.INRETURNPROGRESS)) {
				databaseStatus.put(requestReference, TRequestStatus.CLOSED);
				return TRequestStatus.CLOSED;

			} else if (databaseStatus.get(requestReference).equals(TRequestStatus.CLOSED)) {
				databaseStatus.put(requestReference, TRequestStatus.INRETURNPROGRESS);
				return TRequestStatus.INRETURNPROGRESS;

			}
		} else {
			databaseStatus.put(requestReference, TRequestStatus.INRETURNPROGRESS);
			return TRequestStatus.INRETURNPROGRESS;
		}
		return databaseStatus.get(requestReference);
	}

	/**
	 * attendre.
	 * 
	 * @param seconde
	 *            nombre des secondes.
	 */
	private synchronized void waiting(int seconde) {

		try {
			Thread.sleep(seconde * 1000);
		} catch (InterruptedException exception) {
			LOGGER.error("exception catched during the waiting method", exception);
		}
	}

	@Override
	public CreateSwapDemandResponse createSwapDemand(UserInfos userInfos, String arg1, String arg2,
			TReturnEquipmentDemand tReturnEquipment, TShippingEquipmentDemand tShippingEquipmentDemand, String arg5)
			throws SwapException {
		CreateSwapDemandRequest swapDemand = new CreateSwapDemandRequest();
		swapDemand.setUserInfos(userInfos);
		// swapDemand.setStatus(TRequestStatus.INRETURNPROGRESS);
		swapDemand.setReturnEquipment(tReturnEquipment);
		swapDemand.setShippingEquipment(tShippingEquipmentDemand);
		// swapDemand.s(PropertiesUtil.getInstance().getDateDuJour().toDate());

		database.put(userInfos.getHttpUserAgent(), swapDemand);

		return new CreateSwapDemandResponse();
	}

	@Override
	public TSwapDemand getDemandByKey(String arg0) throws SwapException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TArrayOfShippingEquipment listReferencesToSwap(TArrayOfShippingEquipment arg0) throws SwapException {
		// TODO Auto-generated method stub
		return null;
	}

}
