package routeitServlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import routeitBackend.RouteitSQL;
import routeitClasses.Trip;
import routeitClasses.User;

@WebServlet("/PublicTripServlet")
public class PublicTripServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession ses = request.getSession(true);
		User user = (User)ses.getAttribute("currentUser");
		String tripName = request.getParameter("tripName");
		
		RouteitSQL database = new RouteitSQL();
		for (Trip trip : database.getPublicTrips()) {
			if (user.username.toUpperCase().equals(trip.username.toUpperCase()) && tripName.equals(trip.name)) {
				return;
			}
		}
		
		for (Trip trip : user.savedTrips) {
			if (tripName.equals(trip.name)) {
				database.makePublic(user, trip);
			}
		}
	}
}