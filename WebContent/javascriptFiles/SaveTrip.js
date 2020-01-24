function SaveTrip(tripName) {
	console.log(tripName);
	var tripExists = CheckTrip(tripName); // returns false if trip does not exist
	console.log("CheckTrip: " + tripExists);
	if (tripExists == false) {
		var allPoints = [];
		
		var geocoder = new google.maps.Geocoder();
		geocoder.geocode({
		    'latLng': endpoints.start.position
		  }, function(results, status) {
		    if (status == google.maps.GeocoderStatus.OK) {
		      if (results[0]) {
		        var startID = results[0].place_id;
		        geocoder.geocode({
		    	    'latLng': endpoints.dest.position
		    	  }, function(results, status) {
		    	    if (status == google.maps.GeocoderStatus.OK) {
		    	      if (results[0]) {
		    	    	var destID = results[0].place_id;
		    	    	allPoints.push({
		    	    		name: tripName,
		    	    		start: startID,
		    	    		end: destID,
		    	    		waypoint: []
		    	    		});
		    	    	
		    	    	for (var i = 0; i < Mattswaypoints.length; i++) {
		    	    		allPoints[0].waypoint.push(Mattswaypoints[i]);
		    	    	}
		    	    	
		    	    	var JSONString = JSON.stringify(allPoints);
		    	    	
		    	    	// Moves the JSONString to the servlet to be parsed into a Trip and saved on the SQL database
		    	    	var xhttp = new XMLHttpRequest();
		    	    	xhttp.open("POST", "SaveTripServlet", true);
		    	    	xhttp.send(JSONString);
		    	      }
		    	    }
		    	  });
		      }
		    }
		  });
		$("#cancel").click();

		Swal.fire(
		  'Trip saved!',
		  'View trip in profile.',
		  'success'
		)
		
	}
	else {
		$("#cancel").click();
		
		Swal.fire(
		  'Error',
		  'Trip name already exists. Please choose another name.',
		  'error'
		)
//		alert("This trip already exists. Please choose another name.");
	}
}

function CheckTrip(tripName) {
	var response = "";
	var xhttp = new XMLHttpRequest();
	xhttp.onreadystatechange = function() {
		response = this.responseText;
	}
	xhttp.open("GET", "SaveTripServlet?name=" + tripName, false);
	xhttp.send();
	console.log("response: " + response);
	if (response == "") { // trip does not exist
		return false;
	}
	else {
		return true;
	}
}

function addToTable(address) {
	var table = document.getElementById("POITable");
	var row = table.insertRow(table.length);
	row.innerHTML = address;
}

function clearTrip() {
	var xhttp = new XMLHttpRequest();
	xhttp.open("POST", "ClearTrip", true);
	xhttp.send();
}

function openSavedTrip(currentTrip) {
	document.getElementById("POITable").style.display = "block";
	document.getElementById("back").style.display = "inline-block";
	document.getElementById("start").style.display = "none";
	document.getElementById("dest").style.display = "none";
	document.getElementById("search").style.display = "none";
	
	clearTrip();
	var trip = JSON.parse(currentTrip);
	console.log(trip);
	var geocoder = new google.maps.Geocoder();
	var placeService = new google.maps.places.PlacesService(map);
	placeService.getDetails({
		placeId: trip.startLoc.googleID,
		fields: ['geometry','name']
	}, function(startPlace, status) {
		if (status == google.maps.places.PlacesServiceStatus.OK) {
			var startMarker = new google.maps.Marker({
				position: startPlace.geometry.location,
				map: map
			});
			if (endpoints.start != null) {endpoints.start.setMap(null); delete endpoints.start;}
			endpoints.start = startMarker;
			addToTable(startPlace.name);
			placeService.getDetails({
				placeId: trip.endLoc.googleID,
				fields: ['geometry', 'name']
			}, function(endPlace, status) {
				if (status == google.maps.places.PlacesServiceStatus.OK){
					var endMarker = new google.maps.Marker({
						position: endPlace.geometry.location,
						map: map
					});
					if (endpoints.dest != null) {endpoints.dest.setMap(null); delete endpoints.dest;}
					endpoints.dest = endMarker;
					for (var i=0; i<trip.stops.length; ++i) {
						placeService.getDetails({
		    	      	    placeId: trip.stops[i].googleID,
		    	      	    fields: ['geometry', 'name']
		    	      	  }, function(resultsway, status) {
		    	      	    if (status == google.maps.places.PlacesServiceStatus.OK) {
		    	      	    	waypoints.push({
		    	      	    		location: resultsway.geometry.location,
		    	      	    		stopover: true
		    	      	    	});
		    	    	        addToTable(resultsway.name);
			    	      	    if (waypoints.length === trip.stops.length) {
			    					addToTable(endPlace.name);
			    	      	    	createRoute();
			    	      	    }
		    	      	    }
		    	      	 });
		    	  	}
				}
			})
		  }
	});
}