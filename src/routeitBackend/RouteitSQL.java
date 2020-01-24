package routeitBackend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import routeitClasses.Location;
import routeitClasses.Trip;
import routeitClasses.User;

public class RouteitSQL {
	String dataBase = "routeIt";
	String SQLURL = "jdbc:mysql://localhost:3306/";
	String SQLuser = "root";
	String SQLpass = "root";
	String SQLclass = "com.mysql.cj.jdbc.Driver";
	
	public RouteitSQL() {
		Connection SQLconn = null;
		try {
			Class.forName(SQLclass);
			System.out.println("Check if " + dataBase + " exists");
			SQLconn = DriverManager.getConnection(SQLURL+dataBase+"?user="+SQLuser+"&password="+SQLpass);
		} catch (ClassNotFoundException cnfe) {
			System.out.println(cnfe.getMessage());
		} catch (SQLException sqle) {
			if (sqle.getMessage().contains("Unknown database")) {
				System.out.println("Creating " + dataBase);
				CreateDatabase();
			} else {
				System.out.println(sqle.getMessage());
			}
		} finally {
			try {
				if (SQLconn != null) { SQLconn.close(); }
			} catch (SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
		}
	}
	
	//Creates the database (if on new server)
	private void CreateDatabase() {
		Connection SQLconn = null;
		Statement DBstmt = null;
		
		Connection DBconn = null;
		Statement Userstmt = null;
		Statement Savedstmt = null;
		Statement Stopstmt = null;
		Statement Pubstmt = null;
		Statement PubStopstmt = null;
		try {
			//Creates the actual Database
			Class.forName(SQLclass);
			SQLconn = DriverManager.getConnection(SQLURL, SQLuser, SQLpass);
			DBstmt = SQLconn.createStatement();
			DBstmt.executeUpdate("CREATE DATABASE " + dataBase + ";");
			
			//Create tables in Database
			System.out.println("Creating User Table");
			DBconn = DriverManager.getConnection(SQLURL+dataBase+"?user="+SQLuser+"&password="+SQLpass);
			Userstmt = DBconn.createStatement();
			Userstmt.executeUpdate("CREATE TABLE Users (\r\n" + 
					"	userID INT(11) PRIMARY KEY AUTO_INCREMENT,\r\n" + 
					"    name VARCHAR(50) NOT NULL,\r\n" + 
					"    pass VARCHAR(50) NOT NULL,\r\n" + 
					"    email VARCHAR(50) NOT NULL\r\n" + 
					");");
			System.out.println("Creating SavedTrips Table");
			Savedstmt = DBconn.createStatement();
			Savedstmt.executeUpdate("CREATE TABLE SavedTrips (\r\n" + 
					"	tripID INT(11) PRIMARY KEY AUTO_INCREMENT,\r\n" + 
					"    userID INT(11) NOT NULL,\r\n" + 
					"    name VARCHAR(50) NOT NULL,\r\n" + 
					"    startLocation VARCHAR(100) NOT NULL,\r\n" + 
					"    endLocation VARCHAR(100) NOT NULL,\r\n" + 
					"    savedTime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,\r\n" + 
					"    FOREIGN KEY (userID) REFERENCES Users(userID)\r\n" + 
					");");
			System.out.println("Creating Stops Table");
			Stopstmt = DBconn.createStatement();
			Stopstmt.executeUpdate("CREATE TABLE Stops (\r\n" + 
					"	stopID INT(11) PRIMARY KEY AUTO_INCREMENT,\r\n" + 
					"	tripID INT(11) NOT NULL,\r\n" + 
					"    stopNum INT(11) NOT NULL,\r\n" + 
					"    location VARCHAR(100) NOT NULL\r\n" + 
					");");
			System.out.println("Creating Public Trips Table");
			Pubstmt = DBconn.createStatement();
			Pubstmt.executeUpdate("CREATE TABLE PublicTrips (\r\n" + 
					"	pubID INT(11) PRIMARY KEY AUTO_INCREMENT,\r\n" + 
					"    username VARCHAR(50) NOT NULL,\r\n" + 
					"    name VARCHAR(50) NOT NULL,\r\n" + 
					"    startLocation VARCHAR(100) NOT NULL,\r\n" + 
					"    endLocation VARCHAR(100) NOT NULL,\r\n" + 
					"    savedTime TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP\r\n" + 
					");");
			System.out.println("Creating Public Stops Table");
			PubStopstmt = DBconn.createStatement();
			PubStopstmt.executeUpdate("CREATE TABLE PublicStops (\r\n" + 
					"	pubstopID INT(11) PRIMARY KEY AUTO_INCREMENT,\r\n" + 
					"    pubID VARCHAR(50) NOT NULL,\r\n" + 
					"    stopNum INT(11) NOT NULL,\r\n" + 
					"    location VARCHAR(100) NOT NULL\r\n" + 
					");");
			System.out.println("Successfully created " + dataBase);
		} catch (ClassNotFoundException cnfe) {
			System.out.println(cnfe.getMessage());
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				if (SQLconn != null) { SQLconn.close(); }
				if (DBstmt != null) { DBstmt.close(); }
				if (DBconn != null) { DBconn.close(); }
				if (Userstmt != null) { Userstmt.close(); }
				if (Savedstmt != null) { Savedstmt.close(); }
				if (Stopstmt != null) { Stopstmt.close(); }
				
			} catch (SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
		}
	}
	
	//Saves the registered user into the database and returns the saved user
	//Assumes that the username does not exist already
	public User createUser(String username, String password, String email) {
		User newUser = null;
		Connection DBconn =  null;
		Statement CreateUserstmt = null;
		String sqlStmt = "";
		try {
			Class.forName(SQLclass);
			DBconn = DriverManager.getConnection(SQLURL+dataBase+"?user="+SQLuser+"&password="+SQLpass);
			System.out.println("Inserting user " + username + " into User Table");
			sqlStmt = "INSERT INTO users (name, pass, email) VALUES " + "('"+username+"','"+password+"','"+email+"');";
			CreateUserstmt = DBconn.createStatement();
			CreateUserstmt.executeUpdate(sqlStmt);
			newUser = getUser(username);
			System.out.println("Successfully created new user: " + username);
		} catch (ClassNotFoundException cnfe) {
			System.out.println(cnfe.getMessage());
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				if (DBconn != null) { DBconn.close(); }
				if (CreateUserstmt != null) { CreateUserstmt.close(); }
			} catch (SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
		}
		return newUser;
	}
	
	//Returns null if user does not exist
	public User getUser(String username) {
		System.out.println("Get User Function in SQL Factory");
		User getUser = null;
		String name = null;
		String password = null;
		String email = null;
		Integer ID = null;
		
		Connection DBconn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName(SQLclass);
			DBconn = DriverManager.getConnection(SQLURL+dataBase+"?user="+SQLuser+"&password="+SQLpass);
			System.out.println("Querying for " + username + " in  the User table");
			ps = DBconn.prepareStatement("SELECT * FROM USERS WHERE NAME=?");
			ps.setString(1, username);
			rs = ps.executeQuery();
			if (rs.next()) {
				System.out.println(username + " exists");
				ID = rs.getInt("userID");
				name = rs.getString("name");
				password = rs.getString("pass");
				email = rs.getString("email");
				getUser = new User(ID, name, password, email);
				getUser.savedTrips = getTrips(getUser);
			} else {
				System.out.println(username + " does not exist");
			}
		} catch (ClassNotFoundException cnfe) {
			System.out.println(cnfe.getMessage());
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				if (DBconn != null) { DBconn.close(); }
				if (ps != null) { ps.close(); }
				if (rs != null) { rs.close(); }
			} catch (SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
		}
		
		return getUser;
	}
	
