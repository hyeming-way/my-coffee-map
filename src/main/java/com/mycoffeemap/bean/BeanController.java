package com.mycoffeemap.bean;

import com.mycoffeemap.bean.Bean.RoastLevel;
import com.mycoffeemap.cafe.Cafe;
import com.mycoffeemap.cafe.CafeService;
import com.mycoffeemap.user.User;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/beans")
public class BeanController {

    private final BeanService beanService;
    private final CafeService cafeService;

    // 커피 취향 검색 폼
    @GetMapping("/search")
    public String search(
            @RequestParam(name = "roast", required = false) String roast,
            @RequestParam(name = "flavor", required = false) String flavor,
            Model model) {

        RoastLevel roastLevel = parseRoastLevel(roast);
        List<Bean> recommendedBeans = beanService.findByPreference(roastLevel, flavor);
        model.addAttribute("recommendedBeans", recommendedBeans);

        List<Cafe> recommendedCafes = cafeService.findByBeans(recommendedBeans);
        model.addAttribute("recommendedCafes", recommendedCafes);

        model.addAttribute("selectedRoast", roast);
        model.addAttribute("selectedFlavor", flavor);

        model.addAttribute("flavorOptions", List.of(
                "Floral", "Nutty", "Fruity", "Spicy", "Chocolate","Earthy", "Caramel", "Smoky"
        ));

        return "beans/search-content";
    }

    // 결과 보기
    @GetMapping("/result")
    public String result(
            @RequestParam(name = "roast", required = false) String roast,
            @RequestParam(name = "flavor", required = false) List<String> flavor,
            Model model) {

        RoastLevel roastLevel = parseRoastLevel(roast);
        List<Bean> recommendedBeans = beanService.findByPreference(roastLevel, flavor);
        model.addAttribute("recommendedBeans", recommendedBeans);

        List<Cafe> recommendedCafes = cafeService.findByBeans(recommendedBeans);
        model.addAttribute("recommendedCafes", recommendedCafes);

        model.addAttribute("selectedRoast", roast);
        model.addAttribute("selectedFlavor", flavor);

        return "beans/result-content";
    }

    // 문자열을 RoastLevel enum으로 변환
    private RoastLevel parseRoastLevel(String roast) {
        try {
            return (roast != null && !roast.isBlank())
                    ? RoastLevel.valueOf(roast.toUpperCase())
                    : null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
    
    // 원두 등록 폼
    @GetMapping("/new")
    public String showCreateForm(Model model, HttpSession session) {
        model.addAttribute("bean", new Bean());
        model.addAttribute("roastLevels", RoastLevel.values());
        model.addAttribute("flavorOptions", List.of("Floral", "Nutty", "Fruity", "Spicy", "Chocolate", "Earthy", "Caramel", "Smoky"));

        // 로그인 확인
//        User loginUser = (User) session.getAttribute("loginUser");
//        if (loginUser == null) {
//            return "redirect:/user/login";
//        }

//        model.addAttribute("loginUser", loginUser);

        // 이미지 옵션
        model.addAttribute("imageOptions", List.of(
            "/images/beans/yirgacheffe.png",
            "/images/beans/guatemala.png",
            "/images/beans/peru.png",
            "/images/beans/mandheling.png",
            "/images/beans/mexico.png",
            "/images/beans/costarica.png",
            "/images/beans/geisha.png",
            "/images/beans/kenya.png",
            "/images/beans/Kona.png",
            "/images/beans/rwanda.png",
            "/images/beans/tanzania.png",
            "/images/beans/colombia.png"
        ));

        return "beans/create-content";
    }


    // 원두 등록 처리
    @PostMapping("/new")
    public String createBean(@ModelAttribute Bean bean, HttpSession session) {
//        User loginUser = (User) session.getAttribute("loginUser");
//        if (loginUser == null) {
//            return "redirect:/login"; // 또는 에러 처리
//        }
        beanService.save(bean);
        return "redirect:/mycoffeemap";
    }

}
