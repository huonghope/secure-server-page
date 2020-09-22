package com.openeg.openegscts.student.service;

import com.openeg.openegscts.student.entity.SolvedCode;
import com.openeg.openegscts.student.entity.Users;
import com.openeg.openegscts.student.repository.IUserMapper;
import com.openeg.openegscts.student.dto.UsersDto;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UsersServiceImpl implements IUsersService {

    IUserMapper mapper;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public UsersServiceImpl(IUserMapper mapper, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.mapper = mapper;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public UsersDto signUpUser(UsersDto usersDto) {

        usersDto = UsersDto.builder()
                .userId(UUID.randomUUID().toString())
                .name(usersDto.getName())
                .email(usersDto.getEmail())
                .password(bCryptPasswordEncoder.encode(usersDto.getPassword()))
                .build();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Users userEntity = modelMapper.map(usersDto, Users.class);
        mapper.signUpUser(userEntity);

        UsersDto returnValue = modelMapper.map(userEntity, UsersDto.class);
        return returnValue;
    }

    @Override
    public UsersDto signUpUserOAuth(UsersDto usersDto) {
        usersDto = UsersDto.builder()
                .userId(UUID.randomUUID().toString())
                .name(usersDto.getName())
                .email(usersDto.getEmail())
                .build();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Users userEntity = modelMapper.map(usersDto, Users.class);
        mapper.signUpUserOAuth(userEntity);

        UsersDto returnValue = modelMapper.map(userEntity, UsersDto.class);
        return returnValue;
    }


    @Override
    public UsersDto signUpTrainer(UsersDto usersDto) {

        usersDto = UsersDto.builder()
                .userId(UUID.randomUUID().toString())
                .name(usersDto.getName())
                .email(usersDto.getEmail())
                .password(bCryptPasswordEncoder.encode(usersDto.getPassword()))
                .build();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Users userEntity = modelMapper.map(usersDto, Users.class);
        mapper.signUpTrainer(userEntity);

        UsersDto returnValue = modelMapper.map(userEntity, UsersDto.class);
        return returnValue;
    }

    @Override
    public UsersDto signUpTrainerOAuth(UsersDto usersDto) {
        usersDto = UsersDto.builder()
                .userId(UUID.randomUUID().toString())
                .name(usersDto.getName())
                .email(usersDto.getEmail())
                .build();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        Users userEntity = modelMapper.map(usersDto, Users.class);
        mapper.signUpTrainerOAuth(userEntity);

        UsersDto returnValue = modelMapper.map(userEntity, UsersDto.class);
        return returnValue;
    }


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Users userEntity = mapper.findByEmail(email);

        if(userEntity == null) {
            throw new UsernameNotFoundException("정보를 찾을 수 없습니다.");
        }

        return new User(userEntity.getEmail() /*DB에서 검색되어진 데이터*/, userEntity.getPassword(), true, true, true, true, new ArrayList<>()/*role값*/);
    }

    @Override
    public UsersDto getUserDetailsByEmail(String email) {
        Users userEntity = mapper.findByEmail(email);

        if(userEntity == null) {
            throw new UsernameNotFoundException(email);
        }

        return new ModelMapper().map(userEntity, UsersDto.class);
    }

    @Override
    public UsersDto confirmUser(String email, String encryptedPassword) throws UsernameNotFoundException {
        ModelMapper modelMapper = new ModelMapper();
        Users userEntity = mapper.confirmUser(email);

        if(userEntity == null) {
            log.info(String.format("not exists user : %s", email));
            return null;
        }

        if(bCryptPasswordEncoder.matches(encryptedPassword, userEntity.getPassword())){
            return modelMapper.map(userEntity, UsersDto.class);
        } else {
            return null;
        }
    }

    @Override
    public UsersDto confirmUserOauth(String email) {
        ModelMapper modelMapper = new ModelMapper();
        Users userEntity = mapper.confirmUserOauth(email);

        if(userEntity == null) {
            log.info(String.format("not exists user : %s", email));
            return null;
        }

        return modelMapper.map(userEntity, UsersDto.class);
    }

    @Override
    public UsersDto confirmTrainer(String email, String encryptedPassword) throws UsernameNotFoundException {
        ModelMapper modelMapper = new ModelMapper();
        Users userEntity = mapper.confirmTrainer(email);

        if(userEntity == null) {
            log.info(String.format("not exists trainer : %s", email));
            return null;
        }

        if(bCryptPasswordEncoder.matches(encryptedPassword, userEntity.getPassword())){
            return modelMapper.map(userEntity, UsersDto.class);
        } else {
            return null;
        }
    }

    @Override
    public UsersDto confirmTrainerOauth(String email) {
        ModelMapper modelMapper = new ModelMapper();
        Users userEntity = mapper.confirmTrainerOauth(email);

        if(userEntity == null) {
            log.info(String.format("not exists trainer : %s", email));
            return null;
        }

        return modelMapper.map(userEntity, UsersDto.class);
    }

    @Override
    public UsersDto getUserById(String userId) {
        Users userEntity = mapper.getUserById(userId);

        if(userEntity == null) {
            log.info(String.format("not exists user : %s", userId));
            return null;
        }

        return new ModelMapper().map(userEntity, UsersDto.class);
    }

    @Override
    public List<SolvedCode> getSolvedCode(String userId) {
        List<SolvedCode> scEntity = mapper.getSolvedCode(userId);

        if(scEntity == null) {
            log.info(String.format("not exists solved code : %s", userId));
            return new ArrayList<>();
        }

        return scEntity;
    }

    @Override
    public int confirmPwd(String userId, String password) throws UsernameNotFoundException{

        Users userEntity = mapper.getUserById(userId);

        if(bCryptPasswordEncoder.matches(password, userEntity.getPassword())){
            log.info(String.format("exists user : %s", userId));
            return 1;
        } else {
            log.info(String.format("not matched pwd"));
            return 0;
        }

    }

    @Override
    public UsersDto updateUser(UsersDto usersDto) {

        usersDto = UsersDto.builder()
                .userId(usersDto.getUserId())
                .name(usersDto.getName())
                .email(usersDto.getEmail())
                .password(bCryptPasswordEncoder.encode(usersDto.getPassword()))
                .build();

        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        mapper.updateUser(usersDto);
        Users userEntity = mapper.getUserById(usersDto.getUserId());
        return new ModelMapper().map(userEntity, UsersDto.class);
    }
}