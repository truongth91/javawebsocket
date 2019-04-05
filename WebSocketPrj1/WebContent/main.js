
var webSocket = new WebSocket('ws://localhost:8080/WebSocketPrj1/loginServerEndPoint');
webSocket.onopen = function () {
    console.log('Loginserver Connected!');
};
webSocket.onmessage = function (result){
//	if (result.data != "NG" && result.data != "")
//	{
//		webSocket.close;
//		localStorage.setItem("username",result.data);
//		window.location = "default.html";
//	} else 
//	{
//		console.log("XXX");
//	}
	var createErrorMsg = "";
	switch (result.data)
	{
		case "NG":
			createErrorMsg = "Please check your username and password";
			break;
		case "":
			createErrorMsg = "Something wrong from server";
			break;
		case "taken":
			createErrorMsg = "Username has been taken";
			break;
//		case "create":
//			createErrorMsg = result.data;
//			break;
		default:
			webSocket.close;
			localStorage.setItem("username",result.data);
			window.location = "default.html";
			break;
	}
	document.getElementById('msg').innerText = createErrorMsg;

}

function loginValidate()
{
	var id = document.getElementById('id').value;
	var pw = document.getElementById('pw').value;
	var info = id + "/" + pw + "/" + "login";
	webSocket.send(info);
}

function validate()
{
	var error = false;
	if (document.getElementById('passWord').value != document.getElementById('confirm').value)
	{
		document.getElementById('msg').innerText = "Password and Confirm password are not match.";
		document.getElementById('passWord').style.borderColor = "red";
		document.getElementById('confirm').style.borderColor = "red";
		error = true;
	}
	
	if (document.getElementById('userName').value == '')
	{
		document.getElementById('msg').innerText = "Username can not be empty.";
		document.getElementById('userName').style.borderColor = "red";
		error = true;
	}
	
	if (!error)
	{
		var id = document.getElementById('userName').value;
		var pw = document.getElementById('passWord').value;
		var info = id + "/" + pw + "/" + "create";
		webSocket.send(info);
	} 

}


