package ru.hogwarts.school.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IamWorkingNowController {
    @GetMapping
    public String testApi() {
        return "HI! NOW I AM WORKING";
    }
}
