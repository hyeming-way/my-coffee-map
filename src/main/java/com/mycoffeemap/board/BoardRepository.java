package com.mycoffeemap.board;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 작성일 기준 내림차순 정렬된 전체 게시글 조회
	List<Board> findAllByOrderByCreatedAtDesc();
	
	// 내가 작성한 노트 조회
	List<Board> findByUserIdOrderByCreatedAtDesc(Long userId);
	
	// Top5 노트 조회
	List<Board> findTop5ByOrderByCreatedAtDesc();
	
}
