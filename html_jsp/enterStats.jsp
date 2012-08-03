<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en-GB">
<%@ page import="java.sql.*"%>
<%@ page import="jdbc.Roster"%>
<%@ page import="jdbc.DBConnection"%>
<head>
	<title>KWAstros.com - Home of the KW Astros</title>
	<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8" />
	<meta name="description" content="The Perfect 2 Column Liquid Layout (right menu): No CSS hacks. SEO friendly. iPhone compatible." />
	<meta name="keywords" content="The Perfect 2 Column Liquid Layout (right menu): No CSS hacks. SEO friendly. iPhone compatible." />
	<meta name="robots" content="index, follow" />
	<link rel="shortcut icon" href="/favicon.ico" type="image/x-icon" />
<link rel="stylesheet" type="text/css" href="mystyles2.css" media="screen" />
<%
	if(session.getAttribute("username") == null || 	session.getAttribute("username") == "") {
		String message = "You must be logged in to access this page.";
		response.sendRedirect("loginFail.jsp?msg="+message);
	}

%>
<script type="text/javascript">
	function validateForm()
	{
	var gameid=document.forms["myForm"]["gameid"].value;
	var awayteam=document.forms["myForm"]["awayteam"].value;
	var awayteam=document.forms["myForm"]["awayteam"].value;
	var hometeam=document.forms["myForm"]["hometeam"].value;
	var tournamentid=document.forms["myForm"]["tournamentid"].value;
	var date=document.forms["myForm"]["date"].value;
	var awayruns=document.forms["myForm"]["awayruns"].value;
	var homeruns=document.forms["myForm"]["homeruns"].value;
	if (gameid==null || gameid=="")
	  {
	  alert("Game ID must not be blank.");
	  return false;
	  }
	if (e.name == "enter") {
	if (awayteam=null || awayteam="")
	  {
	  alert("Away Team must not be blank.");
	  return false;
	  }
	if (hometeam==null || hometeam=="")
	  {
	  alert("Home Team must not be blank.");
	  return false;
	  }
	if (tournamentid=null || tournamentid="")
	  {
	  alert("Tournament ID must not be blank.");
	  return false;
	  }
	if (date==null || date=="")
	  {
	  alert("Date must not be blank.");
	  return false;
	  }
	if (awayruns=null || awayruns="")
	  {
	  alert("Away Runs must not be blank.");
	  return false;
	  }
	if (homeruns=null || homeruns="")
	  {
	  alert("Home Runs must not be blank.");
	  return false;
	  }
	 }
	}
