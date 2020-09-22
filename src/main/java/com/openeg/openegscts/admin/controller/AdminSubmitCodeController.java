package com.openeg.openegscts.admin.controller;

import com.openeg.openegscts.admin.dto.AdminRejectDto;
import com.openeg.openegscts.admin.dto.AdminSubmitCodeDto;
import com.openeg.openegscts.admin.entity.AdminSubmitCode;
import com.openeg.openegscts.admin.service.IAdminSubmitCodeService;
import com.openeg.openegscts.trainer.service.ISubmitCodeService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/list")
public class AdminSubmitCodeController {

    IAdminSubmitCodeService iAdminSubmitCodeService;
    ISubmitCodeService iSubmitCodeService;

    @Autowired
    public AdminSubmitCodeController(IAdminSubmitCodeService iAdminSubmitCodeService, ISubmitCodeService iSubmitCodeService) {
        this.iAdminSubmitCodeService = iAdminSubmitCodeService;
        this.iSubmitCodeService = iSubmitCodeService;
    }

    @GetMapping("/all")
    public @ResponseBody
    List<AdminSubmitCode> getAllSubmitCodeList() {
        List<AdminSubmitCode> returnValue = iAdminSubmitCodeService.getAllSubmitCodeList();

        if(returnValue == null || returnValue.isEmpty())
        {
            return new ArrayList<>();
        }

        log.info("Returning " + returnValue.size() + " admin all submit code list");
        return returnValue;
    }

    @GetMapping("/new")
    public @ResponseBody
    List<AdminSubmitCodeDto> getNewSubmitCodeList() {
        List<AdminSubmitCodeDto> returnValue = new ArrayList<>();
        List<AdminSubmitCode> allScList = iAdminSubmitCodeService.getAllNewSubmit();

        if(allScList == null || allScList.isEmpty())
        {
            return returnValue;
        }

        Type listType = new TypeToken<List<AdminSubmitCodeDto>>(){}.getType();

        returnValue = new ModelMapper().map(allScList, listType);
        log.info("Returning " + returnValue.size() + " admin new submit code list");

        return returnValue;
    }

    @PutMapping("/ok")
    public int updateOk(@RequestParam String scodeId) {

        int returnValue = iAdminSubmitCodeService.updateOk(scodeId);
        return returnValue;
    }

    @PutMapping("/nok")
    public int updateNotOk(@ModelAttribute AdminRejectDto adminRejectDto) {

        int returnValue = iAdminSubmitCodeService.updateNotOk(adminRejectDto);
        return returnValue;
    }
}
