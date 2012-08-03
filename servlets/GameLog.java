package servlets;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class contains utility type methods for entering player stats into, and getting player stats from the database. 
 * @author Kyle
 *
 */
public class GameLog
{
    public GameLog(Connection dbConn) throws SQLException
    {
        conn = dbConn;
        stmt = conn.createStatement();
    }

    public ResultSet getSnapShotStats(int playerID, String filter, String year)
            throws SQLException
    {
        String query = "select round(ga.tournament_id/10000) as Year, "
                + "(select sum(ab)+sum(sf)) as AB, "
                + "(select sum(h)) as H, "
                + "count(*) as G, "
                + "(select sum(r)) as R, "
                + "(select sum(hr)) as HR, "
                + "(select sum(rbi)) as RBI, "
                + "(select round(sum(h)/(sum(ab)+sum(sf)),3)) as AVG "
                + "from roster r join gamelog g on r.player_id = g.player_id "
                + "join games ga on g.game_id = ga.game_id join tournaments t on t.tournament_id=ga.tournament_id "
                + "where mod(ga.tournament_id,10000) >= 0 and round(ga.tournament_id/10000)="
                + year
                + " and r.player_id= "
                + playerID
                + " group by round(ga.tournament_id/10000) order by round(ga.tournament_id/10000) desc;";
        ResultSet rs = stmt.executeQuery(query);
        return rs;
    }

    public ResultSet getIndividualPlayerStats(int playerID, int filter)
            throws SQLException
    {
        String teamQuery = "";
        String queryFilter = "";
        if (filter == 1) {
            queryFilter = ">= 0";
        }
        else if (filter == 2) {
            queryFilter = "> 0";
            teamQuery = "t.team as Team, ";
        }
        else if (filter == 3) {
            queryFilter = "= 0";
            teamQuery = "t.team as Team, ";
        }
        String query = "select round(ga.tournament_id/10000) as Year, "
                + teamQuery
                + "count(*) as G, "
                + "(select sum(ab)+sum(sf)) as AB, (select sum(h)) as H, (select sum(r)) as R, "
                + "(select sum(hr)) as HR, "
                + "(select sum(rbi)) as RBI, "
                + "(select round(sum(h)/(sum(ab)+sum(sf)),3)) as AVG, "
                + "(select round((sum(h)+sum(bb))/(sum(ab)+sum(sf)+sum(bb)),3)) as OBP, "
                + "(select round(sum(tb)/(sum(ab)+sum(sf)),3)) as SLG, "
                + "(select (round((sum(h)+sum(bb))/(sum(ab)+sum(sf)+sum(bb)),3))+(round(sum(tb)/(sum(ab)+sum(sf)),3))) as OPS "
                + "from roster r join gamelog g on r.player_id = g.player_id "
                + "join games ga on g.game_id = ga.game_id join tournaments t on t.tournament_id=ga.tournament_id "
                + "where mod(ga.tournament_id,10000) " + queryFilter
                + " and r.player_id=" + playerID
                + " group by round(ga.tournament_id/10000) "
                + "order by round(ga.tournament_id/10000) desc;";
        ResultSet rs = stmt.executeQuery(query);
        return rs;
    }

