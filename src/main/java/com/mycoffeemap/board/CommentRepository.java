package com.mycoffeemap.board;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycoffeemap.user.User;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    
	// 게시글 기준 댓글 조회 (기존)
    List<Comment> findByBoardOrderByCreatedAtAsc(Board board);

    // 유저 기준 댓글 조회 (새로 추가)
    List<Comment> findByUserOrderByCreatedAtDesc(User user);
}
