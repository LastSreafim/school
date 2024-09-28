package ru.hogwarts.school.service;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {

    Map<Long, Student> students = new HashMap<>();

    private long idCounter = 0;

    public Collection<Student> getAllStudent() {
        return students.values();
    }

    public Collection<Student> getStudentByAge(int age) {
        return students.values()
                        .stream()
                .filter(student -> student.getAge() == age).collect(Collectors.toList());
    }

    public Student createStudent(Student student) {
        student.setId(++idCounter);
        students.put(student.getId(), student);
        return student;
    }

    public Student findStudent(long id) {
        return students.get(id);
    }

    public Student editStudent(Student student) {
        if (students.containsKey(student.getId())) {
            students.put(student.getId(), student);
            return student;
        }
        return null;
    }

    public Student deleteStudent(long id) {
        return students.remove(id);
    }

}
