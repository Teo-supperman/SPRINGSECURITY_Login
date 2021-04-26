package com.company.security.config;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.company.service.implement.Account_Service_Implement;

//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Security_Config extends WebSecurityConfigurerAdapter {
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private Account_Service_Implement account_Service_Implement;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		auth.authenticationProvider(authenticationProvider());
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setPasswordEncoder(passwordEncoder);
		authenticationProvider.setUserDetailsService(account_Service_Implement);
		return authenticationProvider;
	}
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.authorizeRequests().antMatchers("/").permitAll();
		http.authorizeRequests().antMatchers("management/api/").hasRole("ADMIN").anyRequest().authenticated();
		//http.formLogin();
			http.formLogin()
			.loginPage("/login")
		  	.permitAll()
		  	.defaultSuccessUrl("/source",true);
		 	//.passwordParameter("password")
		 	//.usernameParameter("username") ;
		  	/* .and() 
	 		.rememberMe()
	 		.tokenValiditySeconds((int)TimeUnit.DAYS.toSeconds(1)).key("somethingverysecured")
	 		.rememberMeParameter("remember-me") 
	  .and() 
	  		.logout() .logoutUrl("/logout")
	  		.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST"))
	  		.clearAuthentication(true).invalidateHttpSession(true)
	  		.deleteCookies("JSESSIONID", "remember-me") .logoutSuccessUrl("/login"); 	*/
	}

}
