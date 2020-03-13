package com.tlc.blog.web.admin;

import com.tlc.blog.po.User;
import com.tlc.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String loginPage() {
        return "admin/login";
    }

    /**
     * 用户登陆
     * @param userName
     * @param password
     * @param session
     * @param attributes
     * @return
     */
    @PostMapping("/login")
    public String login(@RequestParam String userName, @RequestParam String password,
                        HttpSession session, RedirectAttributes attributes) {
        User user = userService.checkUser(userName, password);
        if (user != null) {
            // 防止有人在前端页面拿到密码
            user.setPassword(null);
            session.setAttribute("user", user);
            return "admin/index";
        } else {
            attributes.addFlashAttribute("message", "用户名或密码错误");
            return "redirect:/admin";
        }
    }

    /**
     * 用户注销
     * @param session
     * @return
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/admin";
    }
}
