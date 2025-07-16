package com.mycoffeemap.board;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final CommentRepository commentRepository;
    private final CommentService commentService;
    private final FileStorageService fileStorageService;
    
    
    // 커피 노트 작성 폼
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
    
    // 커피 노트 작성
    @PostMapping("/board/new")
    public String createBoard(
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam(name = "beanId", required = false) Long beanId,
            @RequestParam(name = "cafeId", required = false) Long cafeId,
            @RequestParam(name = "imageFile", required = false) MultipartFile imageFile,
            @RequestParam(name = "rating", required = false) Integer rating,
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
                storedFilename = fileStorageService.storeFile(imageFile, "board");
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
    
    // 커피노트 목록 보기(페이징)
    @GetMapping("/board/list")
    public String listBoards(@RequestParam(name = "page", defaultValue = "0") int page,
                             @RequestParam(name = "size", defaultValue = "6") int size,
                             @RequestParam(name = "beanId", required = false) Long beanId,
                             @RequestParam(name = "cafeId", required = false) Long cafeId,
                             Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());

        Page<Board> boardPage;

        // 게시글 목록 먼저 가져옴
        if (beanId != null && cafeId != null) {
            boardPage = boardRepository.findByBeanIdAndCafeId(beanId, cafeId, pageable);
        } else if (beanId != null) {
            boardPage = boardRepository.findByBeanId(beanId, pageable);
        } else if (cafeId != null) {
            boardPage = boardRepository.findByCafeId(cafeId, pageable);
        } else {
            boardPage = boardRepository.findAll(pageable);
        }

        // 게시글마다 좋아요 수 주입
        for (Board board : boardPage.getContent()) {
            int likeCount = boardLikeRepository.countByBoard(board);
            board.setLikeCount(likeCount);
        }

        // 모델에 추가
        model.addAttribute("boardPage", boardPage);
        model.addAttribute("beanId", beanId);
        model.addAttribute("cafeId", cafeId);
        model.addAttribute("beanList", beanRepository.findAll());
        model.addAttribute("cafeList", cafeRepository.findAll());

        return "board/list";
    }


    // 커피노트 단건 보기
    @GetMapping("/board/{id}")
    public String viewBoard(@PathVariable("id") Long id,
                            @RequestParam(value = "edit", required = false) Long editingCommentId,
                            HttpSession session,
                            Model model) {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("該当する投稿が存在しません。 " + id));

        User loginUser = (User) session.getAttribute("user");
        boolean liked = false;
        if (loginUser != null) {
            liked = boardLikeRepository.findByUserAndBoard(loginUser, board).isPresent();
        }

        int likeCount = boardLikeRepository.countByBoard(board);
        List<Comment> comments = commentService.getCommentsByBoard(board);

        model.addAttribute("board", board);
        model.addAttribute("liked", liked);
        model.addAttribute("likeCount", likeCount);
        model.addAttribute("comments", comments);
        model.addAttribute("editingCommentId", editingCommentId);
        model.addAttribute("session", session);

        // 댓글 수정 중인 ID 전달
        if (editingCommentId != null) {
            model.addAttribute("editingCommentId", editingCommentId);
        }

        return "board/view";
    }
    
    // 커피노트 수정 폼
    @GetMapping("/board/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) {
            return "redirect:/user/login";
        }

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("投稿が見つかりません。"));

        if (!board.getUser().getId().equals(loginUser.getId())) {
            throw new SecurityException("編集権限がありません。");
        }

        model.addAttribute("board", board);
        model.addAttribute("beanList", beanRepository.findAll());
        model.addAttribute("cafeList", cafeRepository.findAll());

        return "board/edit";
    }
    
    // 커피노트 수정
    @PostMapping("/board/edit/{id}")
    public String editBoard(@PathVariable("id") Long id,
                            @RequestParam(name = "title") String title,
                            @RequestParam(name = "content") String content,
                            @RequestParam(name = "beanId", required = false) Long beanId,
                            @RequestParam(name = "cafeId", required = false) Long cafeId,
                            @RequestParam(name = "rating", required = false) Integer rating,
                            @RequestParam(name = "latitude", required = false) Double latitude,
                            @RequestParam(name = "longitude", required = false) Double longitude,
                            @RequestParam(name = "imageFile", required = false) MultipartFile imageFile,
                            HttpSession session) {

        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) return "redirect:/user/login";

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("投稿が見つかりません。"));

        if (!board.getUser().getId().equals(loginUser.getId())) {
            throw new SecurityException("編集権限がありません。");
        }

        // 이미지 교체
        if (imageFile != null && !imageFile.isEmpty()) {
            String storedFilename = fileStorageService.storeFile(imageFile, "board");
            board.setImageUrl(storedFilename);
        }

        board.setTitle(title);
        board.setContent(content);
        board.setRating(rating);
        board.setBean(beanId != null ? beanRepository.findById(beanId).orElse(null) : null);
        board.setCafe(cafeId != null ? cafeRepository.findById(cafeId).orElse(null) : null);
        board.setLatitude(latitude);
        board.setLongitude(longitude);
        board.setUpdatedAt(LocalDateTime.now());

        boardRepository.save(board);

        return "redirect:/board/" + id;
    }

    // 커피노트 삭제
    @PostMapping("/board/delete/{id}")
    public String deleteBoard(@RequestParam Long boardId, HttpSession session) {
        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) return "redirect:/user/login";

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("投稿が見つかりません。"));

        if (!board.getUser().getId().equals(loginUser.getId())) {
            throw new SecurityException("削除権限がありません。");
        }

        boardRepository.delete(board);
        return "redirect:/board/list";
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

    // 댓글 저장
    @PostMapping("/board/{id}/comment")
    public String addComment(@PathVariable("id") Long id,
                             @RequestParam("content") String content,
                             HttpSession session) {
        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) {
            return "redirect:/user/login";
        }

        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("投稿が見つかりません。"));

        commentService.saveComment(board, loginUser, content);

        return "redirect:/board/" + id;
    }
    
    // 댓글 삭제 (작성자 또는 게시글 작성자만 가능)
    public void deleteComment(Long id, User loginUser) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("コメントが見つかりません。"));

        boolean isCommentAuthor = comment.getUser().getId().equals(loginUser.getId());
        boolean isBoardAuthor = comment.getBoard().getUser().getId().equals(loginUser.getId());

        if (!isCommentAuthor && !isBoardAuthor) {
            throw new SecurityException("削除権限がありません。");
        }

        commentRepository.delete(comment);
    }
    
    // 댓글 수정 (댓글 작성자만)
    @PostMapping("/comment/edit")
    public String editComment(@RequestParam(name = "commentId") Long commentId,
                              @RequestParam(name = "boardId") Long boardId,
                              @RequestParam(name = "content") String content,
                              HttpSession session) {
        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) return "redirect:/user/login";

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("コメントが見つかりません。"));

        if (!comment.getUser().getId().equals(loginUser.getId())) {
            throw new SecurityException("修正権限がありません。");
        }

        comment.setContent(content);
        comment.setUpdatedAt(LocalDateTime.now());
        commentRepository.save(comment);

        return "redirect:/board/" + boardId;
    }


}
