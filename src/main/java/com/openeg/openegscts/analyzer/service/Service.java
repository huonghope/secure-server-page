package com.openeg.openegscts.analyzer.service;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ApiResponseElement;
import org.zaproxy.clientapi.core.ApiResponseList;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiException;


public class Service {

    private static final String ZAP_ADDRESS = "localhost"; //210.94.194.81
    private static final int ZAP_PORT = 8081; //
    
    // Change to match the API key set in ZAP, or use NULL if the API key is disabled
    private static final String ZAP_API_KEY = "3343";
    
    // The URL of the application to be tested
    private static String TARGET = "https://public-firing-range.appspot.com";  //default target
    
    public static String runActiveScanRules(String url, String resultType)
    {
    	ClientApi api = new ClientApi(ZAP_ADDRESS, ZAP_PORT, ZAP_API_KEY);
    	TARGET = url;
    	String result = new String("");
    	
        try {
            // Start spidering the target
            System.out.println("Spidering target : " + TARGET);
            ApiResponse resp = api.spider.scan(TARGET, null, null, null, null);
            String scanID;
            int progress;

            // The scan returns a scan id to support concurrent scanning
            scanID = ((ApiResponseElement) resp).getValue();
            // Poll the status until it completes
            while (true) {
                Thread.sleep(1000);
                progress = Integer.parseInt(((ApiResponseElement) api.spider.status(scanID)).getValue());
                System.out.println("Spider progress : " + progress + "%");
                if (progress >= 100) {
                    break;
                }
            }
            System.out.println("Spider completed");
            // If required post process the spider results
            //List<ApiResponse> spiderResults = ((ApiResponseList) api.spider.results(scanID)).getItems();
            //for(int i = 0; i < spiderResults.size(); i++)
            //{
            //	result += spiderResults.get(i) + "<br>";
            //	System.out.println(spiderResults.get(i));
            //}
            
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
            e.printStackTrace();
        }
        try {
            System.out.println("Active Scanning target : " + TARGET);
            ApiResponse resp = api.ascan.scan(TARGET, "True", "False", null, null, null);
            String scanid;
            int progress;

            // The scan now returns a scan id to support concurrent scanning
            scanid = ((ApiResponseElement) resp).getValue();
            // Poll the status until it completes
            while (true) {
                Thread.sleep(1000);
                progress =
                        Integer.parseInt(
                                ((ApiResponseElement) api.ascan.status(scanid)).getValue());
                System.out.println("Active Scan progress : " + progress + "%");
                if (progress >= 100) {
                    break;
                }
            }

            System.out.println("Active Scan complete");
            //System.out.println("Alerts:");
            //System.out.println(new String(api.core.jsonreport(), StandardCharsets.UTF_8));
            // Print vulnerabilities found by the scanning
            
            result = new String(api.core.jsonreport(), StandardCharsets.UTF_8);
            System.out.println("Active result" + result);
            // if you need json format -> api.core.jsonreport()
            // if you need html format -> api.core.htmlreport()
            // if you need xml format -> api.core.xmlreport()
            
            //System.out.println(result);
               
        } catch (Exception e) {
            System.out.println("Exception : " + e.getMessage());
            e.printStackTrace();
        }
        // if you need passive scan,
        // use below code
        // try {
        //     System.out.println("Passive Scan target : " + TARGET);
        //     ApiResponse resp = api.pscan.enableAllScanners();
        //     String scanid;
            
        //     //String progress;

        //     // The scan now returns a scan id to support concurrent scanning
        //     scanid = ((ApiResponseElement) resp).getValue();
            
        //   //iterating till we get response as "0".
        //     while(!resp.toString().equals("0")) {
        //     	resp =	api.pscan.recordsToScan();
    	//     }
            
        //     System.out.println("Passive scan completed!");
        //     // Print vulnerabilities found by the scanning
        //     System.out.println("Alerts:");
        //     System.out.println(new String(api.core.jsonreport(), StandardCharsets.UTF_8));
            
        //     switch (resultType) {
		// 		case "html":
		// 			result = new String(api.core.htmlreport(), StandardCharsets.UTF_8);
		// 			break;
		// 		case "json":
		// 			result = new String(api.core.jsonreport(), StandardCharsets.UTF_8);
		// 			break;
		// 		case "xml":
		// 			result = new String(api.core.xmlreport(), StandardCharsets.UTF_8);
		// 			break;
		// 		default:
		// 			result = "현재 이 형식을 구현 아직 안 됩니다.";
		// 			break;
		// 	}

        // } catch (Exception e) {
        //     System.out.println("Exception : " + e.getMessage());
        //     e.printStackTrace();
        // }

		return result;
    }
//    public static void main(String[] args) {
//    	String result = runActiveScanRules("https://public-firing-range.appspot.com", "html");
//    	System.out.println(result);
//	}
}
