package commonUtil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class StreamUtil {

    public ArrayList<String> streamToArrayOfCommands(BufferedInputStream input) throws IOException {
        ArrayList<String> commands = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        String line = br.readLine();
        while (line != null) {
            commands.add(line);
            line = br.readLine();
        }
        br.close();
        return commands;
    }


    public String streamToString(BufferedInputStream inputStream) throws IOException {
        byte[] contents = new byte[1024];
        int bytesRead;
        StringBuilder strFileContents = new StringBuilder();
        while((bytesRead = inputStream.read(contents)) != -1) {
            strFileContents.append(new String(contents, 0, bytesRead));
        }
        return strFileContents.toString();
    }
}
