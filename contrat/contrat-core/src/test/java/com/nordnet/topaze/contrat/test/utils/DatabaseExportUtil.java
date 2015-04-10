package com.nordnet.topaze.contrat.test.utils;

import static org.hibernate.cfg.AvailableSettings.DIALECT;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.dbunit.DatabaseUnitException;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Util class used to export database with DBUnit.
 * 
 * @author MMORTIER, JLDANSOU
 */
public class DatabaseExportUtil {

	/**
	 * Default logger.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseExportUtil.class);

	/**
	 * JDBC connection.
	 */
	private java.sql.Connection jdbcConnection;

	/**
	 * Default constructor.
	 * <p>
	 * The constructor initialize the database connection.
	 */
	protected DatabaseExportUtil() {
		super();
		try {

			@SuppressWarnings("unused")
			Class<?> driverClass = Class.forName("com.mysql.jdbc.Driver");

			jdbcConnection = DriverManager.getConnection("jdbc:mysql://localhost:3306/topaze_test", "test", "test");
		} catch (ClassNotFoundException cnfe) {
			LOGGER.error("error", cnfe);
		} catch (SQLException sqle) {
			LOGGER.error("error", sqle);
		}

	}

	/**
	 * Méthode qui permet d'exporter la structure d'une base de données dans une dtd.
	 * 
	 * @throws IOException
	 *             en cas d'erreur de connexion à la base, ou d'écriture du fichier.
	 * @throws SQLException
	 *             if error occurs
	 * @throws DatabaseUnitException
	 *             if error occurs
	 * 
	 */
	@SuppressWarnings("resource")
	protected void exportDatabaseToDtd() throws IOException, SQLException, DatabaseUnitException {
		// database connection
		IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);

