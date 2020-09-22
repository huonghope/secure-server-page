package com.openeg.openegscts.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    Environment env;
    private final long MAX_AGE_SECS = 3600;

    @Autowired
    public WebMvcConfig(Environment env) {
        this.env = env;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations(env.getProperty("save.pdf.file"), env.getProperty("save.video.file"));
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(env.getProperty("front.host"))
                .allowedMethods(HttpMethod.POST.name(), HttpMethod.GET.name(), HttpMethod.PUT.name(), HttpMethod.DELETE.name(), HttpMethod.OPTIONS.name())
                .allowedHeaders("Authorization", "Access-Control-Allow-Origin", "Access-Control-Allow-Headers")
                .allowCredentials(true)
                .exposedHeaders("token", "Authorization", "Access-Control-Allow-Origin", "Access-Control-Allow-Headers")
                .maxAge(MAX_AGE_SECS);
    }

}