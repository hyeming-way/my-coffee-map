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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    private String description;

    private String imageUrl;

    private LocalDateTime createdAt;

    // CafeBean 연결
    @OneToMany(mappedBy = "cafe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CafeBean> cafeBeans = new ArrayList<>();
    
    // 연관된 사용자 정보    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

}