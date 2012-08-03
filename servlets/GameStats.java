package servlets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class contains utility type methods for entering game stats into, and getting game stats from the database.
 * @author Kyle
 *
 */
public class GameStats
{
    public GameStats(Connection dbConn) throws SQLException
    {
        conn = dbConn;
        stmt = conn.createStatement();
    }

    public void enterGame(int gameID, int tournamentID, String homeTeam,
            String awayTeam, String date, int[] awayRuns, int[] homeRuns,
            int awayTotalRuns, int homeTotalRuns, int awayHits, int homeHits)
            throws SQLException
    {
        String update = "";
        update = "INSERT INTO games (game_id, tournament_id, visitingteam, hometeam, date, visitorscore, homescore, visitorhits, homehits, inning1home, inning2home, inning3home, inning4home, inning5home, inning6home, inning7home, inning8home, inning9home, inning1visitor, inning2visitor, inning3visitor, inning4visitor, inning5visitor, inning6visitor, inning7visitor, inning8visitor, inning9visitor) VALUES("
                + gameID
                + ", "
                + tournamentID
                + ", "
                + "'"
                + awayTeam
                + "'"
                + ", "
                + "'"
                + homeTeam
                + "'"
                + ", "
                + "'"
                + date
                + "'"
                + ", "
                + awayTotalRuns
                + ", "
                + homeTotalRuns
                + ", "
                + awayHits
                + ", "
                + homeHits + ", ";
        for (int i = 0; i < homeRuns.length; i++) {
            update = update + homeRuns[i] + ", ";
        }
        for (int i = 0; i < awayRuns.length; i++) {
            update = update + awayRuns[i];
            if (i != awayRuns.length - 1) {
                update = update + ", ";
            }
        }
        update = update + ");";
        stmt.executeUpdate(update);
        System.out.println(update);
    }

    public void deleteGame(int gameID) throws SQLException
    {
        String update = "";
        update = "DELETE FROM games WHERE game_id=" + gameID + ";";
        stmt.executeUpdate(update);
        System.out.println(update);
    }

    public String printHeaders() throws SQLException
    {
        String output = "";
        output = "<tr>" + "<td id=\"h3\" align=left width=12%>Date</td>"
                + "<td id=\"h3\" width=15%>Tournament/League</td>"
                + "<td id=\"h3\" width=18%>Home Team</td>"
                + "<td id=\"h3\" width=18%>Away Team</td>"
                + "<td id=\"h3\" width=5%>Result</td>"
                + "<td id=\"h3\" width=8%>Score</td>"
                + "<td id=\"h3\" width=12%>Box Score</td>"
                + "<td id=\"h3\" width=12%>Play by Play</td>";
        return output;
    }

