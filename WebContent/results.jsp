<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title>Results Page</title>
		<link rel="stylesheet" type="text/css" href="results.css" />
	</head>
	<body>
		<div id="logo"><a href="input.jsp">Route.it</a></div>
		<div id="logosh"></div>

		<div id="loginbtn"><a href="login.jsp">login</a></div>
		<div id="regbtn"><a href="register.jsp">register</a></div>

		<table id="POITable">
			<tr>
				<td>USC Village</td> 
			</tr>
			<tr>
				<td>Starbucks</td>
			</tr>
			<tr>
				<td>Office Depot</td>
			</tr>
			<tr>
				<td>Los Angeles Conv...</td>
			</tr>
			<tr>
				<td>STAPLES Center</td>
			</tr>
			<tr>
				<td>Regal Cinemas L...</td>
			</tr>
			<tr>
				<td>Ralphs</td>
			</tr>
			<tr>
				<td>Intercontinental Hotel</td>
			</tr>
			<tr>
				<td></td>
			</tr>
			<tr>
				<td></td>
			</tr>
			<tr>
				<td></td>
			</tr>
			<tr>
				<td></td>
			</tr>
			<tr>
				<td></td>
			</tr>
			<tr>
				<td></td>
			</tr>
			<tr>
				<td></td>
			</tr>
			<tr>
				<td></td>
			</tr>
		</table>
		<form action="Servlet" method="POST">
			<input type="submit" id="SaveTrip" name="SaveTrip" value="Save Trip">
		</form>
	</body>
</html>