    public void enterStats(ArrayList<HashMap<String, int[]>> batterList,
            ArrayList<HashMap<String, int[]>> subbedOutBatterList)
            throws SQLException
    {
        String update = "";
        for (int i = 0; i < batterList.size(); i++) {
            update = "INSERT INTO gamelog (player_id, game_id, ab, h, r, 2b, 3b, hr, tb, rbi, bb, k, sf, fc, dp, ab_risp, h_risp, "
                    + "ab_risp_2, h_risp_2, hrout, roe, roeNoHit, lineup, 1fo, 1fh, 1lo, 1lh, 1go, 1gh, 2fo, 2fh, 2lo, 2lh, 2go, 2gh, 3fo, 3fh, "
                    + "3lo, 3lh, 3go, 3gh, 4fo, 4fh, 4lo, 4lh, 4go, 4gh, 5fo, 5fh, 5lo, 5lh, 5go, 5gh, 6fo, 6fh, 6lo, 6lh, 6go, 6gh, "
                    + "7fo, 7fh, 7lo, 7lh, 7go, 7gh, 8fo, 8fh, 8lo, 8lh, 8go, 8gh, 9fo, 9fh, 9lo, 9lh, 9go, 9gh, 78fo, 78fh, 78lo, "
                    + "78lh, 78go, 78gh, 98fo, 98fh, 98lo, 98lh, 98go, 98gh) VALUES("
                    + batterList.get(i).values().iterator().next()[0]
                    + ", "
                    + batterList.get(i).values().iterator().next()[1]
                    + ", "
                    + batterList.get(i).values().iterator().next()[2]
                    + ", "
                    + batterList.get(i).values().iterator().next()[3]
                    + ", "
                    + batterList.get(i).values().iterator().next()[4]
                    + ", "
                    + batterList.get(i).values().iterator().next()[5]
                    + ", "
                    + batterList.get(i).values().iterator().next()[6]
                    + ", "
                    + batterList.get(i).values().iterator().next()[7]
                    + ", "
                    + batterList.get(i).values().iterator().next()[8]
                    + ", "
                    + batterList.get(i).values().iterator().next()[9]
                    + ", "
                    + batterList.get(i).values().iterator().next()[10]
                    + ", "
                    + batterList.get(i).values().iterator().next()[11]
                    + ", "
                    + batterList.get(i).values().iterator().next()[12]
                    + ", "
                    + batterList.get(i).values().iterator().next()[13]
                    + ", "
                    + batterList.get(i).values().iterator().next()[14]
                    + ", "
                    + batterList.get(i).values().iterator().next()[15]
                    + ", "
                    + batterList.get(i).values().iterator().next()[16]
                    + ", "
                    + batterList.get(i).values().iterator().next()[17]
                    + ", "
                    + batterList.get(i).values().iterator().next()[18]
                    + ", "
                    + batterList.get(i).values().iterator().next()[88]
                    + ", "
                    + batterList.get(i).values().iterator().next()[90]
                    + ", "
                    + batterList.get(i).values().iterator().next()[91]
                    + ", "
                    + batterList.get(i).values().iterator().next()[87]
                    + ", "
                    + batterList.get(i).values().iterator().next()[19]
                    + ", "
                    + batterList.get(i).values().iterator().next()[20]
                    + ", "
                    + batterList.get(i).values().iterator().next()[21]
                    + ", "
                    + batterList.get(i).values().iterator().next()[22]
                    + ", "
                    + batterList.get(i).values().iterator().next()[23]
                    + ", "
                    + batterList.get(i).values().iterator().next()[24]
                    + ", "
                    + batterList.get(i).values().iterator().next()[25]
                    + ", "
                    + batterList.get(i).values().iterator().next()[26]
                    + ", "
                    + batterList.get(i).values().iterator().next()[27]
                    + ", "
                    + batterList.get(i).values().iterator().next()[28]
                    + ", "
                    + batterList.get(i).values().iterator().next()[29]
                    + ", "
                    + batterList.get(i).values().iterator().next()[30]
                    + ", "
                    + batterList.get(i).values().iterator().next()[31]
                    + ", "
                    + batterList.get(i).values().iterator().next()[32]
                    + ", "
                    + batterList.get(i).values().iterator().next()[33]
                    + ", "
                    + batterList.get(i).values().iterator().next()[34]
                    + ", "
                    + batterList.get(i).values().iterator().next()[35]
                    + ", "
                    + batterList.get(i).values().iterator().next()[36]
                    + ", "
                    + batterList.get(i).values().iterator().next()[37]
                    + ", "
                    + batterList.get(i).values().iterator().next()[38]
                    + ", "
                    + batterList.get(i).values().iterator().next()[39]
                    + ", "
                    + batterList.get(i).values().iterator().next()[40]
                    + ", "
                    + batterList.get(i).values().iterator().next()[41]
                    + ", "
                    + batterList.get(i).values().iterator().next()[42]
                    + ", "
                    + batterList.get(i).values().iterator().next()[43]
                    + ", "
                    + batterList.get(i).values().iterator().next()[44]
                    + ", "
                    + batterList.get(i).values().iterator().next()[45]
                    + ", "
                    + batterList.get(i).values().iterator().next()[46]
                    + ", "
                    + batterList.get(i).values().iterator().next()[47]
                    + ", "
                    + batterList.get(i).values().iterator().next()[48]
                    + ", "
                    + batterList.get(i).values().iterator().next()[49]
                    + ", "
                    + batterList.get(i).values().iterator().next()[50]
                    + ", "
                    + batterList.get(i).values().iterator().next()[51]
                    + ", "
                    + batterList.get(i).values().iterator().next()[52]
                    + ", "
                    + batterList.get(i).values().iterator().next()[53]
                    + ", "
                    + batterList.get(i).values().iterator().next()[54]
                    + ", "
                    + batterList.get(i).values().iterator().next()[55]
                    + ", "
                    + batterList.get(i).values().iterator().next()[56]
                    + ", "
                    + batterList.get(i).values().iterator().next()[57]
                    + ", "
                    + batterList.get(i).values().iterator().next()[58]
                    + ", "
                    + batterList.get(i).values().iterator().next()[59]
                    + ", "
                    + batterList.get(i).values().iterator().next()[60]
                    + ", "
                    + batterList.get(i).values().iterator().next()[61]
                    + ", "
                    + batterList.get(i).values().iterator().next()[62]
                    + ", "
                    + batterList.get(i).values().iterator().next()[63]
                    + ", "
                    + batterList.get(i).values().iterator().next()[64]
                    + ", "
                    + batterList.get(i).values().iterator().next()[65]
                    + ", "
                    + batterList.get(i).values().iterator().next()[66]
                    + ", "
                    + batterList.get(i).values().iterator().next()[67]
                    + ", "
                    + batterList.get(i).values().iterator().next()[68]
                    + ", "
                    + batterList.get(i).values().iterator().next()[69]
                    + ", "
                    + batterList.get(i).values().iterator().next()[70]
                    + ", "
                    + batterList.get(i).values().iterator().next()[71]
                    + ", "
                    + batterList.get(i).values().iterator().next()[72]
                    + ", "
                    + batterList.get(i).values().iterator().next()[73]
                    + ", "
                    + batterList.get(i).values().iterator().next()[74]
                    + ", "
                    + batterList.get(i).values().iterator().next()[75]
                    + ", "
                    + batterList.get(i).values().iterator().next()[76]
                    + ", "
                    + batterList.get(i).values().iterator().next()[77]
                    + ", "
                    + batterList.get(i).values().iterator().next()[78]
                    + ", "
                    + batterList.get(i).values().iterator().next()[79]
                    + ", "
                    + batterList.get(i).values().iterator().next()[80]
                    + ", "
                    + batterList.get(i).values().iterator().next()[81]
                    + ", "
                    + batterList.get(i).values().iterator().next()[82]
                    + ", "
                    + batterList.get(i).values().iterator().next()[83]
                    + ", "
                    + batterList.get(i).values().iterator().next()[84] + ");";
            stmt.executeUpdate(update);
        }
        for (int i = 0; i < subbedOutBatterList.size(); i++) {
            update = "INSERT INTO gamelog (player_id, game_id, ab, h, r, 2b, 3b, hr, tb, rbi, bb, k, sf, fc, dp, ab_risp, h_risp, "
                + "ab_risp_2, h_risp_2, hrout, roe, roeNoHit, lineup, 1fo, 1fh, 1lo, 1lh, 1go, 1gh, 2fo, 2fh, 2lo, 2lh, 2go, 2gh, 3fo, 3fh, "
                + "3lo, 3lh, 3go, 3gh, 4fo, 4fh, 4lo, 4lh, 4go, 4gh, 5fo, 5fh, 5lo, 5lh, 5go, 5gh, 6fo, 6fh, 6lo, 6lh, 6go, 6gh, "
                + "7fo, 7fh, 7lo, 7lh, 7go, 7gh, 8fo, 8fh, 8lo, 8lh, 8go, 8gh, 9fo, 9fh, 9lo, 9lh, 9go, 9gh, 78fo, 78fh, 78lo, "
                + "78lh, 78go, 78gh, 98fo, 98fh, 98lo, 98lh, 98go, 98gh) VALUES("
                    + subbedOutBatterList.get(i).values().iterator().next()[0]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[1]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[2]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[3]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[4]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[5]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[6]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[7]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[8]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[9]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[10]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[11]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[12]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[13]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[14]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[15]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[16]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[17]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[18]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[88]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[90]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[91]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[87]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[19]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[20]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[21]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[22]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[23]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[24]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[25]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[26]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[27]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[28]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[29]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[30]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[31]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[32]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[33]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[34]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[35]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[36]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[37]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[38]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[39]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[40]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[41]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[42]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[43]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[44]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[45]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[46]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[47]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[48]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[49]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[50]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[51]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[52]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[53]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[54]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[55]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[56]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[57]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[58]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[59]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[60]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[61]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[62]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[63]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[64]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[65]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[66]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[67]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[68]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[69]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[70]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[71]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[72]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[73]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[74]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[75]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[76]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[77]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[78]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[79]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[80]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[81]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[82]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[83]
                    + ", "
                    + subbedOutBatterList.get(i).values().iterator().next()[84]
                    + ");";
            stmt.executeUpdate(update);
        }
    }

