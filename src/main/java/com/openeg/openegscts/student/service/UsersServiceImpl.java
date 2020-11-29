package com.openeg.openegscts.student.service;

import com.openeg.openegscts.student.entity.Container;
import com.openeg.openegscts.student.entity.OwaspContainer;
import com.openeg.openegscts.student.entity.Project;
import com.openeg.openegscts.student.entity.ProjectDiagnosis;
import com.openeg.openegscts.student.entity.SolvedCode;
import com.openeg.openegscts.student.entity.Users;
import com.openeg.openegscts.student.repository.IUserMapper;
import com.openeg.openegscts.student.dto.ContainerDto;
import com.openeg.openegscts.student.dto.OwaspContainerDto;
import com.openeg.openegscts.student.dto.ProjectDto;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

	@Override
	public List<ProjectDiagnosis> getProjectDiagnosis(String projectId) {
		 List<ProjectDiagnosis> listDiagnosis = mapper.getProjectDiagnosis(projectId);
		return listDiagnosis;
	}

	@Override
	public Container getUserContainer(String userId) {
		Container container = mapper.getUserContainer(userId);
		return container;
	}

	@Override
	public ContainerDto insertUserContainer(ContainerDto containerDto) {
		// TODO Auto-generated method stub
		//프로젝트 경로 == 프로젝트 이름
		containerDto = ContainerDto.builder()
					.containerId(UUID.randomUUID().toString())
					.projectId(containerDto.getProjectId())
	                .userId(containerDto.getUserId())
	                .containerName(containerDto.getContainerName())
	                .vscodePort(containerDto.getVscodePort())
	                .nodePort(containerDto.getNodePort())
	                .javaPort(containerDto.getJavaPort())
	                .pythonPort(containerDto.getPythonPort())
	                .state(containerDto.getState())
	                .build();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
	
		Container containerEntity = modelMapper.map(containerDto, Container.class);
		mapper.insertContainer(containerEntity);
	
	    ContainerDto returnValue = modelMapper.map(containerEntity, ContainerDto.class);
	    return returnValue;
	}

	@Override
	public boolean stopContainer(Container container) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		boolean checkSucc = false;
		try {
			String cmd = "docker ps";
			Process process = Runtime.getRuntime().exec(cmd); 
			process.waitFor(); 
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = "";
			//생성된 컨테이너 확인
			while ((line = reader.readLine()) != null) {
				if(line.contains(container.getContainerName())) {
					cmd = "docker stop " + container.getContainerName();
					Runtime.getRuntime().exec(cmd);
					
					mapper.stopContainer(container.getContainerName());
					checkSucc = true;
					break;
				}
			}
			return checkSucc;
		} catch (Exception e) {
			System.out.println(e);
			return checkSucc;
			// TODO: handle exception
		}
	}
	@Override
	public boolean startContainer(Container container) throws IOException, InterruptedException {
		String cmd = "docker ps --filter \"status=exited\"";
		Process process = Runtime.getRuntime().exec(cmd); 
		process.waitFor(); 
	    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	    String line = "";
	    boolean checkSucc = false;
	    //생성된 컨테이너 확인
	    while ((line = reader.readLine()) != null) {
	    	if(line.contains(container.getContainerName())) {
	    		//컨테이너 다시 시작함
	    		cmd = "docker start " + container.getContainerName();
	    		Runtime.getRuntime().exec(cmd);
	    		process.waitFor(); 
	    		Thread.sleep(2000);
	    		
	    		
	    		//컨테이너 정상적으로 올리는지 한번 다식 확인함
	    		cmd = "docker ps";
	    		process = Runtime.getRuntime().exec(cmd); 
	    		process.waitFor(); 
	    	    reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
	    	    line = "";
	    	    while ((line = reader.readLine()) != null) {    	
	    	    	if(line.contains(container.getContainerName())) {
	    	    		checkSucc = true;
	    	    		mapper.startContainer(container.getContainerName());	    	    	
	    	    		break;
	    	    	}
	    	    }
	    	}
	    }
		return checkSucc;
	}

	@Override
	public boolean updateContainerForProjectId(String projectId, String containerName) {
		boolean result = false;
		try {
			mapper.updateContainerForProjectId(projectId, containerName);
			result = true;
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
			return result;
		}
		return result;
		// TODO Auto-generated method stub
	}

	@Override
	public OwaspContainer getUserOwaspContainer(String containerName) {
		try {
			OwaspContainer owaspcontainer = mapper.getUserOwaspContainer(containerName);
			// TODO Auto-generated method stub
			return owaspcontainer;
		}catch (Exception e) {
			System.out.println(e);
			// TODO: handle exception
		}
		return null;
	}

	@Override
	public OwaspContainerDto insertUserOwaspContainer(OwaspContainerDto owaspcontainerDto) {
		// TODO Auto-generated method stub
		//프로젝트 경로 == 프로젝트 이름
		owaspcontainerDto = OwaspContainerDto.builder()
					.containerId(UUID.randomUUID().toString())
					.containerPort(owaspcontainerDto.getContainerPort())
	                .userId(owaspcontainerDto.getUserId())
	                .containerName(owaspcontainerDto.getContainerName())
	                .build();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
	
		OwaspContainer containerEntity = modelMapper.map(owaspcontainerDto, OwaspContainer.class);
		mapper.insertOwaspContainer(containerEntity);
	
		OwaspContainerDto returnValue = modelMapper.map(containerEntity, OwaspContainerDto.class);
	    return returnValue;
	}
}