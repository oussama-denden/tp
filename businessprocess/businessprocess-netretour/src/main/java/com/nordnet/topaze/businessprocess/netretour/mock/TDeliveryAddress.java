package com.nordnet.topaze.businessprocess.netretour.mock;

/**
 * Adress.
 * 
 * @author Oussama Denden
 * 
 */
public class TDeliveryAddress {

	/**
	 * adress 1.
	 */
	private String address1;

	/**
	 * adress 2.
	 */
	private String address2;

	/**
	 * code postal.
	 */
	private String zipCode;

	/**
	 * region.
	 */
	private String city;

	/**
	 * pays.
	 */
	private String country;

	/**
	 * constructeur par default.
	 */
	public TDeliveryAddress() {

	}

	public TDeliveryAddress(String address1, String address2, String address3, String zipCode, String city,
			String country, String cedexCode, String cedexLabel) {
		super();
		this.address1 = address1;
		this.address2 = address2;
		this.zipCode = zipCode;
		this.city = city;
		this.country = country;
	}

	/**
	 * @return {@link #address1}.
	 */
	public String getAddress1() {
		return address1;
	}

	/**
	 * @param address1
	 *            {@link #address1}.
	 */
	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	/**
	 * @return {@link #address2}.
	 */
	public String getAddress2() {
		return address2;
	}

	/**
	 * @param address2
	 *            {@link #address2}.
	 */
	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	/**
	 * @return {@link #zipCode}.
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * @param zipCode
	 *            {@link #zipCode}.
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * @return {@link #city}.
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            {@link #city}.
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return {@link #country}.
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country
	 *            {@link #country}.
	 */
	public void setCountry(String country) {
		this.country = country;
	}

}
