package scoring;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class StringReader
{
    public StringReader() throws FileNotFoundException
    {
        score = "";
        line = "";
    }
    
    public String readScore(String fileName) throws IOException
    {
        reader = new FileReader(fileName);
        bReader = new BufferedReader(reader);
        while(true)
        {
            line = bReader.readLine();   
            if (line == null) {
                break;
            }
            else {
                score = score + line;
            }
        }
        return score;
    }
    
    public ArrayList<String> readLineup(String fileName) throws IOException
    {
        reader = new FileReader(fileName);
        bReader = new BufferedReader(reader);
        ArrayList<String> lineup = new ArrayList<String>();
        while(true)
        {
            line = bReader.readLine();   
            if (line == null) {
                break;
            }
            else {
                lineup.add(line);
            }
        }
        return lineup;
    }
    
    private FileReader reader;
    private BufferedReader bReader;
    private String score;
    private String line;
}
