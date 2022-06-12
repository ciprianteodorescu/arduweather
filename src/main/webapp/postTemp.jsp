<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ page language="java" import="java.lang.*,java.math.*,db.*,java.sql.*, java.io.*, java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Insert title here</title>
</head>
<jsp:useBean id="jb" scope="session" class="db.JavaBean" />
<jsp:setProperty name="jb" property="*" />
<body>
<%
	int idArduino = Integer.parseInt(request.getParameter("idArduino"));
	int idSensorTemp = Integer.parseInt(request.getParameter("idSensorTemp"));
	int idSensorLight = Integer.parseInt(request.getParameter("idSensorLight"));
	int temp = Integer.parseInt(request.getParameter("temp"));
	int light = Integer.parseInt(request.getParameter("light"));
	System.out.println(idArduino + ", " + idSensorTemp + ", " + idSensorLight + ", " + temp + ", " + light);
	
	jb.postMeasurementToDB(idArduino, idSensorTemp, idSensorLight, temp, light);
%>

</body>
</html>