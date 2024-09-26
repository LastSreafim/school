package ru.hogwarts.school.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

@RestController
@RequestMapping("students")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping({"id"}) //GET
    public Student getStudentInfo(@PathVariable Long id) {
        return studentService.findStudent(id);
    }

    @PostMapping   //POST
    public Student createStudent(Student student) {
        return studentService.createStudent(student);
    }
//    @DeleteMapping({"id"}) //DELETE
//    public Student deleteStudent(@PathVariable Long id) {
//        return studentService.deleteStudent(id);
//    }
}
