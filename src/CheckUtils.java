import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class CheckUtils {
    public static void main(String[] args) {
        //checkSootUsageByBatch();
        checkFailTests();
        /*
        Integer[] tmp = {1,3,6,12,17,21,22,26,28};
        Set<Integer> s = new HashSet<Integer>(Arrays.asList(tmp));
        for(int i=1; i<=38; i++) {
            if(!s.contains(i)) {
                System.out.print(i+",");
            }
        }
        */
    }

    static void checkFailTests() {
        // output directory
        String output_base = "/Users/yumeng/Workspace/FaultLocalization/EXP/buggyProjects-original/output/jsoup";

        String project = "/Users/yumeng/Workspace/FaultLocalization/EXP/buggyProjects-original/jsoup";
        File base = new File(project);
        for(File f : base.listFiles()) {
            if(!f.isDirectory()) {
                continue;
            }
            File properties = new File(f.getAbsolutePath() + "/defects4j.build.properties");
            String bid = f.getName();
            bid = bid.substring(bid.indexOf("_")+1, bid.lastIndexOf("_"));
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
                                String testName = test.substring(test.indexOf("::")+2);
                                location = f.getAbsolutePath() + "/" + testBase + "/" + location.replaceAll("\\.", "/") + ".java";
                                File target = new File(location);
                                if(!target.exists()) {
                                    System.out.println("Not exist : " + location);
                                } else {
                                    File output_dir = new File(output_base + "/" + bid);
                                    output_dir.mkdir();
                                    File output_file = new File(output_dir.getAbsolutePath() + "/" + testName + ".java");
                                    Files.copy(target.toPath(), output_file.toPath());
                                    System.out.println("copy " + location + " to " + output_file.getAbsolutePath());
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
