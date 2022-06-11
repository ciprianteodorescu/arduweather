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
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
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
	ArrayList<Measurement> measurements = jb.getMeasurementsFromArduino(idArduino);
	%>
	
	<div class="chart-container">
		<canvas id="myChart"></canvas>
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
    
    // chart data
    var temperatures = [];
    var lights = [];
    var times = [];
    <%
    for(Measurement m : measurements) {
    	%>
    	temperatures.push(<%=m.temperature%>);
    	lights.push(<%=m.light%>);
<%--     	times.push(new Date("<%=m.time%>")); --%>
		times.push("<%=m.time%>");
    	<%
    }
    %>
    
	// Chart config
	const config = {
		type: 'line',
	  	data: {
	  		datasets: [{
	  			label: 'Temperatures',
	  			data: temperatures,
	  			borderColor: 'rgba(255, 0, 0, 0.7)',
	  		},
	  		{
	  			label: 'Light',
	  			data: lights,
	  			borderColor: 'rgba(255, 255, 0, 0.7)',
	  		}],
	  		labels: times
	  	},
	  	options: {
	    	responsive: true,
	    	plugins: {
	      		legend: {
	        		position: 'top',
	      		},
	      		title: {
	        		display: true,
	        		text: '<%=location%>'
	      		}
	    	}
	  	},
	};
	
	// chart render
	const myChart = new Chart(document.getElementById('myChart'), config);
	
    </script>

  <%
  jb.disconnect();
  %>
  </body>
</html>
