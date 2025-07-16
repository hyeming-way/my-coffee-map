package com.mycoffeemap.cafe;

import com.mycoffeemap.bean.Bean;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CafeRepository extends JpaRepository<Cafe, Long> {

    // 추천 원두 중 하나라도 사용하는 카페를 조회 (중복 제거)
    List<Cafe> findDistinctByCafeBeans_BeanIn(List<Bean> beans);

    // 하나의 원두를 사용하는 카페 조회
    List<Cafe> findByCafeBeansBean(Bean bean);

    // 내가 등록한 카페 목록 조회
    List<Cafe> findByUserId(Long userId);
}
