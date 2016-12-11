package br.garca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;


public class WebApp {

    @Autowired
    private ApplicationContext context;

    @Bean
    public ServletRegistrationBean restApi() {
        return null;
    }

    static public void main(String[] args) throws Exception {
        SpringApplication.run(WebApp.class,args);
    }
}
