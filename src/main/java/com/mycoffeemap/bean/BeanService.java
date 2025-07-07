package com.mycoffeemap.bean;

import com.mycoffeemap.bean.Bean.RoastLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BeanService {

    private final BeanRepository beanRepository;

    // 로스팅 또는 향미 키워드를 기반으로 원두 추천
    public List<Bean> findByPreference(RoastLevel roast, String flavorKeyword) {
        if (roast == null && (flavorKeyword == null || flavorKeyword.isBlank())) {
            return beanRepository.findAll();
        }

        if (roast != null && flavorKeyword != null && !flavorKeyword.isBlank()) {
            return beanRepository.findByRoastLevelAndFlavorNotesContainingIgnoreCase(roast, flavorKeyword);
        }

        if (roast != null) {
            return beanRepository.findByRoastLevel(roast);
        }

        return beanRepository.findByFlavorNotesContainingIgnoreCase(flavorKeyword);
    }

    // 로스팅 + 향미 리스트 기반으로 중복 없는 원두 추천
    public List<Bean> findByPreference(RoastLevel roast, List<String> flavors) {
        if ((flavors == null || flavors.isEmpty()) && roast == null) {
            return beanRepository.findAll();
        }

        Set<Bean> resultSet = new HashSet<>(); // 중복 제거용 Set

        if (roast != null && flavors != null && !flavors.isEmpty()) {
            for (String flavor : flavors) {
                resultSet.addAll(beanRepository.findByRoastLevelAndFlavorNotesContainingIgnoreCase(roast, flavor));
            }
            return new ArrayList<>(resultSet);
        }

        if (roast != null) {
            return beanRepository.findByRoastLevel(roast);
        }

        for (String flavor : flavors) {
            resultSet.addAll(beanRepository.findByFlavorNotesContainingIgnoreCase(flavor));
        }

        return new ArrayList<>(resultSet);
    }

    // 단건 원두 조회
    public Bean findById(Long id) {
        return beanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 원두가 존재하지 않습니다: " + id));
    }

    // 원두 저장
    public Bean save(Bean bean) {
        return beanRepository.save(bean);
    }

    // 내가 등록한 원두 목록 조회
    public List<Bean> findByUserId(Integer userId) {
        return beanRepository.findByUserId(userId);
    }
} 
