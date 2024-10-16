package ru.hogwarts.school.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Student;

import java.util.List;
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> getStudentByAge(int age);

    Student findStudentById(Long id);

    List<Student> findByAgeBetween(int minAge, int maxAge);

    List<Student> findStudentByFacultyId(long facultyId);

    @Query(value = "SELECT COUNT(*) FROM student", nativeQuery = true)
    Integer getStudentsCount();

    @Query(value = "SELECT AVG(age) FROM student", nativeQuery = true)
    Double getAverageAge();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> findLastFiveStudents();

}
