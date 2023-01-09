package db;

import java.util.Date;

public class Value extends DbEntry {
	public int idSensor;
	public int temperature;
	public int luminosity;
	public Date time;
	
	public Value() {}
	
	public Value(int id, int idSensor, int temperature, int luminosity, Date time) {
		this.id = id;
		this.idSensor = idSensor;
		this.temperature = temperature;
		this.luminosity = luminosity;
		this.time = time;
	}
	
	public Value(int idSensor, int temperature, int luminosity, Date time) {
		this.idSensor = idSensor;
		this.temperature = temperature;
		this.luminosity = luminosity;
		this.time = time;
	}
}
