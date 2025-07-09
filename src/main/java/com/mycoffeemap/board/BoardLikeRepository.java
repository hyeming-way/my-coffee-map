package com.mycoffeemap.board;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycoffeemap.user.User;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
	
    Optional<BoardLike> findByUserAndBoard(User user, Board board);
    
    int countByBoard(Board board);
}
