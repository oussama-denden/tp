package com.nordnet.topaze.contrat.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
	 * @param seconDate
	 * @return int (-1 first date is less then second date, 0 equals , first date is great then second date)
	 */
	public static int compareDate(Date firstDate, Date seconDate) {
		LocalDate firstLocalDate = new LocalDate(firstDate);
		LocalDate seconLocalDate = new LocalDate(seconDate);
		return firstLocalDate.compareTo(seconLocalDate);
	}

	/**
	 * Check string contain whitespace.
	 * 
	 * @param str
	 *            the string.
	 * @return <code>true</code> if the string contain whitespace, <code>false</code> otherwise.
	 */
	public static boolean containsWhiteSpace(final String str) {
		Pattern pattern = Pattern.compile("\\s");
		Matcher matcher = pattern.matcher(str.trim());
		return matcher.find();
	}

	public static Date dateMinusMonth(Date date, int i) {
		if (date != null) {
			return LocalDate.fromDateFields(date).minusMonths(i).toDate();
		}
		return null;
	}

	/**
	 * comparer deux date sanc comparer l'heure.
	 * 
	 * @param date1
	 *            le premier date
	 * @param date2
	 *            le deuxieme date
	 * @return true s la date1 egal la dateé
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
	 * comparer deux date sans comparer l'heure
	 * 
	 * @param date1
	 *            le premier date
	 * @param date2
	 *            le deuxieme date
	 * @return true s la date1 egal la dateé
	 * @throws TopazeException
	 *             {@link TopazeException}.
	 */
	public static int compareDateWithTime(Date date1, Date date2) throws TopazeException {
		SimpleDateFormat formatter = Constants.DEFAULT_DATE_FORMAT;
		Date dateDuJour = null;
		Date dateResiliation = null;
		try {
			dateDuJour = formatter.parse(formatter.format(date1));
			dateResiliation = formatter.parse(formatter.format(date2));

		} catch (ParseException e) {
			LOGGER.error("Error to format date", e);
			throw new TopazeException(PropertiesUtil.getInstance().getErrorMessage("0.7"), "0.7");
		}
		return dateDuJour.compareTo(dateResiliation);

	}

	/**
	 * formatter la date avec le temps.
	 * 
	 * @param date
	 *            {@link Date}
	 * @return string
	 */
	public static String formatDateWithTime(Date date) {

		SimpleDateFormat formatter = Constants.DEFAULT_DATE_FORMAT;
		String dateDuJour = null;
		dateDuJour = formatter.format(date);
		return dateDuJour;

	}

	/**
	 * Vérifier si la difference entre les deux date est de l'ordre de 30 minute.
	 * 
	 * @param date1
	 *            date 1
	 * @param date2
	 *            date 2
	 * @return true si la condition est vrai.
	 */
	public static boolean compareDatePourresiliationDifferee(Date date1, Date date2) {
		long diff = date1.getTime() - date2.getTime();
		long minuteDiff = diff / (60 * 1000);

		return minuteDiff >= 30;

	}

}