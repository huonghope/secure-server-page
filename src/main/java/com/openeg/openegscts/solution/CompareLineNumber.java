package com.openeg.openegscts.solution;

import com.openeg.openegscts.student.dto.CompareDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class CompareLineNumber {

    public boolean compareLimeNumber(CompareDto compareDto, CompareDto getLineNumData) {
        boolean compareResult;
        String DBLineNum = getLineNumData.getScodeLineNum();
        String ClientLineNum = compareDto.getScodeLineNum();

        DBLineNum = DBLineNum.replaceAll(" " , ""); // 1,2,3
        DBLineNum = DBLineNum.replaceAll("\\p{Z}", ""); // 1,2,3

        ClientLineNum = ClientLineNum.replaceAll(" " , ""); // 1,2
        ClientLineNum = ClientLineNum.replaceAll("\\p{Z}", ""); // 1,2

        String[] DBLineNumArray = DBLineNum.split(",");
        String[] ClientLineNumArray = ClientLineNum.split(",");

        List<String> DBLineNumList = new ArrayList<>();
        List<String> ClientLineNumList = new ArrayList<>();

        for (int i = 0; i < DBLineNumArray.length; i++) {
            DBLineNumList.add(DBLineNumArray[i]);
        }

        for (int i = 0; i < ClientLineNumArray.length; i++) {
            ClientLineNumList.add(ClientLineNumArray[i]);
        }

        compareResult = ClientLineNumList.equals(DBLineNumList);
        return compareResult;

    }
}
