package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

@RestController
@RequestMapping("faculty")
public class FacultyController {

    private final FacultyService facultyService;

    @Autowired
    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @GetMapping({"id"}) //GET
    public Faculty getFacultyInfo (@PathVariable Long id) {
        return facultyService.findFaculty(id);
    }

    @PostMapping //POST
    public Faculty createFaculty (Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

//    @DeleteMapping({"id"}) //DELETE
//    public Faculty deleteFaculty(@PathVariable Long id) {
//        return facultyService.deleteFaculty(id);
//    }
}
