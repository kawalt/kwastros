package servlets;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This is a servlet class which allows game information to be sent from the HTML form to the database.
 * @author Kyle
 *
 */
public class GameEnterServlet extends HttpServlet
{
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String message = "An error occured";
        try {
            conn = DBConnection.getConnection();
            gameStats = new GameStats(conn);
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
            int[] awayRuns = new int[9];
            int[] homeRuns = new int[9];
            for (int i = 0; i < 9; i++) {
                awayRuns[i] = -1;                
                homeRuns[i] = -1;
            }
            try {
                gameStats.enterGame(Integer.parseInt(request.getParameter("gameid")),
                        Integer.parseInt(request.getParameter("tournamentid")), 
                        request.getParameter("hometeam"), 
                        request.getParameter("awayteam"), 
                        request.getParameter("date"), 
                        awayRuns, 
                        homeRuns, 
                        Integer.parseInt(request.getParameter("awayruns")), 
                        Integer.parseInt(request.getParameter("homeruns")), 
                        -1, -1);
                message = "Game entered successfully";
                response.sendRedirect("servletresult.jsp?msg="+message);
            }
            catch (NumberFormatException e) {
                e.printStackTrace();
                message = "Game enter failed. NumberFormatException was thrown.";
                response.sendRedirect("servletresult.jsp?msg="+message);
            }
            catch (SQLException e) {
                e.printStackTrace();
                message = "Game enter failed. NumberFormatException was thrown.";
                response.sendRedirect("servletresult.jsp?msg="+message);
            }
            finally {
                response.sendRedirect("servletresult.jsp?msg="+message);
                try {
                    gameStats.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (request.getParameter("delete") != null) {
            try {
                gameStats.deleteGame(Integer.parseInt(request.getParameter("gameid")));
                message = "Game deleted successfully.";
            }
            catch (NumberFormatException e) {
                e.printStackTrace();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
            finally {
                response.sendRedirect("servletresult.jsp?msg="+message);
                try {
                    gameStats.close();
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
    
    private GameStats gameStats; 
    private Connection conn;
    private static final long serialVersionUID = 1L;

}
