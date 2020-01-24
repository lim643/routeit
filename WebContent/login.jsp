<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>

<%

	String usernameError = (String)request.getAttribute("usernameError");
	String passwordError = (String)request.getAttribute("passwordError");

	if(usernameError == null){
		usernameError = "";
	}
	if(passwordError == null){
		passwordError = "";
	}
	
	String usrnm = request.getParameter("username");
	String psswd = request.getParameter("password");
	
	if(usrnm == null){
		usrnm = "";
	}
	if(psswd == null){
		psswd = "";
	}
%>

<html>
	<head>
		<meta charset="UTF-8">
		<title>Login</title>
		<link rel="stylesheet" type="text/css" href="login.css" />
	</head>
	<body>
		<div class="page">
			<div id="interactive">
				<div id="logo"><a href="input.jsp">Route.it</a></div>
			
				<div id="btn">
					<div id="loginbtn"><a href="login.jsp">login</a></div>
					<div id="regbtn"><a href="register.jsp">register</a></div>
				</div>
			
			</div>	
			<div id="shadow"> </div>
		</div>
		
		<div id="background"></div>
		
		<div id=title>login</div>
		
		<div id="usrError"> <font color="red"> <%= usernameError %> </font> </div>
		<div id="pwError"> <font color="red"> <%= passwordError %> </font> </div>
		
		<form class="UserValues" action="UserValidate" method="POST">
			<input type="text" id="user" placeholder="username" name="username" value="<%= usrnm %>"/>
			<input type="password" id="pass" placeholder="password" name="password" value="<%= psswd %>"/>
			<input id="login" type="submit" name="login" value="login">
		</form>
		
	</body>
</html>