	//Saves the inputed trip
	public void saveTrip(User curUser, Trip curTrip) {
		Connection DBconn =  null;
		Statement CreateUserstmt = null;
		String sqlStmt = "";
		try {
			Class.forName(SQLclass);
			DBconn = DriverManager.getConnection(SQLURL+dataBase+"?user="+SQLuser+"&password="+SQLpass);
			System.out.println("Inserting new trip " + curTrip.name + " into SavedTrips Table");
			sqlStmt = "INSERT INTO savedtrips (userID, name, startLocation, endLocation) VALUES " +
					"("+curUser.ID+",'"+curTrip.name+"','"+curTrip.startLoc.googleID+"','"+curTrip.endLoc.googleID+"');";
			CreateUserstmt = DBconn.createStatement();
			CreateUserstmt.executeUpdate(sqlStmt);
			curTrip.ID = getTrips(curUser).get(0).ID;
			saveStops(curTrip);
			System.out.println("Successfully added new Saved Trip");
		} catch (ClassNotFoundException cnfe) {
			System.out.println(cnfe.getMessage());
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				if (DBconn != null) { DBconn.close(); }
				if (CreateUserstmt != null) { CreateUserstmt.close(); }
			} catch (SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
		}
	}

	//Updates existing trip
	public void updateTrip(User curUser, Trip curTrip) {
		deleteTrip(curTrip);
		saveTrip(curUser, curTrip);
	}
	
