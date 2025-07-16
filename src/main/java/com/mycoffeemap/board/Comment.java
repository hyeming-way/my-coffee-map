package com.mycoffeemap.board;

import com.mycoffeemap.user.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    // 댓글 ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    // 댓글이 달린 게시글
    @ManyToOne(optional = false)
    @JoinColumn(name = "board_id")
    private Board board; 

    // 댓글 작성자
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user; 

    // 댓글 내용
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content; 

    // 작성일
    @CreationTimestamp
    private LocalDateTime createdAt; 
    
    // 수정일
    @UpdateTimestamp
    private LocalDateTime updatedAt;

}
