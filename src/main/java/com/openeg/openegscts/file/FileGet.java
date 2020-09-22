package com.openeg.openegscts.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;

@Slf4j
@Component
public class FileGet {

    public String getFile(String reviewCodePath, String languageType) {

        String code = "";

        try {

            FileInputStream input = new FileInputStream(reviewCodePath);
            InputStreamReader reader;

            if(languageType.equals("cpp")) {
                reader = new InputStreamReader(input,"euc-kr");
            } else {
                reader = new InputStreamReader(input,"utf-8");
            }
            BufferedReader in = new BufferedReader(reader);

            int ch;

            while((ch=in.read())!=-1){

                code+=(char)ch;
            }

            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return code;
    }
}