</script>
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
		<li><a href="/roster.jsp?id=10&year=2011">Roster</a></li>
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
		int year = 2011;
		%>
		<p id="h9">Stats Enterer App:</p>
		<table>
		<form id="h12" name="myForm" action="${pageContext.request.contextPath}/StatsEnterServlet" method="post">
		<tr id="h5"><td>		
		<table>		
		<tr id="h5"><td>Game ID:</td><td><input type="text" size="20" name="gameid"></td></tr>
		<tr id="h5"><td>Score:</td><td><textarea name="score" type="text" cols="20" rows="25"></textarea></td></tr>
		<tr id="h5"><td><input id="h5" type="submit" name="enter" value="Enter Stats" onclick="return validateForm(this)" ></td>
		<td><input id="h5" type="submit" name="delete" value="Delete Stats" onclick="return validateForm(this)"></td></tr>		
		</table>
		</td>
		<td>
		<table>
		<tr id="h5"><td>
		Lineup:
		</td>
		<td>
		Subs:
		</td>
		<td>
		Inning:
		</td></tr>
		<tr><td>
		<%=roster.listPlayersNoNewForm(year, "KWAstros", "lineupPlayers0") %>
		</td>
		<td>
		<%=roster.listPlayersNoNewForm(year, "KWAstros", "subsPlayers0") %>
		</td>
		<td>
		<input type="text" size="2" name="subinning0">
		</tr></td>
		<tr><td>
		<%=roster.listPlayersNoNewForm(year, "KWAstros", "lineupPlayers1") %>
		</td>
		<td>
		<%=roster.listPlayersNoNewForm(year, "KWAstros", "subsPlayers1") %>
		</td>
		<td>
		<input type="text" size="2" name="subinning1">
		</tr></td>
		<tr><td>
		<%=roster.listPlayersNoNewForm(year, "KWAstros", "lineupPlayers2") %>
		</td>
		<td>
		<%=roster.listPlayersNoNewForm(year, "KWAstros", "subsPlayers2") %>
		</td>
		<td>
		<input type="text" size="2" name="subinning2">
		</tr></td>
		<tr><td>
		<%=roster.listPlayersNoNewForm(year, "KWAstros", "lineupPlayers3") %>
		</td>
		<td>
		<%=roster.listPlayersNoNewForm(year, "KWAstros", "subsPlayers3") %>
		</td>
		<td>
		<input type="text" size="2" name="subinning3">
		</tr></td>
		<tr><td>
		<%=roster.listPlayersNoNewForm(year, "KWAstros", "lineupPlayers4") %>
		</td>
		<td>
		<%=roster.listPlayersNoNewForm(year, "KWAstros", "subsPlayers4") %>
		</td>
		<td>
		<input type="text" size="2" name="subinning4">
		</tr></td>
		<tr><td>
		<%=roster.listPlayersNoNewForm(year, "KWAstros", "lineupPlayers5") %>
		</td>
		<td>
		<%=roster.listPlayersNoNewForm(year, "KWAstros", "subsPlayers5") %>
		</td>
		<td>
		<input type="text" size="2" name="subinning5">
		</tr></td>
		<tr><td>
		<%=roster.listPlayersNoNewForm(year, "KWAstros", "lineupPlayers6") %>
		</td>
		<td>
		<%=roster.listPlayersNoNewForm(year, "KWAstros", "subsPlayers6") %>
		</td>
		<td>
		<input type="text" size="2" name="subinning6">
		</tr></td>
		<tr><td>
		<%=roster.listPlayersNoNewForm(year, "KWAstros", "lineupPlayers7") %>
		</td>
		<td>
		<%=roster.listPlayersNoNewForm(year, "KWAstros", "subsPlayers7") %>
		</td>
		<td>
		<input type="text" size="2" name="subinning7">
		</tr></td>
		<tr><td>
		<%=roster.listPlayersNoNewForm(year, "KWAstros", "lineupPlayers8") %>
		</td>
		<td>
		<%=roster.listPlayersNoNewForm(year, "KWAstros", "subsPlayers8") %>
		</td>
		<td>
		<input type="text" size="2" name="subinning8">
		</tr></td>
		<tr><td>
		<%=roster.listPlayersNoNewForm(year, "KWAstros", "lineupPlayers9") %>
		</td>
		<td>
		<%=roster.listPlayersNoNewForm(year, "KWAstros", "subsPlayers9") %>
		</td>
		<td>
		<input type="text" size="2" name="subinning9">
		</tr></td>
		<tr><td>
		<%=roster.listPlayersNoNewForm(year, "KWAstros", "lineupPlayers10") %>
		</td>
		<td>
		<%=roster.listPlayersNoNewForm(year, "KWAstros", "subsPlayers10") %>
		</td>
		<td>
		<input type="text" size="2" name="subinning10">
		</tr></td>
		<tr><td>
		<%=roster.listPlayersNoNewForm(year, "KWAstros", "lineupPlayers11") %>
		</td>
		<td>
		<%=roster.listPlayersNoNewForm(year, "KWAstros", "subsPlayers11") %>
		</td>
		<td>
		<input type="text" size="2" name="subinning11">
		</tr></td>
		<tr><td>
		<%=roster.listPlayersNoNewForm(year, "KWAstros", "lineupPlayers12") %>
		</td>
		<td>
		<%=roster.listPlayersNoNewForm(year, "KWAstros", "subsPlayers12") %>
		</td>
		<td>
		<input type="text" size="2" name="subinning12">
		</tr></td>
		<tr><td>
		<%=roster.listPlayersNoNewForm(year, "KWAstros", "lineupPlayers13") %>
		</td>
		<td>
		<%=roster.listPlayersNoNewForm(year, "KWAstros", "subsPlayers13") %>
		</td>
		<td>
		<input type="text" size="2" name="subinning13">
		</tr></td>
		
		</table>
		</td>
		</form>
		</table>
		<br>
		<p id="h5">
		<a href="/admin.jsp">Return to admin page</a>
		</p>
		<!-- Column 1 end -->
		</div>
		<div class="col2">
		<!-- Column 2 start -->

		<!-- Column 2 end -->
		</div>
		<div class="col1">				 
		</table>
		</div>
		
	</div>
</div>
<div id="footer">
</div>
<% 
		    roster.close();
		    conn.close();
		%>   
</body>
</div>
</html>
