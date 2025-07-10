package com.mycoffeemap.common;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Component;

//파일 저장용 컴포넌트
@Component
public class FileStorageProperties {
	
	//OS운영체제에 따른 파일 경로 자동 분기
	String os = System.getProperty("os.name").toLowerCase();
	
    public Path getProfileUploadPath() {
        
        if (os.contains("win")) {
            // 윈도우 경로
            return Paths.get("C:/my_coffee_map/profiles");
        } else {
            // 리눅스 경로
            return Paths.get("/home/ubuntu/my_coffee_map/profiles");
        }
    }
    
    
    // 게시글 이미지 저장 경로
    public Path getBoardUploadPath() {
    	
        if (os.contains("win")) {
        	// 윈도우 경로
            return Paths.get("C:/my_coffee_map/boards");            
        } else {
        	// 리눅스 경로
            return Paths.get("/home/ubuntu/my_coffee_map/boards");
        }
    }
    
	
}