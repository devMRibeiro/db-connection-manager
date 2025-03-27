package com.db.utility;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * A classe {@code ConnFactory} é responsável por gerenciar a criação e 
 * o fechamento de conexões com o banco de dados.
 * Ela fornece métodos estáticos para abrir e fechar conexões,
 * garantindo que os recursos sejam manipulados adequadamente.
 * 
 * <p>Este utilitário usa um arquivo de configuração de propriedades {@code application.properties}
 * para obter os dados necessários para a conexão com o banco, como a URL, o usuário e a senha.</p>
 * 
 * <p>O método {@code open()} tenta abrir uma conexão com o banco de dados com base nas
 * configurações fornecidas no arquivo {@code application.properties}.
 * Se o arquivo não for encontrado ou estiver mal-formatado, será exibida uma mensagem de erro.</p>
 * 
 * <p>O método {@code close()} é utilizado para fechar os recursos {@code AutoCloseable},
 * como conexões, ResultSets e Statements, garantindo que todos os recursos sejam corretamente liberados.</p>
 * 
 * @author Michael D. Ribeiro
 */
public class ConnFactory {

	/**
   * Estabelece uma conexão com o banco de dados utilizando as propriedades fornecidas
   * no arquivo {@code application.properties}.
   * 
   * <p>Este método carrega as propriedades de configuração do banco de dados
   * a partir do arquivo de propriedades e tenta estabelecer uma conexão utilizando o {@code DriverManager}.
   * Se o arquivo de propriedades não for encontrado ou estiver mal-formatado, será retornado {@code null}.</p>
   *
   * @return Uma instância de {@link Connection} representando a conexão com o banco de dados,
   * 					ou {@code null} se ocorrer um erro ao tentar estabelecer a conexão.
   */
	public static Connection open() {
		Properties props = new Properties();
		try {

			props.load(ConnFactory.class.getClassLoader().getResourceAsStream("application.properties"));

			if (props == null || props.isEmpty())
				throw new IllegalArgumentException("application.properties não encontrado ou arquivo mal-formatado.");

			return DriverManager.getConnection(
					props.getProperty("DB_URL"),
					props.getProperty("DB_USER"),
					props.getProperty("DB_PASS"));

		} catch (Exception e) {
			throw new RuntimeException("Erro ao tentar estabelecer uma conexão.", e);
		}
	}

	/**
   * Libera os recursos {@code AutoCloseable} passados como parâmetros.
   * 
   * <p>Este método percorre todos os recursos passados no array e tenta fechá-los, começando pelo último recurso. 
   * Caso um recurso não possa ser fechado corretamente, uma mensagem de erro será exibida. 
   * É importante garantir que todos os recursos sejam fechados para evitar vazamentos de memória ou conexões.</p>
   * 
   * @param resources Array de recursos {@code AutoCloseable} a serem fechados. 
   *         Ao menos um recurso deve ser fornecido, caso contrário, uma mensagem de erro será exibida.
   */
	public static void close(AutoCloseable ... resources) {

		if (resources.length == 0)
			throw new IllegalArgumentException("Deve conter pelo menos 1 recurso AutoCloseable");

		for (int i = resources.length - 1; i >= 0; i--) {
			AutoCloseable resource = resources[i];
			if (resource != null) {
				try {
					resource.close();
				} catch (Exception e) {
					throw new RuntimeException("Erro ao tentar liberar recurso: " + resource + "\n" + e);
				}
			}
		}
	}
}