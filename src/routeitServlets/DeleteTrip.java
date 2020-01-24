package routeitServlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import routeitBackend.RouteitSQL;
import routeitClasses.Trip;
import routeitClasses.User;

/**
 * Servlet implementation class DeleteTrip
 */
@WebServlet("/DeleteTrip")
public class DeleteTrip extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RouteitSQL database = new RouteitSQL();
		User user = (User)request.getSession().getAttribute("currentUser");
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String tripName = br.readLine();
		for (Trip trip : user.savedTrips) {
			if (trip.name.equalsIgnoreCase(tripName)) {
				database.deleteTrip(trip);
				user.savedTrips.remove(trip);
				break;
			}
		}
	}

}
