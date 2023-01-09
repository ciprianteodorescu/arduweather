package db;

public class Sensor extends DbEntry {
	public int idArduino;
	public String type;
	
	public Sensor() {}
	
	public Sensor(int id, int idArduino, String type) {
		this.id = id;
		this.idArduino = idArduino;
		this.type = type;
	}
	
	public Sensor(int idArduino, String type) {
		this.idArduino = idArduino;
		this.type = type;
	}
}
