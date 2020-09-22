package com.openeg.openegscts.solution;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Data
public class CompileThread implements Runnable {

    int returnValue;
    String fileName = "";
    String userId = "";
    Environment env;
    StreamGobbler gb1;
    StreamGobbler gb2;
    String getPath = "";

    @Autowired
    public CompileThread(Environment env, StreamGobbler gb1, StreamGobbler gb2) {
        this.env = env;
        this.gb1 = gb1;
        this.gb2 = gb2;
    }

    public CompileThread(String getPath, String fileName, String userId) {
        this.getPath = getPath;
        this.fileName = fileName;
        this.userId = userId;
    }

    @Override
    public void run() {

        Process process = null;
        Runtime runtime = Runtime.getRuntime();
        StringBuffer errorOutput = new StringBuffer();
        String resultMessage = "";
        List<String> cmdList = new ArrayList<String>();

        if (System.getProperty("os.name").indexOf("Windows") > -1) {
            cmdList.add("cmd");
            cmdList.add("/c");
        } else {
            cmdList.add("/bin/sh");
            cmdList.add("-c");
        }

        int extensionIdx = fileName.indexOf(".");
        int fileNameIdx = fileName.lastIndexOf("-");
        String idxResult1 = fileName.substring(fileNameIdx+1);
        String language = fileName.substring(extensionIdx+1);
        String tmpCmd = "";

        if (language.equals("java")) {
            tmpCmd= "javac " + getPath + userId + "/" + idxResult1 + "&&cd ..";
        } else if (language.equals("c")) {
            tmpCmd= "gcc -o openeg " + getPath + userId + "/" + idxResult1;
        } else if (language.equals("cpp")){
            tmpCmd= "g++ -o openeg " + getPath + userId + "/" + idxResult1;
        } else {
            log.info("please, add language");
            tmpCmd= "..";
        }
        cmdList.add(tmpCmd);

        String[] array = cmdList.toArray(new String[cmdList.size()]);

        try {

            process = runtime.exec(array);

            gb1 = new StreamGobbler(process.getInputStream());
            gb2 = new StreamGobbler(process.getErrorStream());
            gb1.start();
            gb2.start();

            process.waitFor();

            if (process.exitValue() == 0) {
                returnValue = 1;
                deleteFolder(getPath, userId);
            } else {
                resultMessage += errorOutput.toString();
                returnValue = 2;
                deleteFolder(getPath, userId);
            }

        } catch (IOException e) {
            log.info(e.getMessage());
        } catch (InterruptedException e) {
            log.info(e.getMessage());
        } finally {
            process.destroy();
        }
    }

    public static void deleteFolder(String COMM_FILE_PATH, String userId) {
        String folderPath = COMM_FILE_PATH + userId;
        File folder = new File(folderPath);

        try {
            while(folder.exists()) {
                File[] getFileInFolder = folder.listFiles();

                for(int i = 0; i < getFileInFolder.length; i++) {
                    getFileInFolder[i].delete();
                }

                if(getFileInFolder.length == 0 && folder.isDirectory()) {
                    folder.delete();
                }
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }
}