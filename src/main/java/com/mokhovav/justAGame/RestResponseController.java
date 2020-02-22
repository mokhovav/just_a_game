package com.mokhovav.justAGame;

import com.mokhovav.base_spring_boot_project.annotations.Tracking;
import com.mokhovav.base_spring_boot_project.exception.CriticalException;
import com.mokhovav.base_spring_boot_project.exception.ValidException;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestResponseController {
    private final Logger logger;

    public RestResponseController(Logger logger) {
        this.logger = logger;
    }

    @RequestMapping(
            value = "/convert",
            method  = RequestMethod.GET)
    @Tracking
    private void get() throws ValidException, CriticalException {
        throw new CriticalException("in REST controller");
    }
}
