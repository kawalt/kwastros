package scoring;

import java.io.IOException;
import java.sql.SQLException;

import uiapps.StatsEntererFrame;

public class Start
{

    /**
     * This class initializes the program and creates either a UserInterface
     * object or a StringReader Object, which are equivalent means of reading in
     * a string.
     * 
     * @param args
     *            command line arguments
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws SQLException
     */
    public static void main(String[] args) throws IOException, SQLException,
            InstantiationException, IllegalAccessException,
            ClassNotFoundException
    {
        StatsEntererFrame frame = new StatsEntererFrame();
        frame.showGUI();
    }
}
