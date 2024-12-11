package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class StudentControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentController studentController;

    @Autowired
    private StudentRepository studentRepository;


    @Test
    public void testDatabaseIsH2() throws SQLException {
        String databaseName = dataSource.getConnection().getMetaData().getDatabaseProductName();
        assertEquals("H2", databaseName);
    }

    @Test
    public void testGetStudents() throws Exception {
        Assertions.
                assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/students", String.class))
                .isNotNull();
    }

    @Test
    public void testPostStudent() throws Exception {

        Student student = new Student(1L, "John", 20);

        Assertions.
                assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/students",
                        student, String.class)).isNotNull();
    }

    @Test
    public void testDeleteStudent() throws Exception {
        Long studentId = 1L;


        ResponseEntity<Void> response = restTemplate.exchange("/students/" + studentId, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

    }
    @Test
    public void testUpdateStudent() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Anybody");
        student.setAge(17);

        Student updatedStudent = new Student();

        updatedStudent.setId(2L);
        updatedStudent.setName("Updated Name");
        updatedStudent.setAge(17);


        HttpEntity<Student> request = new HttpEntity<>(updatedStudent);
        ResponseEntity<Student> response = restTemplate.exchange("/students",
                HttpMethod.PUT, request, Student.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        assertEquals(updatedStudent, response.getBody());

    }

    @Test
    public void testGetAllStudents() throws Exception {
        Collection<Student> students = new ArrayList<>();

        Student student1 = new Student(1L, "John", 20);
        Student student2 = new Student(2L, "Any", 23);
        Student student3 = new Student(3L, "Sergei", 27);

        students.add(student1);
        students.add(student2);
        students.add(student3);


        Assertions.
                assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/students", String.class))
                .isNotNull();
    }

    @Test
    public void testFilteredByAge() throws Exception {
        Collection<Student> students = new ArrayList<>();

        Student student1 = new Student(1L, "John", 20);
        Student student2 = new Student(2L, "Any", 20);
        Student student3 = new Student(3L, "Sergei", 20);

        students.add(student1);
        students.add(student2);
        students.add(student3);


        Assertions.
                assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/students/filter/20", String.class))
                .isNotNull();
    }

    @Test
    public void testFindStudentBetweenAge() throws Exception {
        Collection<Student> students = new ArrayList<>();

        Student student1 = new Student(1L, "John", 20);
        Student student2 = new Student(2L, "Any", 23);
        Student student3 = new Student(3L, "Sergei", 27);

        students.add(student1);
        students.add(student2);
        students.add(student3);


        Assertions.
                assertThat(this.restTemplate.getForObject("http://localhost:" + port
                        + "/students/filter/between?minAge=20&maxAge=28", String.class))
                .isNotNull();
    }

    @Test
    public void testGetStudentByFaculty() {
        Faculty faculty = new Faculty(1L, "Anything", "AnyColor");
        Student student = new Student();
        student.setId(1L);
        student.setName("John");
        student.setAge(17);
        student.setFaculty(faculty);

        List<Student> studentsInFaculty = new ArrayList<>();
        studentsInFaculty.add(student);


        ResponseEntity<Student[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/students/faculty/1",
                Student[].class
        );

        Assertions.assertThat(studentsInFaculty).isNotNull();
    }

    @Test
    public void testGetFacultyByStudent() throws Exception {

        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Anything");

        Student student = new Student();
        student.setId(1L);
        student.setFaculty(faculty);

        Assertions.
                assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/students/1/faculty", String.class))
                .isNotNull();
    }


}