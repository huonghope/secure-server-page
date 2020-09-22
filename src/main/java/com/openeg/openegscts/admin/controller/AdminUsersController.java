package com.openeg.openegscts.admin.controller;

import com.openeg.openegscts.admin.dto.UsersListDto;
import com.openeg.openegscts.admin.service.IAdminUsersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/users")
public class AdminUsersController {

    IAdminUsersService iAdminUsersService;

    @Autowired
    public AdminUsersController(IAdminUsersService iAdminUsersService) {
        this.iAdminUsersService = iAdminUsersService;
    }

    @GetMapping
    public @ResponseBody
    List<UsersListDto> getAllUsers() {
        List<UsersListDto> returnValue = iAdminUsersService.getUsersForAdmin();

        if(returnValue == null || returnValue.isEmpty())
        {
            return new ArrayList<>();
        }

        log.info("Returning " + returnValue.size() + " user list");
        return returnValue;
    }

    @DeleteMapping("/{userId}")
    public int deleteUser(@PathVariable String userId) {
        int deleteResult = iAdminUsersService.deleteUser(userId);
        return deleteResult;
    }

}