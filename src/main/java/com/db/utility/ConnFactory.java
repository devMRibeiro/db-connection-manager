package com.db.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnFactory {
	public static Connection open() {

		Properties props = new Properties();

		try {

			props.load(ConnFactory.class.getClassLoader().getResourceAsStream("application.properties"));

			if (props == null || props.isEmpty()) {
				System.out.println("application.properties não encontrado ou arquivo mal-formatado.");
				return null;
			}

			String url = props.getProperty("DB_URL");
			String user = props.getProperty("DB_USER");
			String pass = props.getProperty("DB_PASS");
			
			return DriverManager.getConnection(url, user, pass);

		} catch (Exception e) {
			System.out.println("Erro ao tentar estabelecer uma conexão.");
			e.printStackTrace();
		}
		return null;
	}

	public static void close(AutoCloseable ... resources) {
		if (resources.length == 0) {
			System.out.println("Deve conter pelo menos 1 recurso AutoCloseable");
			return;
		}

		for (int i = resources.length - 1; i >= 0; i--) {
			AutoCloseable resource = resources[i];
			if (resource != null) {
				try {
					resource.close();
				} catch (Exception e) {
					System.out.println("Erro ao tentar liberar recurso: " + resource);
					e.printStackTrace();
				}
			}
		}
	}
}