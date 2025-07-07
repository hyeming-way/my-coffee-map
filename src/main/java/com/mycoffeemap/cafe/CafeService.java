package com.mycoffeemap.cafe;

import com.mycoffeemap.bean.Bean;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CafeService {

    private final CafeRepository cafeRepository;

    // 여러 개의 원두 리스트를 사용하는 카페 조회
    public List<Cafe> findByBeans(List<Bean> beans) {
        if (beans == null || beans.isEmpty()) {
            return List.of(); // 빈 리스트 반환
        }
        return cafeRepository.findDistinctByCafeBeans_BeanIn(beans);
    }

    // 하나의 원두를 사용하는 카페 조회
    public List<Cafe> findByBean(Bean bean) {
        return cafeRepository.findByCafeBeansBean(bean);
    }
    
    // 전체 카페 조회
    public List<Cafe> findAll() {
        return cafeRepository.findAll();
    }

    // 내가 등록한 카페 목록 조회
    public List<Cafe> findByUserId(Integer userId) {
        return cafeRepository.findByUserId(userId);
    }

    // 단건 조회
    public Cafe findById(Long id) {
        return cafeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 카페가 존재하지 않습니다: " + id));
    }

    // 저장
    public Cafe save(Cafe cafe) {
        return cafeRepository.save(cafe);
    }
}
