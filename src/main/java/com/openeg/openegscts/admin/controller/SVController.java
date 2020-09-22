package com.openeg.openegscts.admin.controller;

import com.openeg.openegscts.admin.dto.SecurityCategoryDto;
import com.openeg.openegscts.student.dto.SecurityCodeDto;
import com.openeg.openegscts.admin.entity.SecurityCategory;
import com.openeg.openegscts.student.entity.SecurityCode;
import com.openeg.openegscts.admin.service.ISVService;
import com.openeg.openegscts.file.FileSaveC;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin")
public class SVController {

    ISVService isvService;
    FileSaveC fileSave;
    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public SVController(ISVService isvService, FileSaveC fileSave) {
        this.isvService = isvService;
        this.fileSave = fileSave;
    }

    @PostMapping("/sv/add")
    public ResponseEntity<SecurityCodeDto> insertSV(@ModelAttribute SecurityCode securityCode, @RequestParam(value="pdf") MultipartFile pdf,
                                                          @RequestParam(value="video") MultipartFile video) {

        int confirmSecName = isvService.confirmSecName(securityCode.getSecName());
        SecurityCodeDto createDto = null;

        if (confirmSecName == 0) {
            SecurityCodeDto scDto = modelMapper.map(securityCode, SecurityCodeDto.class);
            SecurityCodeDto fileSaveReturn = fileSave.setFileStore(scDto, pdf, video);

            createDto = isvService.insertSV(fileSaveReturn);
        } else {
            log.info(String.format("exists security vulnerability name : %s", securityCode.getSecName()));
            return null;

        }

        return ResponseEntity.status(HttpStatus.CREATED).body(createDto);
    }

    @PostMapping("/category")
    public @ResponseBody
    SecurityCategoryDto insertSC(@ModelAttribute SecurityCategory securityCategory) {

        SecurityCategoryDto scDto = modelMapper.map(securityCategory, SecurityCategoryDto.class);
        SecurityCategoryDto createDto = isvService.insertSC(scDto);

        return createDto;
    }

    @GetMapping("/category")
    public @ResponseBody
    List<SecurityCategory> getSCList() {

        List<SecurityCategory> returnValue = new ArrayList<>();
        List<SecurityCategory> scList = isvService.getAllSecurityCategoryInfo();

        if(scList == null || scList.isEmpty())
        {
            return returnValue;
        }

        Type listType = new TypeToken<List<SecurityCategory>>(){}.getType();

        returnValue = new ModelMapper().map(scList, listType);
        log.info("Returning " + returnValue.size() + " category main list");
        return returnValue;
    }

    @GetMapping("/sv")
    public @ResponseBody
    List<SecurityCode> getAllSV() {

        List<SecurityCode> returnValue = new ArrayList<>();
        List<SecurityCode> secList = isvService.getAllSV();

        if(secList == null || secList.isEmpty())
        {
            return returnValue;
        }

        Type listType = new TypeToken<List<SecurityCode>>(){}.getType();

        returnValue = new ModelMapper().map(secList, listType);
        log.info("Returning " + returnValue.size() + " security vulnerability list");
        return returnValue;
    }

    @DeleteMapping("/category/{scategoryId}")
    public int deleteSCategory(@PathVariable long scategoryId) {
        int deleteResult = isvService.deleteSCategory(scategoryId);
        return deleteResult;
    }

    @DeleteMapping("/sv/{secId}")
    public int deleteSV(@PathVariable long secId) {
        int deleteResult = isvService.deleteSV(secId);
        return deleteResult;
    }
}
