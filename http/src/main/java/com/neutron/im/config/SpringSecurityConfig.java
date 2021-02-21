package com.neutron.im.config;

import com.neutron.im.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * SpringSecurity 的配置
 *
 * @since 2020/5/28 15:14
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 需要放行的URL
     * other public endpoints of your API may be appended to this array
     */
    public static final String[] AUTH_WHITELIST = {"/login", "/register", "/"};

    private final AccountService accountService;

    @Autowired
    public SpringSecurityConfig(AccountService service) {
        accountService = service;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService).passwordEncoder(new BCryptPasswordEncoder());
    }

    /**
     * 配置请求拦截
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and()
                //由于使用的是JWT，我们这里不需要csrf
                .csrf().disable()
                //基于token，所以不需要session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                //可以匿名访问的链接
                .antMatchers(AUTH_WHITELIST).permitAll()
                //其他所有请求需要身份认证
                .anyRequest().authenticated()
                .and()
                .addFilter(new JWTLoginFilter(authenticationManager()))
                .addFilter(new JWTAuthenticationFilter(authenticationManager()));
    }

    public static class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {
        AuthenticationManager manager;

        public JWTLoginFilter(AuthenticationManager manager) {
            this.manager = manager;
        }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

            System.out.println("remote host:" + request.getRemoteHost());
            chain.doFilter(request, response);
        }
    }

    public static class JWTAuthenticationFilter extends BasicAuthenticationFilter {
        public JWTAuthenticationFilter(AuthenticationManager manager) {
            super(manager);
        }
    }
}
