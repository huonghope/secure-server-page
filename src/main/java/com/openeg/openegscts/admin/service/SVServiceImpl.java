package com.openeg.openegscts.admin.service;

import com.openeg.openegscts.admin.dto.SecurityCategoryDto;
import com.openeg.openegscts.student.dto.SecurityCodeDto;
import com.openeg.openegscts.admin.entity.SecurityCategory;
import com.openeg.openegscts.student.entity.Language;
import com.openeg.openegscts.student.entity.SecurityCode;
import com.openeg.openegscts.admin.repository.ISecurityVulnerabilityMapper;
import com.openeg.openegscts.student.repository.ILanguageMapper;
import com.openeg.openegscts.student.service.ICodeInfoService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class SVServiceImpl implements ISVService {

    ISecurityVulnerabilityMapper iSecurityVulnerabilityMapper;
    ILanguageMapper iLanguageMapper;
    ICodeInfoService iCodeInfoService;
    ModelMapper modelMapper = new ModelMapper();
    Environment env;

    @Autowired
    public SVServiceImpl(ISecurityVulnerabilityMapper iSecurityVulnerabilityMapper, ILanguageMapper iLanguageMapper, ICodeInfoService iCodeInfoService, Environment env) {
        this.iSecurityVulnerabilityMapper = iSecurityVulnerabilityMapper;
        this.iLanguageMapper = iLanguageMapper;
        this.iCodeInfoService = iCodeInfoService;
        this.env = env;
    }

    @Override
    public SecurityCodeDto insertSV(SecurityCodeDto securityCodeDto) {

        String languagePath = env.getProperty("save.code.path");
        List<Language> getLanguage = iLanguageMapper.getLanList();
        String path = "";

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        SecurityCode svEntity = modelMapper.map(securityCodeDto, SecurityCode.class);
        iSecurityVulnerabilityMapper.insertSV(svEntity);

        for(int i = 0; i < getLanguage.size(); i++) {
            path = languagePath + getLanguage.get(i).getLanguageType() + "/" + svEntity.getSecId();
            File folder = new File(path);

            if (!folder.exists()) {
                try {
                    folder.mkdir();
                } catch (Exception e) {
                    e.getStackTrace();
                }
            }
        }

        SecurityCodeDto returnValue = modelMapper.map(svEntity, SecurityCodeDto.class);

        return returnValue;
    }

    @Override
    public SecurityCategoryDto insertSC(SecurityCategoryDto securityCategoryDto) {

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        SecurityCategory scEntity = modelMapper.map(securityCategoryDto, SecurityCategory.class);
        iSecurityVulnerabilityMapper.insertSC(scEntity);

        SecurityCategoryDto returnValue = modelMapper.map(scEntity, SecurityCategoryDto.class);

        return returnValue;
    }

    @Override
    public List<SecurityCategory> getAllSecurityCategoryInfo() {

        List<SecurityCategory> scEntity = iSecurityVulnerabilityMapper.getAllSecurityCategoryInfo();

        if(scEntity == null) {
            log.info(String.format("not exists security main category list"));
            return new ArrayList<>();
        }

        return scEntity;
    }

    @Override
    public List<SecurityCode> getAllSV() {
        List<SecurityCode> secEntity = iSecurityVulnerabilityMapper.getAllSV();

        if(secEntity == null) {
            log.info(String.format("not exists security vulnerability list"));
            return new ArrayList<>();
        }

        return secEntity;
    }

    @Override
    public int deleteSV(long secId) {

        SecurityCode getSV = iSecurityVulnerabilityMapper.getSV(secId);
        int idx1 = getSV.getSecPdf().lastIndexOf("/");
        int idx2 = getSV.getSecVideo().lastIndexOf("/");
        String pdfName = getSV.getSecPdf().substring(idx1+1);
        String videoName = getSV.getSecVideo().substring(idx2+1);
        String pdfPath = env.getProperty("save.pdf.path") + pdfName;
        String videoPath = env.getProperty("save.video.path") + videoName;
        File deletePdf = new File(pdfPath);
        File deleteVideo = new File(videoPath);
        int deleteResult = 0;

        try {
            if(deletePdf.exists() && deleteVideo.exists()) {
                if(deletePdf.delete() && deleteVideo.delete()) {
                    deleteResult = iSecurityVulnerabilityMapper.deleteSV(secId);
                } else {
                    return deleteResult;
                }
            }
        } catch(Exception e) {
            e.getStackTrace();
        }

        return deleteResult;
    }

    @Override
    public int confirmSecName(String secName) {
        int confirmSecName = iSecurityVulnerabilityMapper.confirmSecName(secName);
        return confirmSecName;
    }

    @Override
    public int deleteSCategory(long scategoryId) {
        int deleteResult = iSecurityVulnerabilityMapper.deleteSCategory(scategoryId);
        return deleteResult;
    }
}
