package com.openeg.openegscts.analyzer.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Dictionary;
import java.util.List;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.model.Container;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.Ports;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DefaultDockerClientConfig.Builder;
import com.openeg.openegscts.analyzer.service.Service;
import com.openeg.openegscts.file.FileSaveC;
import com.openeg.openegscts.student.dto.UserContainerDto;
import com.openeg.openegscts.student.dto.OwaspContainerDto;
import com.openeg.openegscts.student.dto.ProjectDiagnosisDto;
import com.openeg.openegscts.student.dto.UsersDto;
import com.openeg.openegscts.student.entity.UserContainer;
import com.openeg.openegscts.student.entity.OwaspContainer;
import com.openeg.openegscts.student.entity.Project;
import com.openeg.openegscts.student.entity.ProjectDiagnosis;
import com.openeg.openegscts.student.model.DynamicAnalyzerModel;
import com.openeg.openegscts.student.model.LoginRequestModel;
import com.openeg.openegscts.student.service.IProjectService;
import com.openeg.openegscts.student.service.IUsersService;

@CrossOrigin(origins = "*")
@Controller
public class DynamicAnalyzer {
	
	 IUsersService service;
	 FileSaveC fileSave;
	 IProjectService projectService;
	 Environment env;
	 ModelMapper modelMapper = new ModelMapper();
	 
	 //개발환경 맞게 수정필요함
	 Builder configBuilder = new DefaultDockerClientConfig.Builder()
		        .withDockerTlsVerify(false)
		        .withDockerHost("tcp://localhost:2375");

     DockerClient dockerClient = DockerClientBuilder.getInstance(configBuilder).build();
	 
	@Autowired
    public DynamicAnalyzer(IUsersService service, IProjectService projectService, FileSaveC fileSave, Environment env) {
        this.fileSave = fileSave;
        this.service = service;
        this.projectService = projectService;
        this.env = env;
    }
	
