/*
 * 
 * inputMarkerConfig holds functions that directly adjusts the markers
 * 
 * */

function switchEndpoint(endpoint) {
	if (endpoint) {
		if (endpoints.start != null) {
			endpoints.start.setMap(null);
			delete endpoints.start;
		}
		endpoints.start = new google.maps.Marker({
			position : marker.position,
			map : marker.map
		});
	} else {
		if (endpoints.dest != null) {
			endpoints.dest.setMap(null);
			delete endpoints.dest;
		}
		endpoints.dest = new google.maps.Marker({
			position : marker.position,
			map : marker.map
		});
	}
}

function closeMarker() {
	if (marker != null) {
		marker.setMap(null);
		delete marker;
		marker = null;
	}
	if (infowindow != null) {
		infowindow.close();
	}
}

function hasStartDest() {
	var startFilled = true;
	var destFilled = true;
	
	
	if (endpoints.start == null) {
		destFilled = false;
	}
	if (endpoints.dest == null) {
		startFilled = false;
	}
	if (startFilled && destFilled) {
		createRoute();
	}
}

function removeAllPlaceMarkers() {
	for (var i=0; i<resultMarkers.length; i++) {
		resultMarkers[i].setMap(null);
		delete resultMarkers[i];
	}
	placesResults = [];
	resultMarkers = [];
}