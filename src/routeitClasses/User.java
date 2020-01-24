package routeitClasses;

import java.util.ArrayList;

public class User {
	public User(Integer id,String username, String password, String email) {
		this.ID = id;
		this.username = username;
		this.password = password;
		this.email = email;
		savedTrips = new ArrayList<Trip>();
	}
	
	public void addTrip(Trip newTrip) {
		savedTrips.add(newTrip);
	}

	public ArrayList<Trip> savedTrips;
	public String username;
	public String password;
	public String email;
	public Integer ID; //SQL ID
}
