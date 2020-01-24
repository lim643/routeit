<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ page import = "routeitClasses.User" %>

<%
	User user = (User) request.getSession().getAttribute("currentUser");
%>

<html>
	<head>
		<meta charset="UTF-8">
		<title>Profile</title>
		<link rel="stylesheet" type="text/css" href="profile.css" />
		<script src="javascriptFiles/UserNotification.js"></script>
	</head>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
	<script src="javascriptFiles/UserNotification.js"></script>
	<script src="javascriptFiles/makeTripPublic.js"></script>
	<script>
		function selectTrip(tripName) {
			var xhttp = new XMLHttpRequest();
			xhttp.onreadystatechange = function(){
				if (this.responseText != null && this.responseText.trim() != "") {
					console.log(this.responseText);
					window.location.href = "input.jsp";
				}
			}
			xhttp.open("POST", "GetTripServlet", true);
			xhttp.send(tripName);
		}
		
		function logout() {
			console.log("logging out");
			var xhttp = new XMLHttpRequest();
			xhttp.open("GET", "LogUserOut", true);
			xhttp.send();
			xhttp.onreadystatechange = function() {
				window.location.replace("input.jsp");
			}
		}
		
		function DeleteRow(r, trip) {
			var xhttp = new XMLHttpRequest();
			xhttp.open("POST", "DeleteTrip", true);
			xhttp.send(trip);
			var row = document.getElementById(r);
			row.parentElement.removeChild(row);
		}
	</script>
		
	</head>
	<body onload="connectToServer();">
		<div class="page">
			<div id="interactive">
				<div id="logo"><a href="input.jsp">Route.it</a></div>
				
				<div id="btn">
					<div id="loginbtn"><a href="profile.jsp"><%=user.username %></a></div>
					<div id="regbtn"><a href="#" onclick="logout();return false;">logout</a></div>
				</div>
				
			</div>
			<div id="shadow"> </div>
		</div>
		
		<div id="background"></div>
		
		<table class="table">
			<tr id="headContainer">
				<th id="tableHead">Saved Trips</th>
			</tr>
			<tbody>
			  <%
			  for (int i=0; i<user.savedTrips.size(); ++i) {
			  %>
			  <tr>
				  <td id=<%= i %>>
				  	<button id="Xbutton" onclick="DeleteRow(<%= i %>,'<%= user.savedTrips.get(i).name %>')">X</button>
				  	<button id="tripName" onclick="selectTrip('<%= user.savedTrips.get(i).name %>');"><%= user.savedTrips.get(i).name %></button>
				  	<button id="publicButton" onclick="makeTripPublic('<%= user.savedTrips.get(i).name %>')"> Make Public </button>
				  </td>
			  </tr>
			  <%
			  }
			  %>
			</tbody>
		</table>
		<div id="toPublicOrPrivate"><a href="public.jsp">View Public Trips</a></div>
	</body>
</html>