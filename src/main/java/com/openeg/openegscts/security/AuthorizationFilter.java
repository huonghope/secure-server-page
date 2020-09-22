package com.openeg.openegscts.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class AuthorizationFilter extends BasicAuthenticationFilter {

    Environment env;

    public AuthorizationFilter(AuthenticationManager authenticationManager, Environment env) {
        super(authenticationManager);
        this.env = env;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException, SignatureException, ExpiredJwtException, MalformedJwtException {


        String authorizationHeader = request.getHeader(
                env.getProperty("authorization.token.header.name")
        );

        String access = request.getHeader(
                env.getProperty("access.header.name")
        );

        if(authorizationHeader == null || !authorizationHeader.startsWith(env.getProperty("authorization.token.header.prefix"))) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication =
                getAuthentication(request, response);

        if(authentication == null) {
            response.setHeader("Access-Control-Allow-Origin", "*");
            return;
        }

        response.setHeader("Access-Control-Allow-Origin", "*");
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request, HttpServletResponse response) throws IOException, SignatureException, ExpiredJwtException, MalformedJwtException {

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
            return null;
        } catch (ExpiredJwtException e) {
            response.sendError(403, "Expired Jwt Token!");
            return null;
        }

        return new UsernamePasswordAuthenticationToken(userId, null, new ArrayList<>());
    }
}
