package cz.angelo.angellobbycode;

import java.sql.*;

public class Mysql {

	private String host, port, database, username, password;
	private static Connection connection;
	private static Statement statement;

	public static Mysql instance;

	public Mysql(){
		instance = this;
	}

	public Mysql(String host, String port, String database, String username, String password){
		this.host = host;
		this.port = port;
		this.database = database;
		this.username = username;
		this.password = password;
	}

	public void openConnection(){
		try {
			if (connection != null && !connection.isClosed()){
				return;
			}
			connection = DriverManager.getConnection("jdbc:mysql://" + this.host+ ":" + this.port + "/" + this.database, this.username, this.password);
			statement = connection.createStatement();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public void closeConnection(){
		try {
			statement.close();
			connection.close();
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

	public static ResultSet getresult(String query){
		try {
			return statement.executeQuery(query);
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
		return null;
	}
	public static void result(String query){
		try {
			statement.executeUpdate(query);
		} catch (SQLException throwables) {
			throwables.printStackTrace();
		}
	}

}
