package servlets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection
{
    public static Connection getConnection() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException
    {
        String url = "jdbc:mysql://localhost:3306/";
        String dbName = "batsstats2009";
        String driver = "com.mysql.jdbc.Driver";
        String userName = "root";
        String password = "griffey";
        Class.forName(driver).newInstance();
        return DriverManager.getConnection(url + dbName, userName, password);
    } 
}
