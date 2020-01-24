package routeitServlets;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import routeitBackend.RouteitSQL;
import routeitClasses.User;

@WebServlet("/UserValidate")
public class UserValidate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession ses = request.getSession(true);
		String Register = request.getParameter("register");
		String Login = request.getParameter("login");
		RouteitSQL database = null;
		database = new RouteitSQL();
		User user = null;

		System.out.println(Register);
		
		if (Register != null && Register.equals("register")) { // user is trying to register an account
			System.out.println("Registering User");
			String Username = request.getParameter("username");
			String Password = request.getParameter("password");
			String ConfirmPassword = request.getParameter("cfpassword");
			
			if (Username == null || Username.trim().length() == 0) { // no username input
				request.setAttribute("usernameError", "Please enter a username.");
				RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/register.jsp");
				dispatch.forward(request, response);
				return;
			}
			else { // checks if username is unique
				System.out.println("Checking User");
				user = database.getUser(Username);
				if (user != null) {
					System.out.println(user.username);
					request.setAttribute("usernameError", "Username has already been taken.");
					RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/register.jsp");
					dispatch.forward(request, response);
					return;
				}
				else {
					System.out.println("user does not exist");
				}
			}
			
			if (Password == null || Password.trim().length() == 0) { // no password entered
				request.setAttribute("passwordError", "Not a valid password.");
				RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/register.jsp");
				dispatch.forward(request, response);
				return;
			}
			if (ConfirmPassword == null || ConfirmPassword.trim().length() == 0 || // no confirm password entered
				!Password.equals(ConfirmPassword)) { // passwords don't match
				request.setAttribute("cfpasswordError", "The passwords do not match.");
				RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/register.jsp");
				dispatch.forward(request, response);
				return;
			}
			
			//Creating user
			user = database.createUser(Username, Password, "");
			
			// will only execute this code if username is unique and passwords match
			ses.setAttribute("currentUser", user);
			RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/input.jsp");
			dispatch.forward(request, response);
		}
		else if (Login != null && Login.equals("login")) { // user is trying to login
			String Username = request.getParameter("username");
			String Password = request.getParameter("password");
			
			if (Username == null || Username.trim().length() == 0) { // no username input
				request.setAttribute("usernameError", "Please enter a username.");
				RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/login.jsp");
				dispatch.forward(request, response);
				return;
			}
			else { // checks if username exists
				user = database.getUser(Username);
				if (user != null) { //User exists
					if (user.password.equals(Password)) {
						//Successful login
						ses.setAttribute("currentUser", user);
						RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/input.jsp");
						dispatch.forward(request, response);
						return;
					}
					else {
						request.setAttribute("usernameError", "Incorrect login credentials.");
						RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/login.jsp");
						dispatch.forward(request, response);
						return;	
					}
				}
				else {
					request.setAttribute("usernameError", "Incorrect login credentials.");
					RequestDispatcher dispatch = getServletContext().getRequestDispatcher("/login.jsp");
					dispatch.forward(request, response);
					return;
				}
			}
		}
	}

}