		// write DTD file
		FlatDtdDataSet.write(connection.createDataSet(), new FileOutputStream("src/test/resources/dtd/topaze.dtd"));

	}

	/**
	 * Méthode qui permet d'exporter les tables dans l'ordre des clés étrangères.
	 * 
	 * @param fileName
	 *            le nom du fichier xml qui contiendra le dump.
	 * @throws IOException
	 *             en cas d'erreur de connexion à la base, ou d'écriture du fichier.
	 * @throws DatabaseUnitException
	 *             if error occurs
	 * @throws SQLException
	 *             if error occurs
	 */
	@SuppressWarnings("resource")
	protected void exportDataBaseWithForeignKeyOrder(final String fileName)
			throws IOException, DatabaseUnitException, SQLException {

		IDatabaseConnection conn = new DatabaseConnection(jdbcConnection);

		ITableFilter filter = new DatabaseSequenceFilter(conn);
		IDataSet dataset = new FilteredDataSet(filter, conn.createDataSet());

		FlatXmlDataSet.write(dataset, new FileWriter(fileName));

	}

	/**
	 * Méthode qui permet d'exporter les tables .
	 * 
	 * @param fileName
	 *            le nom du fichier xml qui contiendra le dump.
	 * @throws IOException
	 *             en cas d'erreur de connexion à la base, ou d'écriture du fichier.
	 * @throws DatabaseUnitException
	 *             if error occurs
	 * @throws SQLException
	 *             if error occurs
	 */
	@SuppressWarnings("resource")
	protected void exportDataBase(final String fileName) throws IOException, DatabaseUnitException, SQLException {

		IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);
		IDataSet createDataSet = connection.createDataSet();

		FlatXmlDataSet.write(createDataSet, new FileWriter(fileName));
	}

	/**
	 * enumeration for SQL Dialect.
	 * 
	 * @author MMORTIER
	 * 
	 */
	enum Dialect {
		/** {@link Dialect} MYSQL. */
		MYSQL("org.hibernate.dialect.MySQL5InnoDBDialect"),
		/** {@link Dialect} ORACLE. */
		ORACLE("org.unhcr.omss.db.oracle.OracleDialectDeferredFK"),
		/** {@link Dialect} SYBASE. */
		SYBASE("org.hibernate.dialect.SybaseAnywhereDialect");

		/** {@link String} className. */
		private final String className;

		/**
		 * Constructor.
		 * 
		 * @param className
		 *            the class name to set.
		 */
		private Dialect(final String className) {
			this.className = className;
		}

		/**
		 * Getter of className.
		 * 
		 * @return {@link String} the className
		 */
		public String getClassName() {
			return className;
		}

	}

	/**
	 * export database to SQL.
	 * 
	 * @param dialect
	 *            SQL {@link Dialect}.
	 * @param packageName
	 *            package name to scan.
	 * @throws IOException
	 *             if error occurs.
	 * @throws ClassNotFoundException
	 *             if error occurs
	 */
	protected void exportDatabaseToSql(final Dialect dialect, final String packageName)
			throws IOException, ClassNotFoundException {
		// retrieve all class name from package
		List<Class<?>> classes = new ArrayList<>();
		Enumeration<URL> urls =
				Thread.currentThread().getContextClassLoader().getResources(packageName.replace(".", "/"));
		while (urls.hasMoreElements()) {
			URL url = urls.nextElement();
			File dir = new File(url.getFile());
			classes.addAll(getClassNameFromDir(packageName, dir));
		}

		// export
		exportToSql(dialect, classes.toArray(new Class[0]));
	}

	/**
	 * get all defined Class from directory.
	 * 
	 * @param packageName
	 *            the base package name of class.
	 * @param file
	 *            the directory of class file
	 * @return the list of class in this directory.
	 * @throws IOException
	 *             if error occurs.
	 * @throws ClassNotFoundException
	 *             if error occurs
	 */
	private List<Class<?>> getClassNameFromDir(final String packageName, final File file)
			throws IOException, ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<>();
		if (!file.exists()) {
			return classes;
		}
		// if is directory, recurse search
		for (File subFile : file.listFiles()) {
			if (subFile.isDirectory()) {
				classes.addAll(getClassNameFromDir(packageName + "." + subFile.getName(), subFile));
			}
			// if is a class file
			else if (subFile.getName().endsWith(".class")) {
				// get as String with package separator "."
				String path = subFile.getPath().replaceAll("\\\\", ".");
				// get the class name with package name declaration only
				path = path.substring(path.lastIndexOf(packageName));

				// remove .class and get the class for Name
				classes.add(Class.forName(path.substring(0, path.lastIndexOf("."))));
			}
		}
		return classes;
	}

	/**
	 * use hibernate {@link Configuration} to export ddl schema form {@link Dialect}.
	 * 
	 * @param dialect
	 *            {@link Dialect} of ddl.
	 * @param classes
	 *            list of class to export
	 */
	private static void exportToSql(final Dialect dialect, final Class<?>... classes) {
		Configuration configuration = new Configuration();
		configuration.setProperty(DIALECT, dialect.getClassName());
		for (Class<?> entityClass : classes) {
			configuration.addAnnotatedClass(entityClass);
		}
		SchemaExport schemaExport = new SchemaExport(configuration);
		schemaExport.setDelimiter(";");
		schemaExport.setFormat(true);
		schemaExport.setOutputFile(String.format("src/test/resources/sql/%s_%s.%s ", "init", dialect.name()
				.toLowerCase(), "sql"));
		schemaExport.create(false, false);
	}

	/**
	 * Méthode principale à modifier pour tester.
	 * 
	 * @param args
	 *            argument en ligne de commande.
	 */
	public static void main(final String[] args) {
		DatabaseExportUtil dbeo = new DatabaseExportUtil();

		try {
			// dbeo.exportDatabaseToDtd();
			dbeo.exportDatabaseToSql(Dialect.MYSQL, "com.nordnet.topaze.contrat.domain");
			// dbeo.exportDataBase("testWithoutOrder");
			// dbeo.exportDataBaseWithForeignKeyOrder("src/test/resources/dataset/emptyDB.xml");
			// dbeo.exportDataBaseWithForeignKeyOrder("src/test/resources/dataset/caisseItemToCaisseFinal.xml");
			// dbeo.exportDataBaseWithForeignKeyOrder("src/test/resources/dataset/releve/send/test-send-releve.xml");
			// dbeo.exportDataBaseWithForeignKeyOrder("src/test/resources/dataset/bill/check/test-check-prepaid-bill.xml");

		} catch (Exception e) {
			LOGGER.error("error", e);
		}
	}

}