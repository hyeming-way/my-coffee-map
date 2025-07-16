package com.mycoffeemap.bean;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycoffeemap.user.User;

public interface BeanPreferenceRepository extends JpaRepository<BeanPreference, Long> {
    
	Optional<BeanPreference> findByUser(User user);
}
