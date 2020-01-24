function AddToRoute(i) {
	//Adding location to route
	waypoints.push({
		location: placesResults[i].marker.position,
		stopover: true
	});
	createRoute();
	Mattswaypoints.push(placesResults[i].googleID);
	
	// Disable popup button
	document.getElementById(placesResults[i].buttonID).disabled = true;
	placesResults[i].enabled = false;
	document.getElementById(placesResults[i].buttonID).innerHTML = "Added To Trip";
	
	// Add row to cell
	var table = document.getElementById("POITable");
	var row = table.insertRow(1); // Change to reflect actual placement in list
	
	// Add the "x" button + functionality
	var Xbutton = document.createElement("button");
	currentCell = row.insertCell(0);
    currentCell.appendChild(Xbutton);
    row.id = "row" + i + "";
    
    Xbutton.setAttribute("onclick", "Reopen(" + i + "); DeletePOI(" + row.id + "," + i + ");");
	Xbutton.innerHTML = "x";
	Xbutton.style = "margin-top: 18px";
	
	var cell = row.insertCell(1);

	cell.innerHTML = placesResults[i].name;
	cell.style = "padding-left: 10px";
	google.maps.event.trigger(map, 'click');
	
	// Add invisible id
	var idCell = row.insertCell(2);
	idCell.innerHTML = placesResults[i].dist;
	idCell.style.color = '#F8F8F8';
	
	sort();
}
function Reopen(i) {
	placesResults[i].enabled = true;
	google.maps.event.trigger(resultMarkers[i], 'click');
}
function DeletePOI(cell, i) {
	//Remove location from route
	waypoints = removeLocationFromRoute(waypoints, i);
	console.log(waypoints);
	createRoute();
	
	// Delete the row
	DeleteRow(cell.id);
	
	// Re-enable the button on the popup
	document.getElementById(placesResults[i].buttonID).disabled = false;
	document.getElementById(placesResults[i].buttonID).innerHTML = "Add to Trip";
}
function DeleteRow(r) {
	var row = document.getElementById(r);
	row.parentElement.removeChild(row); 
}

function removeLocationFromRoute(points, i) {
	var holder = points;
	return holder.filter(function(ele) {
		return ele.location != placesResults[i].marker.position;
	});
}

function sort(){
	
	var table, rows, switching, i, x, y, shouldSwitch;
	  table = document.getElementById("POITable");
	  switching = true;
	  while (switching) {
	  
	    switching = false;
	    rows = table.rows;

	    for (i = 1; i < (waypoints.length); i++) {
	    
	      shouldSwitch = false;
	      
	      x = rows[i].getElementsByTagName("TD")[2];
	      y = rows[i + 1].getElementsByTagName("TD")[2];
	      
	      if (parseInt(x.innerHTML) > parseInt(y.innerHTML)) {
	        shouldSwitch = true;
	        break;
	      }
	    }
	    if (shouldSwitch) {
	      rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
	      switching = true;
	    }
	  }
}