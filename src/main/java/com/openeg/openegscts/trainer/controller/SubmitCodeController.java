package com.openeg.openegscts.trainer.controller;

import com.openeg.openegscts.student.entity.Users;
import com.openeg.openegscts.file.FileSave;
import com.openeg.openegscts.trainer.dto.SubmitCodeDto;
import com.openeg.openegscts.trainer.entity.SubmitCode;
import com.openeg.openegscts.trainer.model.CreateSubmitCodeModel;
import com.openeg.openegscts.trainer.model.GetSVnameListModel;
import com.openeg.openegscts.trainer.model.GetSubmitCodeListModel;
import com.openeg.openegscts.trainer.model.GetSubmitCodeModel;
import com.openeg.openegscts.trainer.service.ISubmitCodeService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/trainer/sb")
public class SubmitCodeController {

    ISubmitCodeService iSubmitCodeService;
    ModelMapper modelMapper = new ModelMapper();
    FileSave fileSave;

    @Autowired
    public SubmitCodeController(ISubmitCodeService iSubmitCodeService, FileSave fileSave) {
        this.iSubmitCodeService = iSubmitCodeService;
        this.fileSave = fileSave;
    }

    @PostMapping
    public ResponseEntity<CreateSubmitCodeModel> submitCode(@ModelAttribute SubmitCode submitCode, @RequestParam(value="vulFile") MultipartFile vulFile,
                                                            @RequestParam(value="secFile") MultipartFile secFile, HttpServletResponse response) {

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        SubmitCodeDto submitCodeDto = modelMapper.map(submitCode, SubmitCodeDto.class);
        submitCodeDto.setIsOK(1);
        Users email = iSubmitCodeService.getUserEmail(submitCode.getUserId());
        SubmitCode lastData = iSubmitCodeService.getLastData();
        submitCodeDto.setScodeNum(lastData.getScodeNum());
        SubmitCodeDto fileSaveReturn = fileSave.setFileStore(submitCodeDto, vulFile, secFile, email.getEmail(), response);

        if(fileSaveReturn.getIsOK() == 0) {
            CreateSubmitCodeModel createSubmitCodeModel = null;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(createSubmitCodeModel);
        }

        SubmitCodeDto createDto = iSubmitCodeService.insertSubmitCode(fileSaveReturn);
        CreateSubmitCodeModel returnValue = modelMapper.map(createDto, CreateSubmitCodeModel.class);

        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    }

    @PostMapping("/sv")
    public ResponseEntity getSVnameList(@RequestParam long scategoryId, HttpServletRequest request) {
        List<GetSVnameListModel> returnValue = new ArrayList<>();
        List<SubmitCode> svNameList = iSubmitCodeService.getSVbyCategory(scategoryId);

        if(svNameList == null || svNameList.isEmpty()) {
            new ResponseEntity(returnValue, HttpStatus.NOT_FOUND);
        }

        Type listType = new TypeToken<List<GetSVnameListModel>>(){}.getType();

        returnValue = new ModelMapper().map(svNameList, listType);
        log.info("Returning " + returnValue.size() + " S.V name list");

        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        return new ResponseEntity(returnValue, header, HttpStatus.OK);
    }

    @GetMapping("/list/{userId}")
    public @ResponseBody
    List<GetSubmitCodeListModel> getSubmitCodeList(@PathVariable String userId) {
        List<GetSubmitCodeListModel> returnValue = new ArrayList<>();
        List<SubmitCode> allSubmitCodeList = iSubmitCodeService.getSubmitCodeList(userId);

        if(allSubmitCodeList == null || allSubmitCodeList.isEmpty())
        {
            return returnValue;
        }

        Type listType = new TypeToken<List<GetSubmitCodeListModel>>(){}.getType();

        returnValue = new ModelMapper().map(allSubmitCodeList, listType);
        log.info("Returning " + returnValue.size() + " trainer's submit code list");
        return returnValue;
    }

    @GetMapping("/{scodeId}")
    public ResponseEntity<GetSubmitCodeModel> getSubmitCode(@PathVariable String scodeId) {

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        SubmitCodeDto createDto = iSubmitCodeService.getSubmitCode(scodeId);
        GetSubmitCodeModel returnValue = null;

        if (createDto != null) {
            returnValue = modelMapper.map(createDto, GetSubmitCodeModel.class);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    }

    @PutMapping
    public ResponseEntity<CreateSubmitCodeModel> updateSubmitCode(@ModelAttribute SubmitCodeDto submitCodeDto, @RequestParam(value="vulFile", required=false) MultipartFile vulFile,
                                                         @RequestParam(value="secFile", required=false) MultipartFile secFile, HttpServletResponse response) {

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        if (submitCodeDto.getScodeApproval() != 0) {
            return null;
        }

        Users email = iSubmitCodeService.getUserEmail(submitCodeDto.getUserId());
        SubmitCodeDto fileSaveReturn = fileSave.setFileStore(submitCodeDto, vulFile, secFile, email.getEmail(), response);

        SubmitCodeDto createDto = iSubmitCodeService.updateSubmitCode(fileSaveReturn);
        CreateSubmitCodeModel returnValue = modelMapper.map(createDto, CreateSubmitCodeModel.class);
        returnValue.setLanguageId(submitCodeDto.getLanguageId());
        returnValue.setSecId(submitCodeDto.getSecId());

        if(createDto != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
        }

        return null;
    }

    @DeleteMapping("/{scodeId}")
    public String deleteSubmitCode(@PathVariable String scodeId) {
        String deleteResult = iSubmitCodeService.deleteSubmitCode(scodeId);
        return deleteResult;
    }

}
