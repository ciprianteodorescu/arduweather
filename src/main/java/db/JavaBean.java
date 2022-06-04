package db;

import java.io.Console;
import java.sql.*;
import java.util.ArrayList;

import javax.xml.transform.Templates;

import com.mysql.cj.xdevapi.Schema;

public class JavaBean {

	String error;
	Connection con;
	final String SCHEMA = "arduweather";

	public JavaBean() {
	}

	public void connect() throws ClassNotFoundException, SQLException, Exception {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/arduweather?useSSL=false", "ciprian", "Ciprian2021.");
		} catch (ClassNotFoundException cnfe) {
			error = "ClassNotFoundException: Nu s-a gasit driverul bazei de date.";
			throw new ClassNotFoundException(error);
		} catch (SQLException cnfe) {
			error = "SQLException: Nu se poate conecta la baza de date.";
			throw new SQLException(error);
		} catch (Exception e) {
			error = "Exception: A aparut o exceptie neprevazuta in timp ce se stabilea legatura la baza de date.";
			throw new Exception(error);
		}
	} // connect()

	public void connect(String bd) throws ClassNotFoundException, SQLException, Exception {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/" + bd, "ciprian", "Ciprian2021.");
		} catch (ClassNotFoundException cnfe) {
			error = "ClassNotFoundException: Nu s-a gasit driverul bazei de date.";
			throw new ClassNotFoundException(error);
		} catch (SQLException cnfe) {
			error = "SQLException: Nu se poate conecta la baza de date.";
			throw new SQLException(error);
		} catch (Exception e) {
			error = "Exception: A aparut o exceptie neprevazuta in timp ce se stabilea legatura la baza de date.";
			throw new Exception(error);
		}
	} // connect(String bd)

	public void connect(String bd, String ip) throws ClassNotFoundException, SQLException, Exception {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/" + bd, "ciprian", "Ciprian2021.");
		} catch (ClassNotFoundException cnfe) {
			error = "ClassNotFoundException: Nu s-a gasit driverul bazei de date.";
			throw new ClassNotFoundException(error);
		} catch (SQLException cnfe) {
			error = "SQLException: Nu se poate conecta la baza de date.";
			throw new SQLException(error);
		} catch (Exception e) {
			error = "Exception: A aparut o exceptie neprevazuta in timp ce se stabilea legatura la baza de date.";
			throw new Exception(error);
		}
	} // connect(String bd, String ip)

	public void disconnect() throws SQLException {
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException sqle) {
			error = ("SQLException: Nu se poate inchide conexiunea la baza de date.");
			throw new SQLException(error);
		}
	} // disconnect()

	public void addArduino(String name, String city, String country)
			throws SQLException, Exception {
		if (con != null) {
			try {
				// create a prepared SQL statement
				Statement stmt;
				stmt = con.createStatement();
				stmt.executeUpdate("insert into arduinos(name, city, country) values('" + name + "', '" + city + "', '" + country + "');");

			} catch (SQLException sqle) {
				error = "ExceptieSQL: Reactualizare nereusita; este posibil sa existe duplicate.";
				throw new SQLException(error);
			}
		} else {
			error = "Exceptie: Conexiunea cu baza de date a fost pierduta.";
			throw new Exception(error);
		}
	} // end of addArduino()

	public void addSensor(int idArduino, String type)
			throws SQLException, Exception {
		if (con != null) {
			try {
				// create a prepared SQL statement
				Statement stmt;
				stmt = con.createStatement();
				stmt.executeUpdate("insert into sensors(idArduino, type) values(" + idArduino + ", '" + type + ");");

			} catch (SQLException sqle) {
				error = "ExceptieSQL: Reactualizare nereusita; este posibil sa existe duplicate.";
				throw new SQLException(error);
			}
		} else {
			error = "Exceptie: Conexiunea cu baza de date a fost pierduta.";
			throw new Exception(error);
		}
	} // end of addSensor()

	public void addTemperature(int idSensor, int temperature)
			throws SQLException, Exception {
		if (con != null) {
			try {
				// create a prepared SQL statement
				Statement stmt;
				stmt = con.createStatement();
				stmt.executeUpdate("insert into `values`(idSensor, temperature, time) values(" + idSensor + ", " + temperature + ", current_timestamp);");

			} catch (SQLException sqle) {
				error = "ExceptieSQL: Reactualizare nereusita; este posibil sa existe duplicate.";
				throw new SQLException(error);
			}
		} else {
			error = "Exceptie: Conexiunea cu baza de date a fost pierduta.";
			throw new Exception(error);
		}
	} // end of addTemperature()

	public void addLuminosity(int idSensor, int luminosity)
			throws SQLException, Exception {
		if (con != null) {
			try {
				// create a prepared SQL statement
				Statement stmt;
				stmt = con.createStatement();
				stmt.executeUpdate("insert into `values`(idSensor, luminosity, time) values(" + idSensor + ", " + luminosity + ", current_timestamp);");

			} catch (SQLException sqle) {
				error = "ExceptieSQL: Reactualizare nereusita; este posibil sa existe duplicate.";
				throw new SQLException(error);
			}
		} else {
			error = "Exceptie: Conexiunea cu baza de date a fost pierduta.";
			throw new Exception(error);
		}
	} // end of addLuminosity()
	
	public ResultSet listArduinos() throws Exception {
		ResultSet rs = null;
		try {
			String queryString = ("select * from `arduinos`;");
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery(queryString);
			
		} catch (SQLException sqle) {
			error = "SQLException: Interogarea nu a fost posibila.";
			throw new SQLException(error);
		} catch (Exception e) {
			error = "A aparut o exceptie in timp ce se extrageau datele.";
			throw new Exception(error);
		}
		return rs;
	} // listArduinos()
	
	public ResultSet listTable(String table) throws SQLException, Exception {
		ResultSet rs = null;
		try {
			String queryString = ("select * from `" + SCHEMA + "`.`" + table + "`;");
			Statement stmt = con.createStatement(/*ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY*/);
			rs = stmt.executeQuery(queryString);
		} catch (SQLException sqle) {
			error = "SQLException: Interogarea nu a fost posibila.";
			throw new SQLException(error);
		} catch (Exception e) {
			error = "A aparut o exceptie in timp ce se extrageau datele.";
			throw new Exception(error);
		}
		return rs;
	} // listTable()

	public ResultSet listValues() throws SQLException, Exception {
		ResultSet rs = null;
		try {
			String queryString = ("SELECT a.idArduino idArduino, s.idSensor idSensor, v.idValue idValue, v.temperature temperature, v.luminosity luminosity, v.time time FROM arduinos a INNER JOIN sensors s ON a.idArduino = s.idArduino INNER JOIN `values` v ON s.idSensor = v.idSensor;");
			Statement stmt = con.createStatement(/*ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY*/);
			rs = stmt.executeQuery(queryString);
		} catch (SQLException sqle) {
			error = "SQLException: Interogarea nu a fost posibila.";
			throw new SQLException(error);
		} catch (Exception e) {
			error = "A aparut o exceptie in timp ce se extrageau datele.";
			throw new Exception(error);
		}
		return rs;
	} // listValues()
	
	public ResultSet listLastValue() throws Exception {
		ResultSet rs = null;
		try {
			String queryString = ("SELECT * FROM `values` ORDER BY idValue DESC LIMIT 2;");
			// last two values are needed because there are two sensors per arduino:
			// tempereature and light
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery(queryString);
		} catch (SQLException sqle) {
			error = "SQLException: Interogarea nu a fost posibila.";
			throw new SQLException(error);
		} catch (Exception e) {
			error = "A aparut o exceptie in timp ce se extrageau datele.";
			throw new Exception(error);
		}
		return rs;
	} // listLastValue()
	
	public String getArduinoLocation(int idArduino) throws Exception {
		ResultSet rs = null;
		String location = "";
		try {
			String queryString = ("select * from arduinos where idArduino = " + idArduino + ";");
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery(queryString);
			rs.next();
			location = rs.getString("city") + ", " + rs.getString("country");
			rs.close();
		} catch (SQLException sqle) {
			error = "SQLException: Interogarea nu a fost posibila.";
			throw new SQLException(error);
		} catch (Exception e) {
			error = "A aparut o exceptie in timp ce se extrageau datele.";
			throw new Exception(error);
		}
		return location;
	} // getArduinoLocation()
	
	public ArrayList<Measurement> getMeasurementsFromArduino(int idArduino) throws Exception {
		ResultSet rs = null;
		ArrayList<Measurement> measurements = new ArrayList<>();
		try {
			String queryString = ("SELECT s.idSensor, v.temperature, v.luminosity, v.time FROM arduinos a INNER JOIN sensors s ON a.idArduino = s.idArduino INNER JOIN `values` v ON s.idSensor = v.idSensor where a.idArduino = " + idArduino + ";");
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery(queryString);
			while(rs.next()) {
				int t = rs.getInt("temperature");
				int l = rs.getInt("luminosity");
				String time = rs.getString("time");
				int mLast = measurements.size() - 1;
				if(mLast >= 0 && measurements.get(mLast).time.equals(time)) {
					measurements.get(mLast).temperature = t != 0 ? t : measurements.get(mLast).temperature;
					measurements.get(mLast).light = l != 0 ? l : measurements.get(mLast).light;

				} else {
					measurements.add(
						new Measurement(t, l, time)
					);
				}
			}
			rs.close();
		} catch (SQLException sqle) {
			error = "SQLException: Interogarea nu a fost posibila.";
			throw new SQLException(error);
		} catch (Exception e) {
			error = "A aparut o exceptie in timp ce se extrageau datele.";
			throw new Exception(error);
		}
//		for(Measurement m : measurements) {
//			System.out.println(m.time + ", " + m.temperature + ", " + m.light);
//		}
		return measurements;
	}

	public void deleteFromTable(String[] primaryKeys, String table, String id) throws SQLException, Exception {
		if (con != null) {
			try {
				// create a prepared SQL statement
				long aux;
				PreparedStatement delete;
				delete = con.prepareStatement("DELETE FROM `" + table + "` WHERE " + id + "=?;");
				for (int i = 0; i < primaryKeys.length; i++) {
					aux = java.lang.Long.parseLong(primaryKeys[i]);
					delete.setLong(1, aux);
					delete.execute();
				}
			} catch (SQLException sqle) {
				error = "ExceptieSQL: Reactualizare nereusita; este posibil sa existe duplicate.";
				throw new SQLException(error);
			} catch (Exception e) {
				error = "A aparut o exceptie in timp ce erau sterse inregistrarile.";
				throw new Exception(error);
			}
		} else {
			error = "Exceptie: Conexiunea cu baza de date a fost pierduta.";
			throw new Exception(error);
		}
	} // end of deleteFromTable()

	public void deleteTable(String table) throws SQLException, Exception {
		if (con != null) {
			try {
				// create a prepared SQL statement
				Statement stmt;
				stmt = con.createStatement();
				stmt.executeUpdate("delete from `" + table + "`;");
			} catch (SQLException sqle) {
				error = "ExceptieSQL: Stergere nereusita; este posibil sa existe duplicate.";
				throw new SQLException(error);
			}
		} else {
			error = "Exceptie: Conexiunea cu baza de date a fost pierduta.";
			throw new Exception(error);
		}
	} // end of deleteTable()

