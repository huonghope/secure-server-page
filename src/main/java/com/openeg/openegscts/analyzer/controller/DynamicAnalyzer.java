package com.openeg.openegscts.analyzer.controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Dictionary;
import java.util.List;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.openeg.openegscts.analyzer.service.Service;
import com.openeg.openegscts.file.FileSaveC;
import com.openeg.openegscts.student.dto.ProjectDiagnosisDto;
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
	@Autowired
    public DynamicAnalyzer(IUsersService service, IProjectService projectService, FileSaveC fileSave) {
        this.fileSave = fileSave;
        this.service = service;
        this.projectService = projectService;
    }
	
	@RequestMapping("/dynamic-analyzer")
	 public ResponseEntity ServerProcess(
	            @RequestBody DynamicAnalyzerModel dynamicAnalyzerModel
	            ,HttpServletResponse response
	    ) {
		try {
			String userId = dynamicAnalyzerModel.getUserId();
			String projectId = dynamicAnalyzerModel.getProjectId();			
			String option = dynamicAnalyzerModel.getOption();
			String typeReport = dynamicAnalyzerModel.getTypereport();
			
			
			String result = "";
			Project project = projectService.getProjectById(projectId);
		
			Service zapapi = new Service();
            
			// 해당하는 프로젝트 Id를 부터 url를 출력해서 밑에 target로 바꿔주시면 됩니다.
            result = zapapi.runActiveScanRules("https://public-firing-range.appspot.com/redirect/index.html", typeReport);
            //save result to file

            
            
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    	    LocalDateTime now = LocalDateTime.now();
    	    String fileName = dtf.format(now);
    	    
    	    System.out.println(projectId + userId + fileName);
    	    service.insertHistoryDiagnosis(projectId, userId, fileName);
            writeFile(project.getProjectUserId(), project.getProjectPath(),fileName, result);
          
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
			
		} catch (Exception e) {
			 System.out.println("프로젝트 진단 오류" + e);
			 return ResponseEntity.status(HttpStatus.CREATED).body("failed");
			// TODO: handle exception
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
			System.out.println(listDiagnosis.size());
			
			 Project returnValue = new Project();
	    	 returnValue = projectService.getProjectById(projectId);
	    	 
			//진단 파일 읽어서 전달
			for(int i = 0; i < listDiagnosis.size(); i++) {
				System.out.println(listDiagnosis.get(i).getPath());
				System.out.println("HISTORY"+ "/" + userId + "/" + returnValue.getProjectName() + "/" + listDiagnosis.get(i).getPath()  + ".json");
				String result = readFile("HISTORY"+ "/" + userId + "/" + returnValue.getProjectName() + "/" + listDiagnosis.get(i).getPath() + ".json");
				
				listDiagnosis.get(i).setResult(result);
			}
			Project project = projectService.getProjectById(projectId);
			
			
//          return null;C
           return ResponseEntity.status(HttpStatus.CREATED).body(listDiagnosis);
			
		} catch (Exception e) {
			 System.out.println("프로젝트 진단 오류" + e);
			 return ResponseEntity.status(HttpStatus.CREATED).body("failed");
			// TODO: handle exception
		}
	}
	public String readFile(String path) throws FileNotFoundException {
		  File myObj = new File(path);
	      Scanner myReader = new Scanner(myObj);
	      String data = "";
	      while (myReader.hasNextLine()) {
	        data += myReader.nextLine();
	        System.out.println(data);
	      }
	      myReader.close();
	      return data;
	}
	public void writeFile(String user, String project, String fileNameTime, String value){
	    String userPath = "HISTORY/" + user; 
	    String fileName = fileNameTime + ".json";

	    String directoryName = "";
	    File directory = new File(userPath);
	    System.out.println(userPath);
	    
	    File directoryProject = null;
	    if (! directory.exists()){
	        directory.mkdir();
	        String projectPath = userPath + "/" + project;
	        directoryProject = new File(projectPath);
	        if (! directoryProject.exists())
	        {
	        	directoryProject.mkdir();
	        }
	    }else {
	    	
	    }
	    if(directoryProject == null) 
	    	directoryProject = new File(userPath + "/" + project);

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
