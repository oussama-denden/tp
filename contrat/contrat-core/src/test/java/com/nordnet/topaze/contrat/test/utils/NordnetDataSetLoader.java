package com.nordnet.topaze.contrat.test.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.Period;
import org.springframework.core.io.Resource;

import com.github.springtestdbunit.dataset.AbstractDataSetLoader;
import com.nordnet.common.valueObject.utils.Utils;

/**
 * The class {@link NordnetDataSetLoader} allows us to use some 'non-static' values in datasets.
 */
public class NordnetDataSetLoader extends AbstractDataSetLoader {

	@Override
	protected IDataSet createDataSet(final Resource resource) throws Exception {
		FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
		builder.setColumnSensing(true);
		try (InputStream inputStream = resource.getInputStream()) {

			String fichier = new String(Utils.readFromStream(inputStream));

			LocalDateTime nowTime = LocalDateTime.now();
			fichier = fichier.replaceAll("\\[nowWithTime\\]", nowTime.toString("yyyy-MM-dd HH:mm:ss"));
			fichier =
					fichier.replaceAll("\\[now-119Min\\]",
							nowTime.plus(Period.minutes(-119)).toString("yyyy-MM-dd HH:mm:ss"));
			fichier =
					fichier.replaceAll("\\[now\\+119Min\\]",
							nowTime.plus(Period.minutes(+119)).toString("yyyy-MM-dd HH:mm:ss"));

			LocalDate now = LocalDate.now();
			fichier = fichier.replaceAll("\\[now\\]", now.toString("yyyy-MM-dd"));
			fichier = fichier.replaceAll("\\[todaySubstract1\\]", now.plus(Period.days(-1)).toString("yyyy-MM-dd"));
			fichier = fichier.replaceAll("\\[now-7days\\]", now.plus(Period.days(-7)).toString("yyyy-MM-dd"));
			fichier = fichier.replaceAll("\\[now\\+7days\\]", now.plus(Period.days(7)).toString("yyyy-MM-dd"));
			fichier = fichier.replaceAll("\\[now\\+10days\\]", now.plus(Period.days(10)).toString("yyyy-MM-dd"));
			fichier = fichier.replaceAll("\\[now\\+10years\\]", now.plus(Period.years(10)).toString("yyyy-MM-dd"));
			fichier = fichier.replaceAll("\\[now\\+8h\\]", now.plus(Period.hours(8)).toString("yyyy-MM-dd"));
			fichier = fichier.replaceAll("\\[now\\+14h\\]", now.plus(Period.hours(14)).toString("yyyy-MM-dd"));
			fichier = fichier.replaceAll("\\[now\\+18h\\]", now.plus(Period.hours(18)).toString("yyyy-MM-dd"));
			fichier = fichier.replaceAll("\\[now\\-10days\\]", now.minus(Period.days(10)).toString("yyyy-MM-dd"));
			fichier = fichier.replaceAll("\\[now\\+48months\\]", now.plus(Period.months(48)).toString("yyyy-MM-dd"));
			fichier = fichier.replaceAll("\\[now\\-10months\\]", now.minus(Period.months(10)).toString("yyyy-MM-dd"));
			return builder.build(new ByteArrayInputStream(fichier.getBytes()));
		}
	}
}