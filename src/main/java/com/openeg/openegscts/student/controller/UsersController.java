package com.openeg.openegscts.student.controller;

import com.openeg.openegscts.student.entity.UserContainer;
import com.openeg.openegscts.student.entity.Project;
import com.openeg.openegscts.student.entity.SecurityCode;
import com.openeg.openegscts.student.entity.SolvedCode;
import com.openeg.openegscts.student.entity.Users;
import com.openeg.openegscts.student.model.ConfirmPwdModel;
import com.openeg.openegscts.student.model.CreateProject;
import com.openeg.openegscts.student.model.CreateSpringProject;
import com.openeg.openegscts.student.model.CreateUserResponseModel;
import com.openeg.openegscts.student.model.LoginRequestModel;
import com.openeg.openegscts.student.model.UserInfoResponseModel;
import com.openeg.openegscts.student.service.IProjectService;
import com.openeg.openegscts.student.service.IUsersService;
import com.openeg.openegscts.student.dto.UserContainerDto;
import com.openeg.openegscts.student.dto.ProjectDto;
import com.openeg.openegscts.student.dto.SecurityCodeDto;
import com.openeg.openegscts.student.dto.SpringProjectDto;
import com.openeg.openegscts.student.dto.UsersDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/users")
public class UsersController {

    IUsersService service;
    IProjectService projectService;
    Environment env;
    ModelMapper modelMapper = new ModelMapper();

