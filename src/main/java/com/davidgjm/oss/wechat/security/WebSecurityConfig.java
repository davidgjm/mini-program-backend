package com.davidgjm.oss.wechat.security;

import com.davidgjm.oss.wechat.security.filters.WxAuthenticationFilter;
import com.davidgjm.oss.wechat.services.WxUserManagementService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {



    @Bean
    public FilterRegistrationBean<WxAuthenticationFilter> wxAuthenticationFilter(WxUserManagementService wxUserManagementService) {
        FilterRegistrationBean<WxAuthenticationFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new WxAuthenticationFilter(wxUserManagementService));
        filterRegistrationBean.addUrlPatterns("/api/v1/*");
        return filterRegistrationBean;
    }

    @Configuration
    @Order(200)
    public class WxWebSecurity extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                    .antMatcher("/api/v1/wx/**")
                    .authorizeRequests().anyRequest().permitAll()
                    .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                        .logout().disable()
                        .csrf().disable()
                        .headers().frameOptions().disable()
            ;
            // @formatter:on
        }
    }

    @Configuration
    @Order(300)
    public class MajorProjectWebSecurity extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http.antMatcher("/api/v1/admin/**")
                    .authorizeRequests().anyRequest().permitAll()
//                    .anyRequest().permitAll()
                    .and()
                        .csrf().disable()
                        .headers().frameOptions().disable()
            ;
            // @formatter:on
        }
    }


    @Configuration
    @Order(500)
    public class DefaultWebSecurity extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            // @formatter:off
            http
                    .authorizeRequests()
                        .antMatchers("/api/v1/test/**").permitAll()
                        .antMatchers("/v2/api-docs/**","/swagger**/**", "/webjars/**").permitAll()
                        .antMatchers("/", "/h2-console/**", "/error**", "/actuator/*").permitAll()
                    .and()
                        .antMatcher("/**").authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                        .csrf().disable()
                        .logout().disable()
                        .headers().frameOptions().disable()
            ;
            // @formatter:on
        }
    }

}