	@RequestMapping("/dynamic-analyzer")
	 public ResponseEntity ServerProcess(
	            @RequestBody DynamicAnalyzerModel dynamicAnalyzerModel
	            ,HttpServletResponse response
	    ) {
		try {
			String userId = dynamicAnalyzerModel.getUserId();
			String projectId = dynamicAnalyzerModel.getProjectId();			
			String typeReport = dynamicAnalyzerModel.getTypereport();
			
			
			String result = "";
			Project project = projectService.getProjectById(projectId);
			
			UserContainer container = service.getUserContainer(userId);
			
			//프로젝트 타입 및 해당하는 유저의 Container를 받아서 링크를 출력함
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
			//출력되는 링크를 유호하지 않을 링크인지 체크함
	    	URL url = new URL(checkUrl);
	    	try {
	    		HttpURLConnection huc = (HttpURLConnection) url.openConnection();
	    		int responseCode = huc.getResponseCode();
	    		
	    		UsersDto usersDto = service.getUserById(userId);
	    		String userName = usersDto.getName();
	    		//해당하는 페이지를 접근 가능하는지 체크함
	    		if(responseCode == 200) {
	    			String containerName = "owasp_" + usersDto.getName();
	    			OwaspContainer owaspcontainer = service.getUserOwaspContainer(containerName);
	    			int owaspContainerPort = 5555;
	    			
	    			
	    			//해당하는 유저는 Owasp docker를 가지고 있는지 아닌지 체크함
	    			//존제하지 않으면 Docker SDK를 실행하고 Container를 올리고  정보를 디비에서 저장함
	    			//만약에 있으면 restart
	    			if(owaspcontainer == null) {			
	    				ExposedPort expose = ExposedPort.tcp(8080); 
	    				Ports portBindings = new Ports();
	    				portBindings.bind(expose, Ports.Binding.bindPort(owaspContainerPort));
	    				
	    				CreateContainerResponse ctn = dockerClient.createContainerCmd("owaspzap:latest")
    						.withName("owasp_" + userName)
    			           	.withExposedPorts(expose)
    			            .withPortBindings(portBindings).exec();
	    				
	    				dockerClient.startContainerCmd(ctn.getId()).exec();
	    				Thread.sleep(4000);
	    				//컨테이너 정상적으로 생성함
	    				if(ctn.getId() != null) {
	    					OwaspContainerDto owasp = new OwaspContainerDto(ctn.getId(), usersDto.getUserId(), "owasp_" + userName, owaspContainerPort);
	    					owaspcontainer = modelMapper.map(owasp, OwaspContainer.class);
	    					service.insertUserOwaspContainer(owasp);
	    				}
	    			}else {
	    				dockerClient.restartContainerCmd(owaspcontainer.getContainerId());
		    			Thread.sleep(4000);
	    			}
	    			
	    			
	    			Collection<String> ctnName = new ArrayList<String>() {{ add("/owasp_"+ userName); }};
	     			List<Container> containers = dockerClient.listContainersCmd()
	     					  .withShowAll(true)
	     					  .withNameFilter(ctnName)
	     					  .exec();
	     			
	     			//컨테이너 정상적으로 올림
	     			if(containers.size() != 0 && !containers.get(0).getId().equals("")) {
	     				Service zapapi = new Service();
	     				result = Service.runActiveScanRules(checkUrl, typeReport, owaspContainerPort);
	     				
	     				//출력된 결과를 json 파일을 저장함
			    		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
			    		LocalDateTime now = LocalDateTime.now();
			    		String fileName = dtf.format(now);
			    		
			    		projectService.insertHistoryDiagnosis(projectId, userId, fileName);
			    		writeFile(project.getProjectUserId(), project.getProjectPath(),fileName, result);				    		  	    	
			    			
			    		return ResponseEntity.status(HttpStatus.CREATED).body(result);
	    			}else {
	    				return ResponseEntity.status(HttpStatus.CREATED).body("fail");
	    			}
	    		}else {
	    			System.out.println("URL 접근 오류");
	    			return ResponseEntity.status(HttpStatus.CREATED).body("fail");
	    		}
				
			} catch (Exception e) {
				System.out.println("진단 과정이 실패" + e);
				return ResponseEntity.status(HttpStatus.CREATED).body("fail");
			}
		} catch (Exception e) {
			 System.out.println("프로젝트 진단 오류" + e);
			 return ResponseEntity.status(HttpStatus.CREATED).body("fail");
		}
	}
	@RequestMapping("/result_dynamic-analyzers")
	 public ResponseEntity getHistoryAnalyzers(
	            @RequestBody DynamicAnalyzerModel dynamicAnalyzerModel
	            ,HttpServletResponse response
	    ) {
		try {
			String userId = dynamicAnalyzerModel.getUserId();
			String projectId = dynamicAnalyzerModel.getProjectId();			
			String typeReport = dynamicAnalyzerModel.getTypereport();
			
			List<ProjectDiagnosis> listDiagnosis= service.getProjectDiagnosis(projectId);
			
			 Project returnValue = new Project();
	    	 returnValue = projectService.getProjectById(projectId);
	    	 
			//진단 파일 읽어서 전달
			for(int i = 0; i < listDiagnosis.size(); i++) {
				String result = readFile("HISTORY"+ "/" + userId + "/" + returnValue.getProjectName() + "/" + listDiagnosis.get(i).getPath() + ".json");
				listDiagnosis.get(i).setResult(result);
			}
			Project project = projectService.getProjectById(projectId);
			
           return ResponseEntity.status(HttpStatus.CREATED).body(listDiagnosis);
			
		} catch (Exception e) {
			 System.out.println("프로젝트 진단 History" + e);
			 return ResponseEntity.status(HttpStatus.CREATED).body("fail");
			// TODO: handle exception
		}
	}
	public String readFile(String path) throws FileNotFoundException {
		  File myObj = new File(path);
	      Scanner myReader = new Scanner(myObj);
	      String data = "";
	      while (myReader.hasNextLine()) {
	        data += myReader.nextLine();
	      }
	      myReader.close();
	      return data;
	}
	public void writeFile(String user, String project, String fileNameTime, String value){
	    String userPath = "HISTORY/" + user; 
	    String fileName = fileNameTime + ".json";

	    String directoryName = "";
	    File directory = new File(userPath);
	    
	    File directoryProject = null;

	    //대당하는 유저 폰더 전재하지 않을 경우에
	    if (!directory.exists()){
	        directory.mkdir();
	        String projectPath = userPath + "/" + project;
	        directoryProject = new File(projectPath);
	        if (!directoryProject.exists())
	        {
	        	directoryProject.mkdir();
	        }
	    }else {
	    	String projectPath = userPath + "/" + project;
	        directoryProject = new File(projectPath);
	        if (!directoryProject.exists())
	        {
	        	directoryProject.mkdir();
	        }
	    }
	    

	    File file = new File(directoryProject + "/" + fileName);
	    try{
	        FileWriter fw = new FileWriter(file.getAbsoluteFile());
	        BufferedWriter bw = new BufferedWriter(fw);
	        bw.write(value);
	        bw.close();
	    }
	    catch (IOException e){
	        e.printStackTrace();
	        System.exit(-1);
	    }
	}
}
