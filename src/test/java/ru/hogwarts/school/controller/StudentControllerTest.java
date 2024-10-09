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
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentControllerTest {
    @LocalServerPort
    private int port;


    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private StudentService studentService;

    @MockBean
    private FacultyService facultyService;


    @Test
    public void testGetStudents() throws Exception {
        Assertions.
                assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/students", String.class))
                .isNotNull();
    }

    @Test
    public void testPostStudent() throws Exception {
        Student student = new Student(1L, "John", 20);
        when(studentService.createStudent(student)).thenReturn(student);
        Assertions.
                assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/students",
                        student, String.class)).isEqualTo("{\"id\":1,\"name\":\"John\",\"age\":20,\"faculty\":null}");
    }

    @Test
    public void testDeleteStudent() throws Exception {
        Long studentId = 1L;
        doNothing().when(studentService).deleteStudent(studentId);
        ResponseEntity<Void> response = restTemplate.exchange("/students/" + studentId, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(studentService, times(1)).deleteStudent(studentId);
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
        when(studentService.editStudent(any(Student.class))).thenReturn(updatedStudent);
        HttpEntity<Student> request = new HttpEntity<>(updatedStudent);
        ResponseEntity<Student> response = restTemplate.exchange("/students",
                HttpMethod.PUT, request, Student.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedStudent, response.getBody());
        verify(studentService, times(1)).editStudent(any(Student.class));
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
        when(studentService.getAllStudent()).thenReturn(students);
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
        when(studentService.getStudentByAge(20)).thenReturn(students);
        ObjectWriter ow = new ObjectMapper().writer();
        String json = ow.writeValueAsString(students);
        Assertions.
                assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/students/filter/20", String.class))
                .isEqualTo(json);
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
        when(studentService.findStudentBetweenAge(20, 28)).thenReturn(students);
        ObjectWriter ow = new ObjectMapper().writer();
        String json = ow.writeValueAsString(students);
        Assertions.
                assertThat(this.restTemplate.getForObject("http://localhost:" + port
                        + "/students/filter/between?minAge=20&maxAge=28", String.class))
                .isEqualTo(json);
    }

}