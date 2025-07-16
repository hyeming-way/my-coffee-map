package com.mycoffeemap.bean;

import com.mycoffeemap.bean.Bean.RoastLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BeanRepository extends JpaRepository<Bean, Long> {

    // 로스팅 + flavor 포함 (단일)
    List<Bean> findByRoastLevelAndFlavorNotesContainingIgnoreCase(RoastLevel roastLevel, String flavor);

    // 로스팅만
    List<Bean> findByRoastLevel(RoastLevel roastLevel);

    // flavor만
    List<Bean> findByFlavorNotesContainingIgnoreCase(String flavor);
    
    // 내가 등록한 원두 목록 조회
    List<Bean> findByUserId(Long userId);

}