    public String printCustomStatReport(String sort, String filter)
            throws SQLException
    {
        if (sort == null || sort == "") {
            sort = "G";
        }

        String gameType = "";
        String gameYear = "";

        if (filter.equals("1")) {
            gameType = "=0";
            gameYear = "=2011";
        }
        else if (filter.equals("2")) {
            gameType = ">0";
            gameYear = "=2011";
        }
        else if (filter.equals("3")) {
            gameType = ">0";
            gameYear = "=2010";
        }
        else if (filter.equals("4")) {
            gameType = ">0";
            gameYear = ">=2010";
        }
        else if (filter.equals("5")) {
            gameType = ">=0";
            gameYear = "=2011";
        }
        else if (filter.equals("6")) {
            gameType = ">=0";
            gameYear = "=2010";
        }
        else if (filter.equals("7")) {
            gameType = ">=0";
            gameYear = ">=2010";
        }

        String output = "";
        String query = "select concat(r.firstname,' ', r.lastname) as Name, "
                + "count(*) as G, "
                + "(select sum(ab)+sum(sf)) as AB, "
                + "(select sum(ab_risp)+sum(sf)) as AB_RISP, "
                + "(select sum(h_risp)) as H_RISP, "
                + "(select sum(ab_risp_2)) as AB_RISP2, "
                + "(select sum(h_risp_2)) as H_RISP2, "
                + "(select round(sum(h_risp)/(sum(ab_risp)+sum(sf)),3)) as AVG_RISP, "
                + "(select round(sum(h_risp_2)/(sum(ab_risp_2)),3)) as AVG_RISP_2OUT, "
                + "(select sum(rbi)) as RBI "
                + "from roster r join gamelog g on r.player_id = g.player_id "
                + "join games ga on g.game_id = ga.game_id join tournaments t on t.tournament_id=ga.tournament_id "
                + "where t.team='KWAstros' and (r.team1='KWAstros' or r.team2='KWAstros') "
                + "and mod(ga.tournament_id,10000) "
                + gameType
                + " and round(ga.tournament_id/10000) "
                + gameYear
                + " group by r.player_id order by (select (sum(ab)>5)) desc, "
                + sort + " desc;";

        ResultSet rs = stmt.executeQuery(query);

        int numberOfColumns = 10;
        boolean rowColor = false;
        int sortColumn = rs.findColumn(sort);
        while (rs.next()) {
            if (!rs.getString(1).equals("Auto Out")) {
                if (rowColor) {
                    output = output + "<tr class=\"rowColor\">";
                }
                else {
                    output = output + "<tr>";
                }
                if (sortColumn == 1) {
                    output = output
                            + "<td class=\"selectedColor\" width=20.2% align=left>"
                            + rs.getString(1) + "</td>";
                }
                else {
                    output = output + "<td width=20.2% align=left>"
                            + rs.getString(1) + "</td>";
                }
                if (sortColumn == 2) {
                    output = output + "<td class=\"selectedColor\" width=4.2%>"
                            + rs.getString(2) + "</td>";
                }
                else {
                    output = output + "<td width=4.2%>" + rs.getString(2)
                            + "</td>";
                }
                for (int i = 3; i <= numberOfColumns; i++) {
                    if (i == sortColumn) {
                        output = output
                                + "<td class=\"selectedColor\" width=4.2%>"
                                + rs.getString(i) + "</td>";
                    }
                    else {
                        output = output + "<td width=4.2%>" + rs.getString(i)
                                + "</td>";
                    }
                }
                output = output + "</tr>";
                rowColor = !rowColor;
            }
        }
        return output;
    }

