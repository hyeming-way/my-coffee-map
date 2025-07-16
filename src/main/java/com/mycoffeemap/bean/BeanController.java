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
    private final BeanRepository beanRepository;
    private final BeanPreferenceService beanPreferenceService;

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
        model.addAttribute("flavorOptions", List.of("Floral", "Nutty", "Fruity", "Spicy", "Chocolate", "Earthy", "Caramel", "Smoky"));

        return "beans/search";
    }

    // 결과 보기 및 취향 저장
    @GetMapping("/result")
    public String result(
            @RequestParam(name = "roast", required = false) String roast,
            @RequestParam(name = "flavor", required = false) List<String> flavor,
            Model model,
            HttpSession session) {

        RoastLevel roastLevel = parseRoastLevel(roast);
        List<Bean> recommendedBeans = beanService.findByPreference(roastLevel, flavor);
        model.addAttribute("recommendedBeans", recommendedBeans);
        
        List<Cafe> recommendedCafes = cafeService.findByBeans(recommendedBeans);
        model.addAttribute("recommendedCafes", recommendedCafes);

        // 로그인한 사용자일 경우, 취향 저장
        User loginUser = (User) session.getAttribute("user");
        if (loginUser != null) {
            beanPreferenceService.saveOrUpdate(loginUser, roastLevel, flavor);
        }

        // 모델에 저장
        model.addAttribute("selectedRoast", roast);
        model.addAttribute("selectedFlavor", flavor);

        // 세션에도 저장 (나중에 마이페이지에서 다시 보여줄 용도)
        session.setAttribute("selectedRoast", roast);
        session.setAttribute("selectedFlavor", flavor);

        return "beans/result";
    }
    
    // 사용자 등록 원두 등록 폼
    @GetMapping("/new")
    public String showCreateForm(Model model, HttpSession session) {
        model.addAttribute("bean", new Bean());
        model.addAttribute("roastLevels", RoastLevel.values());
        model.addAttribute("flavorOptions", List.of("Floral", "Nutty", "Fruity", "Spicy", "Chocolate", "Earthy", "Caramel", "Smoky"));

        // 로그인 확인
        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) {
            return "redirect:/user/login";
        }

        model.addAttribute("loginUser", loginUser);

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

        return "beans/create-bean";
    }


    // 사용자 등록 원두 등록 처리
    @PostMapping("/new")
    public String createBean(@ModelAttribute Bean bean, HttpSession session) {
        User loginUser = (User) session.getAttribute("user");
        if (loginUser == null) {
            return "redirect:/login"; // 또는 에러 처리
        }
        
        // 작성자 설정
        bean.setUser(loginUser);
        
        beanService.save(bean);
        return "redirect:/mycoffeemap";
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
    
    // 사용자 등록 원두 수정 폼
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model, HttpSession session) {
        User loginUser = (User) session.getAttribute("user");
        Bean bean = beanRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 원두 없음"));

        if (!bean.getUser().getId().equals(loginUser.getId())) {
            return "redirect:/mycoffeemap"; // 권한 없음
        }

        // 기본 데이터 세팅
        model.addAttribute("bean", bean);

        // 로스팅 레벨 목록
        model.addAttribute("roastLevels", Bean.RoastLevel.values());

        // 플레이버 선택지 (임의 예시)
        List<String> flavorOptions = List.of("Floral", "Nutty", "Fruity", "Spicy", "Chocolate", "Earthy", "Caramel", "Smoky");
        model.addAttribute("flavorOptions", flavorOptions);

        // 이미지 선택지 (URL 문자열 리스트)
        List<String> imageOptions = List.of(
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
        );
        model.addAttribute("imageOptions", imageOptions);

        return "beans/bean-edit";
    }


    // 사용자 등록 원두 삭제 처리
    @PostMapping("/delete/{id}")
    public String deleteBean(@PathVariable("id") Long id, HttpSession session) {
        User loginUser = (User) session.getAttribute("user");
        Bean bean = beanRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 원두 없음"));
        if (!bean.getUser().getId().equals(loginUser.getId())) {
            return "redirect:/mycoffeemap"; // 권한 없음
        }

        beanRepository.delete(bean);
        return "redirect:/my/beans";
    }

}
