package com.finconsgroup.performplus.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.finconsgroup.performplus.manager.security.CustomAuthenticationProvider;
import com.finconsgroup.performplus.manager.security.jwt.JwtAuthenticationTokenFilter;
import com.finconsgroup.performplus.service.business.security.LocalUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfiguration {

	@Autowired
	private CustomAuthenticationProvider authenticationProvider;

	public void configureAuthentication(AuthenticationManagerBuilder authenticationManagerBuilder,
			UserDetailsService userDetailService) throws Exception {
		authenticationManagerBuilder.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
		authenticationManagerBuilder.authenticationProvider(authenticationProvider);
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
		return new JwtAuthenticationTokenFilter();
	}

	@Bean
	UserDetailsService userDetailService() throws Exception {
		return new LocalUserDetailsService();
	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
		final CorsConfiguration cors = new CorsConfiguration();
		cors.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));
		cors.setAllowedOrigins(List.of("*"));
		cors.setAllowedHeaders(List.of("*"));
		cors.setMaxAge(360l);

		httpSecurity.cors(c->corsConfigurationSource());
		httpSecurity.csrf(c->c.disable());

		httpSecurity.sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		httpSecurity.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class);

		httpSecurity.authorizeHttpRequests(c -> c.requestMatchers("/api/auth/login").permitAll()
				.requestMatchers("/api/auth/login-sso").permitAll()
				.requestMatchers("/api/auth/logout-sso").permitAll()
				.requestMatchers("/api/auth/logintkn").permitAll()
				.requestMatchers("/api/utente/login").permitAll().requestMatchers("/api/auth/enti").permitAll()
				.requestMatchers("/api/cruscotto/*/anni").permitAll().requestMatchers("/api/report/print").permitAll()
				.requestMatchers("/api/cube/**").permitAll().requestMatchers("/actuator/health").permitAll()
				.requestMatchers("/api/swagger-ui.html").permitAll().requestMatchers("/swagger-ui-custom.html")
				.permitAll().requestMatchers("/v3/swagger.json").permitAll().requestMatchers("/v3/api-docs.yaml")
				.permitAll().requestMatchers("/api-docs.yaml").permitAll().requestMatchers("/swagger.json").permitAll()
				.requestMatchers("/swagger-ui.html").permitAll().requestMatchers("/swagger-ui/index.html").permitAll()
				.requestMatchers("/swagger-ui/**").permitAll()
				.requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
				.requestMatchers("/error").permitAll()
				.anyRequest().authenticated());

		httpSecurity.headers(h -> h.cacheControl());

		return httpSecurity.build();
	}

	@Bean   
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*")); //or add * to allow all origins
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH", "HEAD"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); 
        return source; 
    }
	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> {
			web.ignoring().requestMatchers("/v3/api-docs/**");
			web.ignoring().requestMatchers("/api-docs/**");
			web.ignoring().requestMatchers("/swagger-ui/**");
			web.ignoring().requestMatchers("/swagger.json");
			web.ignoring().requestMatchers("/v3/swagger.json");
			web.ignoring().requestMatchers("/swagger-ui.html");
			web.ignoring().requestMatchers("/swagger-ui-custom.html");
			web.ignoring().requestMatchers("/api/swagger-ui.html");
			web.ignoring().requestMatchers("/swagger-resources/**");
			web.ignoring().requestMatchers("/webjars/**");
			web.ignoring().requestMatchers("/configuration/ui");
			web.ignoring().requestMatchers("/configuration/security");
		};

	}

}