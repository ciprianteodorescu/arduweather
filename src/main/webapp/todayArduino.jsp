<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page language="java" import="java.lang.*,java.math.*,db.*,java.sql.*, java.io.*, java.util.*"%>
<!DOCTYPE html>
<html lang="en" dir="ltr">
  <head>
    <meta charset="utf-8">
    <title>ArduWeather</title>
    <link rel="stylesheet" href="style1.css">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link href='https://unpkg.com/boxicons@2.0.9/css/boxicons.min.css' rel='stylesheet'>
    <link rel="icon" href="Weather Icons/arduvreme.png">
  </head>
  <jsp:useBean id="jb" scope="session" class="db.JavaBean" />
  <jsp:setProperty name="jb" property="*" />
  <body>
  <%
  jb.connect();
  ResultSet rs;
  String arduino, city;
  int idArduino = Integer.parseInt(request.getParameter("idArduino"));
  String location = jb.getArduinoLocation(idArduino);
  int dropdownIdArduino;
  %>
    <header class="navbar">
      <a class="logo" href="index.jsp">
        <img src="Weather Icons/logoard.png" alt="logo arduino" class="logo">
      </a>
          <ul>
          	<div class="dropdown">
	            <li><a onclick="showArduinos(this.id);" class="dropbtn" id="todayDrop">Today</a></li>
            	<div id="todayDropContent" class="dropdown-content">
            		<%
            		rs = jb.listArduinos();
            		while(rs.next()) {
            			arduino = rs.getString("name");
            			city = rs.getString("city");
            			dropdownIdArduino = rs.getInt("idArduino");
            		%>
            			<a href="todayArduino.jsp?idArduino=<%=dropdownIdArduino%>"><%=arduino%> (<%=city%>)</a>
            		<%
            		}
            		rs.close();
            		%>
            	</div>
            </div>
            <div class="dropdown">
	            <li><a onclick="showArduinos(this.id);" class="dropbtn" id="hourlyDrop">Hourly</a></li>
            	<div id="hourlyDropContent" class="dropdown-content">
            		<%
            		rs = jb.listArduinos();
            		while(rs.next()) {
            			arduino = rs.getString("name");
            			city = rs.getString("city");
            			dropdownIdArduino = rs.getInt("idArduino");
            		%>
            			<a href="hourlyArduino.jsp?idArduino=<%=dropdownIdArduino%>"><%=arduino%> (<%=city%>)</a>
            		<%
            		}
            		rs.close();
            		%>
            	</div>
            </div>
            <div class="dropdown">
	            <li><a onclick="showArduinos(this.id);" class="dropbtn" id="dailyDrop">Daily</a></li>
            	<div id="dailyDropContent" class="dropdown-content">
            		<%
            		rs = jb.listArduinos();
            		while(rs.next()) {
            			arduino = rs.getString("name");
            			city = rs.getString("city");
            			dropdownIdArduino = rs.getInt("idArduino");
            		%>
            			<a href="dailyArduino.jsp?idArduino=<%=dropdownIdArduino%>"><%=arduino%> (<%=city%>)</a>
            		<%
            		}
            		rs.close();
            		%>
            	</div>
            </div>
          </ul>
    </header>

	<%
  rs = jb.listLastValue();
  int temperature = 0, light = 0;
  String icon = "";
  while(rs.next()) {
    int aux1 = rs.getInt("temperature");
    int aux2 = rs.getInt("luminosity");
    temperature = aux1 != 0 ? aux1 : temperature;
    light = aux2 != 0 ? aux2 : light;
    
    Timestamp time = rs.getTimestamp("time");
    if(time.getHours() >= 9 && time.getHours() <= 19) {
        if(light <= 20)
          icon = "Weather Icons\\clear.png";
        if(light > 20 && light <= 70)
          icon = "Weather Icons\\sun cloudy.png";
        if(light > 70)
          icon = "Weather Icons\\cloud.png";
    } else 
      icon = "Weather Icons\\moon.png";
  }
  rs.close();
  %>
    <div class="wrapper active">      
      <section class="weather-part">
        <img src ="<%=icon%>" alt="icon">
        <div class="temp">
          <span class="numb"><%=temperature%></span>
          <sup>°</sup>C
        </div>
        <div class="location">
          <i class='bx bx-map'></i>
          <span><%=location%></span>
        </div>
        <div class="bottom-details">

          <div class="row clouds">
            <i class='bx bx-sun'></i>
            <div class="details">
              <span><%=light%></span>%
              <p>Light</p>
            </div>
          </div>

        </div>
      </section>
    </div>
    
<!--     <script src="script.js"></script> -->
    <script>
    function showArduinos(id) {
    	console.log(id + "Content");
    	var dropdowns = document.getElementsByClassName("dropdown-content");
   		var i;
		for (i = 0; i < dropdowns.length; i++) {
      		var openDropdown = dropdowns[i];
      		if (openDropdown.classList.contains('show')) {
        		openDropdown.classList.remove('show');
      		}
   		}
    	document.getElementById(id + "Content").classList.toggle("show");
    }
    
	window.onclick = function(event) {
	  	if (!event.target.matches('.dropbtn')) {
	    	var dropdowns = document.getElementsByClassName("dropdown-content");
	   		var i;
    		for (i = 0; i < dropdowns.length; i++) {
	      		var openDropdown = dropdowns[i];
	      		if (openDropdown.classList.contains('show')) {
	        		openDropdown.classList.remove('show');
	      		}
	   		}
	  	}
	}
    </script>

  <%
  jb.disconnect();
  %>
  </body>
</html>
