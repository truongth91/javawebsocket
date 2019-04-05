//sketch
var sketchFlag = false;
var color = 'black';
var previousColor = 'black';
var size = 5;
var isFirstLoad = true;
var webSocket_chat = new WebSocket('ws://localhost:8080/WebSocketPrj1/chatServerEndPoint');
var webSocket = new WebSocket('ws://localhost:8080/WebSocketPrj1/sketchServerEndPoint1');
webSocket.onopen = function () {
    console.log('Connected!');
};
webSocket.onmessage = function processMessage(sketchMessage){
	var json = JSON.parse(sketchMessage.data);
	sketch(json.x, json.y, json.size, json.color);
}
webSocket.onclose = function () {
    console.log('Lost connection!');
};
function initializeCanvas(){
	if (isFirstLoad)
	{
		webSocket_chat.send(localStorage.getItem("username"));
		isFirstLoad = false;
	}
	checkAuth();
	var sketchCanvas = document.getElementById('sketchCanvas');
	var context = sketchCanvas.getContext('2d');
	sketchCanvas.style.border = "solid";
	context.canvas.addEventListener('mousemove', function(event){
		var positionX = event.clientX - context.canvas.offsetLeft;
		var positionY = event.clientY - context.canvas.offsetTop;
		if (sketchFlag == true){
			sketch(positionX, positionY, size, color);
			webSocket.send(JSON.stringify({'x' : positionX, 'y' : positionY, 'size' : size, 'color' : color}));
		}
	});
	context.canvas.addEventListener('mousedown', function(event){sketchFlag = true;});
	context.canvas.addEventListener('mouseup', function(event){sketchFlag = false;});
	context.canvas.addEventListener('mouseleave', function(event){sketchFlag = false;});
}
function toggleState(event){
	if (event.value == 'Erase'){
		event.value = 'Sketch';
		previousColor = color;
		color = '#FFFFFF';
		size = 50;
		document.getElementById('colorChooser').style.visibility = "hidden";
		document.getElementById('sketchCanvas').style.cursor = 'text';
	} else {
		event.value = 'Erase';
		color = previousColor;
		size = 5;
		document.getElementById('colorChooser').style.visibility = "visible";
		document.getElementById('sketchCanvas').style.cursor = 'auto';
	}
}
function chooseColor(newColor){
	previousColor = color;
	color = newColor;
}
function sketch(x, y, size, color) {
	var context = document.getElementById('sketchCanvas').getContext('2d');
	context.beginPath();
	context.fillStyle = color;
	context.fillRect(x, y, size, size);
	context.closePath();
}

function checkAuth()
{
	if (localStorage.getItem("username") == "" || localStorage.getItem("username") == null )
	{
		window.location = "main.html";
		return;
	} else 
	{
		document.getElementById('welcome').innerText = "Welcome, "+ localStorage.getItem("username");
	}
}

function destroy()
{
	webSocket.close;
	webSocket_chat.close;
	localStorage.removeItem("username");
	window.location = "main.html";
}

//chat
webSocket_chat.onopen = function () {
	
}
webSocket_chat.onmessage = function (message)
{
	var jsonData = JSON.parse(message.data);
	if (jsonData.message != null) 
	{
		document.getElementById('messageArea').value += jsonData.message + "\n";
	}
}

function sendMessage()
{
	webSocket_chat.send(document.getElementById('messageText').value);
	document.getElementById('messageText').value = "";
}
