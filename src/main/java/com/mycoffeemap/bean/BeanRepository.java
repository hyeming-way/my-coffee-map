package com.mycoffeemap.bean;

import com.mycoffeemap.bean.Bean;
import com.mycoffeemap.bean.Bean.RoastLevel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BeanRepository extends JpaRepository<Bean, Long> {

    // 1. 로스팅 + flavor 포함 (단일)
    List<Bean> findByRoastLevelAndFlavorNotesContainingIgnoreCase(RoastLevel roastLevel, String flavor);

    // 2. 로스팅만
    List<Bean> findByRoastLevel(RoastLevel roastLevel);

    // 3. flavor만
    List<Bean> findByFlavorNotesContainingIgnoreCase(String flavor);
}
