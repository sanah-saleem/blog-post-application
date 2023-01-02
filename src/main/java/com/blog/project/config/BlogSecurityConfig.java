package com.blog.project.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.blog.project.servive.CustomUserDetailService;


@Configuration
@EnableWebSecurity
public class BlogSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private CustomUserDetailService customUserDetailService;
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        	.csrf().disable()
            .authorizeRequests()
            .antMatchers("/login","/register","/saveUser","/","/Dashboard","/viewPost/{id}","/searchPost","/searchPost/{pageNo}","/page/{pageNo}")
            .permitAll()
            .antMatchers("/**").hasAnyRole("ADMIN","USER")
            .anyRequest()
            .authenticated()
            .and()
//            .httpBasic(); 
            .formLogin()
            .loginPage("/login")
            .loginProcessingUrl("/doLogin")
            .defaultSuccessUrl("/Dashboard");
    }

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(customUserDetailService).passwordEncoder(passwordEncoder());
	}
	
	@Bean
	protected BCryptPasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder(10);
	}
	
}
