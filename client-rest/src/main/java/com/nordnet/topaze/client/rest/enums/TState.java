package com.nordnet.topaze.client.rest.enums;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Classe Java pour TState.
 * 
 * <p>
 * Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * <p>
 * 
 * <pre>
 * &lt;simpleType name="TState">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="INPROGRESS"/>
 *     &lt;enumeration value="ACTIVABLE"/>
 *     &lt;enumeration value="ACTIVE"/>
 *     &lt;enumeration value="SUSPENDED"/>
 *     &lt;enumeration value="CANCELED"/>
 *     &lt;enumeration value="UNSTABLE"/>
 *     &lt;enumeration value="DELIVERED"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "TState")
@XmlEnum
public enum TState {

	INPROGRESS, ACTIVABLE, ACTIVE, SUSPENDED, CANCELED, UNSTABLE, DELIVERED;

	public String value() {
		return name();
	}

	public static TState fromValue(String v) {
		return valueOf(v);
	}

}