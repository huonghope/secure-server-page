package com.openeg.openegscts.student.service;

import com.openeg.openegscts.student.service.IProjectService;
import com.openeg.openegscts.student.entity.UserContainer;
import com.openeg.openegscts.student.entity.OwaspContainer;
import com.openeg.openegscts.student.entity.Project;
import com.openeg.openegscts.student.entity.ProjectDiagnosis;
import com.openeg.openegscts.student.entity.SolvedCode;
import com.openeg.openegscts.student.entity.Users;
import com.openeg.openegscts.student.repository.IUserMapper;
import com.openeg.openegscts.student.dto.UserContainerDto;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.InspectContainerResponse;
import com.github.dockerjava.api.command.ListContainersCmd;
import com.github.dockerjava.api.command.ListImagesCmd;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Image;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DefaultDockerClientConfig.Builder;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.netty.NettyDockerCmdExecFactory;
import com.openeg.openegscts.student.dto.OwaspContainerDto;
import com.openeg.openegscts.student.dto.ProjectDto;
import com.openeg.openegscts.student.dto.UsersDto;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class UsersServiceImpl implements IUsersService {

    IUserMapper mapper;
    IProjectService projectService;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    ModelMapper modelMapper = new ModelMapper();
  
    Builder configBuilder = new DefaultDockerClientConfig.Builder()
        .withDockerTlsVerify(false)
        .withDockerHost("tcp://localhost:2375");

    DockerClient dockerClient = DockerClientBuilder.getInstance(configBuilder).build();

    @Autowired
    public UsersServiceImpl(IUserMapper mapper, BCryptPasswordEncoder bCryptPasswordEncoder, IProjectService projectService) {
        this.mapper = mapper;
        this.projectService = projectService;
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
	public UserContainer getUserContainer(String userId) {
		UserContainer container = mapper.getUserContainer(userId);
		return container;
	}

	@Override
	public UserContainerDto insertUserContainer(UserContainerDto containerDto) {
		// TODO Auto-generated method stub
		//프로젝트 경로 == 프로젝트 이름
		containerDto = UserContainerDto.builder()
					.containerId(containerDto.getContainerId())
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
	
		UserContainer containerEntity = modelMapper.map(containerDto, UserContainer.class);
		mapper.insertContainer(containerEntity);
	
	    UserContainerDto returnValue = modelMapper.map(containerEntity, UserContainerDto.class);
	    return returnValue;
	}

	@Override
	public boolean stopContainer(UserContainer container) throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		try {
			Collection<String> containerName = new ArrayList<String>() {{ add("/"+container.getContainerName());}};
			List<Container> containers = dockerClient.listContainersCmd()
					  .withStatusFilter(new ArrayList<String>() {{ add("running"); }})
					  .withNameFilter(containerName)
					  .exec();
			//컨테이너 있는지 확인
			if(containers.size() != 0  && !containers.get(0).getId().equals("")) 
			{
				
				dockerClient.stopContainerCmd(container.getContainerId()).exec();
				mapper.stopContainer(container.getContainerName());
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			System.out.println(e);
			return false;
			// TODO: handle exception
		}
	}
	@Override
	public boolean startContainer(UserContainer container) throws IOException, InterruptedException {
		try {
			Collection<String> containerName = new ArrayList<String>() {{ add("/"+container.getContainerName());}};
			List<Container> containers = dockerClient.listContainersCmd()
					  .withShowAll(true)
					  .withStatusFilter(new ArrayList<String>() {{ add("exited"); }})
					  .withNameFilter(containerName)
					  .exec();
			//컨테이너 있는지 확인
			System.out.println(containers.get(0).getId());
			if(containers.size() != 0  && !containers.get(0).getId().equals(""))
			{
				dockerClient.startContainerCmd(containers.get(0).getId()).exec();
				mapper.startContainer(container.getContainerName());
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			System.out.println(e);
			return false;
			// TODO: handle exception
		}
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
					.containerId(owaspcontainerDto.getContainerId())
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

	@Override
	public UserContainer createUserContainer(String userId) {
		try {
			// TODO Auto-generated method stub
			 UsersDto usersDto = getUserById(userId);  
			 String userName = usersDto.getName();
	
			/*
 			# PORT mappping:
 			# @params 4:3000 node
 			# @params 5:8080 python
 			# @params 6:10000 java 
			 */
 			String osName = System.getProperty("os.name");
 			String cmd = "";
 			if(osName.contains("Windows"))
 				cmd = "cmd /c .\\docker\\run-container.sh";
 			else if(osName.contains("Linux"))
 				cmd = ".\\docker\\run-container.sh";
 			else
 				System.out.println("unsupported OS");
 	
					 
 			cmd += " PROJECTS "+ userName + " 3000 8100 8200 8300";
 			Process process = Runtime.getRuntime().exec(cmd); 
 			process.waitFor(); 
 			Thread.sleep(1000);
		
//			int[] exposedPort = new int[] {5111, 3000, 8080, 10000};
//	        ExposedPort[] exposedPorts=new ExposedPort[exposedPort.length];
//	        for(int i=0; i < exposedPort.length; i++){
//	          exposedPorts[i]=ExposedPort.tcp(exposedPort[i]);
//	        }
//	        
//			int[] ports = new int[] {6000, 3005 , 8005 , 10005};
//	        List<PortBinding> portBindings = new ArrayList<>();
//	        if (ports != null) {
//	            for (int i=0; i< exposedPorts.length; i++ ) {
//	                portBindings.add(new PortBinding(Ports.Binding.bindPort(ports[i]), new ExposedPort(exposedPort[i])));
//	            }
//	        }
//	        
//	        
//	        String dockerName = usersDto.getName();
//			CreateContainerResponse container = dockerClient.createContainerCmd("vscode:latest")
//				.withName(dockerName)
//				.withUser("0:0")
//	           	.withExposedPorts(exposedPorts)
//	           	.withBinds(Bind.parse("PROJECTS/"+ dockerName +":/home/coder/projects"))
//	            .withPortBindings(portBindings.toArray(new PortBinding[portBindings.size()])).exec();
 			
 		
 			Collection<String> containerName = new ArrayList<String>() {{ add("/" + userName); }};
 			List<Container> containers = dockerClient.listContainersCmd()
 					  .withNameFilter(containerName)
 					  .exec();
 			
 			System.out.println(containers);
			
			//컨테이너 정상적으로 생성하는 경우에는 
			if(containers.size() != 0 && containers.get(0).getId() != null) {
				
				UserContainer userContainer = new UserContainer();
				List<Project> listproject = new ArrayList<>();
				listproject = projectService.getMyProjects(userId);
				
				UserContainerDto containerDto = new UserContainerDto(containers.get(0).getId(),listproject.get(0).getProjectId(), userId, userName, 
						3000, 8100, 8200, 8300, 1);
	    		UserContainerDto inserteContainer = insertUserContainer(containerDto);
	    		
	    		userContainer = modelMapper.map(inserteContainer, UserContainer.class);
	    		return userContainer;
			}
			return null;

		} catch (Exception e) {
			System.out.println(e);
			return null;
			// TODO: handle exception
		}
		
	    
	}
}