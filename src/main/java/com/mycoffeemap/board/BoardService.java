package com.mycoffeemap.board;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository; // 💡 추가

    public void save(Board board) {
        boardRepository.save(board);
    }

    public List<Board> findAll() {
        return boardRepository.findAll();
    }

    public Board findById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 존재하지 않습니다. id=" + id));
    }

    public List<Board> findByUserId(Long userId) {
        return boardRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    public int countLikes(Board board) {
        return boardLikeRepository.countByBoard(board);
    }
}
