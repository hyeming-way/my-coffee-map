package com.mycoffeemap.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//프로필 사진 리소스 핸들링
@Configuration
public class WebConfig implements WebMvcConfigurer {
	
/*	
	@Value("${file.upload-dir}")
	private String uploadDir;
*/	
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		String os = System.getProperty("os.name").toLowerCase();

		String uploadDir;
		String boardDir;
		
		if (os.contains("win")) {
			uploadDir = "C:/my_coffee_map/profiles/";
			boardDir = "C:/my_coffee_map/boards/";
		} else {
			uploadDir = "/home/ubuntu/my_coffee_map/profiles/";
			boardDir = "/home/ubuntu/my_coffee_map/boards/";
		}

		// 윈도우는 file:/// , 리눅스는 file:/ 로 해도 작동하지만 file:/// 통일 가능
		registry.addResourceHandler("/profiles/**")
				.addResourceLocations("file:///" + uploadDir.replace("\\", "/"));
		
		// 게시판 이미지 리소스 핸들러
		registry.addResourceHandler("/boards/**")
				.addResourceLocations("file:///" + boardDir.replace("\\", "/"));
	}
			
		/*
		registry.addResourceHandler("/profiles/**")
		.addResourceLocations("file:///" + uploadDir + "/");
		
		*/

	
}