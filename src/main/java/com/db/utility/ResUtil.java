package com.db.utility;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * The {@code ResUtil} class is responsible for managing the creation and closing
 * of database connection resources in a Java application.
 * 
 * <p>This utility relies on the {@code application.properties} file to obtain database
 * connection details, such as the URL, username, and password, which are essential for
 * establishing a connection with the database.</p>
 * 
 * <p>The {@code open()} method attempts to establish a connection to the database
 * using the credentials provided in the {@code application.properties} file. If the
 * file is missing, malformatted, or contains invalid values, an error message will be
 * displayed.</p>
 * 
 * <p>The {@code close()} method is used to close {@code AutoCloseable} resources, such as
 * database connections, {@code ResultSet}, {@code Statement}, and other resources,
 * ensuring they are properly freed from memory.</p>
 * 
 * <p>Note that the {@code application.properties} file should be available in the project's
 * classpath and must contain the required keys: {@code DB_URL}, {@code DB_USER}, and
 * {@code DB_PASS}.</p>
 * 
 * @version 1.4.0
 * @author Michael D. Ribeiro
 */
public class ResUtil {

	/**
     * Establishes a connection to the database using the properties provided
     * in the {@code application.properties} file.
     * 
     * <p>This method loads the database configuration properties from the
     * {@code application.properties} file and attempts to establish a connection using
     * {@code DriverManager}. If the properties file is missing or malformed, the method
     * will throw an exception. The method also sets auto-commit to {@code false} to enable
     * manual transaction management.</p>
     * 
     * @return An instance of {@link Connection} representing the connection to the database,
     *         or {@code null} if an error occurs while establishing the connection.
     * 
     * @throws RuntimeException If any error occurs during connection establishment, including
     * 		   issues with loading the properties file or database connection.
     */
	public static Connection open() {
		Connection conn = null;
		Properties props = new Properties();
		InputStream inputStream = null;
		try {

			inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("application.properties");
			
			if (inputStream == null)
				throw new IllegalArgumentException("application.properties file not found.");

			props.load(inputStream);

			if (validateProps(props))
				return null;

			if (props == null || props.isEmpty())
				throw new IllegalArgumentException("malformatted file.");

			conn = DriverManager.getConnection(props.getProperty("DB_URL"), props.getProperty("DB_USER"), props.getProperty("DB_PASS"));
			conn.setAutoCommit(false);
			return conn;

		} catch (Exception e) {
			throw new RuntimeException("An error occurred while establishing the connection.", e);
		} finally {
			close(inputStream);
		}
	}

	/**
     * Validates the {@code application.properties} file to ensure all required keys are present.
     * 
     * <p>This method checks whether the required properties for database connection configuration
     * ({@code DB_URL}, {@code DB_USER}, and {@code DB_PASS}) exist in the loaded properties file.
     * If any key is missing, an exception will be thrown with a description of the missing keys.</p>
     * 
     * @param props The {@link Properties} object representing the database connection settings.
     * 
     * @return {@code true} if validation fails (i.e., a required property is missing), {@code false} otherwise.
     * 
     * @throws IllegalArgumentException If one or more required keys are missing from the properties file.
     */
	public static boolean validateProps(Properties props) throws FileNotFoundException {
		boolean bError = false;

		String missingRequiredKey = "";

		if (!props.containsKey("DB_URL")) {
			bError = true;
			missingRequiredKey = "\nDB_URL";
		}

		if (!props.containsKey("DB_USER")) {
			bError = true;
			missingRequiredKey += "\nDB_USER";
		}

		if (!props.containsKey("DB_PASS")) {
			bError = true;
			missingRequiredKey += "\nDB_PASS";
		}

		if (bError)
			throw new IllegalArgumentException("\nMissing required key:" + missingRequiredKey);

		return bError;
	}

	/**
     * Closes one or more {@code AutoCloseable} resources.
     * 
     * <p>This method ensures that all resources passed to it (such as database connections, 
     * {@code ResultSet}, {@code Statement}, etc.) are properly closed to avoid resource leaks.</p>
     * 
     * <p>If no resources are provided, an {@link IllegalArgumentException} is thrown.</p>
     * 
     * @param resources One or more {@code AutoCloseable} resources to be closed.
     * 
     * @throws IllegalArgumentException If no resources are provided.
     * @throws RuntimeException If any error occurs while closing a resource.
     */
	public static void close(AutoCloseable ... resources) {

		if (resources.length == 0)
			throw new IllegalArgumentException("At least one AutoCloseable resource must be provided.");

		for (int i = resources.length - 1; i >= 0; i--) {
			AutoCloseable resource = resources[i];
			if (resource != null) {
				try {
					resource.close();
				} catch (Exception e) {
					throw new RuntimeException("Error occurred while attempting to release resource: " + resource + "\n" + e);
				}
			}
		}
	}
}