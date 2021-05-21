package com.neutron.im.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neutron.im.core.ResultVO;
import com.neutron.im.service.AccountService;
import com.neutron.im.util.StringUtil;
import com.neutron.im.util.TokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * SpringSecurity 的配置
 *
 * @since 2020/5/28 15:14
 */
@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(securedEnabled = true)
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    /**
     * 需要放行的URL
     * other public endpoints of your API may be appended to this array
     */
    public static final String[] AUTH_WHITELIST = {"/login", "/register", "/", "/captcha-pic", "/captcha-email"};

    private final AccountService accountService;
    private final PasswordEncoder encoder;

    @Autowired
    public SpringSecurityConfig(AccountService service, PasswordEncoder passwordEncoder) {
        accountService = service;
        encoder = passwordEncoder;
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(accountService).passwordEncoder(encoder);
    }

    /**
     * 配置请求拦截
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//            .cors()
//            //由于使用的是JWT，我们这里不需要csrf
//            .and()
            .csrf().disable()
            //基于token，所以不需要session
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
            .and()
            .authorizeRequests()
            //可以匿名访问的链接
            .antMatchers(AUTH_WHITELIST).permitAll()
            .antMatchers(new String[]{
                "/**/*.png", "/**/*.jpg", "/**/*.JPG", "/**/*.PNG", "/**/*.svg", "/**/*.SVG", "/**/*.jpeg", "/**/*.JPEG"
            }).permitAll()
            //其他所有请求需要身份认证
            .anyRequest().authenticated()
            .and()
            .addFilter(new JWTAuthenticationFilter(authenticationManager()));
    }

    public static class JWTAuthenticationFilter extends BasicAuthenticationFilter {
        private final ObjectMapper mapper = new ObjectMapper();

        public JWTAuthenticationFilter(AuthenticationManager manager) {
            super(manager);
        }

        @Override
        protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
            String origin = request.getHeader("origin");
            String allowOrigin = "http://localhost:3000";
            response.setHeader("Access-Control-Allow-Origin", !StringUtil.isEmpty(origin) ? origin : allowOrigin);
            response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
            response.setHeader("Access-Control-Max-Age", "3600");
            response.setHeader("Access-Control-Allow-Credentials", "true");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With, Connection, Upgrade");
//            String referer = request.getHeader("Referer");
//            String host = request.getHeader("Host");
//            String userAgent = request.getHeader("User-Agent");

            //            logger.info("protocol: " + request.getProtocol());

            //            || StringUtil.isEmpty(referer)
//            if (StringUtil.isEmpty(host) || StringUtil.isEmpty(userAgent)) {
//                doResponse(
//                    response,
//                    403,
//                    ResultVO.failed(40300, "浏览器核验失败", null)
//                );
//                return;
//            }

            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                doResponse(
                    response,
                    200,
                    null
                );
                return;
            }

            // Step 1: skip url that is in allow list
            var url = request.getRequestURI();
            if (SpringSecurityConfig.AUTH_WHITELIST != null && Arrays.asList(SpringSecurityConfig.AUTH_WHITELIST).contains(url)) {
                chain.doFilter(request, response);
                return;
            }
            if (url.matches(".+(.jpeg|.JPEG|.jpg|.JPG|.png|.PNG|.svg|.SVG)$")) {
                chain.doFilter(request, response);
                return;
            }

            // Step 2: check if token is existed
            String authToken = request.getHeader(TokenUtil.AUTHORIZATION);

            // if token is empty
            if (authToken == null || authToken.equals("")) {
                Cookie[] cookies = request.getCookies();
                if (cookies != null)
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().equals(TokenUtil.AUTHORIZATION)) {
                            authToken = cookie.getValue();
                        }
                    }
            }

            if (authToken == null || authToken.equals("")) {
                doResponse(response, 401, ResultVO.failed(40100, "Empty Token", null).setError("Unauthorized"));
                return;
            }

            // Step 3: validate token
            try {
                TokenUtil.JwtClaimsData tokenData = TokenUtil.validateTokenWithPrefix(authToken);
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                    tokenData.getId(), null, new ArrayList<>()
                ));
                request.setAttribute("claims", tokenData);

                // 重新签发 jwt
                response.addCookie(new Cookie(
                    TokenUtil.AUTHORIZATION,
                    TokenUtil.generateTokenWithPrefix(tokenData)
                ) {{
                    setMaxAge(TokenUtil.EXPIRATION_TIME_IN_SECOND);
                    setPath("/");
                }});

                chain.doFilter(request, response);
            } catch (ExpiredJwtException e) {
                doResponse(
                    response, 401,
                    ResultVO.failed(40100, "Token已过期", null).setError("Unauthorized")
                );
                logger.error("Token已过期: {} " + e);
            } catch (UnsupportedJwtException e) {
                doResponse(response, 401,
                    ResultVO.failed(40100, "Token格式错误", null).setError("Unauthorized")
                );
                logger.error("Token格式错误: {} " + e);
            } catch (MalformedJwtException e) {
                doResponse(response, 401,
                    ResultVO.failed(40100, "Token没有被正确构造", null).setError("Unauthorized")
                );
                logger.error("Token没有被正确构造: {} " + e);
            } catch (SignatureException e) {
                doResponse(response, 401,
                    ResultVO.failed(40100, "签名失败", null).setError("Unauthorized")
                );
                logger.error("签名失败: {} " + e);
            } catch (IllegalArgumentException e) {
                doResponse(response, 401,
                    ResultVO.failed(40100, "Token非法参数异常", null).setError("Unauthorized")
                );
                logger.error("非法参数异常: " + e);
            } catch (Exception e) {
                doResponse(response, 401,
                    ResultVO.failed(40100, "Invalid Token", null).setError("Unauthorized")
                );
                logger.error("Invalid Token " + e.getMessage());
            }
        }

        private void doResponse(HttpServletResponse response, int status, Object content) throws IOException {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.setStatus(status);
            response.getWriter().write(
                mapper.writeValueAsString(content)
            );
        }
    }
}
