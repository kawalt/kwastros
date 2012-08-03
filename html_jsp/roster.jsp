<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page import="java.sql.*"%>
<%@ page import="jdbc.Roster"%>
<%@ page import="jdbc.DBConnection"%>
<%@ page import="java.util.ArrayList"%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en-GB">
<head>
	<title>KWAstros.com - Home of the KW Astros</title>
	<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8" />
	<meta name="description" content="The Perfect 2 Column Liquid Layout (right menu): No CSS hacks. SEO friendly. iPhone compatible." />
	<meta name="keywords" content="The Perfect 2 Column Liquid Layout (right menu): No CSS hacks. SEO friendly. iPhone compatible." />
	<meta name="robots" content="index, follow" />
	<link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />
<link rel="stylesheet" type="text/css" href="mystyles2.css" media="screen" />

	</style>
<SCRIPT TYPE="text/javascript">
<!--
	function dropdown(mySel)
	{
	var myWin, myVal;
	myVal = mySel.options[mySel.selectedIndex].value;
	if(myVal)
   	{
   		if(mySel.form.target)myWin = parent[mySel.form.target];
   		else myWin = window;
   		if (! myWin) return true;
   		myWin.location = myVal;
  	 }
	return false;
}
	//-->
</SCRIPT>
</head>
<div id="entirepage">
<body>
<div id="header">
<img src="../kwastrosweb/kwastroslogo2.jpg" />
<table align=center cellpadding="0" cellspacing="0"><tr><td align=center>
	<ul>
		<li><a href="/index.html">Home</a></li>
		<li><a href="/scores.jsp?filter=5">Scores</a></li>
		<li><a href="/schedule.jsp">Schedule</a></li>
		<li><a href="/roster.jsp?id=10&year=2011" class="active" ">Roster</a></li>
		<li><a href="/stats.jsp?filter=5&sort=AVG">Player Stats</a></li>
		<li><a href="/photos.jsp">Photos</a></li>
	</ul>
</td></tr></table>
	<p id="layoutdims"></p>
</div>
<div class="colmask rightmenu">
	<div class="colleft">
		<div class="col1">
		<!-- Column 1 start -->
		<%
		    DBConnection db = new DBConnection();
		    Connection conn = db.getConnection();
		    Roster roster = new Roster(conn);
		    String stringID = request.getParameter("id");
		    int id = 99;
		    if (stringID != null && stringID != "")
		    {
		        id = Integer.parseInt(stringID);
		    }
		    String stringGl = request.getParameter("gl");
		    int gl = 0;
		    if (stringGl != null && stringGl != "")
		    {
		    	gl = Integer.parseInt(stringGl);
		    }
		%>
		<% 
			String stringYear = request.getParameter("year");
			int year = 0;
			if (stringYear != null && stringYear != "")
			{
				year = Integer.parseInt(stringYear);
			}
		%>
		<table align=left id="h5" size=100%>
			<tr valign=top>
			<td width=25%>
			<br>
			<img src=
				"../kwastrosweb/profilepics/<%= id %>.jpg"/>
			</td>
			<td width=75%>
			<br>
			<p><%= roster.getFirstName(id) %> <%= roster.getLastName(id) %> | 
			#<%= roster.getNumber(id) %> | <%= roster.getPositions(id) %></p>
			<p>Birthdate: <%= roster.getBirthDate(id) %></p>
			<p>Birthplace: <%= roster.getBirthPlace(id) %></p>
			<p>Throws: <%= roster.getThrowHand(id) %></p>
			<p>Bats: <%= roster.getBatHand(id) %></p>
			<p>
				<table align=left id="h5" size=100%>
				<tr valign=top>					    
				<td width=75%>
				<%= roster.getSnapShotStatTable(id, "1", "2011") %>            
				</td>
				</tr>
				</table>
			</p>
		</table>
		
		<table align=left><tr><td>

		<p id="h9">Combined Stats</p>
		<%= roster.getIndividualPlayerStats(id, 1) %>            
		<p>&nbsp;</p>
		<p id="h9">Tournament Stats</p>
		<%= roster.getIndividualPlayerStats(id, 2) %>            
		<p>&nbsp;</p>
		<p id="h9">League Stats</p>
		<%= roster.getIndividualPlayerStats(id, 3) %>            
		<p>&nbsp;</p>
		</td></tr>
		</table>		
		<table border="0" align=left size=100% width=100% id="h5">
		<tr><td>
			Game Logs: 
			
			<% ArrayList<String> yearsPlayed = roster.getYearsPlayed(id);
			   for (int i = yearsPlayed.size() - 1; i >= 0; i--) { %>
			   	<a href="/roster.jsp?id=<%=id%>&gl=<%= yearsPlayed.get(i) %>&year=<%=year%>"><%= yearsPlayed.get(i) %></a>
			<%
				if (i < 0) { %>
					&nbsp;|
			<% 
			        }
			%>
			<%
			   }
			   
			%>
			
		</tr></td>
		</table>
		<table border="0" align=left size=100% width=100% id="h5">
				
				<p id="h9">
				<%=roster.printGameLog(id, gl) %> </p>						
		</table>

		<!-- Column 1 end -->
		</div>
		<div class="col2">
					<!-- Column 2 start -->
					<br>
					<table valign="center"><tr><td width="50%" valign="center">
					
					<%= roster.listYears(year, id) %>
					<p id="h3"></p>
					<%=roster.listPlayers(year, "KWAstros") %>

					
					</td></tr></table>
					<!-- Column 2 end -->
		</div>
		<div class="col1">				

		<% 
		    roster.close();
		    conn.close();
		%>   
		</table>
		</div>
		
	</div>
</div>
<div id="footer">
</div>
</body>
</div>
</html>
