package com.mycoffeemap.mypage;

import com.mycoffeemap.bean.Bean;
import com.mycoffeemap.bean.BeanPreference;
import com.mycoffeemap.bean.BeanPreferenceRepository;
import com.mycoffeemap.bean.BeanService;
import com.mycoffeemap.board.Board;
import com.mycoffeemap.board.BoardRepository;
import com.mycoffeemap.board.BoardService;
import com.mycoffeemap.board.Comment;
import com.mycoffeemap.board.CommentService;
import com.mycoffeemap.cafe.Cafe;
import com.mycoffeemap.cafe.CafeService;
import com.mycoffeemap.user.User;

import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MyPageController {

    private final BeanService beanService;
    private final CafeService cafeService;
    private final BoardService boardService;
    private final BoardRepository boardRepository;
    private final CommentService commentService;
    private final BeanPreferenceRepository beanPreferenceRepository;
    
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

    // 로그인한 사용자의 커피 취향(로스팅 + 향미) 기반 추천 결과 재확인 페이지
    @GetMapping("/my/preference")
    @SuppressWarnings("unchecked")
    public String myPreference(HttpSession session, Model model) {
        // 로그인 여부 확인
        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) {
            model.addAttribute("loginError", "このサービスを利用するには、ログインが必要です。");
            return "redirect:/user/login";
        }

        // DB에서 사용자의 저장된 취향 불러오기
        Optional<BeanPreference> prefOpt = beanPreferenceRepository.findByUser(loginUser);
        String roast = null;
        List<String> flavors = null;

        if (prefOpt.isPresent()) {
            // DB에 저장된 경우 해당 값 사용
            roast = prefOpt.get().getRoastLevel().name();
            flavors = prefOpt.get().getFlavor();
        } else {
            // 세션에서 이전 선택값 가져오기 (DB에 없는 경우)
            roast = (String) session.getAttribute("selectedRoast");
            flavors = (List<String>) session.getAttribute("selectedFlavor");
        }

        // 둘 다 없는 경우 안내 메시지 출력
        if ((roast == null || roast.isBlank()) && (flavors == null || flavors.isEmpty())) {
            model.addAttribute("message", "保存されたコーヒーの嗜好がありません。/ 이전에 선택한 커피 취향이 없습니다。");
            return "beans/result";
        }

        // 추천 원두 목록 조회
        List<Bean> recommendedBeans = beanService.findByPreference(
            roast != null ? Bean.RoastLevel.valueOf(roast) : null,
            flavors
        );

        // 추천된 원두를 사용하는 카페 목록 조회
        List<Cafe> recommendedCafes = cafeService.findByBeans(recommendedBeans);

        // View 전달 데이터 설정
        model.addAttribute("selectedRoast", roast);
        model.addAttribute("selectedFlavor", flavors);
        model.addAttribute("recommendedBeans", recommendedBeans);
        model.addAttribute("recommendedCafes", recommendedCafes);

        // 추천 결과 페이지로 이동
        return "beans/result";
    }
    
    // 내가 등록한 원두 목록
    @GetMapping("/my/beans")
    public String myBeans(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) {
        	model.addAttribute("loginError", "このサービスを利用するには、ログインが必要です。");
            return "redirect:/user/login";
        }
        
        List<Bean> myBeans = beanService.findByUserId(loginUser.getId().longValue());
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
        List<Cafe> myCafes = cafeService.findByUserId(loginUser.getId().longValue());
        model.addAttribute("myCafes", myCafes);
        return "my/my-cafes";
    }
    
    // 내가 쓴 노트 목록
    @GetMapping("/my/posts")
    public String myPosts(HttpSession session,
    					  @RequestParam(name = "page", defaultValue = "0") int page,
			              @RequestParam(name = "size", defaultValue = "6") int size,
			              Model model) {

        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) {
            return "redirect:/user/login";
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Board> myPostsPage = boardRepository.findByUser(loginUser, pageable);

        // 현재 페이지의 게시글만 좋아요 수 추가
        List<Board> myPosts = myPostsPage.getContent();
        for (Board board : myPosts) {
            int likeCount = boardService.countLikes(board);
            board.setLikeCount(likeCount);
        }

        model.addAttribute("myPostsPage", myPostsPage);
        model.addAttribute("myPosts", myPosts);
        model.addAttribute("currentPage", myPostsPage.getNumber()); // 현재 페이지 번호
        model.addAttribute("totalPages", myPostsPage.getTotalPages()); // 전체 페이지 수

        return "my/my-posts";
    }
    
    // 내가 쓴 댓글 목록
    @GetMapping("/my/comments")
    public String myComments(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) {
            return "redirect:/user/login";
        }

        List<Comment> myComments = commentService.findByUser(loginUser);
        model.addAttribute("myComments", myComments);
        return "my/my-comments";
    }


}
