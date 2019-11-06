package lorablockchain.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private final String username = "user";
	private final String password = "password";
	
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
        	.withUser(username)
            .password("{noop}"+password)
            .roles("USER");
    }
    
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	
        http.csrf().disable().authorizeRequests().antMatchers("/**").permitAll().and().httpBasic();
    }
}