	//Delete existing trip
	public void deleteTrip(Trip curTrip) {
		Connection DBconn =  null;
		Statement CreateUserstmt = null;
		String sqlStmt = "";
		try {
			Class.forName(SQLclass);
			DBconn = DriverManager.getConnection(SQLURL+dataBase+"?user="+SQLuser+"&password="+SQLpass);
			System.out.println("Deleting trip " + curTrip.name + " from SavedTrips Table");
			sqlStmt = "DELETE FROM savedtrips WHERE tripID="+curTrip.ID.toString();
			CreateUserstmt = DBconn.createStatement();
			CreateUserstmt.executeUpdate(sqlStmt);
			deleteStops(curTrip);
			System.out.println("Successfully deleted "+curTrip.name+" from Database");
		} catch (ClassNotFoundException cnfe) {
			System.out.println(cnfe.getMessage());
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				if (DBconn != null) { DBconn.close(); }
				if (CreateUserstmt != null) { CreateUserstmt.close(); }
			} catch (SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
		}
	}
	
	public void makePublic(User curUser, Trip curTrip) {
		Connection DBconn =  null;
		Statement CreateUserstmt = null;
		String sqlStmt = "";
		try {
			Class.forName(SQLclass);
			DBconn = DriverManager.getConnection(SQLURL+dataBase+"?user="+SQLuser+"&password="+SQLpass);
			System.out.println("Adding " + curTrip.name + " into PublicTrips Table");
			sqlStmt = "INSERT INTO publictrips (username, name, startLocation, endLocation) VALUES " +
					"('"+curUser.username+"','"+curTrip.name+"','"+curTrip.startLoc.googleID+"','"+curTrip.endLoc.googleID+"');";
			CreateUserstmt = DBconn.createStatement();
			CreateUserstmt.executeUpdate(sqlStmt);
			Integer ID = -1;
		
			ArrayList<Trip> pubTrips = makePublicGetID();
			for (Trip trip : pubTrips) {
				if (trip.name.equalsIgnoreCase(curTrip.name) && trip.username.equalsIgnoreCase(curUser.username)) {
					ID = trip.ID;
				}
			}
			
			savePublicStops(curTrip, ID);
			System.out.println("Successfully made " + curTrip.name + " public.");
		} catch (ClassNotFoundException cnfe) {
			System.out.println(cnfe.getMessage());
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				if (DBconn != null) { DBconn.close(); }
				if (CreateUserstmt != null) { CreateUserstmt.close(); }
			} catch (SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
		}
	}
	
