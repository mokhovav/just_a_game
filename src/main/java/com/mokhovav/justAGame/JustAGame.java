package com.mokhovav.justAGame;

import com.mokhovav.justAGame.games.Game;
import com.mokhovav.justAGame.games.GameService;
import com.mokhovav.justAGame.authentication.authority.Authority;
import com.mokhovav.justAGame.authentication.authority.AuthorityService;
import com.mokhovav.justAGame.authentication.user.User;
import com.mokhovav.justAGame.authentication.user.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;

@SpringBootApplication(scanBasePackages = { "com.mokhovav.inspiration", "com.mokhovav.base_spring_boot_project", "com.mokhovav.justAGame"})
public class JustAGame {

    private final PasswordEncoder passwordEncoder;
    private final AuthorityService authorityService;
    private final UserService userService;
    private final GameService gameService;

    public JustAGame(PasswordEncoder passwordEncoder, AuthorityService authorityService, UserService userService, GameService gameService) {
        this.passwordEncoder = passwordEncoder;
        this.authorityService = authorityService;
        this.userService = userService;
        this.gameService = gameService;
    }

    public static void main(String[] args) {
        SpringApplication.run(JustAGame.class, args);
    }

    @PostConstruct
    private void addDefaultUser() {
        try {
            authorityService.save(new Authority("admin"));
        } catch (Exception e) {
            System.out.println("PostConstruct: " + e.getMessage());
        }
        try {
            authorityService.save(new Authority("user"));
        } catch (Exception e) {
            System.out.println("PostConstruct: " + e.getMessage());
        }
        try {
            userService.save(new User("admin", passwordEncoder.encode("admin")));
        } catch (Exception e) {
            System.out.println("PostConstruct: " + e.getMessage());
        }

        try {
            gameService.save(new Game("littleCircuit", 4, 10000, true));
        } catch (Exception e) {
            System.out.println("PostConstruct: " + e.getMessage());
        }

        try {
            User usr = userService.getByUserName("admin");
            if (!userService.isHaveAuthority(usr, authorityService.getByAuthority("admin"))) {
                usr.getAuthorities().add(authorityService.getByAuthority("admin"));
                userService.update(usr);
            }
        }  catch (Exception e) {
            System.out.println("PostConstruct: " + e.getMessage());
        }
    }
}