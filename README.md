# ConnFactory - Java Database Utility

## Descrição

O projeto `db-connection-manager` fornece métodos para gerenciar uma conexão e liberar recursos `AutoCloseable` com Banco de Dados relacional em Java. Inclui:

- Um método para estabelecer uma conexão usando JDBC.
- Um método para liberar recursos, como `Connection`, `PreparedStatement` e `ResultSet`, com gerenciamento automático de exceções.

Este projeto é útil para aplicações Java que interagem com Banco de Dados, fornecendo uma abordagem limpa e reutilizável para genrenciar conexões e recursos.

## Funcionalidades

- **Estabelecer Conexão**: Estabelece uma conexão com método `open()` carregando as credenciais do banco que estão no arquivo `.properties`.
- **Gerenciamento de Recursos**: Libera recursos usando o método  `close()`.
- **Gerenciamento de Execeções**: Tratamento de erros ao tentar estebelecer conexão e liberar recursos.

## pré-requisitos

- Java 8 ou posterior.
- Banco de Dados compátivel com JDBC, como MySQL, PostgreSQL, entre outros.
- Arquivo `.properties` para dispor credenciais de acesso ao banco.

## Exemplo de uso:
```
public static void main(String[] args) {
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {

			conn = ConnFactory.open();
			pstm = conn.prepareStatement("SELECT * FROM your_table");
			rs = pstm.executeQuery();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnFactory.close(rs, pstm, conn);
		}
	}
```

---
## EN:

## Description

The `ConnFactory` project provides utility methods for managing database connections and resources in Java. It includes:

- A method to open a database connection using JDBC.
- A method to close multiple resources like `Connection`, `PreparedStatement`, and `ResultSet` with automatic handling of exceptions.

This project is useful for Java applications that interact with databases, providing a clean and reusable approach to managing database connections and resources.

## Features

- **Database Connection**: Open a database connection using properties loaded from `application.properties`.
- **Resource Management**: Free resources like `Connection`, `PreparedStatement`, `ResultSet` using the `close()` method.
- **Error Handling**: Proper error handling during connection opening and resource closing.

## Prerequisites

- Java 8 or higher.
- JDBC-compatible database (MySQL, PostgreSQL, etc.).
- `application.properties` configuration file for database credentials.

## Usage
```
public static void main(String[] args) {
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {

			conn = ConnFactory.open();
			pstm = conn.prepareStatement("SELECT * FROM your_table");
			rs = pstm.executeQuery();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			ConnFactory.close(rs, pstm, conn);
		}
	}
```
