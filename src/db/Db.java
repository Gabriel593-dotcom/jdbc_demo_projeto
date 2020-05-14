package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Db {

	private static Connection con = null; // objeto de conexão com o banco de dados do jdbc.

	// método que abre a conexão com o banco.
	public static Connection getConnection() {

		try {
			if (con == null) {
				Properties props = loadProperties();
				String url = props.getProperty("dburl");
				con = DriverManager.getConnection(url, props);
				// conectar em um banco de dados em jdbc, é instanciar um objeto
				// to tipo Connection.
			}
		}

		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}

		return con;
	}

	// método que carrega os atributos necessários para conexão com o banco de dados
	// através do service FileInputStream e passando como parametro pro método
	// construtor um arquivo que contém essas informações e carregando esses valores
	// à um objeto da classe Properties, usando o método load da classe properties.

	private static Properties loadProperties() {

		try (FileInputStream fs = new FileInputStream("db.properties")) {
			Properties props = new Properties();
			props.load(fs); // O comando load faz a leitura dos dados do arquivo
							// apontados pelo 'fs' e carrega essas informações no objeto
							// props.
			return props;
		}

		catch (IOException e) {
			throw new DbException(e.getMessage());
		}
	}

	// método que fecha a conexão com o banco.
	public static void closeConnection() {
		try {
			if (con != null) {
				con.close();
			}
		}

		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}

	}

	// OBS: foi criada uma classe de exception personalizada, porque a classe
	// SQLException é derivada da classe exception, então para não ficarmos
	// tratando o códico com try/catch toda vez, criamos uma classe de exception
	// personalizada que deriva da classe RuntimeException.

	public static void closeStatment(Statement st) {

		if (st != null) {
			try {
				st.close();

			}

			catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}

	public static void closeResultSet(ResultSet rs) {

		if (rs != null) {
			try {
				rs.close();

			}

			catch (SQLException e) {
				throw new DbException(e.getMessage());
			}
		}
	}

}
