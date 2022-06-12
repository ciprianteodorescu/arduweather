package db;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
	
	public ResultSet listLastValue(int idArduino) throws Exception {
		ResultSet rs = null;
		try {
			String queryString = ("SELECT v.temperature, v.luminosity, v.time FROM sensors s INNER JOIN `values` v ON s.idSensor = v.idSensor WHERE s.idArduino = " + idArduino + " ORDER BY v.time DESC LIMIT 2;");
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
	
	public List<Measurement> listAverageThreeDaysValues(int idArduino) throws Exception {
		ResultSet rs = null;
		List<Measurement> finalMeasurements = new ArrayList<>();
		try {
			String queryString = ("SELECT v.temperature, v.luminosity, v.time FROM arduinos a inner join sensors s on a.idArduino = s.idArduino inner join `values` v on s.idSensor = v.idSensor where a.idArduino = " + idArduino + " ORDER BY v.time;");
			// last two values are needed because there are two sensors per arduino:
			// tempereature and light
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery(queryString);
			
			List<Measurement> allMeasurements = new ArrayList<>();
			int temperature = 0, light = 0;
			int i = 0;
			while(rs.next()) {
				int aux1 = rs.getInt("temperature");
				int aux2 = rs.getInt("luminosity");
				Timestamp timestamp = rs.getTimestamp("time");
				temperature = aux1 != 0 ? aux1 : temperature;
				light = aux2 != 0 ? aux2 : light;
				i++;
				//System.out.println("temp: " + temperature);
				if(i == 2) {
					allMeasurements.add(
							new Measurement(temperature, light, timestamp.toString())
							);
					i = 0;
					temperature = 0;
					light = 0;
				}
			}
			rs.close();
			
			int avgTemp = 0;
			int avgLight = 0;
			i = 0;
			if(allMeasurements.size() > 0) {
				Timestamp time = Timestamp.valueOf(allMeasurements.get(0).time);
				for(Measurement m : allMeasurements) {
					Timestamp mTime = Timestamp.valueOf(m.time);
					if(Integer.valueOf(time.getDate()) == Integer.valueOf(mTime.getDate())) {
						i++;
						avgTemp += m.temperature;
						avgLight += m.light;
					} else {
						if(i > 0) {
							avgTemp = avgTemp / i;
							avgLight = avgLight / i;
						}
						i = 0;
						String date = time.getDate() + "." + (time.getMonth() + 1) + "." + (time.getYear() + 1900);
						finalMeasurements.add(
								new Measurement(avgTemp, avgLight, date)
								);
						//System.out.print(finalMeasurements.get(finalMeasurements.size() - 1).temperature + ", ");
						time = mTime;
						i++;
						avgTemp = m.temperature;
						avgLight = m.light;
					}
				}
				if(i > 0) {
					avgTemp = avgTemp / i;
					avgLight = avgLight / i;
				}
				String date = time.getDate() + "." + (time.getMonth() + 1) + "." + (time.getYear() + 1900);
				finalMeasurements.add(
						new Measurement(avgTemp, avgLight, date)
						);
			}
		} catch (SQLException sqle) {
			error = "SQLException: Interogarea nu a fost posibila.";
			throw new SQLException(error);
		} catch (Exception e) {
			System.out.println(e);
			error = "A aparut o exceptie in timp ce se extrageau datele.";
			throw new Exception(error);
		}

		return finalMeasurements;
	} // listAverageThreeDaysValues()
	
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
	
	// for hourlyArduino.jsp
	public ArrayList<Measurement> getMeasurementsFromArduino(int idArduino) throws Exception {
		ResultSet rs = null;
		ArrayList<Measurement> measurements = new ArrayList<>();
		try {
			String queryString = ("SELECT v.time FROM arduinos a INNER JOIN sensors s ON a.idArduino = s.idArduino INNER JOIN `values` v ON s.idSensor = v.idSensor where a.idArduino = " + idArduino + " order by v.time desc limit 1;");
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery(queryString);
			Timestamp queryDate = null, queryNextDay = null;
			while(rs.next()) {
				queryDate = rs.getTimestamp("time");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(new java.util.Date(queryDate.getTime()));
				calendar.set(
					calendar.get(Calendar.YEAR),
					calendar.get(Calendar.MONTH),
					calendar.get(Calendar.DAY_OF_MONTH),
					0, 0, 0
				);
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				queryNextDay = new Timestamp(calendar.getTime().getTime());
			}
			rs.close();
			if(queryDate != null && queryNextDay != null) {
				String lastDay = queryDate.toString().substring(0, 10);
				String nextDay = queryNextDay.toString().substring(0, 10);
				queryString = ("SELECT s.idSensor, v.temperature, v.luminosity, v.time FROM arduinos a INNER JOIN sensors s ON a.idArduino = s.idArduino INNER JOIN `values` v ON s.idSensor = v.idSensor where a.idArduino = " + idArduino + " and v.time >= \"" + lastDay + "\" and v.time < \"" + nextDay + "\" order by v.time;");
			} else
				queryString = ("SELECT s.idSensor, v.temperature, v.luminosity, v.time FROM arduinos a INNER JOIN sensors s ON a.idArduino = s.idArduino INNER JOIN `values` v ON s.idSensor = v.idSensor where a.idArduino = " + idArduino + " order by v.time;");

			rs = stmt.executeQuery(queryString);
			while(rs.next()) {
				int t = rs.getInt("temperature");
				int l = rs.getInt("luminosity");
				Timestamp time = rs.getTimestamp("time");
				int mLast = measurements.size() - 1;
				if(mLast >= 0 && measurements.get(mLast).time.equals(time.toString())) {
					measurements.get(mLast).temperature = t != 0 ? t : measurements.get(mLast).temperature;
					measurements.get(mLast).light = l != 0 ? l : measurements.get(mLast).light;

				} else {
					String timeString = time.toString();
					//timeString = timeString.substring(0, timeString.length() - 2);
					measurements.add(
						new Measurement(t, l, timeString)
					);
				}
			}
			rs.close();
		} catch (SQLException sqle) {
			error = "SQLException: Interogarea nu a fost posibila.";
			throw new SQLException(sqle);
		} catch (Exception e) {
			error = "A aparut o exceptie in timp ce se extrageau datele.";
			throw new Exception(error);
		}
		
		return measurements;
	}
	
	public void postMeasurementToDB(int idArduino, int idSensorTemp, int idSensorLight, int temp, int light) {
		try {
			connect();
			String queryString = ("select current_timestamp() as time;");
			Statement stmt = con.createStatement();
			ResultSet rs = stmt.executeQuery(queryString);
			
			String time = "";
			rs.next();
			time = rs.getTimestamp("time").toString().substring(0, 19);
			System.out.println(time);
			rs.close();
			
			queryString = ("insert into `values` (idSensor, temperature, time) values (" + idSensorTemp + ", " + temp + ", '" + time + "');");
			stmt.executeUpdate(queryString);
			queryString = ("insert into `values` (idSensor, luminosity, time) values (" + idSensorLight + ", " + light + ", '" + time + "');");
			stmt.executeUpdate(queryString);
			disconnect();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
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