    public String printGames(int filter) throws SQLException
    {
        String query = "SELECT date, hometeam, visitingteam, result, homescore, "
                + "visitorscore, game_id, tournaments.description FROM games JOIN "
                + "tournaments ON games.tournament_id = tournaments.tournament_id ";


        if (filter == (1)) {
            // 2011 League
            query = query
                + "WHERE games.tournament_id = 20110000 AND tournaments.team = 'KWAstros'";
        }
        else if (filter == (2)) {
            // 2011 Tournaments
            query = query
                + "WHERE games.tournament_id > 20110000 AND games.tournament_id < 20120000 AND tournaments.team = 'KWAstros'";
        }
        else if (filter == (3)) {
            // 2010 Tournaments
            query = query
                + "WHERE games.tournament_id > 20100000 AND games.tournament_id < 20110000 AND tournaments.team = 'KWAstros'";
        }
        else if (filter == (4)) {
            // All time Tournaments
            query = query
                + "WHERE games.tournament_id > 20100000 AND games.tournament_id != 20110000 AND tournaments.team = 'KWAstros'";
        }
        else if (filter == (5)) {
            // 2011 Combined
            query = query
                + "WHERE games.tournament_id >= 20110000 AND tournaments.team = 'KWAstros'";
        }
        else if (filter == (6)) {
            // 2010 Combined
            query = query
                + "WHERE games.tournament_id >= 20100000 AND games.tournament_id < 20110000 AND tournaments.team = 'KWAstros'";
        }
        else if (filter == (7)) {
            // All Time All Games
            query = query
            + "WHERE tournaments.team = 'KWAstros'";
        }
        query = query + "ORDER BY game_id DESC;";
        ResultSet rs = stmt.executeQuery(query);
        String output = "";
        int homeScore = 0;
        int awayScore = 0;
        int biggerScore = 0;
        int smallerScore = 0;
        String date = "";
        int gameId = 0;
        int result = 0;
        while (rs.next()) {
            gameId = rs.getInt(7);
            date = rs.getString(1).substring(0, 10);
            output = output + "<tr><td id=\"h4\" align=left width=12%>" + date
                    + "</td>";
            output = output + "<td id=\"h4\" width=15%>" + rs.getString(8)
                    + "</td>";
            output = output + "<td id=\"h4\" width=18%>" + rs.getString(2)
                    + "</td>";
            output = output + "<td id=\"h4\" width=18%>" + rs.getString(3)
                    + "</td>";
            result = 2;
            if (rs.getInt(5) > rs.getInt(6)) {
                result = 0;
            }
            else if (rs.getInt(5) < rs.getInt(6)) {
                result = 1;
            }
            if (!rs.getString(2).equals("KW Astros") && result != 2) {

                result = (result + 1) - (result * 2);
            }
            output = output + "<td id=\"h4\" width=5%>";
            if (result == 0) {
                output = output + "Win</td>";
            }
            else if (result == 1) {
                output = output + "Loss</td>";
            }
            else if (result == 2) {
                output = output + "Tie</td>";
            }
            homeScore = rs.getInt(5);
            awayScore = rs.getInt(6);
            biggerScore = Math.max(homeScore, awayScore);
            smallerScore = Math.min(homeScore, awayScore);
            output = output + "<td id=\"h4\" width=8%>" + biggerScore + " - "
                    + smallerScore + "</td>";
            output = output
                    + "<td id=\"h4\" width=12%><a href=\"/boxscore.jsp?id="
                    + gameId + "\">Box Score</a></td>";
            output = output
                + "<td id=\"h4\" width=12%><a href=\"../kwastrosweb/playbyplay/game"
                + gameId + ".txt\">Play by Play</a></td>";
            output = output + "</tr>";
        }
        return output;
    }

    public String printRecord(int filter) throws SQLException
    {
        String query = "";
        if (filter == 1) {
            query = "SELECT homescore, visitorscore, hometeam FROM games JOIN tournaments ON games.tournament_id = tournaments.tournament_id WHERE games.tournament_id = 20110000 AND tournaments.team = 'KWAstros';";
        }
        else if (filter == 2) {
            query = "SELECT homescore, visitorscore, hometeam FROM games JOIN tournaments ON games.tournament_id = tournaments.tournament_id WHERE games.tournament_id > 20110000 AND games.tournament_id < 20120000 AND tournaments.team = 'KWAstros';";
        }
        else if (filter == 3) {
            query = "SELECT homescore, visitorscore, hometeam FROM games JOIN tournaments ON games.tournament_id = tournaments.tournament_id WHERE games.tournament_id > 20100000 AND games.tournament_id < 20110000 AND tournaments.team = 'KWAstros';";
        }
        else if (filter == 4) {
            query = "SELECT homescore, visitorscore, hometeam FROM games JOIN tournaments ON games.tournament_id = tournaments.tournament_id WHERE games.tournament_id > 20100000 AND games.tournament_id != 20110000 AND tournaments.team = 'KWAstros';";
        }
        else if (filter == 5) {
            query = "SELECT homescore, visitorscore, hometeam FROM games JOIN tournaments ON games.tournament_id = tournaments.tournament_id WHERE games.tournament_id >= 20110000 AND tournaments.team = 'KWAstros';";
        }
        else if (filter == 6) {
            query = "SELECT homescore, visitorscore, hometeam FROM games JOIN tournaments ON games.tournament_id = tournaments.tournament_id WHERE games.tournament_id >= 20100000 AND games.tournament_id < 20110000 AND tournaments.team = 'KWAstros';";
        }
        else if (filter == 7) {
            query = "SELECT homescore, visitorscore, hometeam FROM games JOIN tournaments ON games.tournament_id = tournaments.tournament_id WHERE tournaments.team = 'KWAstros';";
        }
        ResultSet rs = stmt.executeQuery(query);
        int wins = 0;
        int losses = 0;
        int ties = 0;
        int result = 0;
        while (rs.next()) {
            result = 2;
            if (rs.getInt(1) > rs.getInt(2)) {
                result = 0;
            }
            else if (rs.getInt(1) < rs.getInt(2)) {
                result = 1;
            }
            if (!rs.getString(3).equals("KW Astros") && result != 2) {
                result = (result + 1) - (result * 2);
            }
            if (result == 0) {
                wins++;
            }
            else if (result == 1) {
                losses++;
            }
            else if (result == 2) {
                ties++;
            }
        }
        String output = "<p id=\"h4\">" + "Current Record: " + wins + " - "
                + losses + " - " + ties + "</p>";
        return output;
    }

