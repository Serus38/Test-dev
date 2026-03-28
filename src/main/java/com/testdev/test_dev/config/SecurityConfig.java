package com.testdev.test_dev.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final List<String> allowedOrigins;

	/**
	 * Inyecta el filtro JWT para anexarlo antes del filtro de autenticacion de Spring.
	 */
	public SecurityConfig(
		JwtAuthenticationFilter jwtAuthenticationFilter,
		@Value("${CORS_ALLOWED_ORIGINS:http://localhost:4200,http://localhost:8081}") String allowedOriginsConfig
	) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
		this.allowedOrigins = Arrays.stream(allowedOriginsConfig.split(","))
			.map(String::trim)
			.filter(origin -> !origin.isEmpty())
			.collect(Collectors.toList());
	}

	/**
	 * Encoder estandar para contrasenas; se usa en escenarios con usuarios persistidos.
	 */
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * Configura seguridad stateless con JWT y define rutas publicas/protegidas.
	 */
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
			.cors(Customizer.withDefaults())
			.csrf(csrf -> csrf.disable())
			.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(auth -> auth
				.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
				.requestMatchers(
					"/v3/api-docs/**",
					"/api-docs/**",
					"/swagger-ui/**",
					"/swagger-ui.html",
					"/actuator/health",
					"/auth/login",
					"/auth/register",
					"/auth/validate"
				).permitAll()
				.requestMatchers("/client/**", "/bodega/**", "/port/**", "/terrestrial-shipment/**", "/maritime-shipment/**")
				.authenticated()
				.anyRequest().authenticated()
			)
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	/**
	 * Define CORS para frontend local y habilita metodos habituales de la API.
	 */
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(allowedOrigins);
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
		configuration.setExposedHeaders(Arrays.asList("Authorization"));
		configuration.setAllowCredentials(true);
		configuration.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}
