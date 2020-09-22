package com.openeg.openegscts.file;

import com.openeg.openegscts.trainer.dto.SubmitCodeDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@Slf4j
public class FileSave {

    Environment env;
    private static final String language1 = "java";
    private static final String language2 = "c";
    private static final String language3 = "cpp";
    private static final String language4 = "py";

    private String vulFilename = "";
    private String secFilename = "";
    private String vulSaveName = "";
    private String secSaveName = "";

    @Autowired
    public FileSave(Environment env) {
        this.env = env;
    }

    public SubmitCodeDto setFileStore(SubmitCodeDto submitCodeDto, MultipartFile scodeVulFile, MultipartFile scodeSecFile, String email, HttpServletResponse response) {

        String timeStamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());

        try {

            vulFilename = scodeVulFile.getOriginalFilename();
            secFilename = scodeSecFile.getOriginalFilename();

            int idx = vulFilename.indexOf(".");
            int idx2 = email.indexOf("@");
            String idxResult = vulFilename.substring(idx+1);
            String idxResult2 = email.substring(0, idx2);

            if(idxResult.equals(language1)) {
                String codePath = env.getProperty("save.code.path") + language1 + "/" + submitCodeDto.getSecId() + "/";
                fileSaveFunc(codePath, submitCodeDto, scodeVulFile, scodeSecFile, idxResult2, timeStamp);
            } else if(idxResult.equals(language2)) {
                String codePath = env.getProperty("save.code.path") + language2 + "/" + submitCodeDto.getSecId() + "/";
                fileSaveFunc(codePath, submitCodeDto, scodeVulFile, scodeSecFile, idxResult2, timeStamp);
            } else if(idxResult.equals(language3)) {
                String codePath = env.getProperty("save.code.path") + language3 + "/" + submitCodeDto.getSecId() + "/";
                fileSaveFunc(codePath, submitCodeDto, scodeVulFile, scodeSecFile, idxResult2, timeStamp);
            } else {
                log.info("add new language");
                response.sendError(401, "not allowed file");
                submitCodeDto.setIsOK(0);
            }
        } catch (Exception e) {
        }

        submitCodeDto.setScodeVulFile(vulSaveName);
        submitCodeDto.setScodeSecFile(secSaveName);

        return submitCodeDto;
    }

    public void writer(SubmitCodeDto submitCodeDto, BufferedWriter fw) {
        try {
            fw.write(submitCodeDto.getScodeVulFile());
            fw.write(submitCodeDto.getScodeSecFile());
            fw.flush();

            fw.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void fileSaveFunc(String codePath, SubmitCodeDto submitCodeDto, MultipartFile scodeVulFile, MultipartFile scodeSecFile, String email, String timeStamp) throws IOException {
        try {
            scodeVulFile.transferTo(new File(codePath + email + "-" + timeStamp + "-" + submitCodeDto.getSecId() + "-" + (submitCodeDto.getScodeNum()+1) + "-" + vulFilename));
            scodeSecFile.transferTo(new File(codePath + email + "-" +timeStamp + "-" + submitCodeDto.getSecId() + "-" + (submitCodeDto.getScodeNum()+1) + "-" + secFilename));

            vulSaveName = email + "-" + timeStamp + "-" + submitCodeDto.getSecId() + "-" + (submitCodeDto.getScodeNum()+1) + "-" + vulFilename;
            secSaveName = email + "-" + timeStamp + "-" + submitCodeDto.getSecId() + "-" + (submitCodeDto.getScodeNum()+1) + "-" + secFilename;
        } catch (IOException e) {
            e.getMessage();
        }
    }
}