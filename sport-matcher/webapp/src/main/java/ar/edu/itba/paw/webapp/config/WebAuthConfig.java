package ar.edu.itba.paw.webapp.config;

import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import ar.edu.itba.paw.webapp.auth.PlatformUrlAuthenticationSuccessHandler;
import ar.edu.itba.paw.webapp.auth.PlatformUserDetailsService;
import ar.edu.itba.paw.webapp.auth.StatelessFilter;

@Configuration
@EnableWebSecurity
@ComponentScan("ar.edu.itba.paw.webapp.auth")
public class WebAuthConfig extends WebSecurityConfigurerAdapter {
	
    @Autowired
    private PlatformUserDetailsService userDetailsService;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private StatelessFilter sf;

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
    
    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        http.userDetailsService(userDetailsService)
        		.authorizeRequests()
	                .antMatchers(HttpMethod.DELETE).access("hasRole('ROLE_ADMIN')")
	                .antMatchers("/user/create").permitAll()
	                .antMatchers("/user/**").access("hasRole('ROLE_USER')")
	                .antMatchers("/login").permitAll()
	                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
	                .antMatchers("/event/*/join", "/event/*/leave", "/event/*/upvote", "/event/*/downvote",
	                		"/event/*/kick-user/*", "/event/*/delete").access("hasRole('ROLE_USER')")
	                .antMatchers("/event/**", "/events/**").permitAll()
	                .antMatchers("/club/*/comment").access("hasRole('ROLE_USER')")
	                .antMatchers("/club/**", "/clubs/**").permitAll()
	                .antMatchers("/pitch/*/event/create").access("hasRole('ROLE_USER')")
	                .antMatchers("/pitch/**", "/pitches/**").permitAll()
	                .antMatchers("/my-events/**").access("hasRole('ROLE_USER')")
	                .antMatchers("/history/**").access("hasRole('ROLE_USER')")
	                .antMatchers("/tournament/*/team/*/join", 
	                		"/tournament/*/leave").access("hasRole('ROLE_USER')")
	                .antMatchers("/tournament/**", "/tournaments/**").permitAll()
	                .antMatchers("/home").permitAll()
	                .antMatchers("/","/index").permitAll()
	                // POST method restricted unless overridden above, like /user/create/
	                .antMatchers(HttpMethod.POST).access("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
                .and().sessionManagement()
                	.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	            .and().formLogin()
	                .usernameParameter("login_username")
	                .passwordParameter("login_password")
	                .loginProcessingUrl("/users/login")
	                .successHandler(platformAuthenticationSuccessHandler())
	                .failureHandler(new SimpleUrlAuthenticationFailureHandler())
                .and().logout().logoutUrl("/logout").logoutSuccessUrl("/")
                .and().exceptionHandling()
                	.authenticationEntryPoint(new Http403ForbiddenEntryPoint())
                .and().csrf()
                	.disable().addFilterBefore(sf, UsernamePasswordAuthenticationFilter.class);
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
    
    public String tokenSecretKey() {
    	return Base64.getEncoder().encodeToString(
    			"E176EA76D237D19395AD4FBA2B605B82A9BCFB4F8ECBFE7E06C237889BB64EFA".getBytes());
    }

}
