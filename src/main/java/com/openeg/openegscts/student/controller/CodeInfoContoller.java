package com.openeg.openegscts.student.controller;

import com.openeg.openegscts.student.dto.SecurityCodeDto;
import com.openeg.openegscts.student.dto.SolvedCodeDto;
import com.openeg.openegscts.student.entity.CodeList;
import com.openeg.openegscts.student.entity.Compare;
import com.openeg.openegscts.student.model.GetCodeInfoModel;
import com.openeg.openegscts.student.service.ICodeInfoService;
import com.openeg.openegscts.trainer.dto.SubmitCodeDto;
import com.openeg.openegscts.trainer.service.ISubmitCodeService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/sec")
public class CodeInfoContoller {

    ICodeInfoService iCodeInfoService;
    ISubmitCodeService iSubmitCodeService;
    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public CodeInfoContoller(ICodeInfoService iCodeInfoService, ISubmitCodeService iSubmitCodeService) {
        this.iCodeInfoService = iCodeInfoService;
        this.iSubmitCodeService = iSubmitCodeService;
    }

    @GetMapping("/{secId}")
    public @ResponseBody
    SecurityCodeDto getSecurityInfo(@PathVariable long secId) {

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        SecurityCodeDto securityCodeDto = iCodeInfoService.getSecurityInfo(secId);

        return securityCodeDto;
    }

    @PostMapping("/step")
    public @ResponseBody SolvedCodeDto getStepInfo(@RequestParam String scodeId, @RequestParam String userId) {

        SolvedCodeDto solvedCodeDto = iCodeInfoService.getStepInfo(scodeId, userId);

        if(solvedCodeDto == null) {
            return null;
        }

        return solvedCodeDto;
    }

    @GetMapping("/code/{scodeId}")
    public ResponseEntity<GetCodeInfoModel> getSubmitCode(@PathVariable String scodeId) {

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        SubmitCodeDto createDto = iSubmitCodeService.getSubmitCode(scodeId);
        GetCodeInfoModel returnValue = null;

        if (createDto != null) {
            returnValue = modelMapper.map(createDto, GetCodeInfoModel.class);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    }

    @PostMapping("/list")
    List<CodeList> getCodeListBySecId(@ModelAttribute Compare compare) {

        List<CodeList> returnValue = new ArrayList<>();
        List<CodeList> codeList = iCodeInfoService.getCodeListBySecId(compare);

        if(codeList == null || codeList.isEmpty())
        {
            return returnValue;
        }

        Type listType = new TypeToken<List<CodeList>>(){}.getType();

        returnValue = new ModelMapper().map(codeList, listType);
        log.info("Returning " + returnValue.size() + " code list by secId");

        return returnValue;
    }

}
