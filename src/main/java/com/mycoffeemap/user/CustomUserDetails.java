package com.mycoffeemap.user;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Getter;

//Spring Security가 로그인 이후 사용할 인증된 사용자 정보를 담는 객체
@Getter
public class CustomUserDetails implements UserDetails, OAuth2User {
										//UserDetails : 일반 로그인 지원용
										//OAuth2User : 소셜 로그인 지원용

    private final User user;

    //소셜에서 넘겨주는 원본 사용자 정보(JSON 형태)
    private Map<String, Object> attributes;

    //일반 로그인 생성자
    public CustomUserDetails(User user) { this.user = user; }

    //소셜 로그인 생성자
    public CustomUserDetails(User user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    //OAuth2User
    @Override
    public Map<String, Object> getAttributes() { return attributes; } //소셜 로그인한 사용자 원본 정보 반환
    @Override
    public String getName() { return user.getNick(); } // 유저 닉네임 반환

    //UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> "ROLE_USER"); //권한(ROLE) 정보. 기본적으로 모든 유저는 ROLE_USER로 설정
    }

    @Override
    public String getPassword() { return user.getPass(); } //비밀번호 해시값

    @Override
    public String getUsername() { return user.getEmail(); } //로그인 ID로 사용

    @Override
    public boolean isAccountNonExpired() { return true; } //계정 만료 로직 없으면 true

    @Override
    public boolean isAccountNonLocked() { return true; } //잠금 처리 안하면 true

    @Override
    public boolean isCredentialsNonExpired() { return true; } //비밀번호 유효기간 관리 안하면 true

    @Override
    public boolean isEnabled() { return user.isEnabled(); } // 이메일 인증 여부

}