	public ArrayList<Trip> getPublicTrips() {
		ArrayList<Trip> savedTrips = new ArrayList<Trip>();
		
		Connection DBconn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName(SQLclass);
			DBconn = DriverManager.getConnection(SQLURL+dataBase+"?user="+SQLuser+"&password="+SQLpass);
			System.out.println("Querying for public trips");
			ps = DBconn.prepareStatement("SELECT * FROM PUBLICTRIPS ORDER BY SAVEDTIME DESC");
			rs = ps.executeQuery();
			
			Integer ID = null;
			String name = null;
			String start = null;
			String end = null;
			ArrayList<Location> stops = null;
			
			while (rs.next()) {
				ID = rs.getInt("pubID");
				name = rs.getString("name");
				start = rs.getString("startLocation");
				end = rs.getString("endLocation");
				stops = getPublicStops(ID);
				savedTrips.add(new Trip(ID, name, start, end, stops));
				savedTrips.get(savedTrips.size()-1).username = rs.getString("username");
			}

		} catch (ClassNotFoundException cnfe) {
			System.out.println(cnfe.getMessage());
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				if (DBconn != null) { DBconn.close(); }
				if (ps != null) { ps.close(); }
				if (rs != null) { rs.close(); }
			} catch (SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
		}
		System.out.println("Found all public trips");
		return savedTrips;
	}
	
	/*
	 *
	 *
	 *`
	 *	Helper functions 
	 *
	 *
	 *
	 */
	
	//Deletes each stop in the trip from stops table
	private void deleteStops(Trip curTrip) {
		Connection DBconn =  null;
		Statement CreateUserstmt = null;
		String sqlStmt = "";
		try {
			Class.forName(SQLclass);
			DBconn = DriverManager.getConnection(SQLURL+dataBase+"?user="+SQLuser+"&password="+SQLpass);
			System.out.println("Deleting stops " + curTrip.name + " from Stops Table");
			sqlStmt = "DELETE FROM stops WHERE tripID="+curTrip.ID.toString();
			CreateUserstmt = DBconn.createStatement();
			CreateUserstmt.executeUpdate(sqlStmt);
		} catch (ClassNotFoundException cnfe) {
			System.out.println(cnfe.getMessage());
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				if (DBconn != null) { DBconn.close(); }
				if (CreateUserstmt != null) { CreateUserstmt.close(); }
			} catch (SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
		}
	}
	
	//Inserts each stop into the SQL database
	private void saveStops(Trip curTrip) {
		Connection DBconn =  null;
		Statement createStopStmt = null;
		String sqlStmt = "";
		try {
			Class.forName(SQLclass);
			DBconn = DriverManager.getConnection(SQLURL+dataBase+"?user="+SQLuser+"&password="+SQLpass);
			System.out.println("Inserting each stop into " + curTrip.name + " into Stops Table");
			for (Integer i=0; i<curTrip.stops.size(); ++i) {
				sqlStmt = "INSERT INTO stops (tripID, stopNum, location) VALUES " +
						"("+curTrip.ID+","+i+",'"+curTrip.stops.get(i).googleID+"');";
				createStopStmt = DBconn.createStatement();
				createStopStmt.executeUpdate(sqlStmt);
			}
		} catch (ClassNotFoundException cnfe) {
			System.out.println(cnfe.getMessage());
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				if (DBconn != null) { DBconn.close(); }
				if (createStopStmt != null) { createStopStmt.close(); }
			} catch (SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
		}
	}
	
	//Returns an empty arraylist if there are no saved trips
	private ArrayList<Trip> getTrips(User curUser) {
		ArrayList<Trip> savedTrips = new ArrayList<Trip>();
		
		Connection DBconn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName(SQLclass);
			DBconn = DriverManager.getConnection(SQLURL+dataBase+"?user="+SQLuser+"&password="+SQLpass);
			System.out.println("Querying for " + curUser.username + "'s trips");
			ps = DBconn.prepareStatement("SELECT * FROM SAVEDTRIPS WHERE userID=? ORDER BY SAVEDTIME DESC");
			ps.setInt(1, curUser.ID);
			rs = ps.executeQuery();
			
			Integer ID = null;
			String name = null;
			String start = null;
			String end = null;
			ArrayList<Location> stops = null;
			
			while (rs.next()) {
				ID = rs.getInt("tripID");
				name = rs.getString("name");
				start = rs.getString("startLocation");
				end = rs.getString("endLocation");
				stops = getStops(ID);
				savedTrips.add(new Trip(ID, name, start, end, stops));
			}

		} catch (ClassNotFoundException cnfe) {
			System.out.println(cnfe.getMessage());
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				if (DBconn != null) { DBconn.close(); }
				if (ps != null) { ps.close(); }
				if (rs != null) { rs.close(); }
			} catch (SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
		}
		
		return savedTrips;
	}
	
	//Returns an empty arraylist if there are no stops
	private ArrayList<Location> getStops(Integer tripID) {
		ArrayList<Location> stops = new ArrayList<Location>();
		
		Connection DBconn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName(SQLclass);
			DBconn = DriverManager.getConnection(SQLURL+dataBase+"?user="+SQLuser+"&password="+SQLpass);
			ps = DBconn.prepareStatement("SELECT * FROM STOPS WHERE TRIPID=? ORDER BY STOPNUM ASC");
			ps.setInt(1, tripID);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				stops.add(new Location(rs.getString("location")));
			}

		} catch (ClassNotFoundException cnfe) {
			System.out.println(cnfe.getMessage());
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				if (DBconn != null) { DBconn.close(); }
				if (ps != null) { ps.close(); }
				if (rs != null) { rs.close(); }
			} catch (SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
		}
		
		return stops;
	}
	
	private void savePublicStops(Trip curTrip, int ID) {
		Connection DBconn =  null;
		Statement createStopStmt = null;
		String sqlStmt = "";
		System.out.println("Saving Public Stops for " +curTrip.name);
		try {
			Class.forName(SQLclass);
			DBconn = DriverManager.getConnection(SQLURL+dataBase+"?user="+SQLuser+"&password="+SQLpass);
			System.out.println("Inserting each stop into " + curTrip.name + " into Public Stops Table");
			for (Integer i=0; i<curTrip.stops.size(); ++i) {
				sqlStmt = "INSERT INTO publicstops (pubID, stopNum, location) VALUES " +
						"("+ID+","+i+",'"+curTrip.stops.get(i).googleID+"');";
				createStopStmt = DBconn.createStatement();
				createStopStmt.executeUpdate(sqlStmt);
			}
		} catch (ClassNotFoundException cnfe) {
			System.out.println(cnfe.getMessage());
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				if (DBconn != null) { DBconn.close(); }
				if (createStopStmt != null) { createStopStmt.close(); }
			} catch (SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
		}
	}

	private ArrayList<Location> getPublicStops(Integer tripID) {
		ArrayList<Location> stops = new ArrayList<Location>();
		
		Connection DBconn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			System.out.println("Getting Public Stops for ID:" + tripID);
			Class.forName(SQLclass);
			DBconn = DriverManager.getConnection(SQLURL+dataBase+"?user="+SQLuser+"&password="+SQLpass);
			ps = DBconn.prepareStatement("SELECT * FROM PUBLICSTOPS WHERE PUBID=? ORDER BY STOPNUM ASC");
			ps.setInt(1, tripID);
			rs = ps.executeQuery();
			
			while (rs.next()) {
				stops.add(new Location(rs.getString("location")));
			}
			System.out.println("Finish getting all stops");

		} catch (ClassNotFoundException cnfe) {
			System.out.println(cnfe.getMessage());
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				if (DBconn != null) { DBconn.close(); }
				if (ps != null) { ps.close(); }
				if (rs != null) { rs.close(); }
			} catch (SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
		}
		
		return stops;
	}

	private ArrayList<Trip> makePublicGetID() {
		ArrayList<Trip> pubTrips = new ArrayList<Trip>();
		
		Connection DBconn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			Class.forName(SQLclass);
			DBconn = DriverManager.getConnection(SQLURL+dataBase+"?user="+SQLuser+"&password="+SQLpass);
			System.out.println("Querying for public trips");
			ps = DBconn.prepareStatement("SELECT * FROM PUBLICTRIPS ORDER BY SAVEDTIME DESC");
			rs = ps.executeQuery();
			
			while (rs.next()) {
				Integer ID = rs.getInt("pubID");
				String username = rs.getString("username");
				String name = rs.getString("name");
				pubTrips.add(new Trip(name, username, ID));
			}
		} catch (ClassNotFoundException cnfe) {
			System.out.println(cnfe.getMessage());
		} catch (SQLException sqle) {
			System.out.println(sqle.getMessage());
		} finally {
			try {
				if (DBconn != null) { DBconn.close(); }
				if (ps != null) { ps.close(); }
				if (rs != null) { rs.close(); }
			} catch (SQLException sqle) {
				System.out.println(sqle.getMessage());
			}
		}
		return pubTrips;
	}
}
