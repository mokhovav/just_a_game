package com.mokhovav.justAGame;

import com.mokhovav.base_spring_boot_project.annotations.Tracking;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    private final Logger logger;

    public MainController(Logger logger) {
        this.logger = logger;
    }

    @GetMapping
    @Tracking
    public String index(){
        return "index";
    }
}