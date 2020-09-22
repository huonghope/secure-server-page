package com.openeg.openegscts.solution;

import com.openeg.openegscts.student.dto.CompareDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CompareCode {

    public boolean compareCode(CompareDto compareDto, CompareDto getKeywordData) {
        boolean compareResult;

        String DBKeyword = getKeywordData.getScodeKeyword();
        String ClientCode = compareDto.getScodeSecCode();

        DBKeyword = replaceDB(DBKeyword);
        ClientCode = replaceClient(ClientCode);
        DBKeyword = replaceSpace(DBKeyword);

        String[] split = DBKeyword.split(",");
        StringBuilder pattern = new StringBuilder("^");

        for(int i = 0; i < split.length; i++) {
            pattern.append(".*" + split[i]);
        }

        pattern.append(".*");
        compareResult = ClientCode.matches(pattern.toString());

        return compareResult;
    }

    public static String replaceDB(String db) {
        String returnValue = db.replaceAll("\\[", "\\\\\\\\\\\\[").replaceAll("\\]", "\\\\\\\\\\\\]");
        returnValue = returnValue.replaceAll("\\(", "\\\\(").replaceAll("\\)", "\\\\)");
        returnValue = returnValue.replaceAll("\\\"", "\\\\\"");
        return returnValue;
    }

    public static String replaceClient(String client) {
        String returnValue = client.replaceAll("\\[", "\\\\[").replaceAll("\\]", "\\\\]");
        returnValue = returnValue.replaceAll("\\\"", "\\\\\"");
        return returnValue;
    }

    public static String replaceSpace(String rs) {
        String returnValue = rs.replaceAll("/", ",");
        returnValue = returnValue.replaceAll(" ", "");
        returnValue = returnValue.replaceAll("\\p{Z}", "");
        return returnValue;
    }

}
