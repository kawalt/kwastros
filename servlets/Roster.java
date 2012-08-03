package servlets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains utility type methods dealing with player information. 
 * @author Kyle
 *
 */
public class Roster
{
    public Roster(Connection dbConn) throws SQLException
    {
        conn = dbConn;
        stmt = conn.createStatement();
        stats = new GameLog(conn);
    }

    public String getFirstName(int playerID) throws SQLException
    {
        String query = "SELECT firstname FROM roster WHERE player_id="
                + playerID + ";";
        System.out.println(query);
        String name = "error";
        rs = stmt.executeQuery(query);
        while (rs.next()) {
            name = rs.getString("firstname");
        }
        return name;
    }

    public String getLastName(int playerID) throws SQLException
    {
        String query = "SELECT lastname FROM roster WHERE player_id="
                + playerID + ";";
        System.out.println(query);
        String name = "error";
        rs = stmt.executeQuery(query);
        while (rs.next()) {
            name = rs.getString("lastname");
        }
        return name;
    }

    public String getBirthDate(int playerID) throws SQLException
    {
        String query = "SELECT birthdate FROM roster WHERE player_id="
                + playerID + ";";
        System.out.println(query);
        String name = "error";
        rs = stmt.executeQuery(query);
        while (rs.next()) {
            name = rs.getString("birthdate");
        }
        return name;
    }

    public String getBirthPlace(int playerID) throws SQLException
    {
        String query = "SELECT birthplace FROM roster WHERE player_id="
                + playerID + ";";
        System.out.println(query);
        String name = "error";
        rs = stmt.executeQuery(query);
        while (rs.next()) {
            name = rs.getString("birthplace");
        }
        return name;
    }

    public String getThrowHand(int playerID) throws SQLException
    {
        String query = "SELECT throwhand FROM roster WHERE player_id="
                + playerID + ";";
        System.out.println(query);
        String name = "error";
        rs = stmt.executeQuery(query);
        while (rs.next()) {
            name = rs.getString("throwhand");
        }
        return name;
    }

    public String getBatHand(int playerID) throws SQLException
    {
        String query = "SELECT bathand FROM roster WHERE player_id=" + playerID
                + ";";
        System.out.println(query);
        String name = "error";
        rs = stmt.executeQuery(query);
        while (rs.next()) {
            name = rs.getString("bathand");
        }
        return name;
    }

    public String getNumber(int playerID) throws SQLException
    {
        String query = "SELECT number FROM roster WHERE player_id=" + playerID
                + ";";
        System.out.println(query);
        String name = "error";
        rs = stmt.executeQuery(query);
        while (rs.next()) {
            name = rs.getString("number");
        }
        return name;
    }

    public String getPositions(int playerID) throws SQLException
    {
        String query = "SELECT positions FROM roster WHERE player_id="
                + playerID + ";";
        System.out.println(query);
        String name = "error";
        rs = stmt.executeQuery(query);
        while (rs.next()) {
            name = rs.getString("positions");
        }
        return name;
    }

    public String getSnapShotStatTable(int playerID, String filter, String year) throws SQLException
    {
        String table = "";
        rs = stats.getSnapShotStats(playerID, filter, year);
        table = "<table border=0 bgcolor=\"#ded9d9\" align=center id=\"h4\">" + "<tr>" + "<td>&nbsp;&nbsp;G&nbsp;&nbsp;</td>"
                + "<td>&nbsp;&nbsp;R&nbsp;&nbsp;</td>" + "<td>&nbsp;&nbsp;HR&nbsp;&nbsp;</td>" + "<td>&nbsp;&nbsp;RBI&nbsp;&nbsp;</td>"
                + "<td>&nbsp;&nbsp;BA&nbsp;&nbsp;</td>"
                + "</tr>" + "<tr>";
        if (rs.next()) {
            for (int i = 4; i <= 8; i++) {
                table = table + "<td>" + rs.getString(i) + "</td>";
            }
        }
        table = table + "</tr></table>";
        return table;
    }
    
    public String getIndividualPlayerStats(int playerID, int filter) throws SQLException
    {
        String table = "";
        String teamHeading = "";
        int rows = 11;
        if (filter == 2 || filter == 3) {
            teamHeading = "<td>Team</td>";
            rows = 12;
        }
        rs = stats.getIndividualPlayerStats(playerID, filter);
        table = "<table border=0 bgcolor=\"#ded9d9\" align=center id=\"h4\">" + "<tr>" + "<td>Year</td>" + teamHeading + "<td>&nbsp;&nbsp;G&nbsp;&nbsp;</td>"
        + "<td>&nbsp;&nbsp;AB&nbsp;&nbsp;</td>" + "<td>&nbsp;&nbsp;H&nbsp;&nbsp;</td>" + "<td>&nbsp;&nbsp;R&nbsp;&nbsp;</td>" 
        + "<td>&nbsp;&nbsp;HR&nbsp;&nbsp;</td>" + "<td>&nbsp;&nbsp;RBI&nbsp;&nbsp;</td>"
        + "<td>&nbsp;&nbsp;BA&nbsp;&nbsp;</td>" + "<td>&nbsp;&nbsp;OBP&nbsp;&nbsp;</td>" + "<td>&nbsp;&nbsp;SLG&nbsp;&nbsp;</td>"
        + "<td>&nbsp;&nbsp;OPS&nbsp;&nbsp;</td>"
        + "</tr>";
        
        while (rs.next()) {     
            table = table + "<tr>";
            for (int i = 1; i <= rows; i++) {
                table = table + "<td>" + rs.getString(i) + "</td>";
            }
            table = table + "</tr>";
        }
        table = table + "</table>";
        return table;
    }

