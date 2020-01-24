/*
 * 
 * inputCreateRoute handles creating the routes
 * 
 * */
function calculateAndDisplayRoute(directionsService, directionsDisplay) {
	directionsService.route({
    origin: endpoints.start.position,
    destination: endpoints.dest.position,
    waypoints: waypoints,
    optimizeWaypoints: true,
    travelMode: google.maps.DirectionsTravelMode.DRIVING
    }, function(response, status) {
	    if (status === google.maps.DirectionsStatus.OK) {
	      directionsDisplay.setDirections(response);
	      currentRoute = response.routes[0].overview_path;
	      routeBoxes = rBoxer.box(currentRoute, 25);
	    } else {
	      window.alert('Directions request failed due to ' + status);
	    }
  });
}

function createRoute() {
	calculateAndDisplayRoute(directionsService, directionsDisplay);
}