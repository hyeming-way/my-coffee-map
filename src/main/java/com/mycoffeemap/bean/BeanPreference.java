package com.mycoffeemap.bean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.mycoffeemap.cafe.CafeBean;
import com.mycoffeemap.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@Table(name = "beanPreference")
@NoArgsConstructor
public class BeanPreference {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 연관된 사용자 정보
 	@ManyToOne
 	@JoinColumn(name = "user_id", nullable = false)
 	private User user;

    // 로스팅 레벤
 	@Enumerated(EnumType.STRING)
 	private Bean.RoastLevel roastLevel;
 	
	// 향미(플레이버)
    @ElementCollection
    private List<String> flavor;

    // 작성일
    @CreationTimestamp
    private LocalDateTime createdAt;

    // 수정일
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    
    
}