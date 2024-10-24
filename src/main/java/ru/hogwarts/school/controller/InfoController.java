package ru.hogwarts.school.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.hogwarts.school.service.StudentService;

@RestController
@RequestMapping("info")

public class InfoController {

    private final StudentService studentService;

    @Autowired
    public InfoController(StudentService studentService) {
        this.studentService = studentService;
    }

    private static final Logger logger = LoggerFactory.getLogger(InfoController.class);

    @Value("${server.port}")
    private int serverPort;

    @GetMapping("/port")
    public int getPort() {
        logger.info("Was invoked method to get server port: {}", serverPort);
        return serverPort;
    }

    @GetMapping("/get-sum")
    public Integer getSum() {
        return studentService.getSum();
    }

    @GetMapping("/old-get-sum")
    public Integer oldGetSum() {
        return studentService.oldGetSum();
    }
}
