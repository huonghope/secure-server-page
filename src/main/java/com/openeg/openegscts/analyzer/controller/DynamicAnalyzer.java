package com.openeg.openegscts.analyzer.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.openeg.openegscts.analyzer.service.Service;
import com.openeg.openegscts.student.model.DynamicAnalyzerModel;
import com.openeg.openegscts.student.model.LoginRequestModel;

@CrossOrigin(origins = "*")
@Controller
public class DynamicAnalyzer {
	
	@RequestMapping("/dynamic-analyzer")
	 public ResponseEntity ServerProcess(
	            @RequestBody DynamicAnalyzerModel dynamicAnalyzerModel
	            ,HttpServletResponse response
	    ) {
		try {
			String target = dynamicAnalyzerModel.getUrl();
			String option = dynamicAnalyzerModel.getOption();
			String typeReport = dynamicAnalyzerModel.getTypereport();
			String result = "";
			
			System.out.println(option);
			
			Service zapapi = new Service();
            
            result = zapapi.runActiveScanRules(target, typeReport);
            System.out.println(result);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
			
		} catch (Exception e) {
			 return ResponseEntity.status(HttpStatus.CREATED).body("failed");
			// TODO: handle exception
		}
	}
}
