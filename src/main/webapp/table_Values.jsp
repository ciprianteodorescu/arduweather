<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page language="java" import="java.lang.*,java.math.*,db.*,java.sql.*, java.io.*, java.util.*"%>    
<!DOCTYPE html>
<html>
<head>
	<meta charset="ISO-8859-1">
	<title>Values Table</title>

	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"	integrity="sha384-1BmE4kWBq78iYhFldvKuhfTAU6auU8tT94WrHftjDbrCEXSU1oBoqyl2QvZ6jIW3"	crossorigin="anonymous">
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-ka7Sk0Gln4gmtz2MlQnikT1wXgYsOg+OMhuP+IlRH9sENBO0LRn5q+8nbTov4+1p"	crossorigin="anonymous"></script>
	
</head>

<jsp:useBean id="jb" scope="session" class="db.JavaBean" />
<jsp:setProperty name="jb" property="*" />

<body>
	<div class="table-responsive">
		<table style="width: 70%;" class="table table-hover" align="center">
			<thead>
				<tr>
					<td><b>idArduino:</b></td>
					<td><b>idSensor:</b></td>
					<td><b>idValue:</b></td>
					<td><b>temperature:</b></td>
					<td><b>luminosity:</b></td>
					<td><b>date&time:</b></td>
				</tr>
			</thead>
			<tbody>
				<%
				jb.connect();
				ResultSet rs = jb.listValues();
				long idValue;
				while (rs.next()) {
					idValue = rs.getInt("idValue");
				%>
					<tr>
						<td><%=rs.getInt("idArduino")%></td>
						<td><%=rs.getInt("idSensor")%></td>
						<td><%=idValue%></td>
						<td><%=rs.getString("temperature")%></td>
						<td><%=rs.getString("luminosity")%></td>
						<td><%=rs.getString("time")%></td>
					</tr>
				<%
				}
				%>
			</tbody>
		</table>
	</div>
</body>
</html>