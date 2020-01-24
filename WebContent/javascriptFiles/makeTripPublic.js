function MakePublic(tripName) {
	var xhttp = new XMLHttpRequest();
	xhttp.open("GET", "PublicTripServlet?tripName=" + tripName, false);
	xhttp.send();
}