package org.zerock.club.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.zerock.club.security.handler.ClubLoginSuccessHandler;

@Configuration
@EnableWebSecurity
@Log4j2
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
//    public InMemoryUserDetailsManager userDetailsService() {
//        UserDetails user = User.builder()
//                .username("user1")
//                .password(passwordEncoder().encode("1111"))
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(user);
//    }
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
//
//        log.info("build Auth global........");
//
//        auth.inMemoryAuthentication()
//                .withUser("user1")
//                .password((new BCryptPasswordEncoder()).encode("1111"))
//                .roles("USER");
//    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth->{
            auth.requestMatchers("/sample/all").permitAll()
                    .requestMatchers("/login").permitAll()
//                    .requestMatchers("/logout").permitAll()
                    .requestMatchers("/sample/member").hasRole("USER")
                    .requestMatchers("/sample/mustLogin").authenticated()
                    .anyRequest().permitAll();
        });
        http.formLogin(form -> {
            form.defaultSuccessUrl("/sample/all");
//            form.loginPage("/login");
        });
        http.csrf(csrf->csrf.disable());
        http.logout(logout -> {});
        http.oauth2Login(oauth2->oauth2.successHandler(clubLoginSuccessHandler()));
        http.rememberMe(rm->rm.tokenValiditySeconds(60*60*24*7)); // 7일
        return http.build();
    }
    @Bean
    public ClubLoginSuccessHandler clubLoginSuccessHandler() {
        return new ClubLoginSuccessHandler(passwordEncoder());
    }
}
