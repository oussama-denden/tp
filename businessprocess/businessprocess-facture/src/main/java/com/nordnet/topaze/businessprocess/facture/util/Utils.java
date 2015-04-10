package com.nordnet.topaze.businessprocess.facture.util;

import java.util.Date;
import java.util.List;

import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;

/**
 * Utils class.
 * 
 * @author mazemzemi
 * 
 */
public final class Utils {

	/**
	 * Logger of the service.
	 */
	private static final org.apache.commons.logging.Log LOGGER = LogFactory.getLog(Utils.class);

	/**
	 * Default constructor.
	 */
	private Utils() {
		LOGGER.info("Default constructor.");
	}

	/**
	 * Check string is null or empty.
	 * 
	 * @param str
	 *            the string.
	 * @return <code>true</code> if the string is null or empty, <code>false</code> otherwise.
	 */
	public static boolean isStringNullOrEmpty(String str) {
		return (str == null) ? true : str.trim().length() == 0;
	}

	/**
	 * Tests if a list is null or empty.
	 * 
	 * @param list
	 *            the list of objects
	 * @return <code>false</code> if the list is null or empty, <code>true</code> otherwise.
	 */
	public static boolean isListNullOrEmpty(List<?> list) {
		return (list == null) ? true : list.size() == 0;
	}

	/**
	 * Comparer deux date.
	 * 
	 * @param firstDate
	 *            premier date.
	 * @param seconDate
	 *            deuxieme date.
	 * @return int (-1 first date is less then second date, 0 equals , first date is great then second date)
	 */
	public static int compareDate(Date firstDate, Date seconDate) {
		LocalDate firstLocalDate = new LocalDate(firstDate);
		LocalDate seconLocalDate = new LocalDate(seconDate);
		return firstLocalDate.compareTo(seconLocalDate);
	}

}
