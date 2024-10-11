package ru.hogwarts.school.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class StudentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository; // Мок для репозитория

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private StudentService studentService;


    @InjectMocks
    private StudentController studentController;

    @Test
    public void createStudentTest() throws Exception {
        Long studentId = 1L;
        String name = "test";


        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);


        Student student = new Student();
        student.setId(studentId);
        student.setName(name);


        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/students")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(studentId))
                .andExpect(jsonPath("$.name").value(name));
    }

    @Test
    public void findStudentTest() throws Exception {

        Long studentId = 1L;
        String name = "test";


        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);


        Student student = new Student();
        student.setId(studentId);
        student.setName(name);

        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/1")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(studentId))
                .andExpect(jsonPath("$.name").value(name));

    }

    @Test
    public void editStudentTest() throws Exception {
        Long studentId = 1L;
        String name = "test";


        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);


        Student student = new Student();
        student.setId(studentId);
        student.setName(name);


        when(studentRepository.save(any(Student.class))).thenReturn(student);
        when(studentRepository.findById(any(Long.class))).thenReturn(Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/students")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(studentId))
                .andExpect(jsonPath("$.name").value(name));
    }


    @Test
    public void deleteStudentTest() throws Exception {
        Long studentId = 1L;
        String name = "test";

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);

        Student student = new Student();
        student.setId(studentId);
        student.setName(name);


        doNothing().when(studentRepository).deleteById(studentId);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/students/" + studentId))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllStudentsTest() throws Exception {
        Long studentId = 1L;
        String name = "test";

        Long studentId2 = 2L;
        String name2 = "test2";

        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);

        JSONObject studentObject2 = new JSONObject();
        studentObject2.put("name", name2);


        Student student = new Student();
        student.setId(studentId);
        student.setName(name);

        Student student2 = new Student();
        student2.setId(studentId2);
        student2.setName(name2);

        List<Student> students = new ArrayList<>();
        students.add(student);
        students.add(student2);

        when(studentRepository.findAll()).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(studentId))
                .andExpect(jsonPath("$[0].name").value(name))
                .andExpect(jsonPath("$[1].id").value(studentId2))
                .andExpect(jsonPath("$[1].name").value(name2));


    }

    @Test
    public void filterStudentsTest() throws Exception {
        Long studentId = 1L;
        String name = "test";
        int age = 20;
        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        Student student = new Student();
        student.setId(studentId);
        student.setName(name);
        student.setAge(age);

        Long studentId2 = 2L;
        String name2 = "test2";
        int age2 = 22;
        JSONObject studentObject2 = new JSONObject();
        studentObject2.put("name", name2);
        Student student2 = new Student();
        student2.setId(studentId2);
        student2.setName(name2);
        student2.setAge(age2);

        List<Student> students = new ArrayList<>();
        students.add(student);
        students.add(student2);

        List<Student> filteredStudents = new ArrayList<>();
        filteredStudents.add(student);

        when(studentRepository.getStudentByAge(20)).thenReturn(filteredStudents);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/filter/20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(studentId))
                .andExpect(jsonPath("$[0].name").value(name))
                .andExpect(jsonPath("$[0].age").value(age));
    }

    @Test
    public void findStudentBetweenAgeTest() throws Exception {
        Long studentId = 1L;
        String name = "test";
        int age = 20;
        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        Student student = new Student();
        student.setId(studentId);
        student.setName(name);
        student.setAge(age);

        Long studentId2 = 2L;
        String name2 = "test2";
        int age2 = 22;
        JSONObject studentObject2 = new JSONObject();
        studentObject2.put("name", name2);
        Student student2 = new Student();
        student2.setId(studentId2);
        student2.setName(name2);
        student2.setAge(age2);

        List<Student> students = new ArrayList<>();
        students.add(student);
        students.add(student2);


        when(studentRepository.findByAgeBetween(age, age2)).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/filter/between?minAge=20&maxAge=22"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(studentId))
                .andExpect(jsonPath("$[0].name").value(name))
                .andExpect(jsonPath("$[0].age").value(age))
                .andExpect(jsonPath("$[1].id").value(studentId2))
                .andExpect(jsonPath("$[1].name").value(name2))
                .andExpect(jsonPath("$[1].age").value(age2));
    }

    @Test
    public void getStudentsByFacultyTest() throws Exception {
        int facultyId = 1;
        String facultyName = "anything";
        String color = "anything";
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", facultyName);
        Faculty faculty = new Faculty();
        faculty.setId(facultyId);
        faculty.setName(facultyName);
        faculty.setColor(color);

        int facultyId2 = 2;
        String facultyName2 = "anything2";
        String color2 = "anything2";
        JSONObject facultyObject2 = new JSONObject();
        facultyObject2.put("name", facultyName2);
        Faculty faculty2 = new Faculty();
        faculty2.setId(facultyId2);
        faculty2.setName(facultyName2);
        faculty2.setColor(color2);

        List<Faculty> faculties = new ArrayList<>();
        faculties.add(faculty);
        faculties.add(faculty2);

        Long studentId = 1L;
        String name = "test";
        int age = 20;
        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        Student student = new Student();
        student.setId(studentId);
        student.setName(name);
        student.setAge(age);
        student.setFaculty(faculty);

        Long studentId2 = 2L;
        String name2 = "test2";
        int age2 = 22;
        JSONObject studentObject2 = new JSONObject();
        studentObject2.put("name", name);
        Student student2 = new Student();
        student2.setId(studentId2);
        student2.setName(name2);
        student2.setAge(age2);
        student2.setFaculty(faculty2);

        List<Student> expectedStudents = new ArrayList<>();
        expectedStudents.add(student);

        when(studentRepository.findStudentByFacultyId(facultyId)).thenReturn(expectedStudents);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(studentId))
                .andExpect(jsonPath("$[0].name").value(name))
                .andExpect(jsonPath("$[0].age").value(age));
//                .andExpect(jsonPath("$[0].facultyId").value(facultyId))
//                .andExpect(jsonPath("$[0].facultyName").value(facultyName))
//                .andExpect(jsonPath("$[0].facultyColor").value(faculty.getColor()));

    }

    @Test
    public void getFacultyByStudentTest() throws Exception {
        int facultyId = 1;
        String facultyName = "anything";
        String color = "anything";
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", facultyName);
        Faculty faculty = new Faculty();
        faculty.setId(facultyId);
        faculty.setName(facultyName);
        faculty.setColor(color);

        Long studentId = 1L;
        String name = "test";
        int age = 20;
        JSONObject studentObject = new JSONObject();
        studentObject.put("name", name);
        Student student = new Student();
        student.setId(studentId);
        student.setName(name);
        student.setAge(age);
        student.setFaculty(faculty);

        when(studentRepository.findById(studentId)).thenReturn(java.util.Optional.of(student));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/1/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(facultyId))
                .andExpect(jsonPath("$.name").value(facultyName))
                .andExpect(jsonPath("$.color").value(color));


    }


}