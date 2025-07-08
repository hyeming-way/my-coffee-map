package com.mycoffeemap.cafe;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.mycoffeemap.user.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cafes")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cafe {

	// id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 카페이름
    private String name;

    // 카페주소
    private String address;

    // 카페 설명
    private String description;

    // 카페 이미지
    private String imageUrl;

    // 등록일
    private LocalDateTime createdAt;

    // CafeBean 연결
    @OneToMany(mappedBy = "cafe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CafeBean> cafeBeans = new ArrayList<>();
    
    // 연관된 사용자 정보    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

}