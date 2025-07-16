package com.mycoffeemap;

import com.mycoffeemap.board.Board;
import com.mycoffeemap.board.BoardLikeRepository;
import com.mycoffeemap.board.BoardRepository;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;

    // http://localhost:8070/mycoffeemap
    @GetMapping("/mycoffeemap")
    public String index(Model model) {
        List<Board> recentPosts = boardRepository.findTop5ByOrderByCreatedAtDesc();
        
        // 좋아요 수 주입
        for (Board board : recentPosts) {
            int likeCount = boardLikeRepository.countByBoard(board);
            board.setLikeCount(likeCount);
        }
        
        model.addAttribute("recentPosts", recentPosts);
        return "fragments/main-content"; // 이 프래그먼트가 실제로 메인 뷰에서 렌더링되는 구조
    }
    
}
