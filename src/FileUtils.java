import java.io.*;

public class FileUtils {
    private static String _name = "@FileUtils ";
    /**
     * return string from file
     * @param file
     * @return
     */
    public static String readFileToString(File file) {
        String result = "";
        try {
            FileReader reader = new FileReader(file);
            BufferedReader bReader = new BufferedReader(reader);
            StringBuilder builder = new StringBuilder();
            String tmp = "";
            while((tmp = bReader.readLine()) != null) {
                builder.append(tmp + '\n');
            }
            result = builder.toString();
            bReader.close();
        } catch (FileNotFoundException e) {
            System.out.println(_name + "#readFileToString File not found : " + file.getAbsolutePath());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println(_name + "#readFileToString IO exception : " + file.getAbsolutePath());
            e.printStackTrace();
        }
        return result;
    }
}
