package com.mycoffeemap.mypage;

import com.mycoffeemap.bean.Bean;
import com.mycoffeemap.bean.BeanService;
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

    // 로그인한 사용자가 등록한 원두 목록을 보여주는 페이지
    @GetMapping("/my/beans")
    public String myBeans(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/user/login"; 
        }

        List<Bean> myBeans = beanService.findByUserId(loginUser.getId());
        model.addAttribute("myBeans", myBeans);
        return "my/my-beans";
    }

    // 로그인한 사용자가 등록한 카페 목록을 보여주는 페이지
      @GetMapping("/my/cafes")
    public String myCafes(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("loginUser");

        if (loginUser == null) {
            return "redirect:/user/login";
        }

        List<Cafe> myCafes = cafeService.findByUserId(loginUser.getId());
        model.addAttribute("myCafes", myCafes);
        return "my/my-cafes";
    }
}
