package com.openeg.openegscts.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openeg.openegscts.student.model.LoginRequestModel;
import com.openeg.openegscts.student.service.IUsersService;
import com.openeg.openegscts.student.dto.UsersDto;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private IUsersService userService;
    private Environment env;

    @Autowired
    public AuthenticationFilter(IUsersService userService, Environment env, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.env = env;
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestModel creds = new ObjectMapper()
                    .readValue(request.getInputStream(), LoginRequestModel.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            new ArrayList<>()
                    )
            );
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

        String email = ((User)authResult.getPrincipal()).getUsername();
        UsersDto usersDto = userService.getUserDetailsByEmail(email);

        String token = Jwts.builder()
                        .setSubject(usersDto.getEmail())
                        .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(env.getProperty("token.expiration_time"))))
                        .signWith(SignatureAlgorithm.HS512, env.getProperty("token.secret"))
                        .compact();

        response.addHeader("token", token);
        response.addHeader("userId", usersDto.getUserId());

    }
}