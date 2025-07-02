package com.mycoffeemap.cafe;

import java.time.LocalDateTime;

import com.mycoffeemap.bean.Bean;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "cafe_beans")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CafeBean {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    // 카페
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cafe_id")
    private Cafe cafe;

    // 원두
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bean_id")
    private Bean bean;

    // 추가 정보
    private String useType; // 예: "대표원두", "시즌원두"
    private LocalDateTime linkedAt;
}
