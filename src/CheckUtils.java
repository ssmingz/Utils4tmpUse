import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CheckUtils {
    public static void main(String[] args) {
        checkSootUsageByBatch();
    }

    static void checkFailTests() {
        // output directory

        String project = "D:\\EXPspace\\FaultLocalization\\EXP\\buggyProjects-original\\compress";
        File base = new File(project);
        for(File f : base.listFiles()) {
            if(!f.isDirectory()) {
                continue;
            }
            File properties = new File(f.getAbsolutePath() + "\\defects4j.build.properties");
            List<String> testPaths = new ArrayList<>();
            if(properties.exists()) {
                try {
                    FileReader reader = new FileReader(properties);
                    BufferedReader bReader = new BufferedReader(reader);
                    String tmp = "", testBase = "";
                    while((tmp = bReader.readLine()) != null) {
                        if(tmp.startsWith("d4j.dir.src.tests")) {
                            tmp = tmp.trim();
                            tmp = tmp.substring(tmp.indexOf("=")+1);
                            testBase = tmp;
                        }
                        if(tmp.startsWith("d4j.tests.trigger")) {
                            tmp = tmp.trim();
                            tmp = tmp.substring(tmp.indexOf("=")+1);
                            String[] tests = tmp.split(",");
                            for(String test : tests) {
                                String location =  test.substring(0,test.indexOf("::"));
                                location = f.getAbsolutePath() + "/" + testBase + "/" + location.replaceAll("\\.", "/");
                                File target = new File(location);
                                if(!target.exists()) {
                                    System.out.println("Not exist : " + location);
                                } else {

                                }
                            }
                        }
                    }
                    bReader.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
