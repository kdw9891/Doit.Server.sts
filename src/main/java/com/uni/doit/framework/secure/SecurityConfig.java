//package com.datasignal.mooroc.framework.secure;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    protected SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf().disable() // CSRF 비활성화
//            .authorizeRequests(auth -> auth
//                .antMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/webjars/**").permitAll()
//                .anyRequest().authenticated()
//            )
//            .formLogin(form -> form
//                .permitAll()
//            )
//            .httpBasic();
//
//        return http.build();
//    }
//}
