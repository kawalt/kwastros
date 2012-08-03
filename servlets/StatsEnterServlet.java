package servlets;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import scoring.Parser;
import scoring.Sub;

/**
 * This is a servlet class which allows player stats to be sent from the HTML form to the database.
 * @author Kyle
 *
 */
public class StatsEnterServlet extends HttpServlet
{
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String message = "An error occured.";
        try {
            conn = DBConnection.getConnection();
            gameStats = new GameStats(conn);
            roster = new Roster(conn);
            gameLog = new GameLog(conn);
        }
        catch (SQLException e1) {
            e1.printStackTrace();
        }
        catch (ClassNotFoundException e1) {
            e1.printStackTrace();
        }
        catch (IllegalAccessException e1) {
            e1.printStackTrace();
        }
        catch (InstantiationException e1) {
            e1.printStackTrace();
        }    
        
        if (request.getParameter("enter") != null) {
            lineup = new ArrayList<Integer>();
            subs = new ArrayList<Sub>();
            StringTokenizer stringTok;
            String firstName = "";
            String lastName = "";
            String subFirstName = "";
            String subLastName = "";
            int subPlayerID = 0;
            int playerID = 0;
            int inning = 0;
            if (request.getParameter("gameid") != null && !request.getParameter("gameid").equals("")) {
                lineup.add(Integer.parseInt(request.getParameter("gameid")));
            }
            if (!(request.getParameter("lineupPlayers0").equals("Empty"))) {
                stringTok = new StringTokenizer(request.getParameter("lineupPlayers0"), " ");
                firstName = stringTok.nextToken();
                lastName = stringTok.nextToken();
                try {
                    playerID = roster.getPlayerID(firstName, lastName);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
                
                lineup.add(playerID);
                if (!(request.getParameter("subsPlayers0").equals("Empty")) && (request.getParameter("subinning0") != null) 
                        && (!request.getParameter("subinning0").equals(""))) {
                    stringTok = new StringTokenizer(request.getParameter("subsPlayers0"), " ");
                    subFirstName = stringTok.nextToken();
                    subLastName = stringTok.nextToken();
                    try {
                        subPlayerID = roster.getPlayerID(subFirstName, subLastName);
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                    inning = Integer.parseInt(request.getParameter("subinning0"));
                    subs.add(new Sub(inning, firstName, lastName, subFirstName, subLastName, subPlayerID));
                }
            }
            if (!(request.getParameter("lineupPlayers1").equals("Empty"))) {
                stringTok = new StringTokenizer(request.getParameter("lineupPlayers1"), " ");
                firstName = stringTok.nextToken();
                lastName = stringTok.nextToken();
                try {
                    playerID = roster.getPlayerID(firstName, lastName);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
                lineup.add(playerID);
                if (!(request.getParameter("subsPlayers1").equals("Empty")) && (request.getParameter("subinning1") != null) 
                        && (!request.getParameter("subinning1").equals(""))) {
                    stringTok = new StringTokenizer(request.getParameter("subsPlayers1"), " ");
                    subFirstName = stringTok.nextToken();
                    subLastName = stringTok.nextToken();
                    try {
                        subPlayerID = roster.getPlayerID(subFirstName, subLastName);
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                    inning = Integer.parseInt(request.getParameter("subinning1"));
                    subs.add(new Sub(inning, firstName, lastName, subFirstName, subLastName, subPlayerID));
                }
            }
            if (!(request.getParameter("lineupPlayers2").equals("Empty"))) {
                stringTok = new StringTokenizer(request.getParameter("lineupPlayers2"), " ");
                firstName = stringTok.nextToken();
                lastName = stringTok.nextToken();
                try {
                    playerID = roster.getPlayerID(firstName, lastName);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
                lineup.add(playerID);
                if (!(request.getParameter("subsPlayers2").equals("Empty")) && (request.getParameter("subinning2") != null) 
                        && (!request.getParameter("subinning2").equals(""))) {
                    stringTok = new StringTokenizer(request.getParameter("subsPlayers2"), " ");
                    subFirstName = stringTok.nextToken();
                    subLastName = stringTok.nextToken();
                    try {
                        subPlayerID = roster.getPlayerID(subFirstName, subLastName);
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                    inning = Integer.parseInt(request.getParameter("subinning2"));
                    subs.add(new Sub(inning, firstName, lastName, subFirstName, subLastName, subPlayerID));
                }
            }
            if (!(request.getParameter("lineupPlayers3").equals("Empty"))) {
                stringTok = new StringTokenizer(request.getParameter("lineupPlayers3"), " ");
                firstName = stringTok.nextToken();
                lastName = stringTok.nextToken();
                try {
                    playerID = roster.getPlayerID(firstName, lastName);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
                lineup.add(playerID);
                if (!(request.getParameter("subsPlayers3").equals("Empty")) && (request.getParameter("subinning3") != null) 
                        && (!request.getParameter("subinning3").equals(""))) {
                    stringTok = new StringTokenizer(request.getParameter("subsPlayers3"), " ");
                    subFirstName = stringTok.nextToken();
                    subLastName = stringTok.nextToken();
                    try {
                        subPlayerID = roster.getPlayerID(subFirstName, subLastName);
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                    inning = Integer.parseInt(request.getParameter("subinning3"));
                    subs.add(new Sub(inning, firstName, lastName, subFirstName, subLastName, subPlayerID));
                }
            }
            if (!(request.getParameter("lineupPlayers4").equals("Empty"))) {
                stringTok = new StringTokenizer(request.getParameter("lineupPlayers4"), " ");
                firstName = stringTok.nextToken();
                lastName = stringTok.nextToken();
                try {
                    playerID = roster.getPlayerID(firstName, lastName);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
                lineup.add(playerID);
                if (!(request.getParameter("subsPlayers4").equals("Empty")) && (request.getParameter("subinning4") != null) 
                        && (!request.getParameter("subinning4").equals(""))) {
                    stringTok = new StringTokenizer(request.getParameter("subsPlayers4"), " ");
                    subFirstName = stringTok.nextToken();
                    subLastName = stringTok.nextToken();
                    try {
                        subPlayerID = roster.getPlayerID(subFirstName, subLastName);
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                    inning = Integer.parseInt(request.getParameter("subinning4"));
                    subs.add(new Sub(inning, firstName, lastName, subFirstName, subLastName, subPlayerID));
                }
            }
            if (!(request.getParameter("lineupPlayers5").equals("Empty"))) {
                stringTok = new StringTokenizer(request.getParameter("lineupPlayers5"), " ");
                firstName = stringTok.nextToken();
                lastName = stringTok.nextToken();
                try {
                    playerID = roster.getPlayerID(firstName, lastName);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
                lineup.add(playerID);
                if (!(request.getParameter("subsPlayers5").equals("Empty")) && (request.getParameter("subinning5") != null) 
                        && (!request.getParameter("subinning5").equals(""))) {
                    stringTok = new StringTokenizer(request.getParameter("subsPlayers5"), " ");
                    subFirstName = stringTok.nextToken();
                    subLastName = stringTok.nextToken();
                    try {
                        subPlayerID = roster.getPlayerID(subFirstName, subLastName);
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                    inning = Integer.parseInt(request.getParameter("subinning5"));
                    subs.add(new Sub(inning, firstName, lastName, subFirstName, subLastName, subPlayerID));
                }
            }
            if (!(request.getParameter("lineupPlayers6").equals("Empty"))) {
                stringTok = new StringTokenizer(request.getParameter("lineupPlayers6"), " ");
                firstName = stringTok.nextToken();
                lastName = stringTok.nextToken();
                try {
                    playerID = roster.getPlayerID(firstName, lastName);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
                lineup.add(playerID);
                if (!(request.getParameter("subsPlayers6").equals("Empty")) && (request.getParameter("subinning6") != null) 
                        && (!request.getParameter("subinning6").equals(""))) {
                    stringTok = new StringTokenizer(request.getParameter("subsPlayers6"), " ");
                    subFirstName = stringTok.nextToken();
                    subLastName = stringTok.nextToken();
                    try {
                        subPlayerID = roster.getPlayerID(subFirstName, subLastName);
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                    inning = Integer.parseInt(request.getParameter("subinning6"));
                    subs.add(new Sub(inning, firstName, lastName, subFirstName, subLastName, subPlayerID));
                }
            }
            if (!(request.getParameter("lineupPlayers7").equals("Empty"))) {
                stringTok = new StringTokenizer(request.getParameter("lineupPlayers7"), " ");
                firstName = stringTok.nextToken();
                lastName = stringTok.nextToken();
                try {
                    playerID = roster.getPlayerID(firstName, lastName);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
                lineup.add(playerID);
                if (!(request.getParameter("subsPlayers7").equals("Empty")) && (request.getParameter("subinning7") != null) 
                        && (!request.getParameter("subinning7").equals(""))) {
                    stringTok = new StringTokenizer(request.getParameter("subsPlayers7"), " ");
                    subFirstName = stringTok.nextToken();
                    subLastName = stringTok.nextToken();
                    try {
                        subPlayerID = roster.getPlayerID(subFirstName, subLastName);
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                    inning = Integer.parseInt(request.getParameter("subinning7"));
                    subs.add(new Sub(inning, firstName, lastName, subFirstName, subLastName, subPlayerID));
                }
            }
            if (!(request.getParameter("lineupPlayers8").equals("Empty"))) {
                stringTok = new StringTokenizer(request.getParameter("lineupPlayers8"), " ");
                firstName = stringTok.nextToken();
                lastName = stringTok.nextToken();
                try {
                    playerID = roster.getPlayerID(firstName, lastName);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
                lineup.add(playerID);
                if (!(request.getParameter("subsPlayers8").equals("Empty")) && (request.getParameter("subinning8") != null) 
                        && (!request.getParameter("subinning8").equals(""))) {
                    stringTok = new StringTokenizer(request.getParameter("subsPlayers8"), " ");
                    subFirstName = stringTok.nextToken();
                    subLastName = stringTok.nextToken();
                    try {
                        subPlayerID = roster.getPlayerID(subFirstName, subLastName);
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                    inning = Integer.parseInt(request.getParameter("subinning8"));
                    subs.add(new Sub(inning, firstName, lastName, subFirstName, subLastName, subPlayerID));
                }
            }
            if (!(request.getParameter("lineupPlayers9").equals("Empty"))) {
                stringTok = new StringTokenizer(request.getParameter("lineupPlayers9"), " ");
                firstName = stringTok.nextToken();
                lastName = stringTok.nextToken();
                try {
                    playerID = roster.getPlayerID(firstName, lastName);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
                lineup.add(playerID);
                if (!(request.getParameter("subsPlayers9").equals("Empty")) && (request.getParameter("subinning9") != null) 
                        && (!request.getParameter("subinning9").equals(""))) {
                    stringTok = new StringTokenizer(request.getParameter("subsPlayers9"), " ");
                    subFirstName = stringTok.nextToken();
                    subLastName = stringTok.nextToken();
                    try {
                        subPlayerID = roster.getPlayerID(subFirstName, subLastName);
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                    inning = Integer.parseInt(request.getParameter("subinning9"));
                    subs.add(new Sub(inning, firstName, lastName, subFirstName, subLastName, subPlayerID));
                }
            }
            if (!(request.getParameter("lineupPlayers10").equals("Empty"))) {
                stringTok = new StringTokenizer(request.getParameter("lineupPlayers10"), " ");
                firstName = stringTok.nextToken();
                lastName = stringTok.nextToken();
                try {
                    playerID = roster.getPlayerID(firstName, lastName);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
                lineup.add(playerID);
                if (!(request.getParameter("subsPlayers10").equals("Empty")) && (request.getParameter("subinning10") != null) 
                        && (!request.getParameter("subinning10").equals(""))) {
                    stringTok = new StringTokenizer(request.getParameter("subsPlayers10"), " ");
                    subFirstName = stringTok.nextToken();
                    subLastName = stringTok.nextToken();
                    try {
                        subPlayerID = roster.getPlayerID(subFirstName, subLastName);
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                    inning = Integer.parseInt(request.getParameter("subinning10"));
                    subs.add(new Sub(inning, firstName, lastName, subFirstName, subLastName, subPlayerID));
                }
            }
            if (!(request.getParameter("lineupPlayers11").equals("Empty"))) {
                stringTok = new StringTokenizer(request.getParameter("lineupPlayers11"), " ");
                firstName = stringTok.nextToken();
                lastName = stringTok.nextToken();
                try {
                    playerID = roster.getPlayerID(firstName, lastName);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
                lineup.add(playerID);
                if (!(request.getParameter("subsPlayers11").equals("Empty")) && (request.getParameter("subinning11") != null) 
                        && (!request.getParameter("subinning11").equals(""))) {
                    stringTok = new StringTokenizer(request.getParameter("subsPlayers11"), " ");
                    subFirstName = stringTok.nextToken();
                    subLastName = stringTok.nextToken();
                    try {
                        subPlayerID = roster.getPlayerID(subFirstName, subLastName);
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                    inning = Integer.parseInt(request.getParameter("subinning11"));
                    subs.add(new Sub(inning, firstName, lastName, subFirstName, subLastName, subPlayerID));
                }
            }
            if (!(request.getParameter("lineupPlayers12").equals("Empty"))) {
                stringTok = new StringTokenizer(request.getParameter("lineupPlayers12"), " ");
                firstName = stringTok.nextToken();
                lastName = stringTok.nextToken();
                try {
                    playerID = roster.getPlayerID(firstName, lastName);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
                lineup.add(playerID);
                if (!(request.getParameter("subsPlayers12").equals("Empty")) && (request.getParameter("subinning12") != null) 
                        && (!request.getParameter("subinning12").equals(""))) {
                    stringTok = new StringTokenizer(request.getParameter("subsPlayers12"), " ");
                    subFirstName = stringTok.nextToken();
                    subLastName = stringTok.nextToken();
                    try {
                        subPlayerID = roster.getPlayerID(subFirstName, subLastName);
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                    inning = Integer.parseInt(request.getParameter("subinning12"));
                    subs.add(new Sub(inning, firstName, lastName, subFirstName, subLastName, subPlayerID));
                }
            }
            if (!(request.getParameter("lineupPlayers13").equals("Empty"))) {
                stringTok = new StringTokenizer(request.getParameter("lineupPlayers13"), " ");
                firstName = stringTok.nextToken();
                lastName = stringTok.nextToken();
                try {
                    playerID = roster.getPlayerID(firstName, lastName);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
                lineup.add(playerID);
                if (!(request.getParameter("subsPlayers13").equals("Empty")) && (request.getParameter("subinning13") != null) 
                        && (!request.getParameter("subinning13").equals(""))) {
                    stringTok = new StringTokenizer(request.getParameter("subsPlayers13"), " ");
                    subFirstName = stringTok.nextToken();
                    subLastName = stringTok.nextToken();
                    try {
                        subPlayerID = roster.getPlayerID(subFirstName, subLastName);
                    }
                    catch (SQLException e) {
                        e.printStackTrace();
                    }
                    inning = Integer.parseInt(request.getParameter("subinning13"));
                    subs.add(new Sub(inning, firstName, lastName, subFirstName, subLastName, subPlayerID));
                }
            }
            
            try {
                Parser parser = new Parser(request.getParameter("score"), "game"
                        + request.getParameter("gameid") + ".txt", lineup, subs);
                parser.parseScore();
                message = "Stats entered successfully";
                response.sendRedirect("servletresult.jsp?msg="+message);
            }
            catch (IOException e) {
                response.sendRedirect("servletresult.jsp?msg="+message);
                e.printStackTrace();
            }
            catch (SQLException e) {
                response.sendRedirect("servletresult.jsp?msg="+message);
                e.printStackTrace();
            }
            catch (InstantiationException e) {
                response.sendRedirect("servletresult.jsp?msg="+message);
                e.printStackTrace();
            }
            catch (IllegalAccessException e) {
                response.sendRedirect("servletresult.jsp?msg="+message);
                e.printStackTrace();
            }
            catch (ClassNotFoundException e) {
                response.sendRedirect("servletresult.jsp?msg="+message);
                e.printStackTrace();
            }
            finally {
                try {
                    gameStats.close();
                    roster.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (request.getParameter("delete") != null) {
            try {
                gameLog.deleteGame(Integer.parseInt(request.getParameter("gameid")));
                message = "Stats deleted successfully."; 
                response.sendRedirect("servletresult.jsp?msg="+message);
            }
            catch (NumberFormatException e) {
                response.sendRedirect("servletresult.jsp?msg="+message);
                e.printStackTrace();
            }
            catch (SQLException e) {
                response.sendRedirect("servletresult.jsp?msg="+message);
                e.printStackTrace();
            }
            finally {           
                try {
                    gameStats.close();
                    roster.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
       
    }
    
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
    
    private GameLog gameLog;
    private Roster roster;
    private GameStats gameStats; 
    private Connection conn;
    private static final long serialVersionUID = 1L;
    private ArrayList<Integer> lineup;
    private ArrayList<Sub> subs;

}
