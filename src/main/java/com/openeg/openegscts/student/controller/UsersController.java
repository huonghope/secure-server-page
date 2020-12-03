package com.openeg.openegscts.student.controller;

import com.openeg.openegscts.student.entity.Container;
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
import com.openeg.openegscts.student.dto.ContainerDto;
import com.openeg.openegscts.student.dto.ProjectDto;
import com.openeg.openegscts.student.dto.SecurityCodeDto;
import com.openeg.openegscts.student.dto.SpringProjectDto;
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
    public UsersController(IUsersService service,IProjectService projectService,  Environment env) {
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

    //일발 프로잭트: Nodejs & Python
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
    
    //Spring 프로잭트: Spring Project
    @PostMapping("/springprojects")
    public ResponseEntity<String> createSpringProject(@RequestBody CreateSpringProject createSpringProject)
    {
		SpringProjectDto createSpringProjectDto = modelMapper.map(createSpringProject, SpringProjectDto.class);
		
		System.out.println("control" + createSpringProject);
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
    
    //프로젝트 출력함
    @GetMapping("/projects/{userId}")
    public ResponseEntity<List<Project>> getListMyProjects(@PathVariable String userId)
    {
    	try {
    		List<Project> returnValue = new ArrayList<>();
    		returnValue = projectService.getMyProjects(userId);
    		if(returnValue != null) {
    			return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
    		}
    		return ResponseEntity.status(HttpStatus.CREATED).body(returnValue);
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
			return ResponseEntity.status(HttpStatus.CREATED).body(null);
		}
    }
    
    //해당하는 프로젝트 출력함
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
    public ResponseEntity<String> deletProject(@PathVariable String userId, @PathVariable String projectId)
    {
    	 Project project = projectService.getProjectById(projectId);
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
    		      	
    		 //프로젝트 삭제
    		 //!수정 필요합니다.
    		 File projectPath = new File("PROJECTS/" + usersDto.getName() +"/" + project.getProjectPath());
    		 System.out.println(projectPath);
    		 String[]entries = projectPath.list();
    		 for(String s: entries){
    		     File currentFile = new File(projectPath.getPath(),s);
    		     currentFile.delete();
    		 }
    		 projectPath.delete();
    			
    		 
    		 if(returnValue) {
    			 return ResponseEntity.status(HttpStatus.CREATED).body("success");
    		 }
    		 return ResponseEntity.status(HttpStatus.CREATED).body("fail");   
    	 }else {
    		 return ResponseEntity.status(HttpStatus.CREATED).body("fail");   
    	 }
    }
 
    @GetMapping("/checkExistsProjectName/{userId}/{projectName}")
    public ResponseEntity<String> checkExistsProjectName(@PathVariable String projectName, @PathVariable String userId)
    {
    	boolean returnValue = projectService.checkExistsProjectName(projectName, userId);
	    if(returnValue != false) {
            return ResponseEntity.status(HttpStatus.CREATED).body("exists");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("no-exists");
    }
   
    @GetMapping("/container/{userId}")
    public ResponseEntity<Container> getUserContainer(@PathVariable String userId) throws IOException
    {
    	Container container = service.getUserContainer(userId);
    	
    	//유저 ID를 받아서 유저의 Container를 출력함
    	//해당하는 유저는 Container가 없으면 생성해줌
    	//처음 한번 실행함
    	if(container == null) {
    		try {
    			// sh 파일을 통해서 Container를 올림
    			/*
    			# PORT mappping:
				# $NODE_PORT:3000 node.js
				# $PYTHON_PORT:8080 python
				# $JAVA_PORT:10000 java
				*/
    			String osName = System.getProperty("os.name");
    			String cmd = "";
    			if(osName.contains("Windows"))
    				cmd = "cmd /c .\\docker\\run-container.sh";
    			else if(osName.contains("Linux"))
    				cmd = ".\\docker\\run-container.sh";
    			else
    				System.out.println("unsupported OS");
    		
			   List<Project> listproject = new ArrayList<>();
			   listproject = projectService.getMyProjects(userId);
			   
			   UsersDto usersDto = service.getUserById(userId);
			   
			   //Vscode, node, java, python - ports
			   /*
			    * @params:
			    * PROJECTS: 모든 프로젝트를 저장하는 폴더
			    * userName: 유저별로 프로젝트를 저장하는 폴더
			    * @port1: vs-code를 접근하기 위해서 port
			    * @port2: node.js 접근하기위해서 port
			    * @port3: python.js 접근하기위해서 port
			    * @port4: java.js 접근하기위해서 port
			    * */
    			cmd += " PROJECTS "+ usersDto.getName() + " 3000 8100 8200 8300";
    			Process process = Runtime.getRuntime().exec(cmd); 
    			process.waitFor(); 
    			Thread.sleep(1000);
			  
			    //sh 파일을 통해서 Container를 올린 후에 Container를 정상적으로 올리는지 확인
    			//현재 돌리는 Container 리스트 출력해서 비교함
			    process = Runtime.getRuntime().exec("docker ps"); 
			    BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			    String line = "";
			    boolean checkSucc = false;
			    
			    //생성된 컨테이너 확인
			    while ((line = reader.readLine()) != null) {
			    	if(line.contains(usersDto.getName())) {
			    		//Container를 정상적으로 올렸으면 데이트베이스에다가 정보를 저장함
			    		//Default 올리 프로젝트는 첫번째 프로젝트임
			    		ContainerDto containerDto = new ContainerDto(listproject.get(0).getProjectId(), userId,  usersDto.getName(), 3000,8100,8200,8300,1);
			    		ContainerDto inserteContainer = service.insertUserContainer(containerDto);
			    		
			    		container = modelMapper.map(inserteContainer, Container.class);
			    		checkSucc = true;
			    		break;
			    	}
			    }
			    //컨테이서 정상적으로 하면 프로젝트 정보를 출력함
			    if(checkSucc) {
			    	return ResponseEntity.status(HttpStatus.CREATED).body(container);
			    }
			    else {
			    	 return ResponseEntity.status(HttpStatus.CREATED).body(null);
			    }
			 
			} catch (Exception e) {
				System.out.println(e);
				// TODO: handle exception
			}
    	}
    	
    	//이미 프로젝트를 존재하면 프로젝트정보를 출력함
	    if(container != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(container);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
    
    @GetMapping("/moveeditor/{userId}/{projectId}")
    public ResponseEntity<Container> moveEditor(@PathVariable String userId, @PathVariable String projectId)
    {
   
    	Project project = projectService.getProjectById(projectId);
    	String path = project.getProjectPath();
    	String type = project.getProjectType();
    	
    	UsersDto usersDto = service.getUserById(userId);
    	service.updateContainerForProjectId(projectId, usersDto.getName());
    	
    	Container container = service.getUserContainer(userId);
    
	    if(container != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(container);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
    
    @GetMapping("/stopContainer/{userId}")
    public ResponseEntity<String> stopContainer(@PathVariable String userId) throws IOException, InterruptedException
    {
   
    	Container container = service.getUserContainer(userId);
    	int vsCodePort = container.getState();
    	boolean result = false;
    	if(vsCodePort == 1) { 
    		//컨테이너 실행 중일 경우는 중지시킴
    		result = service.stopContainer(container);
    	}
	    if(result) {
            return ResponseEntity.status(HttpStatus.CREATED).body("success");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("fail");
    }
    @GetMapping("/startContainer/{userId}")
    public ResponseEntity<String> startContainer(@PathVariable String userId) throws IOException, InterruptedException
    {
   
    	Container container = service.getUserContainer(userId);
    	int vsCodePort = container.getState();
    	boolean result = false;
    	if(vsCodePort == 0) { 
    		//컨테이너 실행 중일 경우는 다시 시작
    		result = service.startContainer(container);
    	}
	    if(result) {
            return ResponseEntity.status(HttpStatus.CREATED).body("success");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body("fail");
    }
    
    @GetMapping("/webservice/{userId}")
    public ResponseEntity<String> webService(@PathVariable String userId) throws IOException, InterruptedException
    {
    	//현재 컨테이너내에서 올리는 프로젝트를 웹 페이지를 접근함
    	Container container = service.getUserContainer(userId);
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
    	String checkUrl = "http://210.94.194.70:" + Integer.toString(port);
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