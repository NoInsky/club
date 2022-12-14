package org.zerock.club.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration
       .WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zerock.club.handler.ApiLoginFailHandler;
import org.zerock.club.handler.ClubLoginSuccessHandler;
import org.zerock.club.security.filter.ApiCheckFilter;
import org.zerock.club.security.filter.ApiLoginFilter;
import org.zerock.club.security.service.ClubUserDetailsService;
import org.zerock.club.security.util.JWTUtil;

import lombok.extern.log4j.Log4j2;

@Configuration
@Log4j2
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

@Autowired
private ClubUserDetailsService userDetailsService;

    @Bean
    PasswordEncoder passwordEncoder(){
       return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        
      //  http.authorizeRequests()
      //      .antMatchers("/sample/all").permitAll()
      //      .antMatchers("/sample/member").hasRole("USER");
           
       http.formLogin();
       http.csrf().disable();
       http.logout();
       
       http.oauth2Login().successHandler(successHandler());
       http.rememberMe().tokenValiditySeconds(60*60*24*7)
           .userDetailsService(userDetailsService);

           http.addFilterBefore(apiCheckFilter(), 
           UsernamePasswordAuthenticationFilter.class);
           http.addFilterBefore(apiLoginFilter(), 
           UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public ApiLoginFilter apiLoginFilter() throws Exception{

        ApiLoginFilter apiLoginFilter = new ApiLoginFilter("/api/login",jwtUtil());
        apiLoginFilter.setAuthenticationManager(authenticationManager());

        apiLoginFilter
              .setAuthenticationFailureHandler(new ApiLoginFailHandler());

        return apiLoginFilter;
    }

    @Bean
    public JWTUtil jwtUtil(){
      return new JWTUtil();
    }

    @Bean
    public ApiCheckFilter apiCheckFilter(){
      return new ApiCheckFilter("/notes/**/*", jwtUtil());
    }

    @Bean
    public ClubLoginSuccessHandler successHandler(){
      return new ClubLoginSuccessHandler(passwordEncoder());
    }



   //  @Override
   //  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        
   //     auth.inMemoryAuthentication().withUser("user1")
   //     .password("$2a$10$oxyqca7jxAw3Hwnqb5Catedu1I/.rnRmZUO.q9ZhaJEwqXBQcOTv.")
   //     .roles("USER");
   //  }
}
