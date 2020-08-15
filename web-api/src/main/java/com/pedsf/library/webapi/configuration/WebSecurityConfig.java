package com.pedsf.library.webapi.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
   @Autowired
   private UserDetailsService userDetailsService;

   @Bean
   public BCryptPasswordEncoder bCryptPasswordEncoder() {
      return new BCryptPasswordEncoder();
   }

   @Override
   protected void configure(HttpSecurity http) throws Exception {
      http
         .authorizeRequests()
            .antMatchers("/resources/**", "/login", "/registration", "/index", "/static/images/**","/user/**", "/**" )
            .permitAll()
            .anyRequest()
            .authenticated()
            .and()
         .formLogin()
            .loginPage("/login")
            .usernameParameter("email")
            .permitAll()
            .and()
         .csrf()
         .ignoringAntMatchers()//don't apply CSRF
            .and()
         .headers()
            .frameOptions()
            .sameOrigin()
            .and()
         .logout()
            .logoutUrl("/logout")
            .permitAll();
   }

   @Bean
   public AuthenticationManager customAuthenticationManager() throws Exception {
      return authenticationManager();
   }

   @Autowired
   public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
      auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
   }

}