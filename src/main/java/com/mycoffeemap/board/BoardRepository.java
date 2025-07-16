package com.mycoffeemap.board;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mycoffeemap.user.User;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 작성일 기준 내림차순 정렬된 전체 게시글 조회
	List<Board> findAllByOrderByCreatedAtDesc();
	
	// 내가 작성한 노트 조회
	List<Board> findByUserIdOrderByCreatedAtDesc(Long userId);
	
	// 사용자 기준으로 게시글 페이징 조회
    Page<Board> findByUser(User user, Pageable pageable);
	
	// Top5 노트 조회
	List<Board> findTop5ByOrderByCreatedAtDesc();
	
	// 노트 검색
	Page<Board> findByBeanId(Long beanId, Pageable pageable);
	
	Page<Board> findByCafeId(Long cafeId, Pageable pageable);
	
	Page<Board> findByBeanIdAndCafeId(Long beanId, Long cafeId, Pageable pageable);
	
}