    @Autowired
    public UsersController(IUsersService service,IProjectService projectService, Environment env) {
        this.service = service;
        this.projectService = projectService;
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
    public @ResponseBody List<SolvedCode> getSolvedCode(@PathVariable String userId) {

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

    //일발 프로잭트 생성: Nodejs & Python
    @PostMapping("/projects")
    public ResponseEntity<String> createProject(@RequestBody CreateProject createProject)
    {
    	ProjectDto projectDto = modelMapper.map(createProject, ProjectDto.class);
		ProjectDto createProjectDto = projectService.createProject(projectDto);
		UsersDto usersDto = service.getUserById(projectDto.getProjectUserId());
		
		//프로젝트 만들때 프로젝트 코스코드 저정하는 풀더 만들
		String path = ".\\PROJECTS\\" + usersDto.getName() + "\\" + projectDto.getProjectName();
		
		File files = new File(path);
        if (!files.exists()) {
            if (files.mkdirs()) {
                System.out.println("프로젝트 폴더 생성 성곡");
            } else {
                System.out.println("프로젝트 폴더 생성 성곡");
            }
        }
		
	    if(createProjectDto != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("success");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("failed");
    }
    
    //Spring 프로잭트 생성: Spring Project
    @PostMapping("/springprojects")
    public ResponseEntity<String> createSpringProject(@RequestBody CreateSpringProject createSpringProject)
    {
		SpringProjectDto createSpringProjectDto = modelMapper.map(createSpringProject, SpringProjectDto.class);
		SpringProjectDto createProjectDto = projectService.createSpringProject(createSpringProjectDto);
		UsersDto usersDto = service.getUserById(createProjectDto.getProjectUserId());
		
		//프로젝트 만들때 프로젝트 코스코드 저정하는 풀더 만들
		String path = ".\\PROJECTS\\" + usersDto.getName() + "\\" + createProjectDto.getProjectName();
	
		File files = new File(path);
        if (!files.exists()) {
            if (files.mkdirs()) {
                System.out.println("프로젝트 폴더 생성 성곡");
            } else {
                System.out.println("프로젝트 폴더 생성 성곡");
            }
        }
	    if(createProjectDto != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body("success");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("failed");
    }
    
    //해당하는 유저 프로젝트 리스트 출력함
    @GetMapping("/projects/{userId}")
    public ResponseEntity<List<Project>> getListMyProjects(@PathVariable String userId)
    {
    	try {
    		List<Project> returnValue = new ArrayList<>();
    		returnValue = projectService.getMyProjects(userId);
    		return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    		
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.CREATED).body(null);
		}
    }
    
    //특정 프로젝트 출력함
    @GetMapping("/projects/{userId}/{projectId}")
    public ResponseEntity<Project> getProjectById(@PathVariable String userId, @PathVariable String projectId)
    {
    	Project project = projectService.getProjectById(projectId);
    	if(project.getProjectUserId().equals(userId)) {
    		 Project returnValue = projectService.getProjectById(projectId);
    		 if(returnValue != null) {
    			 return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    		 }else {
    			 return ResponseEntity.status(HttpStatus.CREATED).body(null);      			 
    		 }
    	}else {
    		return ResponseEntity.status(HttpStatus.CREATED).body(null);    		
    	}
    }
    //프로젝트 삭제
    @DeleteMapping("/projects/{userId}/{projectId}")
    public ResponseEntity<String> deletProject(@PathVariable String userId, @PathVariable String projectId) throws IOException
    {
    	 Project project = projectService.getProjectById(projectId);
    	 
    	 //프로젝트 및 유저 ID 체크함
    	 if(project.getProjectUserId().equals(userId)) {
    		 
    		 boolean returnValue = projectService.deleteProject(projectId);
    		 List<Project> listProject = new ArrayList<>();
    		 listProject = projectService.getMyProjects(userId);
    		 UsersDto usersDto = service.getUserById(userId);
    		 
    		 if(listProject.size() != 0) {
    			 service.updateContainerForProjectId(listProject.get(0).getProjectId(), usersDto.getName());    			 
    		 }else {
    			 service.updateContainerForProjectId(null, usersDto.getName()); 
    		 }
    		      	
    		 //프로젝트 퐆더 삭제
    		 File projectPath = new File("PROJECTS/" + usersDto.getName() +"/" + project.getProjectPath());
    		 if(projectPath.exists()) {
    			 FileUtils.deleteDirectory(projectPath);
    		 }

    		 if(returnValue) {
    			 return ResponseEntity.status(HttpStatus.CREATED).body("success");
    		 }
    		 return ResponseEntity.status(HttpStatus.CREATED).body("fail");   
    	 }else {
    		 return ResponseEntity.status(HttpStatus.CREATED).body("fail");   
    	 }
    }
 
    //만든 프로젝트 이름 체크
    @GetMapping("/check-projectname/{userId}/{projectName}")
    public ResponseEntity<String> checkExistsProjectName(@PathVariable String projectName, @PathVariable String userId)
    {
    	boolean returnValue = projectService.checkExistsProjectName(projectName, userId);
	    if(returnValue != false) {
            return ResponseEntity.status(HttpStatus.CREATED).body("exists");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("no-exists");
    }
   
    //컨테이너 출력 및 생성
    //해당하는 유저의 Vs-code 컨네이너 한번 체크하고 만약에 없으면 생성해줌
    @GetMapping("/container/{userId}")
    public ResponseEntity<UserContainer> getUserContainer(@PathVariable String userId) throws IOException
    {
    	UserContainer container = service.getUserContainer(userId);
    	if(container == null) {
    		try {
    			//컨테이너 생성 성공할 경우에는
    			container = service.createUserContainer(userId);
    			if(container != null) {
    				return ResponseEntity.status(HttpStatus.CREATED).body(container);
    			}
    			return ResponseEntity.status(HttpStatus.CREATED).body(null);
			} catch (Exception e) {
				System.out.println(e);
				// TODO: handle exception
			}
    	}
    	//첫 프로젝트는 컨테이너 정보를 업데이트
		String projectId = container.getProjectId();
		if(projectId == null) {
			List<Project> listProject = new ArrayList<>();
	   		listProject = projectService.getMyProjects(userId);
	   		UsersDto usersDto = service.getUserById(userId);
	   		service.updateContainerForProjectId(listProject.get(0).getProjectId(), usersDto.getName());  
	   		container = service.getUserContainer(userId);
		}
    	return ResponseEntity.status(HttpStatus.CREATED).body(container);
    	
    }
    
    //Vs-code 에디터 클릭할떄
    @GetMapping("/move-editor/{userId}/{projectId}")
    public ResponseEntity<UserContainer> moveEditor(@PathVariable String userId, @PathVariable String projectId)
    {
   
    	Project project = projectService.getProjectById(projectId);
    	if(project.getProjectUserId().equals(userId)) {
    		UsersDto usersDto = service.getUserById(userId);
    		service.updateContainerForProjectId(projectId, usersDto.getName());
    		
    		//컨테이너 출력함
    		UserContainer container = service.getUserContainer(userId);
    		if(container != null) {
    			return ResponseEntity.status(HttpStatus.CREATED).body(container);
    		}
    	}
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
    //컨테이너 중지됨
    @GetMapping("/stop-container/{userId}")
    public ResponseEntity<String> stopContainer(@PathVariable String userId) throws IOException, InterruptedException
    {
   
    	UserContainer container = service.getUserContainer(userId);
    	int containerState = container.getState();
    	boolean result = false;
    	if(containerState == 1) { 
    		//컨테이너 실행 중일 경우는 중지시킴
    		result = service.stopContainer(container);
    	}
	    if(result) {
            return ResponseEntity.status(HttpStatus.CREATED).body("success");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("fail");
    }
    //컨테이너 시작함
    @GetMapping("/start-container/{userId}")
    public ResponseEntity<String> startContainer(@PathVariable String userId) throws IOException, InterruptedException
    {
   
    	UserContainer container = service.getUserContainer(userId);
    	int containerPort = container.getState();
    	boolean result = false;
    	if(containerPort == 0) { 
    		//컨테이너 실행 중일 경우는 다시 시작
    		result = service.startContainer(container);
    	}
	    if(result) {
            return ResponseEntity.status(HttpStatus.CREATED).body("success");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("fail");
    }
    
    //현재 컨테이너내에 가동되어 있는 웹 서비스를 접근함
    @GetMapping("/webservice/{userId}")
    public ResponseEntity<String> webService(@PathVariable String userId) throws IOException, InterruptedException
    {
    	//현재 컨테이너내에서 올리는 프로젝트를 웹 페이지를 접근함
    	UserContainer container = service.getUserContainer(userId);
    	Project project = projectService.getProjectById(container.getProjectId());
    	
    	String projectType = project.getProjectType();
    	int port = 0;
    	switch (projectType) {
			case "nodejs":
				port = container.getNodePort();
				break;
			case "python":
				port = container.getPythonPort();		
				break;
	
			case "javaspring":
				port = container.getJavaPort();
				break;
			default:
				break;
		}
    	String checkUrl = env.getProperty("ip.addr").toString() + ":" + Integer.toString(port);
    	URL url = new URL(checkUrl);
    	try {
    		HttpURLConnection huc = (HttpURLConnection) url.openConnection();
    		int responseCode = huc.getResponseCode();
    		
    		//해당하는 페이지를 접근 가능하는지 체크함
    		if(responseCode == 200) {
    			return ResponseEntity.status(HttpStatus.CREATED).body(Integer.toString(port));
    			
    		}else {
    			return ResponseEntity.status(HttpStatus.CREATED).body("fail");
    		}
			
		} catch (Exception e) {
			System.out.println("서비스 접근 오류" + e);
			return ResponseEntity.status(HttpStatus.CREATED).body("fail");
			// TODO: handle exception
		}
    	
    }
    
}