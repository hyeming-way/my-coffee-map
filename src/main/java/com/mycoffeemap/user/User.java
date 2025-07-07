package com.mycoffeemap.user;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

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
	
	@Column(length = 255, nullable = false)
	private String pass;
	
	@Column(length = 20, nullable = false)
	private String nick;

	@Column(unique = true ,length = 100, nullable = false)
	private String email;
	
	@Column(length = 255)
	private String profileImg;
	
	private String verificationToken;	//이메일 인증용 토큰 저장
	
	private LocalDateTime tokenExpiry;	//토큰 만료일 저장
	
	@Column(nullable = false)
	private boolean enabled = false;	//메일 인증 여부
	
	@CreationTimestamp
	private Timestamp joinDate;
	
}
