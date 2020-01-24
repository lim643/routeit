package routeitClasses;

import java.util.ArrayList;

public class Trip {
	public Trip(String name, String username, Integer ID) {
		this.name = name;
		this.username = username;
		this.ID = ID; 
	}
	
	public Trip(String name, Location start, Location end, ArrayList<Location> stops) {
		this.name = name;
		this.startLoc = start;
		this.endLoc = end;
		this.stops = stops;
	}
	
	public Trip(Integer ID, String name, Location start, Location end, ArrayList<Location> stops) {
		this.ID = ID;
		this.name = name;
		this.startLoc = start;
		this.endLoc = end;
		this.stops = stops;
	}

	public Trip(Integer iD2, String name2, String start, String end, ArrayList<Location> stops2) {
		this.ID = iD2;
		this.name = name2;
		this.startLoc = new Location(start);
		this.endLoc = new Location(end);
		this.stops = stops2;
	}

	public Integer ID; //SQL ID
	public String name;
	public Location startLoc;
	public Location endLoc;
	public ArrayList<Location> stops;
	public String username;
}
