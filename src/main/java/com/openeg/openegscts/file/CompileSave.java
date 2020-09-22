package com.openeg.openegscts.file;

import com.openeg.openegscts.student.dto.CompareDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.*;

@Slf4j
@Component
public class CompileSave {

    Environment env;

    @Autowired
    public CompileSave(Environment env) {
        this.env = env;
    }

    public int compileSave(CompareDto compareDto, String secFileName) {

        String getUserId = compareDto.getUserId();
        String COMM_FILE_PATH = env.getProperty("compile.path") + getUserId;
        int idx = secFileName.lastIndexOf("-");
        String fileName = COMM_FILE_PATH + "/" + secFileName.substring(idx+1);
        File folder = new File(COMM_FILE_PATH);

        if(!folder.exists()) {
            try {
                folder.mkdir();

                FileWriter fileWriter = new FileWriter(fileName);
                fileWriter.write(compareDto.getCompileCode());

                fileWriter.close();

                return 1;
            } catch(Exception e) {
                e.getStackTrace();
                return 0;
            }
        } else {
            log.info("이미 있는 폴더");
            return 0;
        }
    }
}