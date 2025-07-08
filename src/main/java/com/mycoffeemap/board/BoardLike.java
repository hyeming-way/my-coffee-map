package com.mycoffeemap.board;

import com.mycoffeemap.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "board_likes", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "board_id"}) // 중복 좋아요 방지
})
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardLike {

    // 좋아요 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    // 좋아요 누른 사용자
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user; 

    // 좋아요 대상 게시글
    @ManyToOne(optional = false)
    @JoinColumn(name = "board_id")
    private Board board; 

    // 좋아요시간
    private LocalDateTime likedAt; 
}
