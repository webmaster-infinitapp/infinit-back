package com.paypro.wallet.wallet.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.paypro.wallet.wallet.security.CustomerUserDetailsService;
import com.paypro.wallet.wallet.security.JWTAuthenticationFilter;
import com.paypro.wallet.wallet.security.JWTLoginFilter;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled=true)
public class CustomUserDetailsSecurityConfig extends WebSecurityConfigurerAdapter {
	
    @Bean
    public CustomerUserDetailsService userDetails() {
        return new CustomerUserDetailsService();
    }
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    	CustomerUserDetailsService userDetailsService = userDetails();
        auth.authenticationProvider( userDetailsService);
    }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {

    	http.csrf().disable().authorizeRequests()
        .antMatchers("/").permitAll()
        .antMatchers(HttpMethod.POST, "/login").permitAll()
        .anyRequest().authenticated()
        .and()
        //We filter the api/login requests
        .addFilterBefore(new JWTLoginFilter("/login", authenticationManager()),
                UsernamePasswordAuthenticationFilter.class)
        //And filter other requests to check the presence of JWT in header
        .addFilterBefore(new JWTAuthenticationFilter(),
                UsernamePasswordAuthenticationFilter.class);
    	
    }
}