package ar.edu.itba.paw.webapp.config;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import ar.edu.itba.paw.webapp.auth.PlatformUrlAuthenticationSuccessHandler;
import ar.edu.itba.paw.webapp.auth.PlatformUserDetailsService;

@Configuration
@EnableWebSecurity
@ComponentScan("ar.edu.itba.paw.webapp.auth")
public class WebAuthConfig extends WebSecurityConfigurerAdapter {
	
    @Autowired
    private PlatformUserDetailsService userDetailsService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
	
	@Value("classpath:rememberme.key")
    private Resource key; //org.springframework.core.io

    @Bean
    public DaoAuthenticationProvider getDaoAuth() {
        DaoAuthenticationProvider ap = new DaoAuthenticationProvider();
        ap.setUserDetailsService(userDetailsService);
        ap.setPasswordEncoder(passwordEncoder);
        return ap;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(getDaoAuth());
    }
    
    private String getRememberMeKey() {
        final StringWriter writer = new StringWriter();
        try (Reader reader = new InputStreamReader(key.getInputStream())) {
            char[] data = new char[1024];
            int len;
            while ((len = reader.read(data)) != -1) {
                writer.write(data,0,len);
            }
        }
        catch (IOException e){
            throw new RuntimeException(e);
        }
        return writer.toString();
    }
    
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.userDetailsService(userDetailsService)
                .sessionManagement()
                .invalidSessionUrl("/login")
                .and().authorizeRequests()
                .antMatchers(HttpMethod.DELETE).access("hasRole('ROLE_ADMIN')")
                .antMatchers("/user/create").permitAll()
                .antMatchers("/user/*").access("hasRole('ROLE_USER')")
                .antMatchers("/login").permitAll()
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/event/**", "/events/**").access("hasRole('ROLE_USER')")
                .antMatchers("/","/index").permitAll()
                .and().formLogin()
                .usernameParameter("login_username")
                .passwordParameter("login_password")
                .successHandler(platformAuthenticationSuccessHandler())
                .failureUrl("/login?error=true")
                .loginPage("/login")
                .and().rememberMe()
                .rememberMeParameter("login_remember_me")
                .userDetailsService(userDetailsService)
                .key(getRememberMeKey())
                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(30))
                .and().logout().logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .and().exceptionHandling()
                .accessDeniedPage("/403")
                .and().csrf().disable();
    }
    
    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/css/**", "/js/**", "/img/**",
                        "/favicon.ico", "/403", "/404", "/oops");
    }
    
    @Bean
    public AuthenticationSuccessHandler platformAuthenticationSuccessHandler(){
        return new PlatformUrlAuthenticationSuccessHandler();
    }
    
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
