package com.openeg.openegscts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class OpenegApplication {

    Environment env;

    @Autowired
    public OpenegApplication(Environment env) {
        this.env = env;
    }

    public static void main(String[] args) {
        SpringApplication.run(OpenegApplication.class, args);
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // crossOrigin을 위해 추가
    @Bean
    public FilterRegistrationBean FilterRegistrationBean(){
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList(env.getProperty("front.host")));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Accept", "Content-Type", "Origin", "XSRF-TOKEN", "X-XSRF-TOKEN", "X-Requested-With", "Access-Control-Allow-Origin", "referrer", "Access-Control-Allow-Headers"));
        config.addAllowedHeader("Access-Control-Allow-Origin, referrer, Access-Control-Allow-Headers");
        config.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE"));
        config.addAllowedMethod("*");
        List<String> allowHeaderList = Arrays.asList("userId", "token", "Content-Type", "Access-Control-Allow-Orgin",
                "x-xsrf-token", "Origin", "Accept", "X-Requested-With", "Access-Control-Max-Age",
                "Access-Control-Allow-Methods", "Access-Control-Allow-Headers", "Access-Control-Request-Method", "Access-Control-Request-Headers");
        config.setExposedHeaders(allowHeaderList);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }
}
