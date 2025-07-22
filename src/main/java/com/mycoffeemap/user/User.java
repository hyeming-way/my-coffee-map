package com.mycoffeemap.user;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(length = 20)
	private String socialType;     //소셜 로그인 정보 구분 (GOOGLE, LINE)

	@Column(length = 100)
	private String socialId;   //소셜 아이디 (Google의 sub, Line의 userId)

	@Column(length = 255, nullable = true) //소셜 로그인의 경우 패스워드가 없을 수 있으니 true로 변경
	private String pass;

	@Column(unique = true, length = 20, nullable = false)
	private String nick;

	@Column(unique = true, length = 100, nullable = false)
	private String email;

	@Column(length = 255)
	private String profileImg;

	private String verificationToken;	//이메일 인증용 토큰 저장

	private LocalDateTime tokenExpiry;	//토큰 만료일 저장

	@Column(nullable = false)
	private boolean enabled = false;	//메일 인증 여부

	@CreationTimestamp
	private Timestamp joinDate;

	@Column(nullable = false)
	private boolean deleted = false;	//탈퇴여부

	private LocalDateTime deletedDate;	//탈퇴일자

}
