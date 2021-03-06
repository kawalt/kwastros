<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en-GB">
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
		<br><br>
		<p id="h5">
		<% if (request.getParameter("msg") != null && request.getParameter("msg") != "") {
			out.print("User " + request.getParameter("msg"));
		   }
		%>
		</p>
		<br>
		<p id="h5">
		<a href="/enterGame.jsp">Click here to enter a game</a>
		</p>
		<br>
		<p id="h5">
		<a href="/enterStats.jsp">Click here to enter stats</a>
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
</body>
</div>
</html>
