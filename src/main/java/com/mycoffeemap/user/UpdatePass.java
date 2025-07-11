package com.mycoffeemap.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdatePass {

	@NotBlank
	@Size(min = 8, max = 20)
	private String pass;
	
	@NotBlank
	private String passCheck;
	
}
