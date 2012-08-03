package scoring;
import java.io.FileWriter;
import java.io.IOException;

public class StringWriter
{
    public StringWriter(String outputFile) throws IOException
    {
        writer = new FileWriter(outputFile);
    }

    public void write(String output)
    {
        try {
            writer.write(output);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void flush() 
    {
        try {
            writer.flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void close()
    {
        try {
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private FileWriter writer;

}