    public String listYears(int selectedYear, int playerID) 
    {
        ArrayList<Integer> years = new ArrayList<Integer>();
        years.add(2010);
        years.add(2011);
        
        String output = "";
        output = "<form name=\"yearList\">" + 
        "<SELECT name=\"yearLinks\" onChange=\"window.location=document.yearList.yearLinks.options[document.yearList.yearLinks.selectedIndex].value\">";
        if (selectedYear == 0) {
               output += "<OPTION value=\"/roster.jsp?id=" + playerID + "&gl=0&year=0\">Choose a year";            
               for (int i = 0; i < years.size(); i++) {
               output += "<OPTION value=\"/roster.jsp?id=" + playerID + "&gl=0&year=" + years.get(i) + "\">" + years.get(i);
            }
        }
        else {
            years.remove(new Integer(selectedYear));
            output += "<OPTION value=\"/roster.jsp?id=" + playerID + "&gl=0&year=" + selectedYear + "\">Choose a year";
            output += "<OPTION selected>" + selectedYear;
            for (int i = 0; i < years.size(); i++) {
                output += "<OPTION value=\"/roster.jsp?id=" + playerID + "&gl=0&year=" + years.get(i) + "\">" + years.get(i);
             }        
        }
        output += "</SELECT></FORM>";
        return output;
    }
    
    public String listPlayers(int year, String team) throws SQLException
    {
        String query = "select firstname, lastname, player_id " +
        		"from roster r where player_id in " +
        		"(select player_id from gamelog gl" +
        		" join games ga on gl.game_id=ga.game_id " +
        		"join tournaments t on ga.tournament_id=t.tournament_id " +
        		"where t.team='" + team + "' " + "and round(ga.tournament_id/10000)=" + year +
        		" and (r.team1='KWAstros' or r.team2='KWAstros')) order by lastname asc;";
        rs = stmt.executeQuery(query);
        String players = "";
        players = "<form name=\"playerList\">" + 
        "<SELECT name=\"playerLinks\" onChange=\"window.location=document.playerList.playerLinks.options[document.playerList.playerLinks.selectedIndex].value\">" +
        "<OPTION>Choose a player";
        
        int i = 0;
        while (rs.next()) {
            players += "<OPTION name =\"" + i + "\" " + "value=\"/roster.jsp?id=" + rs.getString("player_id") + "&gl=0" + "&year=" + year + "\">"
                    + rs.getString("firstname") + " "
                    + rs.getString("lastname");
            i++;
        }
        players += "</SELECT></FORM>";
        return players;
    }
    
    public String listPlayersNoNewForm(int year, String team, String listName) throws SQLException
    {
        String query = "select firstname, lastname, player_id " +
                "from roster r where player_id in " +
                "(select player_id from gamelog gl" +
                " join games ga on gl.game_id=ga.game_id " +
                "join tournaments t on ga.tournament_id=t.tournament_id " +
                "where t.team='" + team + "' " + "and round(ga.tournament_id/10000)=" + year +
                " and (r.team1='KWAstros' or r.team2='KWAstros')) order by lastname asc;";
        rs = stmt.executeQuery(query);
        String players = "";
        players = "<SELECT name=\"" + listName + "\">" + "<OPTION value=\"Empty\">Choose a player</OPTION>";
        
        while (rs.next()) {
            players += "<OPTION value=\"" + rs.getString("firstname") + " " + rs.getString("lastname") + "\">"
                    + rs.getString("firstname") + " "
                    + rs.getString("lastname")
                    + "</OPTION>";
        }
        players += "</SELECT>";
        return players;
    }
    
    public ArrayList<String> getYearsPlayed(int playerID) throws SQLException 
    {
        String query = "select distinct round(ga.game_id/1000000,0) from gamelog gl join games ga on gl.game_id=ga.game_id " +
        	"join tournaments t on ga.tournament_id=t.tournament_id where gl.player_id =" + playerID + " and t.team='KWAstros';";
        ArrayList<String> years = new ArrayList<String>();
        rs = stmt.executeQuery(query);
        while(rs.next()) {
            years.add(rs.getString(1));
        }
        return years;
    }

