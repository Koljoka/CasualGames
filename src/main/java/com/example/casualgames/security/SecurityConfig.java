package com.example.casualgames.security;

import com.example.casualgames.view.LoginView;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


//Spring Security konfiguraatio
@Configuration
public class SecurityConfig {

    //Määritetään sovelluksen security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        //Vaadinin oma security configurer
        http.with(VaadinSecurityConfigurer.vaadin(), config -> {
            config.loginView(LoginView.class);
            config.enableRequestCacheConfiguration(false);
        });

        return http.build();
    }

    //Salasanojen enkoodaus
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}