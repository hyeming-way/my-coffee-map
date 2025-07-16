package com.mycoffeemap.bean;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.mycoffeemap.bean.Bean.RoastLevel;
import com.mycoffeemap.user.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BeanPreferenceService {

    private final BeanPreferenceRepository beanPreferenceRepository;

    // 취향 저장 또는 업데이트
    public void saveOrUpdate(User user, RoastLevel roastLevel, List<String> flavors) {
        BeanPreference preference = beanPreferenceRepository.findByUser(user)
                .orElseGet(() -> {
                    BeanPreference newPref = new BeanPreference();
                    newPref.setUser(user);
                    return newPref;
                });

        preference.setRoastLevel(roastLevel);
        preference.setFlavor(flavors);
        beanPreferenceRepository.save(preference);
    }

    // 사용자 취향 조회
    public Optional<BeanPreference> findByUser(User user) {
        return beanPreferenceRepository.findByUser(user);
    }
    
}
