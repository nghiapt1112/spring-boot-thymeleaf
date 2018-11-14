package com.lyna.web.security;

import com.lyna.web.domain.user.service.impl.UserDetailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static final String[] STATIC_RESOURCES = new String[]{"/resources/**"};

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Autowired
    private UserDetailServiceImpl userDetailService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(STATIC_RESOURCES).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").permitAll()
                .successForwardUrl("/index")
                .and()
                .logout().permitAll().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login")
                .and()
                .sessionManagement().invalidSessionUrl("/invalidSession")
                .maximumSessions(1).sessionRegistry(sessionRegistry()).and()
                .sessionFixation().none()
        ;
    }

    /**
     * TODO: => NghiaPT using customAuthenService to login from DB.
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService);
//        auth.inMemoryAuthentication()
//                .withUser("user")
//                .password(passwordEncoder().encode("password"))
//                .roles("USER")
//                .and()
//                .withUser("admin")
//                .password(passwordEncoder().encode("admin"))
//                .roles("ADMIN");
    }

}
