package com.mokhovav;


import com.mokhovav.exception.CriticalException;
import com.mokhovav.exception.LogicalException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {
    @GetMapping
    public ModelAndView getError(Model model) throws Exception {
        throw new LogicalException("My Valid Exception");
    }
}