package com.webshoprest.api.v1.config;

import com.webshoprest.api.v1.security.*;
import com.webshoprest.api.v1.services.impl.UserSecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true
)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserAuthenticationEntryPoint userAuthenticationEntryPoint;
    private GlobalAuthenticationEntryPoint globalEntryPoint;
    private BCryptPasswordEncoder passwordEncoder;
    private UserSecurityService userSecurityService;
    private JwtTokenProvider jwtTokenProvider;

    private final AntPathRequestMatcher userAuthEntryPointPath = new AntPathRequestMatcher(SecurityConstants.USER_AUTH_PATH);
    private final AntPathRequestMatcher globalEntryPointPaths = new AntPathRequestMatcher("/api/v1/**");

    @Autowired
    public SecurityConfig(UserAuthenticationEntryPoint userAuthenticationEntryPoint, GlobalAuthenticationEntryPoint globalEntryPoint, BCryptPasswordEncoder passwordEncoder, UserSecurityService userSecurityService, JwtTokenProvider jwtTokenProvider) {
        this.userAuthenticationEntryPoint = userAuthenticationEntryPoint;
        this.globalEntryPoint = globalEntryPoint;
        this.passwordEncoder = passwordEncoder;
        this.userSecurityService = userSecurityService;
        this.jwtTokenProvider = jwtTokenProvider;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()
                .exceptionHandling()
                .defaultAuthenticationEntryPointFor(userAuthenticationEntryPoint, userAuthEntryPointPath)
                .and()
                .exceptionHandling()
                .defaultAuthenticationEntryPointFor(globalEntryPoint, globalEntryPointPaths)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .headers().frameOptions().sameOrigin()   // to enable h2
                .and()
                .authorizeRequests()
                .antMatchers(SecurityConstants.PUBLIC_PATHS).permitAll()
                .antMatchers(SecurityConstants.H2_URL).permitAll()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated();

        http
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userSecurityService).passwordEncoder(passwordEncoder);
    }

    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter();
    }
}
