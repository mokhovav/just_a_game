package com.mokhovav.justAGame;

import com.mokhovav.base_spring_boot_project.exceptions.ValidException;
import com.mokhovav.inspiration.board.Board;
import com.mokhovav.inspiration.board.BoardFileData;
import com.mokhovav.inspiration.board.BoardService;
import com.mokhovav.justAGame.games.Game;
import com.mokhovav.justAGame.games.GameService;
import com.mokhovav.justAGame.authentication.authority.Authority;
import com.mokhovav.justAGame.authentication.authority.AuthorityService;
import com.mokhovav.justAGame.authentication.user.User;
import com.mokhovav.justAGame.authentication.user.UserService;
import com.mokhovav.justAGame.littleCircuit.LittleCircuit;
import com.mokhovav.justAGame.mongodb.GameSession;
import com.mokhovav.justAGame.mongodb.GameSessionRepository;
import com.mokhovav.justAGame.mongodb.MongoConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;

@SpringBootApplication(scanBasePackages = { "com.mokhovav.inspiration", "com.mokhovav.base_spring_boot_project", "com.mokhovav.justAGame"})
public class JustAGame {
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private BoardService boardService;
    @Autowired
    private LittleCircuit littleCircuit;


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
        // For Annotation
      /*  ApplicationContext ctx =
                new AnnotationConfigApplicationContext(MongoConfig.class);
        MongoOperations mongoOperation = (MongoOperations) ctx.getBean("mongoTemplate");
        GameSession gameSession = new GameSession("Andrey", "M");
        gameSession.setId(3);
        mongoOperation.insert(gameSession, "Games");/**/




    }

    @PostConstruct
    private void addDefaultUser() throws ValidException {
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

        //mongoTemplate.dropCollection("Games");
        BoardFileData boardFileData = boardService.convertToBoardFileData(littleCircuit);
        GameSession gameSession = new GameSession("Andrey", "M", boardFileData);
        mongoTemplate.insert(gameSession, "Games");
    }
}