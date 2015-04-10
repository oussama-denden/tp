package com.nordnet.topaze.businessprocess.core.test.mock;

import org.json.JSONException;
import org.json.JSONObject;

import com.nordnet.topaze.businessprocess.core.mock.PackagerConfig;
import com.nordnet.topaze.businessprocess.core.util.Constants;

/**
 * Un mock por le client NetCatalogue de nordNet, ce mock n'est pas une reprensentation du client reel.
 * 
 * @author akram-moncer
 * 
 */
public class NetCatalogueMock extends com.nordnet.topaze.businessprocess.core.mock.NetCatalogueMock {

	/**
	 * @return les information necaissaire pour effectue un appel vers packager.
	 * @throws JSONException
	 */
	@Override
	public PackagerConfig getPackagerConfig(String referenceProduit) throws JSONException {

		PackagerConfig packagerConfig = new PackagerConfig();
		packagerConfig.setTemplate(referenceProduit.replace(".", "-").replace(" ", "-") + ".xml");
		JSONObject JSONConfig;
		if (referenceProduit.equals(Constants.REFERENCE_KIT_SAT)) {
			JSONConfig = new JSONObject();
			JSONConfig.put("freezone", true);
			packagerConfig.setConfigs(JSONConfig);
			return packagerConfig;
		} else if (referenceProduit.equals(Constants.REFERENCE_VOIP)) {
			JSONConfig = new JSONObject();
			packagerConfig.setConfigs(JSONConfig);
			return packagerConfig;
		} else if (referenceProduit.equals(Constants.REFERENCE_JET)) {
			JSONConfig = new JSONObject();
			JSONConfig.put("freezone", true);
			packagerConfig.setConfigs(JSONConfig);
			return packagerConfig;
		} else if (referenceProduit.equals(Constants.REFERENCE_MAX)) {
			JSONConfig = new JSONObject();
			JSONConfig.put("freezone", true);
			JSONConfig.put("abondance", false);
			packagerConfig.setConfigs(JSONConfig);
			return packagerConfig;
		} else if (referenceProduit.equals(Constants.REFERENCE_DOMAIN)) {
			JSONConfig = new JSONObject();
			packagerConfig.setConfigs(JSONConfig);
			return packagerConfig;
		} else if (Constants.REFERENCES_OPTION_PLUS.contains(referenceProduit)) {
			packagerConfig.setTemplate(null);
			JSONConfig = new JSONObject();
			packagerConfig.setConfigs(JSONConfig);
			return packagerConfig;
		} else {
			JSONConfig = new JSONObject();
			packagerConfig.setConfigs(JSONConfig);
			return packagerConfig;
		}
	}

	/**
	 * retourner le product model.
	 * 
	 * @param referenceProduit
	 *            reference du produit.
	 * @return product model.
	 */
	@Override
	public String getProductModel(String referenceProduit) {
		return referenceProduit.replace(" ", ".").replace("-", ".") + ".product.model";
	}

	/**
	 * retourner le packager model.
	 * 
	 * @param referenceProduit
	 *            reference du produit.
	 * @return packager model.
	 */
	@Override
	public String getPackagerModel(String referenceProduit) {
		return referenceProduit.replace(" ", ".").replace("-", ".") + ".product.model";
	}

	/**
	 * retourner la valeur de l'option plus.
	 * 
	 * @param referenceProduit
	 *            reference du produit.
	 * @return la valeur de l'option plus.
	 */
	@Override
	public String getOptionPlusValeur(String referenceProduit) {
		if (referenceProduit.equals(Constants.REFERENCE_OPTION_PLUS_20G)) {
			return "20G";
		} else if (referenceProduit.equals(Constants.REFERENCE_OPTION_PLUS_50G)) {
			return "50G";
		}
		return null;
	}
}
