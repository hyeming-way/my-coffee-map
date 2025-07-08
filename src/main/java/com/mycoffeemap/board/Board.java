package com.mycoffeemap.board;

import com.mycoffeemap.user.User;
import com.mycoffeemap.bean.Bean;
import com.mycoffeemap.cafe.Cafe;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "boards")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 제목
    @Column(nullable = false, length = 100)
    private String title;

    // 내용
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // 작성자
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    // 관련 원두
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bean_id")
    private Bean bean;

    // 관련 카페
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    // 작성일
    @CreationTimestamp
    private LocalDateTime createdAt;

    // 수정일
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // 이미지 URL
    private String imageUrl;

    // 별점 (1~5, 선택)
    private Integer rating;
}
