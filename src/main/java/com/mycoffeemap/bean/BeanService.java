package com.mycoffeemap.bean;

import com.mycoffeemap.bean.Bean;
import com.mycoffeemap.bean.Bean.RoastLevel;
import com.mycoffeemap.bean.BeanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BeanService {

    private final BeanRepository beanRepository;

    // 취향 기반 필터링
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

    public List<Bean> findByPreference(RoastLevel roast, List<String> flavors) {
        if ((flavors == null || flavors.isEmpty()) && roast == null) {
            return beanRepository.findAll();
        }

        if (roast != null && flavors != null && !flavors.isEmpty()) {
            List<Bean> result = new ArrayList<>();
            for (String flavor : flavors) {
                result.addAll(beanRepository.findByRoastLevelAndFlavorNotesContainingIgnoreCase(roast, flavor));
            }
            return result;
        }

        if (roast != null) {
            return beanRepository.findByRoastLevel(roast);
        }

        List<Bean> result = new ArrayList<>();
        for (String flavor : flavors) {
            result.addAll(beanRepository.findByFlavorNotesContainingIgnoreCase(flavor));
        }
        return result;
    }

    // 단건 조회
    public Bean findById(Long id) {
        return beanRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 원두가 존재하지 않습니다: " + id));
    }

    public Bean save(Bean bean) {
        return beanRepository.save(bean);
    }
}
