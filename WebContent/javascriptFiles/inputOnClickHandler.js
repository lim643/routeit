/*
 * 
 * inputOnClickHandler moves the start and end markers when location is clicked on
 * 
 * */
function setStarting() {
	if (marker != null) {
		document.getElementById('start').value = onClickAddress;
		onClickAddress = null;
		switchEndpoint(true);
		closeMarker();
	}
	hasStartDest();
}

function setEnding() {
	if (marker != null) {
		document.getElementById('dest').value = onClickAddress;
		onClickAddress = null;
		switchEndpoint(false);
		closeMarker();
	}
	hasStartDest();
}