//	public void updateTable(String table, String IDTabela, int ID, String[] campuri, String[] valori) throws SQLException, Exception {
//		String update = "update `" + table + "` set ";
//		String temp = "";
//		if (con != null) {
//			try {
//				for (int i = 0; i < campuri.length; i++) {
//					if (i != (campuri.length - 1)) {
//						temp = temp + campuri[i] + "='" + valori[i] + "', ";
//					} else {
//						temp = temp + campuri[i] + "='" + valori[i] + "' where " + IDTabela + " = '" + ID + "';";
//					}
//				}
//				update = update + temp;
//				// create a prepared SQL statement
//				Statement stmt;
//				stmt = con.createStatement();
//				stmt.executeUpdate(update);
//			} catch (SQLException sqle) {
//				error = "ExceptieSQL: Reactualizare nereusita; este posibil sa existe duplicate.";
//				throw new SQLException(error);
//			}
//		} else {
//			error = "Exceptie: Conexiunea cu baza de date a fost pierduta.";
//			throw new Exception(error);
//		}
//	} // end of updateTable()

	public ResultSet returnLine(String table, int id) throws SQLException, Exception {
		ResultSet rs = null;
		try {
			// Execute query
			String queryString = ("SELECT * FROM `" + table + "` where idValue=" + id + ";");
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(queryString); //sql exception
		} catch (SQLException sqle) {
			error = "SQLException: Interogarea nu a fost posibila.";
			throw new SQLException(error);
		} catch (Exception e) {
			error = "A aparut o exceptie in timp ce se extrageau datele.";
			throw new Exception(error);
		}
		return rs;
	} // end of returnLine()

	public ResultSet returnLineById(String table, String idName, int id) throws SQLException, Exception {
		ResultSet rs = null;
		try {
			// Execute query
			String queryString = ("SELECT * FROM `" + table + "` where " + idName + "=" + id + ";");
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(queryString); //sql exception
		} catch (SQLException sqle) {
			error = "SQLException: Interogarea nu a fost posibila.";
			throw new SQLException(error);
		} catch (Exception e) {
			error = "A aparut o exceptie in timp ce se extrageau datele.";
			throw new Exception(error);
		}
		return rs;
	} // end of returnLineById()

	public ResultSet returnValueById(int id) throws SQLException, Exception {
		ResultSet rs = null;
		try {
			// Execute query
			String queryString = ("SELECT a.idArduino, s.idSensor, v.idValue, v.temperature, v.luminosity, v.time FROM arduinos a INNER JOIN sensors s ON a.idArduino = s.idArduino INNER JOIN `values` v ON s.idSensor = v.idSensor WHERE idValue = " + id + ";");
			Statement stmt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
			rs = stmt.executeQuery(queryString); //sql exception
		} catch (SQLException sqle) {
			error = "SQLException: Interogarea nu a fost posibila.";
			throw new SQLException(error);
		} catch (Exception e) {
			error = "A aparut o exceptie in timp ce se extrageau datele.";
			throw new Exception(error);
		}
		return rs;
	} // end of returnValueById()
	
	public class Measurement{
		public int temperature;
		public int light;
		public String time;
		
		public Measurement() {
		}
		
		public Measurement(int temp, int light, String time) {
			this.temperature = temp;
			this.light = light;
			this.time = time;
		}
	}
}