package com.mycoffeemap.board;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.mycoffeemap.bean.Bean;
import com.mycoffeemap.bean.BeanRepository;
import com.mycoffeemap.cafe.Cafe;
import com.mycoffeemap.cafe.CafeRepository;
import com.mycoffeemap.common.FileStorageService;
import com.mycoffeemap.user.User;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor 
public class BoardController {

    private final BoardRepository boardRepository;
    private final BeanRepository beanRepository;
    private final CafeRepository cafeRepository;
    private final BoardLikeRepository boardLikeRepository;
    private final FileStorageService fileStorageService;
    
    
    // 커피 노트 작성
    @GetMapping("/board/new")
    public String showCreateForm(Model model, HttpSession session) {    	
        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) {
            return "redirect:/user/login";
        }
        List<Bean> beanList = beanRepository.findAll();  // 원두 목록
        List<Cafe> cafeList = cafeRepository.findAll();  // 카페 목록

        model.addAttribute("beanList", beanList);
        model.addAttribute("cafeList", cafeList);
        return "board/new"; 
    }
    
    @PostMapping("/board/new")
    public String createBoard(
            @RequestParam String title,
            @RequestParam String content,
            @RequestParam(required = false) Long beanId,
            @RequestParam(required = false) Long cafeId,
            @RequestParam(required = false) MultipartFile imageFile,
            @RequestParam(required = false) Integer rating, // 별점 값 받기
            HttpSession session
    ) {
        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) {
            return "redirect:/user/login";
        }

        // 이미지 저장
        String storedFilename = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                storedFilename = fileStorageService.storeFile(imageFile);
            } catch (Exception e) {
                e.printStackTrace();
                return "redirect:/error";
            }
        }


        // 관련 Bean, Cafe 조회
        Bean bean = (beanId != null) ? beanRepository.findById(beanId).orElse(null) : null;
        Cafe cafe = (cafeId != null) ? cafeRepository.findById(cafeId).orElse(null) : null;

        // Board 생성
        Board board = Board.builder()
                .title(title)
                .content(content)
                .user(loginUser)
                .bean(bean)
                .cafe(cafe)
                .imageUrl(storedFilename)
                .rating(rating) // 별점 저장
                .createdAt(LocalDateTime.now())
                .build();

        boardRepository.save(board);

        return "redirect:/board/list"; // 목록 페이지로
    }
    
    // 커피노트 목록 보기
    @GetMapping("/board/list")
    public String listBoards(Model model) {
        List<Board> boardList = boardRepository.findAllByOrderByCreatedAtDesc();
        model.addAttribute("boardList", boardList);
        return "board/list";
    }

    // 커피노트 단건 보기
    @GetMapping("/board/{id}")
    public String viewBoard(@PathVariable("id") Long id, HttpSession session, Model model) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("該当する投稿が存在しません。 " + id));

        User loginUser = (User) session.getAttribute("user");
        boolean liked = false;
        if (loginUser != null) {
            liked = boardLikeRepository.findByUserAndBoard(loginUser, board).isPresent();
        }

        int likeCount = boardLikeRepository.countByBoard(board);

        model.addAttribute("board", board);
        model.addAttribute("liked", liked);
        model.addAttribute("likeCount", likeCount);

        return "board/view";  // 뷰 페이지 렌더링
    }
    
    // 커피노트 좋아요
    @PostMapping("/board/like")
    public String likeBoard(@RequestParam("boardId") Long boardId, HttpSession session) {
        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) {
            return "redirect:/user/login";
        }

        Board board = boardRepository.findById(boardId)
        		.orElseThrow(() -> new IllegalArgumentException("該当する投稿が存在しません。"));

        Optional<BoardLike> existingLike = boardLikeRepository.findByUserAndBoard(loginUser, board);

        if (existingLike.isPresent()) {
            boardLikeRepository.delete(existingLike.get()); // 좋아요 취소
        } else {
            BoardLike newLike = BoardLike.builder()
                    .user(loginUser)
                    .board(board)
                    .likedAt(LocalDateTime.now())
                    .build();
            boardLikeRepository.save(newLike); // 좋아요 추가
        }

        return "redirect:/board/" + boardId; // 다시 상세보기로 리다이렉트
    }


}
