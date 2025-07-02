package com.mycoffeemap.user;

import java.util.Date;


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
	
	@Column(length = 200, nullable = false)
	private String pass;
	
	@Column(length = 200, nullable = false)
	private String nick;
	
	@Column(length = 200, nullable = false)
	private String name;
	
	@Column(nullable = false)
	private Date birth;
	
	@Column(unique = true ,length = 200, nullable = false)
	private String email;
	
	@Column(length = 200)
	private String profileImg;
	
	@Column(length = 200)
	private String address;
		
}
