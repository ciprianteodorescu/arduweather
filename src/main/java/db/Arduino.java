package db;

public class Arduino extends DbEntry {
	public String name;
	public String city;
	public String country;
	
	public Arduino() {}
	
	public Arduino(int id, String name, String city, String country) {
		this.id = id;
		this.name = name;
		this.city = city;
		this.country = country;
	}
	
	public Arduino(String name, String city, String country) {
		this.name = name;
		this.city = city;
		this.country = country;
	}
}
