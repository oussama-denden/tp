package com.nordnet.topaze.communication.test.utils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ReplacementDataSet;
import org.unitils.dbunit.datasetfactory.impl.MultiSchemaXmlDataSetFactory;
import org.unitils.dbunit.util.MultiSchemaDataSet;

/**
 * The class {@link TopazeMultiSchemaXmlDataSetFactory} allows us to use some 'non-static' values in datasets.
 * <p>
 * 
 * @author ngendron
 */
public class TopazeMultiSchemaXmlDataSetFactory extends MultiSchemaXmlDataSetFactory {

	/**
	 * Date format yyyy-MM-dd HH:mm:ss.S.
	 */
	private final static String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.S";

	/**
	 * Date format yyyy-MM-dd HH:mm:ss.
	 */
	private final static String DATE_FORMAT2 = "yyyy-MM-dd HH:mm:ss";

	/**
	 * -1.
	 */
	private final int substract1 = -1;

	/**
	 * Creates a {@link MultiSchemaDataSet} using the given file.
	 * 
	 * @param dataSetFiles
	 *            The dataset files, not null
	 * @return A {@link MultiSchemaDataSet} containing the datasets per schema, not null.
	 */
	@Override
	public MultiSchemaDataSet createDataSet(File... dataSetFiles) {
		MultiSchemaDataSet multiSchemaDataSet = super.createDataSet(dataSetFiles);
		if (multiSchemaDataSet != null && multiSchemaDataSet.getSchemaNames() != null
				&& multiSchemaDataSet.getSchemaNames().size() > 0) {
			for (String schemaName : multiSchemaDataSet.getSchemaNames()) {
				IDataSet cachedDataSet = multiSchemaDataSet.getDataSetForSchema(schemaName);

				ReplacementDataSet replacementDataSet = new ReplacementDataSet(cachedDataSet);

				Calendar now = Calendar.getInstance();
				now.set(Calendar.MINUTE, 0);
				now.set(Calendar.SECOND, 0);
				now.set(Calendar.MILLISECOND, 0);
				replacementDataSet.addReplacementObject("[now]",
						new SimpleDateFormat(DATE_FORMAT).format(now.getTime()));

				Calendar nowWithTime = Calendar.getInstance();
				nowWithTime.set(Calendar.MILLISECOND, 0);
				replacementDataSet.addReplacementObject("[nowWithTime]",
						new SimpleDateFormat(DATE_FORMAT2).format(nowWithTime.getTime()));

				nowWithTime.add(Calendar.MINUTE, -119);
				replacementDataSet.addReplacementObject("[now-119Min]",
						new SimpleDateFormat(DATE_FORMAT).format(nowWithTime.getTime()));

				nowWithTime.add(Calendar.MINUTE, 119);
				replacementDataSet.addReplacementObject("[now+119Min]",
						new SimpleDateFormat(DATE_FORMAT).format(nowWithTime.getTime()));

				Calendar todaySubstract1 = Calendar.getInstance();
				todaySubstract1.add(Calendar.DAY_OF_MONTH, substract1);
				todaySubstract1.set(Calendar.HOUR_OF_DAY, 0);
				todaySubstract1.set(Calendar.MINUTE, 0);
				todaySubstract1.set(Calendar.SECOND, 0);
				todaySubstract1.set(Calendar.MILLISECOND, 0);
				replacementDataSet.addReplacementObject("[todaySubstract1]",
						new SimpleDateFormat(DATE_FORMAT).format(todaySubstract1.getTime()));

				Calendar now1 = Calendar.getInstance();
				now1.set(Calendar.MINUTE, 0);
				now1.set(Calendar.SECOND, 0);
				now1.set(Calendar.MILLISECOND, 0);
				now1.add(Calendar.DAY_OF_MONTH, 10);
				replacementDataSet.addReplacementObject("[now+10days]",
						new SimpleDateFormat(DATE_FORMAT).format(now1.getTime()));

				Calendar now2 = Calendar.getInstance();
				now2.set(Calendar.MINUTE, 0);
				now2.set(Calendar.SECOND, 0);
				now2.set(Calendar.MILLISECOND, 0);
				now2.add(Calendar.YEAR, 10);
				replacementDataSet.addReplacementObject("[now+10years]",
						new SimpleDateFormat(DATE_FORMAT).format(now2.getTime()));

				Calendar now3 = Calendar.getInstance();
				now3.set(Calendar.MINUTE, 0);
				now3.set(Calendar.SECOND, 0);
				now3.set(Calendar.HOUR_OF_DAY, 8);
				now3.set(Calendar.MILLISECOND, 0);
				replacementDataSet.addReplacementObject("[now+8h]",
						new SimpleDateFormat(DATE_FORMAT).format(now3.getTime()));

				Calendar now4 = Calendar.getInstance();
				now4.set(Calendar.MINUTE, 0);
				now4.set(Calendar.SECOND, 0);
				now4.set(Calendar.HOUR_OF_DAY, 14);
				now4.set(Calendar.MILLISECOND, 0);
				replacementDataSet.addReplacementObject("[now+14h]",
						new SimpleDateFormat(DATE_FORMAT).format(now4.getTime()));

				Calendar now5 = Calendar.getInstance();
				now5.set(Calendar.MINUTE, 0);
				now5.set(Calendar.SECOND, 0);
				now5.set(Calendar.HOUR_OF_DAY, 18);
				now5.set(Calendar.MILLISECOND, 0);
				replacementDataSet.addReplacementObject("[now+18h]",
						new SimpleDateFormat(DATE_FORMAT).format(now5.getTime()));

				Calendar now6 = Calendar.getInstance();
				now6.set(Calendar.MINUTE, 0);
				now6.set(Calendar.SECOND, 0);
				now6.set(Calendar.MILLISECOND, 0);
				now6.add(Calendar.DAY_OF_MONTH, -7);
				replacementDataSet.addReplacementObject("[now-7days]",
						new SimpleDateFormat(DATE_FORMAT).format(now6.getTime()));

				Calendar now7 = Calendar.getInstance();
				now7.set(Calendar.MINUTE, 0);
				now7.set(Calendar.SECOND, 0);
				now7.set(Calendar.MILLISECOND, 0);
				now7.add(Calendar.DAY_OF_MONTH, 7);
				replacementDataSet.addReplacementObject("[now+7days]",
						new SimpleDateFormat(DATE_FORMAT).format(now7.getTime()));

				multiSchemaDataSet.setDataSetForSchema(schemaName, replacementDataSet);
			}
		}

		return multiSchemaDataSet;
	}
}
