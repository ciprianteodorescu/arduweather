<%@page import="db.JavaBean.Measurement"%>
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
  ArrayList<Arduino> arduinos;
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
            		arduinos = jb.listArduinos();
            		for(Arduino arduino : arduinos) {
            		%>
            			<a href="todayArduino.jsp?idArduino=<%=arduino.id%>"><%=arduino.name%> (<%=arduino.city%>)</a>
            		<%
            		}
            		%>
            	</div>
            </div>
            <div class="dropdown">
	            <li><a onclick="showArduinos(this.id);" class="dropbtn" id="hourlyDrop">Hourly</a></li>
            	<div id="hourlyDropContent" class="dropdown-content">
            		<%
            		arduinos = jb.listArduinos();
            		for(Arduino arduino : arduinos) {
            		%>
            			<a href="hourlyArduino.jsp?idArduino=<%=arduino.id%>"><%=arduino.name%> (<%=arduino.city%>)</a>
            		<%
            		}
            		%>
            	</div>
            </div>
            <div class="dropdown">
	            <li><a onclick="showArduinos(this.id);" class="dropbtn" id="dailyDrop">Daily</a></li>
            	<div id="dailyDropContent" class="dropdown-content">
            		<%
            		arduinos = jb.listArduinos();
            		for(Arduino arduino : arduinos) {
            		%>
            			<a href="dailyArduino.jsp?idArduino=<%=arduino.id%>"><%=arduino.name%> (<%=arduino.city%>)</a>
            		<%
            		}
            		%>
            	</div>
            </div>
          </ul>
    </header>

	<div class="location">
		<i class="bx bx-map"></i>
		<span><%=location%></span>
	</div>
	<div class="daily-wrapper">
	<%
	//System.out.println(idArduino);
	List<Measurement> m = jb.listAverageThreeDaysValues(idArduino);
	int mSize = m.size();
	int start = mSize - 3 >=0 ? mSize - 3 : 0;
	//List<Integer> tempList = new ArrayList<>();
	//List<Integer> lightList = new ArrayList<>();
	if(mSize > 0) {
		for(int i = start; i < mSize; i++) {
		%>
			<div class="wrapper active">      
		      <section class="weather-part">	       
		        <div class="temp">
		          <span class="numb"><%=m.get(i).temperature%></span>
		          <sup>°</sup>C
		        </div>
		        <div class="location">
					<span><%=m.get(i).time%></span>
				</div>
		        <div class="bottom-details">
		
		          <div class="row clouds">
		            <i class='bx bx-sun'></i>
		            <div class="details">
		              <span><%=m.get(i).light%>%</span>
		              <p>Light</p>
		            </div>
		          </div>
		
		        </div>
		      </section>
		    </div>
		<%
		}
	} else {
		%>
		<div class="location">
			<span>No measurements for this location</span>
		</div>
		<%
	}
	%>
	    
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
