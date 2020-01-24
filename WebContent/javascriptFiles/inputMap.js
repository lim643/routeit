var map;
var directionsService;
var directionsDisplay;
var onClickAddress;
var infowindow;
var marker;

var endpoints = [];
endpoints.push({
	start : null,
	dest : null
})

var currentRoute;
var rBoxer;
var routeBoxes;

var resultMarkers = [];
var placesResults = [];

// WAYPOINTS used when updating route when POI added
var waypoints = [];
var Mattswaypoints = [];

function initMap() {
/*INITIALIZE MAP*/
	map = new google.maps.Map(document.getElementById('map'), {
		center: {lat: 41.85, lng: -87.65},
		zoom: 4,
		disableDoubleClickZoom: true,
		mapTypeControl: false,
	    zoomControl: true,
	    zoomControlOptions: {
	        position: google.maps.ControlPosition.LEFT_TOP
	    },
	    scaleControl: true,
	    streetViewControl: false,
	    fullscreenControl: true,
	    fullscreenControlOptions: {
	    	position: google.maps.ControlPosition.LEFT_TOP
	    }
	});
	
/*INITIALIZE LISTENER FOR ONCLICK EVENTS*/
	map.addListener('dblclick', function(event) {
		//Reset the map
		if (marker != null) {
			marker.setMap(null);
			delete marker;
		}
		if (infowindow != null) {
			infowindow.close();
		}
		//Generate marker for infowindow
		marker = new google.maps.Marker({
			position : event.latLng,
			map: this
		})
		var contentString = '<div id="popupDiv">' +
		'<button onclick="setStarting()">Starting Location</button>' +
		'<button onclick="setEnding()">Ending Location</button>' +
		'</div>';
		infowindow = new google.maps.InfoWindow({
			content: contentString
		});
		google.maps.event.addListener(infowindow, 'closeclick', function() {
			marker.setMap(null);
			delete marker;
		});
		infowindow.open(map, marker);	
		
		//Generate Geocoder
		var geocoder = new google.maps.Geocoder();
	    geocoder.geocode({
		    'latLng': event.latLng
		  }, function(results, status) {
		    if (status == google.maps.GeocoderStatus.OK) {
		      if (results[0]) {
		        onClickAddress = results[0].formatted_address;
		      }
		    }
		  });
		});
	
/*INITIALIZE SEARCH BOXES*/
	createStartSearch();
	createDestSearch();
	
/*INITIALIZE ROUTE FUNCTIONALITIES*/
	directionsService = new google.maps.DirectionsService;
	directionsDisplay = new google.maps.DirectionsRenderer;
	directionsDisplay.setMap(map);
	rBoxer = new RouteBoxer();
}