package com.nordnet.topaze.billing.outil.business;

import java.util.Date;

import org.joda.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Period definit par date debut periode et date fin periode.
 * 
 * @author Oussama Denden
 * 
 */
public class TimePeriod {

	/**
	 * date debut periode.
	 */
	private Date dateDebut;

	/**
	 * date fin periode.
	 */
	private Date dateFin;

	/**
	 * constructeur par defaut.
	 */
	public TimePeriod() {

	}

	/**
	 * @return {@link #dateDebut}.
	 */
	public Date getDateDebut() {
		return dateDebut;
	}

	/**
	 * @param dateDebut
	 *            {@link #dateDebut}.
	 */
	public void setDateDebut(Date dateDebut) {
		this.dateDebut = dateDebut;
	}

	/**
	 * @return {@link #dateFin}.
	 */
	public Date getDateFin() {
		return dateFin;
	}

	/**
	 * @param dateFin
	 *            {@link #dateFin}.
	 */
	public void setDateFin(Date dateFin) {
		this.dateFin = dateFin;
	}

	/**
	 * @return une period.
	 */
	@JsonIgnore
	public com.nordnet.common.valueObject.date.TimePeriod getTimePeriod() {

		LocalDate dateDebutPeriod = LocalDate.fromDateFields(dateDebut);
		LocalDate dateFinPeriod = LocalDate.fromDateFields(dateFin);

		return new com.nordnet.common.valueObject.date.TimePeriod(dateDebutPeriod, dateFinPeriod);
	}

	/**
	 * @param timePeriod
	 *            set date debut et date fin a partir d'une {@link com.nordnet.common.valueObject.date.TimePeriod}.
	 */
	@JsonIgnore
	public void setTimePeriod(com.nordnet.common.valueObject.date.TimePeriod timePeriod) {
		this.dateDebut = timePeriod.getStartFrom().toDate();
		this.dateFin = timePeriod.getEndTo().toDate();
	}

}
