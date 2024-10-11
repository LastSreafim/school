package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequestMapping("info")
@RestController
public class IamWorkingNowController {
    @GetMapping
    public ResponseEntity<String> testApi() {
        return ResponseEntity.ok("HI! NOW I AM WORKING") ;
    }
}
