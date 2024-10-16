package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    public Student createStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        return studentRepository.findById(id).get();
    }

    public Student editStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        studentRepository.deleteById(id);
    }

    public Collection<Student> getAllStudent() {
        return studentRepository.findAll();
    }

    public Collection<Student> getStudentByAge(int age) {
        return studentRepository.getStudentByAge(age);
    }

    public Collection<Student> findStudentBetweenAge(int minAge, int maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Collection<Student> findStudentByFacultyId(long facultyId) {
        return studentRepository.findStudentByFacultyId(facultyId);
    }

    public Integer getStudentsCount() {
        return studentRepository.getStudentsCount();
    }

    public Double getAverageAge() {
        return studentRepository.getAverageAge();
    }

    public List<Student> findLastFiveStudents() {
        return studentRepository.findLastFiveStudents();
    }


}
