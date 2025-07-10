package com.mycoffeemap.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

	//사용자 본인 인증 토큰값 조회
	Optional<User> findByVerificationToken(String token);

	//로그인 이메일 조회
	User findByEmailAndDeletedFalse(String email);

	//이메일 중복 검사
	boolean existsByEmail(String email);

	//닉네임 중복 검사
	boolean existsByNick(String nick);


	
}
