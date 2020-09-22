package com.openeg.openegscts.file;

import com.openeg.openegscts.student.dto.SecurityCodeDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedWriter;
import java.io.*;

@Component
@Slf4j
public class FileSaveC {

    Environment env;

    @Autowired
    public FileSaveC(Environment env) {
        this.env = env;
    }

//    private static final String PDF_PATH = "http://59.6.55.253:10000/pdf/";
//    private static final String VIDEO_PATH = "http://59.6.55.253:10000/video/";

    public SecurityCodeDto setFileStore(SecurityCodeDto securityCodeDto, MultipartFile pdf, MultipartFile video) {

        String ipAddr = env.getProperty("ip.addr");
        String portNum = env.getProperty("server.port");
        String pdfDbPath = env.getProperty("save.pdf.db-path");
        String videoDbPath = env.getProperty("save.video.db-path");
        String PDF_PATH = ipAddr + ":" + portNum + pdfDbPath;
        String VIDEO_PATH = ipAddr + ":" + portNum + videoDbPath;

        String pdfFilename = "";
        String videoFilename = "";
        String pdfSaveName = "";
        String videoSaveName = "";

        try {
            pdfFilename = pdf.getOriginalFilename();
            videoFilename = video.getOriginalFilename();

            String pdfPath = env.getProperty("save.pdf.path");
            pdf.transferTo(new File(pdfPath + securityCodeDto.getScategoryId() + "-" + pdfFilename));
            pdfSaveName = PDF_PATH + securityCodeDto.getScategoryId() + "-" + pdfFilename;

            String videoPath = env.getProperty("save.video.path");
            video.transferTo(new File(videoPath + securityCodeDto.getScategoryId()+ "-" + videoFilename));
            videoSaveName = VIDEO_PATH + securityCodeDto.getScategoryId() + "-" + videoFilename;

        } catch (Exception e) {
            e.printStackTrace();
        }

        securityCodeDto.setSecPdf(pdfSaveName);
        securityCodeDto.setSecVideo(videoSaveName);

        return securityCodeDto;
    }

    public void writer(SecurityCodeDto securityCodeDto, BufferedWriter fw) {
        try {
            fw.write(securityCodeDto.getSecPdf());
            fw.write(securityCodeDto.getSecVideo());
            fw.flush();

            fw.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}