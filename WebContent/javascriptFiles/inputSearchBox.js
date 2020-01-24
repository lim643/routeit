/*
 * 
 * inputSearchBox initializes Google Search Boxes
 * 
 * */

function createStartSearch() {
	// Create the search box and link it to the UI element.
	  var input = document.getElementById('start');
	  var searchBox = new google.maps.places.SearchBox(input);
	  map.controls[google.maps.ControlPosition.TOP_RIGHT].push(input);
	  // Bias the SearchBox results towards current map's viewport.
	  map.addListener('bounds_changed', function() {
	    searchBox.setBounds(map.getBounds());
	  });
	  searchBox.addListener('places_changed', function() {
		var places = searchBox.getPlaces();
		var bounds = new google.maps.LatLngBounds();
		places.forEach(function(place) {
		    if (!place.geometry) {
		      console.log("Returned place contains no geometry");
		  return;
		}
		
		if (place.geometry.viewport) {
		  // Only geocodes have viewport.
		      bounds.union(place.geometry.viewport);
		    } else {
		      bounds.extend(place.geometry.location);
		    }
		  });
		map.fitBounds(bounds);
		marker = new google.maps.Marker({
			position : bounds.getCenter(),
			map : map
		});
		switchEndpoint(true);
		closeMarker();
		hasStartDest();
	  });
}

function createDestSearch() {
	  var input = document.getElementById('dest');
	  var searchBox = new google.maps.places.SearchBox(input);
	  map.controls[google.maps.ControlPosition.RIGHT_TOP].push(input);
	  // Bias the SearchBox results towards current map's viewport.
	  map.addListener('bounds_changed', function() {
	    searchBox.setBounds(map.getBounds());
	  });
	  searchBox.addListener('places_changed', function() {
		var places = searchBox.getPlaces();
		var bounds = new google.maps.LatLngBounds();
		places.forEach(function(place) {
		    if (!place.geometry) {
		      console.log("Returned place contains no geometry");
		  return;
		}
		
		if (place.geometry.viewport) {
		  // Only geocodes have viewport.
		      bounds.union(place.geometry.viewport);
		    } else {
		      bounds.extend(place.geometry.location);
		    }
		  });
		map.fitBounds(bounds);
		marker = new google.maps.Marker({
			position : bounds.getCenter(),
			map : map
		});
		switchEndpoint(false);
		closeMarker();
		hasStartDest();
	  });
}

function searchBoxes() {
	if (routeBoxes != null) {
		var searchBox = new google.maps.places.PlacesService(map);
		var i = 0;
		var lastError = 0;
		var count = 0;
		var intervalID = setInterval(function() {
			searchBox.nearbySearch({
				bounds : routeBoxes[i],
				radius : 25,
				rankBy: google.maps.places.RankBy.PROMINENCE,
				name: "restaurant"
			}, function(places, status) {
				if (status == google.maps.places.PlacesServiceStatus.OK) {
					places.forEach(function(place){
						var placeMarker = new google.maps.Marker({
							position : place.geometry.location,
							map: map
						});
						
						var p1 = endpoints.start.position;
						var p2 = place.geometry.location;
						
						var distFromStart = calcDistance(p1, p2);
						
						function calcDistance(p1, p2) {
						  return (google.maps.geometry.spherical.computeDistanceBetween(p1, p2) / 1000).toFixed(2);
						}
						
						var placeInfo = {
							marker: placeMarker,
							googleID: place.place_id,
							address: place.formatted_address,
							name: place.name,
							photos: place.photos,
							types: place.types,
							enabled: true,
							buttonID: count,
							dist: distFromStart
						};
						placeMarker.setMap(map);
						resultMarkers.push(placeMarker); // Markers
						placesResults.push(placeInfo); // Has info about each place
						createPlacesInfo(count);
						
						++count;
					});
				} else {
					if (i != lastError) {
						lastError = --i;
					}
				}
			});
			if (++i === routeBoxes.length) {
				window.clearInterval(intervalID);
			}
		}, i * 400);
	} else {
		alert("Please Input Start and Destination");
	}
}