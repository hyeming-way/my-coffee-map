package com.mycoffeemap;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.mycoffeemap.board.Board;
import com.mycoffeemap.board.BoardLikeRepository;
import com.mycoffeemap.board.BoardRepository;
import com.mycoffeemap.user.CustomUserDetails;
import com.mycoffeemap.user.User;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;

    // http://localhost:8070/
    // http://localhost:8070/mycoffeemap
    @GetMapping({"/mycoffeemap", "/"})
    public String index(Model model, HttpServletRequest request) {
        List<Board> recentPosts = boardRepository.findTop5ByOrderByCreatedAtDesc();

        // 좋아요 수 주입
        for (Board board : recentPosts) {
            int likeCount = boardLikeRepository.countByBoard(board);
            board.setLikeCount(likeCount);
        }

        model.addAttribute("recentPosts", recentPosts);

        //소셜로그인 사용자 정보 가져와서 세션과 모델에 넣기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails customUser) {
            User user = customUser.getUser();
            request.getSession().setAttribute("user", user);
            model.addAttribute("user", user);
        }

        return "fragments/main-content"; // 이 프래그먼트가 실제로 메인 뷰에서 렌더링되는 구조
    }

}
