package com.openeg.openegscts.student.controller;

import com.openeg.openegscts.student.entity.SolvedCode;
import com.openeg.openegscts.student.entity.Users;
import com.openeg.openegscts.student.model.ConfirmPwdModel;
import com.openeg.openegscts.student.model.CreateUserResponseModel;
import com.openeg.openegscts.student.model.LoginRequestModel;
import com.openeg.openegscts.student.model.UserInfoResponseModel;
import com.openeg.openegscts.student.service.IUsersService;
import com.openeg.openegscts.student.dto.UsersDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Type;
import java.util.*;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/users")
public class UsersController {

    IUsersService service;
    Environment env;
    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public UsersController(IUsersService service, Environment env) {
        this.service = service;
        this.env = env;
    }

    @PostMapping("/signup")
    public ResponseEntity<CreateUserResponseModel> createUser(@RequestBody Users user) {

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UsersDto usersDto = modelMapper.map(user, UsersDto.class);
        UsersDto createDto = service.signUpUser(usersDto);

        CreateUserResponseModel returnValue = modelMapper.map(createDto, CreateUserResponseModel.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    }

    @PostMapping("/signup/oauth")
    public ResponseEntity<CreateUserResponseModel> createUserOauth(@RequestBody Users user) {

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UsersDto usersDto = modelMapper.map(user, UsersDto.class);
        UsersDto createDto = service.signUpUserOAuth(usersDto);

        CreateUserResponseModel returnValue = modelMapper.map(createDto, CreateUserResponseModel.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    }

    @PostMapping("/login")
    public ResponseEntity loginUser(
            @RequestBody LoginRequestModel loginRequestModel
            , HttpServletResponse response
    ) {
    	System.out.println("Hello I connected login");
        UsersDto userDto = service.confirmUser(loginRequestModel.getEmail(), loginRequestModel.getPassword());

        if(userDto != null){
            Map<String, Object> map = new HashMap<>();
            String token = Jwts.builder()
                    .setSubject(Integer.toString(loginRequestModel.getType())) // setSubject는 하나만 저장되고 또 쓰면 오버라이트 됨
                    .setHeader(map)
                    .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expiration_time"))))
                    .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                    .compact();

            MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
            header.add("userId", userDto.getUserId());
            header.add("token", token);

            return new ResponseEntity(header, HttpStatus.OK);

        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/login/oauth")
    public ResponseEntity loginUserOauth(
            @RequestBody LoginRequestModel loginRequestModel
            , HttpServletResponse response
    ) {
        UsersDto userDto = service.confirmUserOauth(loginRequestModel.getEmail());
        if(userDto != null && loginRequestModel.getAccessToken() != null){
            Map<String, Object> map = new HashMap<>();
            String token = Jwts.builder()
                    .setSubject(Integer.toString(loginRequestModel.getType())) // setSubject는 하나만 저장되고 또 쓰면 오버라이트 됨
                    .setHeader(map)
                    .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expiration_time"))))
                    .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                    .compact();

            MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
            header.add("userId", userDto.getUserId());
            header.add("token", token);

            return new ResponseEntity(header, HttpStatus.OK);

        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserInfoResponseModel> getUserById(@PathVariable String userId) {

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UsersDto usersDto = service.getUserById(userId);

        UserInfoResponseModel returnValue = modelMapper.map(usersDto, UserInfoResponseModel.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    }

    @GetMapping("/list/{userId}")
    public @ResponseBody
    List<SolvedCode> getSolvedCode(@PathVariable String userId) {

        List<SolvedCode> returnValue = new ArrayList<>();
        List<SolvedCode> solvedCodeList = service.getSolvedCode(userId);

        if(solvedCodeList == null || solvedCodeList.isEmpty())
        {
            return returnValue;
        }

        Type listType = new TypeToken<List<SolvedCode>>(){}.getType();

        returnValue = new ModelMapper().map(solvedCodeList, listType);
        log.info("Returning " + returnValue.size() + " user's solved code list");

        return returnValue;
    }

    @PostMapping("/auth")
    public @ResponseBody int authPassword(@RequestBody ConfirmPwdModel confirmPwdModel) {
        int authResult = service.confirmPwd(confirmPwdModel.getUserId(), confirmPwdModel.getPassword());
        return authResult;
    }

    @PutMapping
    public ResponseEntity<UserInfoResponseModel> updateUser(@RequestBody UsersDto usersDto) {

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        UsersDto result = service.updateUser(usersDto);
        UserInfoResponseModel returnValue = modelMapper.map(result, UserInfoResponseModel.class);

        if(result != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
        }

        return null;
    }

}