    public String[] getLineupString() throws SQLException
    {
        String query = "SELECT firstname, lastname FROM roster;";
        ResultSet rs = stmt.executeQuery(query);
        ArrayList<String> playersArray = new ArrayList<String>();
        while (rs.next()) {
            playersArray.add(rs.getString(1) + " " + rs.getString(2));
        }
        String[] players = new String[playersArray.size() + 1];
        players[0] = "Empty";
        for (int i = 0; i < playersArray.size(); i++) {
            players[i + 1] = playersArray.get(i);
        }
        return players;
    }

    public int getPlayerID(String firstName, String lastName)
            throws SQLException
    {
        System.out.println("getPlayerID for " + firstName + " " + lastName);
        ResultSet rs = stmt
                .executeQuery("SELECT player_id FROM roster WHERE firstname='"
                        + firstName + "' AND lastname='" + lastName + "';");
        System.out.println("SELECT player_id FROM roster WHERE firstname='"
                + firstName + "' AND lastname='" + lastName + "';");
        rs.next();
        return rs.getInt(1);
    }

    public String printGameLog(int playerID, int glID) throws SQLException
    {
        if (glID == 0) {
            return "";
        }
        else {
            glID *= 1000000;
        }
        String yearQuery = "t.game_id>" + glID + " and t.game_id<" + (glID + 1000000);
        String query = "select date_format(g.date, \'%b %d\') as _DATE, if(g.hometeam=\'KW Astros\', g.visitingteam, " +
        		"(select concat(\'@\',g.hometeam))) as OPP, (select if(g.hometeam=\'KW Astros\', (select (select concat" +
        		"(if(g.homescore>g.visitorscore,\'W\',\'L\'),\'\\, \',(select cast(g.homescore as char(2))),\'-\'," +
        		"(select cast(g.visitorscore as char(2)))))), (select (select concat(if(g.homescore<g.visitorscore," +
        		"\'W\',\'L\'),\'\\, \',(select cast(g.visitorscore as char(2))),\'-\',(select cast(g.homescore as " +
        		"char(2)))))))) as Result, t.ab, t.r, t.h, t.2b, t.3b, t.hr, t.rbi, t.bb, t.k, (select round(sum(t1.h)/(sum(t1.ab)+sum(t1.sf)),3)" +
        		" from gamelog t1 where t1.game_id<=t.game_id and round(t1.game_id/1000000)=round(t.game_id/1000000) " +
        		"and t1.player_id = t.player_id) as AVG, (select round((sum(t1.h)+sum(t1.bb))/(sum(t1.ab)+sum(t1.bb)),3) " +
        		"from gamelog t1 where t1.game_id<=t.game_id and round(t1.game_id/1000000)=round(t.game_id/1000000) " +
        		"and t1.player_id = t.player_id) as OBP, (select round(sum(tb)/sum(ab),3) from gamelog t1 where t1.game_id<=t.game_id " +
        		"and round(t1.game_id/1000000)=round(t.game_id/1000000) and t1.player_id = t.player_id) as SLG, (select SLG+OBP) " +
        		"as OPS from gamelog t join games g on t.game_id = g.game_id join tournaments t1 on g.tournament_id = " +
        		"t1.tournament_id where t.player_id=" + playerID + " and " + yearQuery + " and t1.team=\'KWAstros\' order by t.game_id DESC;";
                
        ResultSet rs = stmt.executeQuery(query);
        String output = "<tr><td width=10% id=\"h9\">Date</td><td width=19% id=\"h9\">Opponent</td>"
                + "<td width=11% id=\"h9\">Result</td><td width=5% id=\"h9\">AB</td>"
                + "<td width=5% id=\"h9\">R</td><td width=5% id=\"h9\">H</td>"
                + "<td width=5% id=\"h9\">2B</td>"
                + "<td width=5% id=\"h9\">3B</td>"
                + "<td width=5% id=\"h9\">HR</td><td width=5% id=\"h9\">RBI</td>"
                + "<td width=5% id=\"h9\">BB</td><td width=5% id=\"h9\">K</td>"
                + "<td width=5% id=\"h9\">BA</td>"
                + "<td width=5% id=\"h9\">SLG</td><td width=5% id=\"h9\">OPS</td></tr>";
        while (rs.next()) {
            output += "<tr><td id=\"h7\">" + rs.getString(1)
                    + "</td><td id=\"h7\">" + rs.getString(2)
                    + "</td><td id=\"h7\">" + rs.getString(3) + "</td>";
            for (int i = 4; i < 17; i++) {
                if (i != 14) {
                    output += "<td id=\"h7\">" + rs.getString(i) + "</td>";
                }

            }
        }
        return output;
    }

    public void close() throws SQLException
    {
        stmt.close();
        if (!conn.isClosed()) {
            conn.close();
        }
    }

    private Connection conn;
    private Statement stmt;
    private ResultSet rs;
    private GameLog stats;
}
