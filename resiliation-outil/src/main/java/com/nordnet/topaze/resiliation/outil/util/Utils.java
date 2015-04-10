package com.nordnet.topaze.resiliation.outil.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.LogFactory;
import org.joda.time.LocalDate;

import com.nordnet.topaze.exception.TopazeException;

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

	/**
	 * comparer deux date sanc comparer l'heure.
	 * 
	 * @param date1
	 *            le premier date.
	 * @param date2
	 *            le deuxieme date.
	 * @return true s la date1 egal la dateé.
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static int compareDateWithoutTime(Date date1, Date date2) throws TopazeException {
		SimpleDateFormat formatter = Constants.DEFAULT_DATE_WITHOUT_TIME_FORMAT;
		Date dateDuJour = null;
		Date dateFacutre = null;
		try {
			dateDuJour = formatter.parse(formatter.format(date1));
			dateFacutre = formatter.parse(formatter.format(date2));

		} catch (ParseException e) {
			LOGGER.error("Error to format date", e);
			throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("0.7"), "0.7");
		}
		return dateDuJour.compareTo(dateFacutre);

	}

	/**
	 * Rounds up a double value.
	 * 
	 * @param value
	 *            double value.
	 * @param places
	 *            the number of decimal places.
	 * @return rounded value.
	 */
	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(String.valueOf(value));
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

}
