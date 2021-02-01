package com.security.jwt.config;
import com.security.jwt.security.FilterSkipMatcher;
import com.security.jwt.security.HeaderTokenExtractor;
import com.security.jwt.security.filters.FormLoginFilter;
import com.security.jwt.security.filters.JwtAuthenticationFilter;
import com.security.jwt.security.handlers.FormLoginAuthenticationFailureHandler;
import com.security.jwt.security.handlers.FormLoginAuthenticationSuccessHandler;
import com.security.jwt.security.providers.FormLoginAuthenticationProvider;
import com.security.jwt.security.providers.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //시큐리티 메서드@PreAuthorize등을 사용할수있음
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final HeaderTokenExtractor headerTokenExtractor;

    private final FormLoginAuthenticationSuccessHandler formLoginAuthenticationSuccessHandler;
    private final FormLoginAuthenticationFailureHandler formLoginAuthenticationFailureHandler;
    private final FormLoginAuthenticationProvider formLoginAuthenticationProvider;

    protected FormLoginFilter formLoginFilter() throws Exception {
        FormLoginFilter formLoginFilter = new FormLoginFilter("/api/tokens", formLoginAuthenticationSuccessHandler, formLoginAuthenticationFailureHandler);
        formLoginFilter.setAuthenticationManager(super.authenticationManagerBean());
        return formLoginFilter;
    }

    protected JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        FilterSkipMatcher matcher = new FilterSkipMatcher(Arrays.asList
                ( "/api/members/test", "/api/tokens", "/api/members/new"),//허용 url
                "/api/**"); //비허용 url
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(matcher, headerTokenExtractor);
        jwtAuthenticationFilter.setAuthenticationManager(super.authenticationManagerBean());
        return jwtAuthenticationFilter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .authenticationProvider(formLoginAuthenticationProvider)
                .authenticationProvider(jwtAuthenticationProvider);
    }

    /**
     *  시큐리티 설정
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic().disable()    // security 에서 기본으로 생성하는 login 페이지 사용 안 함
                .csrf().disable()    // csrf 사용 안 함 == REST API 사용하기 때문에
                .headers().frameOptions().disable()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)// JWT 인증 사용하므로 세션 사용안함
                .and()
                .addFilterBefore(formLoginFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
