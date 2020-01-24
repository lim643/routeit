package routeitClasses;

public class Location {
	public Location(Double lat, Double lng) {
		this.lat = lat;
		this.lng = lng;
	}
	
	public Location(String googleID) {
		this.googleID = googleID;
	}
	
	public String type;
	public String address;
	public String name;
	public Double lat;
	public Double lng;
	public String googleID;
}
