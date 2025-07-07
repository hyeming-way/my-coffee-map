package com.mycoffeemap.cafe;

import java.time.LocalDateTime;

import com.mycoffeemap.bean.Bean;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cafe_beans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CafeBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 카페
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    // 원두
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bean_id")
    private Bean bean;

    // 사용 용도 (enum 사용)
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private UseType useType;

    public enum UseType {
        대표원두, 시즌원두, 한정판
    }

    // 등록 시각
    private LocalDateTime linkedAt;
}