    public String printPlayerStats(String sort, String filter)
            throws SQLException
    {
        if (sort == null || sort == "") {
            sort = "AVG";
        }

        String gameType = "";
        String gameYear = "";

        if (filter.equals("1")) {
            gameType = "=0";
            gameYear = "=2011";
        }
        else if (filter.equals("2")) {
            gameType = ">0";
            gameYear = "=2011";
        }
        else if (filter.equals("3")) {
            gameType = ">0";
            gameYear = "=2010";
        }
        else if (filter.equals("4")) {
            gameType = ">0";
            gameYear = ">=2010";
        }
        else if (filter.equals("5")) {
            gameType = ">=0";
            gameYear = "=2011";
        }
        else if (filter.equals("6")) {
            gameType = ">=0";
            gameYear = "=2010";
        }
        else if (filter.equals("7")) {
            gameType = ">=0";
            gameYear = ">=2010";
        }

        String output = "";
        String query = "select concat(r.firstname,' ', r.lastname) as Name, "
                + "count(*) as G, "
                + "(select sum(ab)+sum(sf)) as AB, "
                + "(select sum(h)) as H, "
                + "(select sum(r)) as R, "
                + "(select sum(2b)) as 2B, "
                + "(select sum(3b)) as 3B, "
                + "(select sum(hr)) as HR, "
                + "(select sum(rbi)) as RBI, "
                + "(select sum(bb)) as BB, "
                + "(select sum(k)) as K, "
                + "(select sum(sf)) as SF, "
                + "(select sum(roe)) as ROE, "
                + "(select sum(tb)) as TB, "
                + "(select round(sum(h)/(sum(ab)+sum(sf)),3)) as AVG, "
                + "(select round((sum(h)+sum(bb))/(sum(ab)+sum(sf)+sum(bb)),3)) as OBP, "
                + "(select round(sum(tb)/(sum(ab)+sum(sf)),3)) as SLG, "
                + "(select (round((sum(h)+sum(bb))/(sum(ab)+sum(sf)+sum(bb)),3))+(round(sum(tb)/(sum(ab)+sum(sf)),3))) as OPS, "
                + "(select round(sum(h_risp)/(sum(ab_risp)+sum(sf)),3)) as AVG_RISP, "
                + "(select round(sum(h_risp_2)/(sum(ab_risp_2)),3)) as AVG_RISP_2OUT "
                + "from roster r join gamelog g on r.player_id = g.player_id "
                + "join games ga on g.game_id = ga.game_id join tournaments t on t.tournament_id=ga.tournament_id "
                + "where t.team='KWAstros' and (r.team1='KWAstros' or r.team2='KWAstros') "
                + "and mod(ga.tournament_id,10000) "
                + gameType
                + " and round(ga.tournament_id/10000) "
                + gameYear
                + " group by r.player_id order by (select (sum(ab)+sum(sf))>5) desc, "
                + sort + " desc;";

        ResultSet rs = stmt.executeQuery(query);

        int numberOfColumns = 20;
        boolean rowColor = false;
        int sortColumn = rs.findColumn(sort);
        while (rs.next()) {
            if (!rs.getString(1).equals("Auto Out")) {
                if (rowColor) {
                    output = output + "<tr class=\"rowColor\">";
                }
                else {
                    output = output + "<tr>";
                }
                if (sortColumn == 1) {
                    output = output
                            + "<td class=\"selectedColor\" width=20.2% align=left>"
                            + rs.getString(1) + "</td>";
                }
                else {
                    output = output + "<td width=20.2% align=left>"
                            + rs.getString(1) + "</td>";
                }
                if (sortColumn == 2) {
                    output = output + "<td class=\"selectedColor\" width=4.2%>"
                            + rs.getString(2) + "</td>";
                }
                else {
                    output = output + "<td width=4.2%>" + rs.getString(2)
                            + "</td>";
                }
                for (int i = 3; i <= numberOfColumns; i++) {
                    if (i == sortColumn) {
                        output = output
                                + "<td class=\"selectedColor\" width=4.2%>"
                                + rs.getString(i) + "</td>";
                    }
                    else {
                        output = output + "<td width=4.2%>" + rs.getString(i)
                                + "</td>";
                    }
                }
                output = output + "</tr>";
                rowColor = !rowColor;
            }
        }
        return output;
    }

