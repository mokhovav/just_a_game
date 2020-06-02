package com.mokhovav.justAGame.authentication;

import com.mokhovav.base_spring_boot_project.exceptions.ValidException;
import com.mokhovav.justAGame.authentication.authority.Authority;
import com.mokhovav.justAGame.authentication.authority.AuthorityService;
import com.mokhovav.justAGame.authentication.user.User;
import com.mokhovav.justAGame.authentication.user.UserData;
import com.mokhovav.justAGame.authentication.user.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Controller
public class LoginController {
    private final UserService userService;
    private final AuthorityService authorityService;

    public LoginController(UserService userService, AuthorityService authorityService) {
        this.userService = userService;
        this.authorityService = authorityService;
    }

    @GetMapping("login")
    public String login(@RequestParam Optional<Boolean> error, Model model) {
          model.addAttribute("action", "login");
        if (error.isPresent())
            model.addAttribute("error", "Invalid user name or password");
        return "index";
    }
    @GetMapping("logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        logoutPage(request, response);
        return "index";
    }

    @GetMapping("signUp")
    public String signUp(@RequestParam Optional<Boolean> error, Model model) {
        UserData userData = new UserData();

        model.addAttribute("userData", userData);
        model.addAttribute("action", "signUp");
        return "index";
    }

    @PostMapping("signUp")
    public String signUp(UserData userData, Model model) throws ValidException {
        try{
            User user = userService.createUser(userData);
            Set<Authority> authorities = new HashSet<>();
            authorities.add(authorityService.getByAuthority("user"));
            user.setAuthorities(authorities);
            userService.save(user);
        } catch (ValidException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("action", "signUp");
            return "index";
        }
        return "redirect:/login";
    }

    private boolean logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
            return true;
        }
        else return false;
    }
}
