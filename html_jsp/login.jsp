<%@ page language="java"%>
<%@ page import="jdbc.DBConnection"%>
<%@ page import="java.sql.*"%>
<%

    DBConnection db = new DBConnection();
    Connection conn = db.getConnection();
    Statement stmt = conn.createStatement();
    
    ResultSet resultSet = null;
    
    String rUsername = request.getParameter("username");
    String rPassword = request.getParameter("password");
    
    try {
	    String sqlQuery = "select * from websiteusers where username='" + rUsername + "' and password='" + rPassword + "';";

	    resultSet = stmt.executeQuery(sqlQuery);
	    String message = "\'" + rUsername + "\'" + " has been successfully logged in.";
	    if(resultSet.next())
	    {   
		session.setAttribute("username", resultSet.getString("username"));
		session.setAttribute("accesslevel", new Integer(resultSet.getInt("accesslevel")));		
		response.sendRedirect("admin.jsp?msg="+message);
	    }
	    else
	    {
	      	message = "Incorrect username/password combination. <a href=\"/index.html\">Click here</a> to return to the home page.";
	      	response.sendRedirect("loginFail.jsp?msg="+message);
	    }
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
    
    
    try{
         if(stmt != null) {
             stmt.close();
         }
         if(resultSet != null) {
             resultSet.close();
         }
         
         if(conn != null) {
             conn.close();
         }
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }

%>