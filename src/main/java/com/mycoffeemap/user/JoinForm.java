package com.mycoffeemap.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class JoinForm {
	
	//@NotNull : null만 막음. 값이 비어 있어도 OK
	//@NotEmpty : null + 빈 값 모두 막음
	//@NotBlank : null + 빈 문자열 + 공백 문자열 전부 ❌

	@Email
	@NotBlank
	private String email;
	
	@NotBlank
	@Size(min = 8, max = 20)
	private String pass;
	
	@NotBlank
	private String passCheck;
	
	@NotBlank
    @Size(min = 2, max = 10)
    @Pattern(regexp = "^[\\u4E00-\\u9FFF\\u3040-\\u309F\\u30A0-\\u30FF\\u30FCa-zA-Z0-9]{2,10}$")
	private String nick;
	
}
