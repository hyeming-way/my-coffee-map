package com.mycoffeemap.board;

import com.mycoffeemap.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    // 댓글 저장
    public Comment saveComment(Board board, User user, String content) {
        Comment comment = Comment.builder()
                .board(board)
                .user(user)
                .content(content)
                .build();
        return commentRepository.save(comment);
    }

    // 특정 게시글의 댓글 목록 조회 (작성일 기준 오름차순)
    public List<Comment> getCommentsByBoard(Board board) {
        return commentRepository.findByBoardOrderByCreatedAtAsc(board);
    }

    // 댓글 ID로 단건 조회
    public Comment getCommentById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("コメントが見つかりません。"));
    }

    // 댓글 삭제 (작성자 본인만 가능)
    public void deleteComment(Long id, User loginUser) {
        Comment comment = getCommentById(id);

        if (!comment.getUser().getId().equals(loginUser.getId())) {
            throw new SecurityException("削除権限がありません。");
        }

        commentRepository.delete(comment);
    }
    
    // 내 댓글 볼러오기
    public List<Comment> findByUser(User user) {
        return commentRepository.findByUserOrderByCreatedAtDesc(user);
    }
}
