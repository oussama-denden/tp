package com.nordnet.topaze.businessprocess.packager.mock;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.springframework.stereotype.Component;

import com.nordnet.topaze.businessprocess.packager.util.Constants;

/**
 * Mock pour le projet Opale.
 * 
 * @author akram-moncer
 * 
 */
@Component("opaleMock")
public class OpaleMock {

	private String asia_Info = "<ndd:contact nnbo_optional=\"true\" nnbo_dependsOn=\"asia_useCustom\">"
			+ "<ndd:type>Local</ndd:type>" + "<ndd:data>" + "<ndd:details>"
			+ "<ndd:company><![CDATA[as_company]]></ndd:company>"
			+ "<ndd:civility><![CDATA[as_civility]]></ndd:civility>"
			+ "<ndd:firstName><![CDATA[as_firstName]]></ndd:firstName>"
			+ "<ndd:lastName><![CDATA[as_name]]></ndd:lastName>" + "<ndd:address>"
			+ "<ndd:address1><![CDATA[as_address1]]></ndd:address1>"
			+ "<ndd:address2><![CDATA[as_address2]]></ndd:address2>" + "<ndd:address3 />"
			+ "<ndd:zipCode><![CDATA[as_zipCode]]></ndd:zipCode>" + "<ndd:city><![CDATA[as_city]]></ndd:city>"
			+ "<ndd:country><![CDATA[as_country]]></ndd:country>" + "</ndd:address>"
			+ "<ndd:phone><![CDATA[as_phone]]></ndd:phone>" + "<ndd:fax><![CDATA[as_fax]]></ndd:fax>"
			+ "<ndd:gsm><![CDATA[as_gsm]]></ndd:gsm>" + "<ndd:email><![CDATA[as_email]]></ndd:email>"
			+ "<ndd:status><![CDATA[as_profileType]]></ndd:status>" + "<ndd:additionalData>"
			+ "<ndd:entityIdentifier><![CDATA[as_cediprIdentNumber]]></ndd:entityIdentifier>"
			+ "<ndd:entityForm><![CDATA[as_cediprIdentForm]]></ndd:entityForm>"
			+ "<ndd:entityType><![CDATA[as_cediprLegalEntityType]]></ndd:entityType>"
			+ "<ndd:contactCountry><![CDATA[as_cediprCountry]]></ndd:contactCountry>" + "</ndd:additionalData>"
			+ "</ndd:contact>";

	/**
	 * 
	 * @param referenceCommande
	 * @param referenceProduit
	 * @return
	 * @throws JSONException
	 */
	public Map<String, Object> getPackagerConfig(String referenceCommande, String referenceProduit)
			throws JSONException {
		Map<String, Object> configs;
		if (referenceProduit.equals(Constants.REFERENCE_DOMAIN)) {
			configs = new HashMap<String, Object>();
			configs.put("Order_asia", asia_Info);
			return configs;
		}
		return new HashMap<String, Object>();
	}

}
