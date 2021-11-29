import java.io.*;

public class CheckUtils {
    public static void main(String[] args) {
        checkSootUsageByBatch();
    }

    static void checkSootUsageByBatch() {
        String dirPath = "D:\\EXPspace\\FaultLocalization\\EXP\\stdout\\math-old";
        File dir = new File(dirPath);
        if(dir.isDirectory()) {
            for(File f : dir.listFiles()) {
                checkSootUsageByFile(f);
            }
        }
    }

    static void checkSootUsageByFile(File log) {
        if(!log.exists()) {
            System.out.println("log file doesn't exist : " + log.getAbsolutePath());
            return;
        }
        try {
            FileReader reader = new FileReader(log);
            BufferedReader bReader = new BufferedReader(reader);
            String tmp = "";
            String sootUsage = "";
            boolean startFlag = false;
            while((tmp = bReader.readLine()) != null) {
                if(tmp.startsWith("Soot started on")) {
                    startFlag = true;
                    continue;
                }
                if(tmp.startsWith("Soot finished on")) {
                    break;
                }
                if(startFlag) {
                    sootUsage += tmp;
                }
            }
            if(sootUsage.length() != 0) {
                System.out.println(log.getAbsolutePath() + " use soot");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
