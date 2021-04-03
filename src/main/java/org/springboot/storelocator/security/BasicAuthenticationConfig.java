package org.springboot.storelocator.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true) // Enable @PreAuthorize method-level security
@ConditionalOnProperty(name = "store.authentication.enabled", havingValue = "true")
public class BasicAuthenticationConfig extends WebSecurityConfigurerAdapter {

    
    @Autowired
	UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .authorizeRequests()
                .anyRequest()
                    .authenticated()
                    .and()
                .httpBasic()
                    .and()
                .csrf()
                    .disable();
        // @formatter:on
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // @formatter:off
        auth.userDetailsService(userDetailsService);
        // @formatter:on
    }
}
