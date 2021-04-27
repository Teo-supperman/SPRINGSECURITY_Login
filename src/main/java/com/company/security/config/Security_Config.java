package com.company.security.config;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.company.security.config.JWT.AuthenticationFilter;
import com.company.security.config.JWT.JsonWebToken;
import com.company.security.config.JWT.TokenVerifier;
import com.company.service.implement.Account_Service_Implement;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Security_Config extends WebSecurityConfigurerAdapter {
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private Account_Service_Implement account_Service_Implement;
	@Autowired
	private SecretKey secretKey;
	@Autowired
	private JsonWebToken jsonWebToken;

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

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {

		// Sét đặt dịch vụ để tìm kiếm User trong Database.
		// Và sét đặt PasswordEncoder.
		auth.userDetailsService(account_Service_Implement);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.addFilter(new AuthenticationFilter(authenticationManager(), jsonWebToken, secretKey))
				.addFilterAfter(new TokenVerifier(secretKey, jsonWebToken), AuthenticationFilter.class);

		http.authorizeRequests().antMatchers("/").permitAll();
		http.authorizeRequests().antMatchers("management/api/").hasAnyRole("ROLE_ADMIN").anyRequest().authenticated();

		http.formLogin(); /*
							 * http.formLogin().loginPage("/login").permitAll().defaultSuccessUrl("/source",
							 * true) .passwordParameter("password").usernameParameter("username");
							 * http.rememberMe().tokenValiditySeconds((int)
							 * TimeUnit.DAYS.toSeconds(14)).key("somethingverysecured")
							 * .rememberMeParameter("remember-me");
							 * http.logout().logoutUrl("/logout").logoutRequestMatcher(new
							 * AntPathRequestMatcher("/logout", "GET")) // VI DANG DUNG CSRF DISABLE
							 * .clearAuthentication(true).invalidateHttpSession(true).deleteCookies(
							 * "JSESSIONID", "remember-me") .logoutSuccessUrl("/login");
							 */

	}

}
