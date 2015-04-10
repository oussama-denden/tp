package com.nordnet.topaze.businessprocess.netretour.mock;

public class TReceiverContact implements java.io.Serializable {

	private long id;

	private java.lang.String firstName;

	private java.lang.String lastName;

	private java.lang.String company;

	private java.lang.String email;

	private java.lang.String phoneNumber;

	private TCivility civility;

	private TDeliveryAddress address;

	public TReceiverContact() {
	}

	public TReceiverContact(long id, java.lang.String firstName, java.lang.String lastName, java.lang.String company,
			java.lang.String email, java.lang.String phoneNumber, TCivility civility, TDeliveryAddress address) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.company = company;
		this.email = email;
		this.phoneNumber = phoneNumber;
		this.civility = civility;
		this.address = address;
	}

	/**
	 * Gets the id value for this TReceiverContact.
	 * 
	 * @return id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Sets the id value for this TReceiverContact.
	 * 
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Gets the firstName value for this TReceiverContact.
	 * 
	 * @return firstName
	 */
	public java.lang.String getFirstName() {
		return firstName;
	}

	/**
	 * Sets the firstName value for this TReceiverContact.
	 * 
	 * @param firstName
	 */
	public void setFirstName(java.lang.String firstName) {
		this.firstName = firstName;
	}

	/**
	 * Gets the lastName value for this TReceiverContact.
	 * 
	 * @return lastName
	 */
	public java.lang.String getLastName() {
		return lastName;
	}

	/**
	 * Sets the lastName value for this TReceiverContact.
	 * 
	 * @param lastName
	 */
	public void setLastName(java.lang.String lastName) {
		this.lastName = lastName;
	}

	/**
	 * Gets the company value for this TReceiverContact.
	 * 
	 * @return company
	 */
	public java.lang.String getCompany() {
		return company;
	}

	/**
	 * Sets the company value for this TReceiverContact.
	 * 
	 * @param company
	 */
	public void setCompany(java.lang.String company) {
		this.company = company;
	}

	/**
	 * Gets the email value for this TReceiverContact.
	 * 
	 * @return email
	 */
	public java.lang.String getEmail() {
		return email;
	}

	/**
	 * Sets the email value for this TReceiverContact.
	 * 
	 * @param email
	 */
	public void setEmail(java.lang.String email) {
		this.email = email;
	}

	/**
	 * Gets the phoneNumber value for this TReceiverContact.
	 * 
	 * @return phoneNumber
	 */
	public java.lang.String getPhoneNumber() {
		return phoneNumber;
	}

	/**
	 * Sets the phoneNumber value for this TReceiverContact.
	 * 
	 * @param phoneNumber
	 */
	public void setPhoneNumber(java.lang.String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	/**
	 * Gets the civility value for this TReceiverContact.
	 * 
	 * @return civility
	 */
	public TCivility getCivility() {
		return civility;
	}

	/**
	 * Sets the civility value for this TReceiverContact.
	 * 
	 * @param civility
	 */
	public void setCivility(TCivility civility) {
		this.civility = civility;
	}

	/**
	 * Gets the address value for this TReceiverContact.
	 * 
	 * @return address
	 */
	public TDeliveryAddress getAddress() {
		return address;
	}

	/**
	 * Sets the address value for this TReceiverContact.
	 * 
	 * @param address
	 */
	public void setAddress(TDeliveryAddress address) {
		this.address = address;
	}

	private java.lang.Object __equalsCalc = null;

	public synchronized boolean equals(java.lang.Object obj) {
		if (!(obj instanceof TReceiverContact))
			return false;
		TReceiverContact other = (TReceiverContact) obj;
		if (obj == null)
			return false;
		if (this == obj)
			return true;
		if (__equalsCalc != null) {
			return (__equalsCalc == obj);
		}
		__equalsCalc = obj;
		boolean _equals;
		_equals =
				true
						&& this.id == other.getId()
						&& ((this.firstName == null && other.getFirstName() == null) || (this.firstName != null && this.firstName
								.equals(other.getFirstName())))
						&& ((this.lastName == null && other.getLastName() == null) || (this.lastName != null && this.lastName
								.equals(other.getLastName())))
						&& ((this.company == null && other.getCompany() == null) || (this.company != null && this.company
								.equals(other.getCompany())))
						&& ((this.email == null && other.getEmail() == null) || (this.email != null && this.email
								.equals(other.getEmail())))
						&& ((this.phoneNumber == null && other.getPhoneNumber() == null) || (this.phoneNumber != null && this.phoneNumber
								.equals(other.getPhoneNumber())))
						&& ((this.civility == null && other.getCivility() == null) || (this.civility != null && this.civility
								.equals(other.getCivility())))
						&& ((this.address == null && other.getAddress() == null) || (this.address != null && this.address
								.equals(other.getAddress())));
		__equalsCalc = null;
		return _equals;
	}

	private boolean __hashCodeCalc = false;

	public synchronized int hashCode() {
		if (__hashCodeCalc) {
			return 0;
		}
		__hashCodeCalc = true;
		int _hashCode = 1;
		_hashCode += new Long(getId()).hashCode();
		if (getFirstName() != null) {
			_hashCode += getFirstName().hashCode();
		}
		if (getLastName() != null) {
			_hashCode += getLastName().hashCode();
		}
		if (getCompany() != null) {
			_hashCode += getCompany().hashCode();
		}
		if (getEmail() != null) {
			_hashCode += getEmail().hashCode();
		}
		if (getPhoneNumber() != null) {
			_hashCode += getPhoneNumber().hashCode();
		}
		if (getCivility() != null) {
			_hashCode += getCivility().hashCode();
		}
		if (getAddress() != null) {
			_hashCode += getAddress().hashCode();
		}
		__hashCodeCalc = false;
		return _hashCode;
	}

}