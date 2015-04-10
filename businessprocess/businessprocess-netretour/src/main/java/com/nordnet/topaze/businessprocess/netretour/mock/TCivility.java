package com.nordnet.topaze.businessprocess.netretour.mock;

public class TCivility implements java.io.Serializable {

	private java.lang.String _value_;
	private static java.util.HashMap _table_ = new java.util.HashMap();

	// Constructor
	protected TCivility(java.lang.String value) {
		_value_ = value;
		_table_.put(_value_, this);
	}

	public static final java.lang.String _value1 = "M.";
	public static final java.lang.String _value2 = "Mlle";
	public static final java.lang.String _value3 = "Mme";
	public static final TCivility M = new TCivility(_value1);
	public static final TCivility Mlle = new TCivility(_value2);
	public static final TCivility Mme = new TCivility(_value3);

	static {
		_table_.put(_value1, M);
		_table_.put(_value2, Mlle);
		_table_.put(_value3, Mme);
	}

	public java.lang.String getValue() {
		return _value_;
	}

	public static TCivility fromValue(java.lang.String value) throws java.lang.IllegalArgumentException {
		TCivility enumeration = (TCivility) _table_.get(value);
		if (enumeration == null)
			throw new java.lang.IllegalArgumentException();
		return enumeration;
	}

	public static TCivility fromString(java.lang.String value) throws java.lang.IllegalArgumentException {
		return fromValue(value);
	}

	public boolean equals(java.lang.Object obj) {
		return (obj == this);
	}

	public int hashCode() {
		return toString().hashCode();
	}

	public java.lang.String toString() {
		return _value_;
	}

	public java.lang.Object readResolve() throws java.io.ObjectStreamException {
		return fromValue(_value_);
	}

}