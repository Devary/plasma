package projects;

import files.AbstractFile;
import org.xml.sax.InputSource;

import java.io.*;

public class ProjectFile extends AbstractFile {
    public String getFileContent()
    {
        if (getPath() != null && getName()!= null && getExtension() !=null)
            return readContent();
        return null;
    }
    private String readContent()
    {
        StringBuilder contentBuilder = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(this.getPath())))
        {

            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null)
            {
                contentBuilder.append(sCurrentLine).append("\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return contentBuilder.toString();
    }
}
