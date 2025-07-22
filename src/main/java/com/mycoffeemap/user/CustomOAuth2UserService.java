package com.mycoffeemap.user;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

//소셜 로그인 로직 처리
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    final private UserService userService;

    //소셜 로그인시 호출되는 메소드
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    										   //userRequest : OAuth2 로그인 요청 정보가 담김

    	//기본 OAuth2UserService로부터 사용자 정보 받아옴
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        //어떤 소셜 로그인인지 식별
        String registrationId = userRequest.getClientRegistration().getRegistrationId(); //google, 추후 추가될 소셜 타입

        //프로필 정보 추출
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String socialId;
        String email = null;
        String name = null;

        if ("google".equalsIgnoreCase(registrationId)) {
        	socialId = (String) attributes.get("sub");
            email = (String) attributes.get("email");
            name = (String) attributes.get("name");
        } else {
            throw new OAuth2AuthenticationException("지원하지 않는 소셜 로그인 제공자: " + registrationId);
        }

        //UserService 메소드 호출해 DB에 이미 등록된 사용자면 조회, 아니면 신규 회원 등록 처리
        User user = userService.findOrCreateSocialUser(
            registrationId.toUpperCase(), socialId, email, name
        );

        //스프링 시큐리티 UserDetails 구현체
        //인증 완료된 사용자 정보와 권한 담아서 반환 -> 시큐리티 컨텍스트에 저장됨
        return new CustomUserDetails(user, attributes);

    }
}