    public String printStatsHeaders(String filter)
    {
        String headers = "";
        headers = "<tr class=\"headerColor\">"
                + "<td width=10% align=left>Player</td>"
                + "<td width=4.5%><a href=\"/stats.jsp?filter="
                + filter
                + "&sort=G\">G</a></td>"
                + "<td width=4.5%><a href=\"/stats.jsp?filter="
                + filter
                + "&sort=AB\">AB</a></td>"
                + "<td width=4.5%><a href=\"/stats.jsp?filter="
                + filter
                + "&sort=H\">H</a></td>"
                + "<td width=4.5%><a href=\"/stats.jsp?filter="
                + filter
                + "&sort=R\">R</a></td>"
                + "<td width=4.5%><a href=\"/stats.jsp?filter="
                + filter
                + "&sort=2B\">2B</a></td>"
                + "<td width=4.5%><a href=\"/stats.jsp?filter="
                + filter
                + "&sort=3B\">3B</a></td>"
                + "<td width=4.5%><a href=\"/stats.jsp?filter="
                + filter
                + "&sort=HR\">HR</a></td>"
                + "<td width=4.5%><a href=\"/stats.jsp?filter="
                + filter
                + "&sort=RBI\">RBI</a></td>"
                + "<td width=4.5%><a href=\"/stats.jsp?filter="
                + filter
                + "&sort=BB\">BB</a></td>"
                + "<td width=4.5%><a href=\"/stats.jsp?filter="
                + filter
                + "&sort=K\">K</a></td>"
                + "<td width=4.5%><a href=\"/stats.jsp?filter="
                + filter
                + "&sort=SF\">SF</a></td>"
                + "<td width=4.5%><a href=\"/stats.jsp?filter="
                + filter
                + "&sort=ROE\">ROE</a></td>"
                + "<td width=4.5%><a href=\"/stats.jsp?filter="
                + filter
                + "&sort=TB\">TB</a></td>"
                + "<td width=4.5%><a href=\"/stats.jsp?filter="
                + filter
                + "&sort=AVG\">BA</a></td>"
                + "<td width=4.5%><a href=\"/stats.jsp?filter="
                + filter
                + "&sort=OBP\">OBP</a></td>"
                + "<td width=4.5%><a href=\"/stats.jsp?filter="
                + filter
                + "&sort=SLG\">SLG</a></td>"
                + "<td width=4.5%><a href=\"/stats.jsp?filter="
                + filter
                + "&sort=OPS\">OPS</a></td>"
                + "<td width=4.5%><a href=\"/stats.jsp?filter="
                + filter
                + "&sort=AVG_RISP\">RISP</a></td>"
                + "<td width=4.5%><a href=\"/stats.jsp?filter="
                + filter
                + "&sort=AVG_RISP_2OUT\">RISP2</a></td>" + "</tr>";
        return headers;
    }
    
