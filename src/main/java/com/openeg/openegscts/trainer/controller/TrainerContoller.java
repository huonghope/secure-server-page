package com.openeg.openegscts.trainer.controller;

import com.openeg.openegscts.student.dto.UsersDto;
import com.openeg.openegscts.student.entity.Users;
import com.openeg.openegscts.student.model.CreateUserResponseModel;
import com.openeg.openegscts.student.model.LoginRequestModel;
import com.openeg.openegscts.student.service.IUsersService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/trainer")
public class TrainerContoller {

    IUsersService service;
    Environment env;
    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public TrainerContoller(IUsersService service, Environment env) {
        this.service = service;
        this.env = env;
    }

    @PostMapping("/signup")
    public ResponseEntity<CreateUserResponseModel> createTrainer(@RequestBody Users user) {


        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UsersDto usersDto = modelMapper.map(user, UsersDto.class);
        UsersDto createDto = service.signUpTrainer(usersDto);

        CreateUserResponseModel returnValue = modelMapper.map(createDto, CreateUserResponseModel.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    }

    @PostMapping("/signup/oauth")
    public ResponseEntity<CreateUserResponseModel> createTrainerOauth(@RequestBody Users user) {

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UsersDto usersDto = modelMapper.map(user, UsersDto.class);
        UsersDto createDto = service.signUpTrainerOAuth(usersDto);

        CreateUserResponseModel returnValue = modelMapper.map(createDto, CreateUserResponseModel.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    }

    @PostMapping("/login")
    public ResponseEntity loginTrainer(
            @RequestBody LoginRequestModel loginRequestModel
            , HttpServletResponse response
    ) {
        UsersDto userDto = service.confirmTrainer(loginRequestModel.getEmail(), loginRequestModel.getPassword());

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
    public ResponseEntity loginTrainerOauth(
            @RequestBody LoginRequestModel loginRequestModel
            , HttpServletResponse response
    ) {

        UsersDto userDto = service.confirmTrainerOauth(loginRequestModel.getEmail());

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
}

