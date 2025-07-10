package com.mycoffeemap.common;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//이미지 리소스 핸들링
@Configuration
public class WebConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
		String os = System.getProperty("os.name").toLowerCase();

		String profileDir;
		String boardDir;
		
		if (os.contains("win")) {
			profileDir = "C:/my_coffee_map/profiles/";
			boardDir = "C:/my_coffee_map/boards/";
		} else {
			profileDir = "/home/ubuntu/my_coffee_map/profiles/";
			boardDir = "/home/ubuntu/my_coffee_map/boards/";
		}

		// 윈도우는 file:/// , 리눅스는 file:/ 로 해도 작동하지만 file:/// 통일 가능
		registry.addResourceHandler("/profiles/**")
				.addResourceLocations("file:///" + profileDir.replace("\\", "/"));
		
		// 게시판 이미지 리소스 핸들러
		registry.addResourceHandler("/boards/**")
				.addResourceLocations("file:///" + boardDir.replace("\\", "/"));
	}
			
		/*
		registry.addResourceHandler("/profiles/**")
		.addResourceLocations("file:///" + uploadDir + "/");
		
		*/

	
}