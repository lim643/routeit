package routeitServlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import routeitBackend.RouteitSQL;
import routeitClasses.Location;
import routeitClasses.Trip;
import routeitClasses.User;
import routeitJSON.JSONObject;
import routeitJSON.JSONParser;

@WebServlet("/SaveTripServlet")
public class SaveTripServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession ses = request.getSession(true);
		User user = (User)ses.getAttribute("currentUser");
		
		String name = request.getParameter("name");
		System.out.println("name: " + name);
		if (user.savedTrips != null) {
			for (Trip trip : user.savedTrips) {
				System.out.println("tripname: " + trip.name);
				if (name.equals(trip.name)) {
					response.setContentType("text/html");
					PrintWriter out = response.getWriter();
					out.println("Trip already exists, please choose a different name.");
				}
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession ses = request.getSession(true);
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		
		String JSONString = br.readLine();
		
		// Pass JSONString to be parsed into JSONObject class
		JSONParser jp = new JSONParser(JSONString);
		
		ArrayList<JSONObject> jo = jp.getJSONObjects();
		String name = jo.get(0).getName();
		
		// Start and end store googleID
		Location start = new Location(jo.get(0).getStart());
		Location end = new Location(jo.get(0).getEnd());
		
		
		// waypoints ArrayList stores googleID of each POI stop
		ArrayList<Location> waypoints = new ArrayList<Location>();
		for (String s : jo.get(0).getWaypoint()) {
			Location POI = new Location(s);
			waypoints.add(POI);
		}
		
		Trip trip = new Trip(name, start, end, waypoints);
		
		User currentUser = (User)ses.getAttribute("currentUser");
		currentUser.addTrip(trip);
		RouteitSQL database = new RouteitSQL();
		database.saveTrip(currentUser, trip);
		System.out.println("Trip Saved");
	}

}
