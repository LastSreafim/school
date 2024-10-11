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
import ru.hogwarts.school.repositories.FacultyRepository;
import ru.hogwarts.school.repositories.StudentRepository;
import ru.hogwarts.school.service.FacultyService;

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
public class FacultyControllerTest {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository; // Мок для репозитория

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private FacultyService facultyService;

    @InjectMocks
    private FacultyController facultyController;

    @Test
    public void getFacultyInfoTest() throws Exception {
        int facultyId = 1;
        String facultyName = "Faculty Name";
        String color = "Color";

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", facultyName);

        Faculty faculty = new Faculty();
        faculty.setId(facultyId);
        faculty.setName(facultyName);
        faculty.setColor(color);

        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/1")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(facultyId))
                .andExpect(jsonPath("$.name").value(facultyName))
                .andExpect(jsonPath("$.color").value(color));

    }

    @Test
    public void createFacultyTest() throws Exception {
        int facultyId = 1;
        String facultyName = "anything";
        String color = "Color";

        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", facultyName);

        Faculty faculty = new Faculty();
        faculty.setId(facultyId);
        faculty.setName(facultyName);
        faculty.setColor(color);


        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(facultyId))
                .andExpect(jsonPath("$.name").value(facultyName))
                .andExpect(jsonPath("$.color").value(color));

    }

    @Test
    public void editFacultyTest() throws Exception {
        int facultyId = 1;
        String facultyName = "anything";
        String color = "Color";
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", facultyName);
        Faculty faculty = new Faculty();
        faculty.setId(facultyId);
        faculty.setName(facultyName);
        faculty.setColor(color);

        when(facultyRepository.save(any(Faculty.class))).thenReturn(faculty);
        when(facultyRepository.findById(any(Long.class))).thenReturn(Optional.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty/1")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(facultyId))
                .andExpect(jsonPath("$.name").value(facultyName))
                .andExpect(jsonPath("$.color").value(color));
    }

    @Test
    public void deleteFacultyTest() throws Exception {
        long facultyId = 1L;
        String facultyName = "anything";
        String color = "Color";
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", facultyName);
        Faculty faculty = new Faculty();
        faculty.setId(facultyId);
        faculty.setName(facultyName);
        faculty.setColor(color);

        doNothing().when(facultyRepository).deleteById(facultyId);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + facultyId))
                .andExpect(status().isOk());

    }

    @Test
    public void getAllFacultyTest() throws Exception {
        long facultyId = 1L;
        String facultyName = "anything";
        String color = "Color";
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", facultyName);
        Faculty faculty = new Faculty();
        faculty.setId(facultyId);
        faculty.setName(facultyName);
        faculty.setColor(color);

        long facultyId2 = 2L;
        String facultyName2 = "anything2";
        String color2 = "Color2";
        JSONObject facultyObject2 = new JSONObject();
        facultyObject2.put("name", facultyName2);
        Faculty faculty2 = new Faculty();
        faculty2.setId(facultyId2);
        faculty2.setName(facultyName2);
        faculty2.setColor(color2);

        List<Faculty> faculties = new ArrayList<>();

        when(facultyRepository.findAll()).thenReturn(faculties);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(facultyId))
                .andExpect(jsonPath("$[0].name").value(facultyName))
                .andExpect(jsonPath("$[1].id").value(facultyId2))
                .andExpect(jsonPath("$[1].name").value(facultyName2));
    }

    @Test
    public void facultyFilterTest() throws Exception {
        long facultyId = 1L;
        String facultyName = "anything";
        String color = "Color";
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", facultyName);
        Faculty faculty = new Faculty();
        faculty.setId(facultyId);
        faculty.setName(facultyName);
        faculty.setColor(color);

        long facultyId2 = 2L;
        String facultyName2 = "anything2";
        String color2 = "Color2";
        JSONObject facultyObject2 = new JSONObject();
        facultyObject2.put("name", facultyName2);
        Faculty faculty2 = new Faculty();
        faculty2.setId(facultyId2);
        faculty2.setName(facultyName2);
        faculty2.setColor(color2);

        List<Faculty> faculties = new ArrayList<>();
        faculties.add(faculty);
        faculties.add(faculty2);

        List<Faculty> filteredFaculties = new ArrayList<>();
        filteredFaculties.add(faculty);

        when(facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase("anything", "color")).thenReturn(filteredFaculties);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/filter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(facultyId))
                .andExpect(jsonPath("$[0].name").value(facultyName))
                .andExpect(jsonPath("$[0].color").value(color));


    }

}
