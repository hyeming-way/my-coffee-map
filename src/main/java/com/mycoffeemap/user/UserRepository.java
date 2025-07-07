package com.mycoffeemap.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

	//사용자 본인 인증 토큰값 조회
	Optional<User> findByVerificationToken(String token);
	
	
}
