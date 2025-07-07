package com.mycoffeemap;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration		//스프링의 환경 설정 파일임을 의미. 스프링 시큐리티를 설정하기 위해 사용
@EnableWebSecurity	//모든 요청 URL이 스프링 시큐리티의 제어를 받도록 만드는 애너테이션
public class SecurityConfig {
	
    @Bean
    PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
    
    
    
  	//로그인하지 않더라도 모든 페이지에 접근할 수 있도록 설정
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	
    	//HttpSecurity객체의 authorizeHttpRequests()메소드 : HTTP요청에 대한 접근 제어 및 보안설정을 구성 	
    	http.authorizeHttpRequests( auth -> 
    								auth.requestMatchers(new AntPathRequestMatcher("/**")) 
    								//requestMatchers() : URL 패턴 기반으로 접근 정책 지정
    								//AntPathRequestMatcher("/**") : 모든 경로에 대한 요청을 누구나 접근할수 있도록 허용
    								.permitAll() )
    								//permitAll() : 해당 요청 경로에 대해 인증 없이 접근 허용
    								.headers( headers ->
    								//응답 헤더 설정 - X-Frame-Options 보안 헤더 추가
	    								headers.addHeaderWriter(
	    										new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)) )
	    										//X-Frame-Options: 페이지가 다른 사이트의 iframe에 삽입되지 않도록 설정
	    										//브라우저에서 클릭재킹(clickjacking) 공격을 방지하는 데 사용되는 표준 보안 헤더
	    										//기본값은 DENY이며, 어떤 사이트도 해당 페이지를 iframe에 포함시킬 수 없음
	    										//SAMEORIGIN: 같은 출처(origin)의 문서만 iframe으로 포함할 수 있도록 허용
    								.formLogin( formLogin -> formLogin.loginPage("/user/login")
    								//사용자 인증 처리 설정 - 로그인 기능(form-based authentication)
    								//loginPage(): 인증되지 않은 사용자가 보호된 자원에 접근 시 리디렉션할 로그인 페이지 URL 설정
										.defaultSuccessUrl("/"))
    									//defaultSuccessUrl(): 로그인 성공 후 이동할 기본 경로 설정
    								.logout( logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))
    								//로그아웃 처리 설정
    								//logoutRequestMatcher(): 로그아웃 처리를 트리거할 경로 설정 (POST 방식)
										.logoutSuccessUrl("/")
										//logoutSuccessUrl(): 로그아웃 성공 시 리디렉션할 경로 설정]
										.invalidateHttpSession(true)
										//invalidateHttpSession(true): 로그아웃 시 HttpSession 객체를 완전히 무효화 (세션 내 사용자 정보 및 인증 객체 삭제)				    								
		    						);
    
    	return http.build();

    }
    

    
    
    
    
    
}
