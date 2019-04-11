package com.davidgjm.oss.wechat.config;

import com.davidgjm.oss.wechat.auth.WxAuthFilter;
import com.davidgjm.oss.wechat.auth.WxUserManagementService;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {


    @Bean
    public FilterRegistrationBean<WxAuthFilter> wxAuthFilter(WxUserManagementService wxUserManagementService) {
        FilterRegistrationBean<WxAuthFilter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new WxAuthFilter(wxUserManagementService));
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

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring().antMatchers(HttpMethod.POST, "/api/v1/wx/session");
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
