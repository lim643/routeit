package routeitServlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

import routeitClasses.Trip;
import routeitClasses.User;

/**
 * Servlet implementation class GetTripServlet
 */
@WebServlet("/GetTripServlet")
public class GetTripServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession ses = request.getSession(true);
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String tripName = br.readLine();
		System.out.println(tripName);
		User user = (User)ses.getAttribute("currentUser");
		
		Gson gson = new Gson();
		ArrayList<Trip> trips = user.savedTrips;

		Trip trip = null;
		for(Trip t : trips) {
			System.out.println(t.name);
			if (tripName.equalsIgnoreCase(t.name)) {
				System.out.println("Enter here");
				trip = t;
				break;
			}
		}
		String jsonString = gson.toJson(trip);
		System.out.println(jsonString);
		ses.setAttribute("currentTrip", jsonString);
		
		response.getWriter().println(jsonString);
		
	}


}
