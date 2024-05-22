package com.hello.forum.beans.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.hello.forum.beans.security.handler.LoginFailureHandler;
import com.hello.forum.beans.security.handler.LoginSuccessHandler;
import com.hello.forum.beans.security.jwt.JwtAuthenticationFilter;
import com.hello.forum.beans.security.oauth2.OAuthService;
import com.hello.forum.member.dao.MemberDao;

/**
 * <pre>
 * Spring Security 의 전반적인 설정이 이루어지는 곳.
 * 
 * Spring Security에 필요한 Bean의 생성.
 * Spring Security의 보안정책을 설정.
 * </pre>
 */
@Configuration
@EnableWebSecurity // Spring Security Filter
public class SecurityConfig {
	
	@Autowired
	private MemberDao memberDao;
	
	@Autowired
	private JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Autowired
	private OAuthService oauthService;
	
	/**
	 * 사용자 세부정보 서비스에 대한 Spring Bean 생성.
	 * 
	 * @return SecurityUserDetailsService의 bean
	 */
	@Bean
	UserDetailsService userDetailsService() {
		return new SecurityUserDetailsService(memberDao);
	}
	
	/**
	 * 암호 인코더에 대한 Spring Bean 생성
	 * 
	 * @return SecuritySHA의 bean
	 */
	@Bean
	PasswordEncoder passwordEncoder() {
		return new SecuritySHA();
	}
	
	
	/**
	 * <pre>
	 * Spring Security가 절대 개입하지 말아야 하는 URL들을 정의.
	 * 아래 URL에서 보여지는 페이지 내부에서는 Security Taglib을 사용할 수 없다.
	 * </pre>
	 * @return
	 */
	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring()
						   .requestMatchers(AntPathRequestMatcher.antMatcher("/WEB-INF/views/**"))
						   // CSRF 적용을 위해 Security 설정 필요.
//						   .requestMatchers(AntPathRequestMatcher.antMatcher("/member/login"))
//						   .requestMatchers(AntPathRequestMatcher.antMatcher("/member/regist/**"))
						   .requestMatchers(AntPathRequestMatcher.antMatcher("/error/**"))
						   .requestMatchers(AntPathRequestMatcher.antMatcher("/favicon.ico"))
						   .requestMatchers(AntPathRequestMatcher.antMatcher("/member/**-delete-me"))
						   .requestMatchers(AntPathRequestMatcher.antMatcher("/js/**"))
						   .requestMatchers(AntPathRequestMatcher.antMatcher("/css/**"));
						   
	}
	
	/**
	 * SPring Security Filter가 동작해야할 방식(순서)를 정의
	 * 
	 * @param http HttpSecurity 필터 전략
	 * @return SpringSecurityFilterChain Spring Security가 동작해야할 순서
	 * @throws Exception
	 */
	@Bean
	SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		
		// 로그인(필터) 정책 설정.
		// 우리 애플리케이션은 Form 기반으로 로그인을 하며
		// 로그인이 완료되면, /board/search로 이동을 해야한다.
		// 로그인 페이지 변경.
		http.formLogin(formLogin -> formLogin
											 // Spring Security 인증이 성공할 경우, LoginSuccessHandler가 동작되도록 설정.
											 .successHandler(new LoginSuccessHandler())
											 // Spring Security 인증이 실패할 경우, LoginFailureHandler가 동작되도록 설정
											 .failureHandler(new LoginFailureHandler())
											 // SpringSecurity Login URL 변경
											 .loginPage("/member/login")
											 // SpringSecurity Login 처리 URL 변경
											 // SecurityAuthenticationProvider 실행 경로 지정
											 .loginProcessingUrl("/member/login-proc")
											 .usernameParameter("email")
											 .passwordParameter("password"));
		
		http.authorizeHttpRequests(httpRequest ->
					// /board/search는 인증 여부와 관계없이 모두 접근이 가능하다.
					httpRequest.requestMatchers(AntPathRequestMatcher.antMatcher("/board/search"))
							   .permitAll() // /board/search는 Security 인증 여부와 관계없이 접근 허용한다.
							   .requestMatchers(AntPathRequestMatcher.antMatcher("/ajax/menu/list"))
							   .permitAll() // /ajax/menu/list는 Security 인증 여부와 관계없이 접근 허용한다.
							   .requestMatchers(AntPathRequestMatcher.antMatcher("/member/login"))
							   .permitAll()
							   .requestMatchers(AntPathRequestMatcher.antMatcher("/member/regist/**"))
							   .permitAll()
							   .requestMatchers(AntPathRequestMatcher.antMatcher("/ajax/member/regist/available"))
							   .permitAll()
							   .requestMatchers(AntPathRequestMatcher.antMatcher("/auth/token"))
							   .permitAll()
							   .requestMatchers(AntPathRequestMatcher.antMatcher("/board/excel/download2"))
							   .hasRole("ADMIN")
							   .requestMatchers(AntPathRequestMatcher.antMatcher("/ajax/board/delete/massive"))
							   .hasRole("ADMIN")
							   .requestMatchers(AntPathRequestMatcher.antMatcher("/ajax/board/excel/write"))
							   .hasRole("ADMIN")
							   // 그외 모든 URL은 인증이 필요하다.
							   .anyRequest()
							   .authenticated());
		
		http.oauth2Login(auth -> auth.defaultSuccessUrl("/board/search", true)
									 .userInfoEndpoint(user -> user.userService(oauthService))
									 .loginPage("/member/login"));
		
		
		// CSRF 방어로직 무효화.
		// 아래 코드가 없을 경우, HTTP POST, PUT, DELETE 등은 정상 동작하지 않는다.
//		http.csrf(csrf -> csrf.disable());
		
		// /auth/token URL에서는 CSRF 검사를 하지 않음.
		http.csrf(csrf -> csrf.ignoringRequestMatchers(AntPathRequestMatcher.antMatcher("/auth/token"),
													   AntPathRequestMatcher.antMatcher("/api/**")));
		
		http.addFilterAfter(jwtAuthenticationFilter, BasicAuthenticationFilter.class);
		
		http.cors(cors -> {
			// CORS 설정
			CorsConfigurationSource source = (request) -> {
				CorsConfiguration config = new CorsConfiguration();
				config.setAllowedOrigins(List.of("http://localhost:3000"));
				config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
				config.setAllowedHeaders(List.of("*"));
				return config;
			};
			
			cors.configurationSource(source);
		});
		
		return http.build();
	}
	
}
