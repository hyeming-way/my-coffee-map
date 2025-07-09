package com.mycoffeemap.mypage;

import com.mycoffeemap.bean.Bean;
import com.mycoffeemap.bean.BeanService;
import com.mycoffeemap.board.Board;
import com.mycoffeemap.board.BoardService;
import com.mycoffeemap.cafe.Cafe;
import com.mycoffeemap.cafe.CafeService;
import com.mycoffeemap.user.User;

import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MyPageController {

    private final BeanService beanService;
    private final CafeService cafeService;
    private final BoardService boardService;

    // 마이페이지 홈
    @GetMapping("/my")
    public String myPage(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) {
        	model.addAttribute("loginError", "このサービスを利用するには、ログインが必要です。");
            return "redirect:/user/login";
        }
        return "my/my-page";
    }

    // 내가 등록한 원두 목록
    @GetMapping("/my/beans")
    public String myBeans(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) {
        	model.addAttribute("loginError", "このサービスを利用するには、ログインが必要です。");
            return "redirect:/user/login";
        }
        List<Bean> myBeans = beanService.findByUserId(loginUser.getId());
        model.addAttribute("myBeans", myBeans);
        return "my/my-beans";
    }

    // 내가 등록한 카페 목록
    @GetMapping("/my/cafes")
    public String myCafes(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) {
        	model.addAttribute("loginError", "このサービスを利用するには、ログインが必要です。");
            return "redirect:/user/login";
        }
        List<Cafe> myCafes = cafeService.findByUserId(loginUser.getId());
        model.addAttribute("myCafes", myCafes);
        return "my/my-cafes";
    }

    // 나의 취향 다시 보기 (세션에 저장된 내용 기반)
    @GetMapping("/my/preference")
    @SuppressWarnings("unchecked")
    public String myPreference(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) {
        	model.addAttribute("loginError", "このサービスを利用するには、ログインが必要です。");
            return "redirect:/user/login";
        }

        // 세션에서 데이터 꺼냄 (먼저 선언)
        String roast = (String) session.getAttribute("selectedRoast");
        List<String> flavors = (List<String>) session.getAttribute("selectedFlavor");

        // 데이터가 없을 경우 안내 메시지
        if (roast == null && (flavors == null || flavors.isEmpty())) {
            model.addAttribute("message", "이전에 선택한 커피 취향이 없습니다.");
            return "beans/result";
        }

        // 추천 결과 조회
        List<Bean> recommendedBeans = beanService.findByPreference(
            roast != null ? Bean.RoastLevel.valueOf(roast) : null,
            flavors
        );
        List<Cafe> recommendedCafes = cafeService.findByBeans(recommendedBeans);

        model.addAttribute("selectedRoast", roast);
        model.addAttribute("selectedFlavor", flavors);
        model.addAttribute("recommendedBeans", recommendedBeans);
        model.addAttribute("recommendedCafes", recommendedCafes);

        return "beans/result";
    }
    
    // 내가 쓴 노트 목록
    @GetMapping("/my/posts")
    public String myPosts(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) {
            return "redirect:/user/login";
        }

        List<Board> myPosts = boardService.findByUserId(loginUser.getId().longValue());

        // 게시글별 좋아요 수 세팅
        for (Board board : myPosts) {
            int likeCount = boardService.countLikes(board);
            board.setLikeCount(likeCount);
        }

        model.addAttribute("myPosts", myPosts);
        return "my/my-posts";
    }


}
