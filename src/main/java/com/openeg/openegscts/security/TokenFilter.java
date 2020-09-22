package com.openeg.openegscts.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/token")
public class TokenFilter {

    Environment env;

    public TokenFilter(Environment env) {
        this.env = env;
    }

    @GetMapping("/con")
    private String getAuthenticationToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String authorizationHeader =
                request.getHeader(env.getProperty("authorization.token.header.name")); // 헤더 값 가져오기

        if (authorizationHeader == null) { // or not exists token info > 초기값 사용
            return null;
        }

        String token = authorizationHeader.replace(env.getProperty("authorization.token.header.prefix"),
                ""
        ); // "" > 빈 값이라 지워지게 됨

        String userId = "";

        try {
            userId = Jwts.parser()
                    .setSigningKey(env.getProperty("token.secret"))
                    .parseClaimsJws(token.trim())
                    .getBody()
                    .getSubject();

            if(userId == null) {
                return null;
            }
        } catch (SignatureException | MalformedJwtException e) {
            response.sendError(401, "SignatureException Error!");
        } catch (ExpiredJwtException e) {
            response.sendError(403, "Expired Jwt Token!");
        }

        return userId;
    }

}