    public String printCustomReportStatsHeaders(String filter)
    {
        String headers = "";
        headers = "<tr class=\"headerColor\">"
                + "<td width=10% align=left>Player</td>"
                + "<td width=4.5%><a href=\"/customstatreport.jsp?filter="
                + filter
                + "&sort=G\">G</a></td>"
                + "<td width=4.5%><a href=\"/customstatreport.jsp?filter="
                + filter
                + "&sort=AB\">AB</a></td>"
                + "<td width=4.5%><a href=\"/customstatreport.jsp?filter="
                + filter
                + "&sort=AB_RISP\">AB_RISP</a></td>"
                + "<td width=4.5%><a href=\"/customstatreport.jsp?filter="
                + filter
                + "&sort=H_RISP\">H_RISP</a></td>"
                + "<td width=4.5%><a href=\"/customstatreport.jsp?filter="
                + filter
                + "&sort=AB_RISP2\">AB_RISP2</a></td>"
                + "<td width=4.5%><a href=\"/customstatreport.jsp?filter="
                + filter
                + "&sort=H_RISP2\">H_RISP2</a></td>"
                + "<td width=4.5%><a href=\"/customstatreport.jsp?filter="
                + filter
                + "&sort=AVG_RISP\">RISP</a></td>"
                + "<td width=4.5%><a href=\"/customstatreport.jsp?filter="
                + filter
                + "&sort=AVG_RISP_2OUT\">RISP2</a></td>"
                + "<td width=4.5%><a href=\"/customstatreport.jsp?filter="
                + filter
                + "&sort=RBI\">RBI</a></td>"
                + "</tr>";
        return headers;
    }

    public void deleteGame(int gameID) throws SQLException
    {
        String update = "";
        update = "DELETE FROM gamelog WHERE game_id=" + gameID + ";";
        stmt.executeUpdate(update);
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
