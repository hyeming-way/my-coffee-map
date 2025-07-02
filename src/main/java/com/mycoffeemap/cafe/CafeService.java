package com.mycoffeemap.cafe;

import com.mycoffeemap.bean.Bean;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CafeService {

    private final CafeRepository cafeRepository;

    public List<Cafe> findByBeans(List<Bean> beans) {
        if (beans == null || beans.isEmpty()) {
            return List.of(); // 빈 리스트 반환
        }
        return cafeRepository.findDistinctByCafeBeans_BeanIn(beans);
    }
    
    public List<Cafe> findByBean(Bean bean) {
        return cafeRepository.findByCafeBeansBean(bean);
    }

}
