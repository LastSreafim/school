package ru.hogwarts.school.controller;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.AvatarService;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.io.IOException;
import java.util.Collection;

@RestController
@RequestMapping("students")
public class StudentController {

    private final StudentService studentService;

    private final FacultyService facultyService;

    private final AvatarService avatarService;

    @Autowired
    public StudentController(StudentService studentService,
                             FacultyService facultyService,
                             AvatarService avatarService) {
        this.studentService = studentService;
        this.facultyService = facultyService;
        this.avatarService = avatarService;
    }

    @GetMapping("{id}") //GET
    public ResponseEntity<Student> getStudentInfo(@PathVariable Long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);

    }

    @PostMapping   //POST
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @PutMapping //PUT
    public ResponseEntity<Student> editStudent(@RequestBody Student student) {
        Student foundStudent = studentService.editStudent(student);
        if (foundStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("{id}") //DELETE
    public ResponseEntity<Student> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public ResponseEntity<Collection<Student>> getAllStudent() {
        return ResponseEntity.ok(studentService.getAllStudent());
    }

    @GetMapping("/filter/{age}")
    public ResponseEntity<Collection<Student>> filteredByAge(@RequestParam int age) {
        return ResponseEntity.ok(studentService.getStudentByAge(age));
    }

    @GetMapping(("/filter/between"))
    public ResponseEntity<Collection<Student>> findStudentBetweenAge(@RequestParam int minAge,
                                                                     @RequestParam int maxAge) {
        return ResponseEntity.ok(studentService.findStudentBetweenAge(minAge, maxAge));
    }

    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<Collection<Student>> getStudentsByFaculty(@PathVariable Long facultyId) {
        Collection<Student> students = studentService.findStudentByFacultyId(facultyId);
        if (students.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{studentId}/faculty")
    public ResponseEntity<Faculty> getFacultyByStudent(@PathVariable Long studentId) {
        Student student = studentService.findStudent(studentId);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        Faculty faculty = student.getFaculty();
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploaderAvatar(@PathVariable Long id,
                                                 @RequestParam("avatar") MultipartFile avatar) throws IOException {
        if (avatar.getSize() >= 1024 * 300) {
            return ResponseEntity.badRequest().body("Your avatar size is too big");
        }
        avatarService.uploadAvatar(id, avatar);
        return ResponseEntity.ok().build();
    }
}