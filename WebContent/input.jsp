<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ page import="routeitClasses.User" %>
<%
	User user = (User)request.getSession().getAttribute("currentUser");
	String username = null;
	boolean isLogged = false;
	if (user == null) {
		username = "";
		isLogged = false;
	} else {
		username = user.username;
		isLogged = true;
	}
	
	boolean hasTrip = false;
	String currentTrip = (String) request.getSession().getAttribute("currentTrip");
	if (currentTrip == null || currentTrip.trim() == "") {
		currentTrip = "";
	} else {
		hasTrip = true;
	}
%>

<html>
	<head>	
	<meta charset="UTF-8">
		<title>RouteIt</title>
		<link rel="stylesheet" type="text/css" href="input.css" />
		<link rel="stylesheet" type="text/css" href="building.css" />
		<script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDkcuhXxU1QlMNNesrI8N5H3JIXp7DqzwM&libraries=places, geometry"></script>
		<script src="javascriptFiles/buildMarkerConfig.js"></script>
		<script src="javascriptFiles/buildingMarkerInfo.js"></script>
		<script src="javascriptFiles/inputMarkerConfig.js"></script>
		<script src="javascriptFiles/inputSearchBox.js"></script>
		<script src="javascriptFiles/inputCreateRoute.js"></script>
		<script src="javascriptFiles/inputOnClickHandler.js"></script>
		<script src="javascriptFiles/RouteBoxer.js"></script>
		<script src="javascriptFiles/inputMap.js"></script>
		<script src="javascriptFiles/SaveTrip.js"></script>
		
		<!-- SweetAlert2 -->
		<script src="https://cdn.jsdelivr.net/npm/sweetalert2@8"></script>
		<script src="sweetalert2.all.min.js"></script>
		<script src="sweetalert2.min.js"></script>
		<link rel="stylesheet" href="sweetalert2.min.css">
		
		<script>
		
			const Toast = Swal.mixin({
			  toast: true,
			  position: 'bottom-end',
			  showConfirmButton: false,
			  timer: 3000
			});
		
			function initPage() {
				initMap();
				console.log("<%= user %>");
				if (<%= isLogged %>) {
					document.getElementById("btn").style.display = 'none';
					document.getElementById("btn2").style.display = 'flex';
					
					if (<%= hasTrip %>) {
						openSavedTrip('<%= currentTrip %>');
						
						var trip = JSON.parse('<%= currentTrip %>');
						var tName = trip.name;
						
						Swal.fire(
						  ' ' + tName + ' ',
						  'Trip has been loaded.',
						  'success'
						)
					}
					else{
						Toast.fire({
						  type: 'success',
						  title: 'Signed in successfully'
						})
					}
				}
				else {
					document.getElementById("btn").style.display = 'flex';
					document.getElementById("btn2").style.display = 'none';
				}
			}
			function toBuilding() {
				document.getElementById("POITable").style.display = "block";
				document.getElementById("back").style.display = "inline-block";
				document.getElementById("complete").style.display = "inline-block";
				document.getElementById("start").style.display = "none";
				document.getElementById("dest").style.display = "none";
				document.getElementById("search").style.display = "none";
				var table = document.getElementById("POITable");
				var rowStart = table.insertRow(0);
				var startCell = rowStart.insertCell(0);
				var rowDest = table.insertRow(1);
				var destCell = rowDest.insertCell(0);
				startCell.innerHTML = document.getElementById("start").value;
				destCell.innerHTML = document.getElementById("dest").value;
				searchBoxes();
			}
			function toInput() {
				document.getElementById("POITable").style.display = "none";
				document.getElementById("back").style.display = "none";
				document.getElementById("saveTrip").style.display = "none";
				document.getElementById("complete").style.display = "none";
				document.getElementById("start").style.display = "block";
				document.getElementById("dest").style.display = "block";
				document.getElementById("search").style.display = "block";
				var table = document.getElementById("POITable");
				while (table.hasChildNodes()) {
					table.removeChild(table.firstChild);
				}
				removeAllPlaceMarkers();
				waypoints = [];
				createRoute();
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
			function populateTable(){
				for(var i = 1; i < 8; i++){
					var table = document.getElementById("POITable");
					var row = table.insertRow(-1);
				    row.insertCell(0).innerHTML = " ";
				}
			}
			function completeTrip() {
				var table = document.getElementById("POITable");
				for (var i=0, row; row = table.rows[i]; i++) {
					console.log(row.childNodes.length);
					if (row.childNodes.length > 1) {
						row.removeChild(row.firstChild);
					}
				}
				removeAllPlaceMarkers();
				document.getElementById("complete").style.display = "none";
				document.getElementById("saveTrip").style.display = "block";
			}
		</script>
		
		<script src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.1/jquery.min.js"></script>
		<script>
			$(function() {
			  $("#saveTrip").click(function() {
			    $(".fullscreen-container").fadeTo(200, 1);
			  });
			  $("#cancel").click(function() {
			    $(".fullscreen-container").fadeOut(200);
			  });
			});
		</script>
	</head>
	<body onload="initPage();">
	
		<div class="page">
		
			<div id="interactive">
				<div id="logo"><a href="input.jsp">Route.it</a></div>
				
				<div id="btn">
					<div id="loginbtn"><a href="login.jsp">login</a></div>
					<div id="regbtn"><a href="register.jsp">register</a></div>
				</div>
				<div id="btn2" style="display:none">
					<div id="loginbtn"><a href="profile.jsp">profile</a></div>
					<div id="regbtn"><a href="#" onclick="logout();return false;">logout</a></div>
				</div>
			</div>
			
			<div id="shadow"> </div>
			
			<form class="locSearch">
				<div id="inputs">
					<input id=start type="text" placeholder="Enter starting point..." name="start" onchange="setStarting();">
					<input id=dest type="text" placeholder="Enter destination..." name="dest" onchange="setEnding();">
				</div>
			</form>
			<table id="POITable" style="display:none">
			</table>
			<div>
				<button type="button" id="search" onclick="toBuilding(); populateTable();">Search</button>
				<button type="button" id="back" onclick="toInput();" style="display:none">Change Start and End</button>
				<button type="button" id="complete" onclick="completeTrip()" style="display:none">Complete</button>
				<% if (user != null) { %>
					<button type="button" id="saveTrip" style="display:none">Save Trip</button>
				<% } %>
				
			</div>
				
			<div id="map"></div>
		</div>
		
		<div id='placeSearch'></div>
		
		<!-- temporary, until map api is implemented -->
		<div id="background"></div>
		<div id="popup" style="display:none"></div>
		
		<div class="fullscreen-container">
  			<div id="popdiv">
    			<h1>Enter name of trip</h1>
    			<input type="text" id="tripName"/>
    			<button id="cancel">Cancel</button>
    			<button id="save" onclick="SaveTrip(document.getElementById('tripName').value)">Save</button>
  			</div>
		</div>
	</body>
</html>