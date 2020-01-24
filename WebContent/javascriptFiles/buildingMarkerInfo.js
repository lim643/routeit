function createPlacesInfo(i) {
	var infoWindow = new google.maps.InfoWindow({
	    maxWidth: 350
	  });
	google.maps.event.addListener(placesResults[i].marker, 'click', function() {
		var content;
		if (placesResults[i].enabled == true) {
			content = '<h1>' + placesResults[i].name + '</h1><button id="' + placesResults[i].buttonID + '" onclick="AddToRoute(' + i + ')">Add To Trip</button>';
		}
		else {
			content = '<h1>' + placesResults[i].name + '</h1><button id="' + placesResults[i].buttonID + '" disabled onclick="AddToRoute(' + i + ')">Added To Trip</button>';
		}
		infoWindow.setContent(content);
		infoWindow.open(map, placesResults[i].marker);
	});
	google.maps.event.addListener(map, 'click', function(event) {
		infoWindow.close();
	});
}