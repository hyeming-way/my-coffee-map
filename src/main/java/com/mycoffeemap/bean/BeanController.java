package com.mycoffeemap.bean;

import com.mycoffeemap.bean.Bean.RoastLevel;
import com.mycoffeemap.cafe.Cafe;
import com.mycoffeemap.cafe.CafeService;

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

    // 원두 등록 폼
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("bean", new Bean()); // 폼 바인딩용 빈 객체
        model.addAttribute("roastLevels", RoastLevel.values());
        return "bean/create"; // templates/bean/create.html
    }

    // 원두 등록 처리
    @PostMapping("/new")
    public String createBean(@ModelAttribute Bean bean) {
        beanService.save(bean);
        return "redirect:/"; // 등록 후 메인으로
    }
    
    @GetMapping("/search")
    public String search(@RequestParam(required = false) String roast,
                        @RequestParam(required = false) String flavor,
                        Model model) {

        // Enum 변환
        RoastLevel roastLevel = parseRoastLevel(roast);
        List<Bean> recommendedBeans = beanService.findByPreference(roastLevel, flavor);
        model.addAttribute("recommendedBeans", recommendedBeans);

        List<Cafe> recommendedCafes = cafeService.findByBeans(recommendedBeans);
        model.addAttribute("recommendedCafes", recommendedCafes);

        model.addAttribute("selectedRoast", roast);
        model.addAttribute("selectedFlavor", flavor);

        model.addAttribute("flavorOptions", List.of(
                "Floral", "Nutty", "Fruity", "Spicy", "Chocolate",
                "Citrus", "Earthy", "Caramel", "Smoky"
        ));

        return "search";
    }

    @GetMapping("/result")
    public String result(@RequestParam(required = false) String roast,
                         @RequestParam(required = false) List<String> flavor,
                         Model model) {

        RoastLevel roastLevel = parseRoastLevel(roast);
        List<Bean> recommendedBeans = beanService.findByPreference(roastLevel, flavor);
        model.addAttribute("recommendedBeans", recommendedBeans);

        List<Cafe> recommendedCafes = cafeService.findByBeans(recommendedBeans);
        model.addAttribute("recommendedCafes", recommendedCafes);

        model.addAttribute("selectedRoast", roast);
        model.addAttribute("selectedFlavor", flavor);

        return "result";
    }

    // 문자열을 RoastLevel enum으로 변환
    private RoastLevel parseRoastLevel(String roast) {
        try {
            return (roast != null && !roast.isBlank()) ? RoastLevel.valueOf(roast.toUpperCase()) : null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
