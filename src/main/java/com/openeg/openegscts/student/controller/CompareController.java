package com.openeg.openegscts.student.controller;

import com.openeg.openegscts.student.dto.CompareDto;
import com.openeg.openegscts.student.dto.CorrectResultDto;
import com.openeg.openegscts.student.service.ICompareService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/comp")
@CrossOrigin(origins = "*")
public class CompareController {

    ICompareService iCompareService;
    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public CompareController(ICompareService iCompareService) {
        this.iCompareService = iCompareService;
    }

    @PostMapping("/first")
    public
    CorrectResultDto getCompareLineResult(@ModelAttribute CompareDto compareDto) {

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        CorrectResultDto returnValue = iCompareService.compareLineNum(compareDto);

        if (returnValue == null) {
            log.info("not exists first compare result data");
            return null;
        }

        return returnValue;
    }

    @PostMapping("/second")
    public
    CorrectResultDto getCompareKeywordResult(@ModelAttribute CompareDto compareDto) throws InterruptedException {

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        CorrectResultDto returnValue = iCompareService.compareSolution(compareDto);

        if (returnValue == null) {
            log.info("not exists second compare result data");
            return null;
        }

        return returnValue;
    }

}