    public String printLineScore(int gameID) throws SQLException
    {
        String query = "SELECT inning1home, inning2home, inning3home, "
                + "inning4home, inning5home, inning6home, inning7home, inning8home, "
                + "inning9home, inning10home, inning1visitor, inning2visitor, inning3visitor, "
                + "inning4visitor, inning5visitor, inning6visitor, inning7visitor, "
                + "inning8visitor, inning9visitor, inning10visitor, hometeam, visitingteam, "
                + "homescore, visitorscore, "
                + "homehits, visitorhits  from games where game_id=" + gameID
                + ";";
        ResultSet rs = stmt.executeQuery(query);
        int lastInningAway = 0;
        int lastInningHome = 0;

        boolean homeHitInLast = false;
        rs.next();
        for (int i = 11; i <= 20; i++) {
            if (rs.getInt(i) == -1) {
                lastInningAway = i - 10 - 1;
                break;
            }
        }
        for (int i = 1; i <= 10; i++) {
            if (rs.getInt(i) == -1) {
                lastInningHome = i - 1;
                break;
            }
        }
        if (lastInningAway == lastInningHome) {
            homeHitInLast = true;
        }
        String output = "<tr><td width=34%></td>";
        
        for (int i = 0; i < lastInningAway; i++) {
            output = output + "<td width=3%>" + (i + 1) + "</td>";
        }
        output = output + "<td width=3%>Total</td>";
        output = output + "</tr>";
        output = output + "<tr><td align=left width=34%>"
                + rs.getString("visitingteam") + "</td>";
        for (int i = 11; i < lastInningAway + 11; i++) {
            output = output + "<td width=3%>" + rs.getString(i) + "</td>";
        }
        output = output + "<td width=3%>" + rs.getInt(24)
                + "</td>";
        output = output + "</tr>";
        output = output + "<tr><td align=left width=34%>"
                + rs.getString("hometeam") + "</td>";

        for (int i = 1; i < lastInningHome + 1; i++) {
            output = output + "<td width=3%>" + rs.getString(i) + "</td>";
        }
        if (!homeHitInLast) {
            output = output + "<td width=3%>-</td>";
        }
        output = output + "<td width=3%>" + rs.getInt(23)
                + "</td>";
        output = output + "</tr>";
        return output;
    }
    
    public String printBoxScore(int gameID) throws SQLException
    {
        String query = "select concat(if(lastname='McMahon',substring(firstname,1,2),(if(lastname='Knaap' and " +
        "substring(firstname,1,1)='M',substring(firstname,1,3),substring(firstname,1,1)))),'. ', lastname) as Name, (t.ab + t.sf), " +
        "t.r, t.h, t.rbi, t.2b, t.3b, t.hr, t.bb, t.k, " +
        "(select round(1.000*sum(t1.h)/sum(t1.ab),3) from gamelog t1 join games g on t1.game_id = g.game_id join tournaments ts " +
        "on ts.tournament_id = g.tournament_id where t1.game_id<=t.game_id and " +
        "round(t1.game_id/1000000)=round(t.game_id/1000000) and t1.player_id = t.player_id and ts.team='KWAstros') as " +
        "AVG from gamelog t join roster r on t.player_id = r.player_id where t.game_id=" + gameID +
        " order by t.lineup;";
        ResultSet rs = stmt.executeQuery(query);
        String output = "";
        output = output + "<tr><td width=30% id=\"h3\"></td><td width=7% id=\"h3\">AB</td>" +
        		            "<td width=7% id=\"h3\">R</td><td width=7% id=\"h3\">H</td>" +
        		            "<td width=7% id=\"h3\">RBI</td><td width=7% id=\"h3\">2B</td>" +
        		            "<td width=7% id=\"h3\">3B</td><td width=7% id=\"h3\">HR</td>" +
        		            "<td width=7% id=\"h3\">BB</td>" +
        		            "<td width=7% id=\"h3\">K</td>" +
                            "<td width=7% id=\"h3\">BA</td></tr>";
        
        while (rs.next()) {
            output = output + "<tr><td width=30% id=\"h7\">" + rs.getString(1) + "</td>";
            for (int i = 2; i < 11; i++) {
                output = output + "<td width=7%>" + rs.getInt(i) + "</td>";
            }
            output = output + "<td width=7%>" + rs.getDouble(11) + "</td>";
            output = output + "</tr>";
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
}
