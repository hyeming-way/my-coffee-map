package com.mycoffeemap.user;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JoinForm {

	@NotEmpty
	private String email;
	
	@NotEmpty
	private String pass;
	
	@NotEmpty
	private String passCheck;
	
	@NotEmpty
	private String nick;
	
}
