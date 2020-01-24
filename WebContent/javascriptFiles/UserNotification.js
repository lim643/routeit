var socket;
function connectToServer() {
	socket = new WebSocket("ws://localhost:8080/RouteIt/profile");
	socket.onopen = function(event) {
		console.log("Connected");
	}
	socket.onmessage = function(event) {
		var message = event.data;
		
		
	}
	socket.onclose = function(event) {
		console.log("Disconnected");
	}
}
function sendMessage() {
	socket.send(document.getElementById("#loginbtn").innerHTML + " has made a trip public.");
}