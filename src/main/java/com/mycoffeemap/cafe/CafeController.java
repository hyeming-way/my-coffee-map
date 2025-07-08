package com.mycoffeemap.cafe;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.mycoffeemap.bean.Bean;
import com.mycoffeemap.bean.BeanRepository;
import com.mycoffeemap.user.User;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/cafes")
@RequiredArgsConstructor
public class CafeController {

    private final CafeRepository cafeRepository;
    private final BeanRepository beanRepository;
    private final CafeBeanRepository cafeBeanRepository;

    @GetMapping("/new")
    public String showCreateCafeForm(Model model, HttpSession session) {
        // 로그인 체크
        User loginUser = (User) session.getAttribute("user");
        	if (loginUser == null) return "redirect:/user/login";
        
        // 카페 이미지 목록
        model.addAttribute("imageOptions", List.of(
            "/images/cafe/asakusa.png",
            "/images/cafe/cafelab.png",
            "/images/cafe/geisha.png",
            "/images/cafe/haruharu.png",
            "/images/cafe/hibiya.png",
            "/images/cafe/kenya_house.png",
            "/images/cafe/kilimanjaro.png",
            "/images/cafe/kona.png",
            "/images/cafe/midorinoki.png",
            "/images/cafe/quiet_tree.png",
            "/images/cafe/rwanda.png",
            "/images/cafe/sakura.png",
            "/images/cafe/slow.png",
            "/images/cafe/yume.png"
        ));

        // 원두 목록 (bean.imageUrl 사용 가능)
        model.addAttribute("beanList", beanRepository.findAll());

        // 사용자 정보도 필요하면 전달
        model.addAttribute("loginUser", loginUser);

        return "beans/create-cafe";
    }


    @PostMapping("/new")
    public String createCafe(
            @RequestParam String name,
            @RequestParam String address,
            @RequestParam String description,
            @RequestParam String imageUrl,
            @RequestParam Long beanId,
            @RequestParam CafeBean.UseType useType, HttpSession session
    ) {
         // 로그인 검사
         User loginUser = (User) session.getAttribute("user");
         if (loginUser == null) {
             return "redirect:/user/login";
         }

        Bean bean = beanRepository.findById(beanId)
                .orElseThrow(() -> new IllegalArgumentException("해당 원두 없음"));

        Cafe cafe = Cafe.builder()
                .name(name)
                .address(address)
                .description(description)
                .imageUrl(imageUrl)
                .createdAt(LocalDateTime.now())
                .user(loginUser)
                .build();

        Cafe savedCafe = cafeRepository.save(cafe);

        CafeBean cafeBean = CafeBean.builder()
                .cafe(savedCafe)
                .bean(bean)
                .useType(useType)
                .linkedAt(LocalDateTime.now())
                .build();

        cafeBeanRepository.save(cafeBean);

        return "redirect:/mycoffeemap";
    }

}
