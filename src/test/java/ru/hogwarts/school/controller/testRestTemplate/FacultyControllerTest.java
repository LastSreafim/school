package ru.hogwarts.school.controller.testRestTemplate;

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
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @MockBean
    private StudentService studentService;

    @MockBean
    private FacultyService facultyService;


    @Test
    public void testGetFaculty() throws Exception {
        Faculty faculty = new Faculty(1, "test", "test");

        when(facultyService.findFaculty(1)).thenReturn(faculty);

        Assertions.
                assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/1", String.class))
                .isNotNull();
    }

    @Test
    public void testCreateFaculty() throws Exception {
        Faculty faculty = new Faculty(1, "test", "test");

        when(facultyService.createFaculty(faculty)).thenReturn(faculty);

        ObjectWriter ow = new ObjectMapper().writer();
        String json = ow.writeValueAsString(faculty);

        Assertions.assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, String.class))
                .isEqualTo(json);

    }

    @Test
    public void testEditFaculty() throws Exception {
        Faculty faculty = new Faculty(1, "test", "test");
        Faculty faculty2 = new Faculty(2, "test2", "test2");

        when(facultyService.editFaculty(any(Faculty.class))).thenReturn(faculty2);

        HttpEntity<Faculty> request = new HttpEntity<>(faculty2);

        ResponseEntity<Faculty> response = restTemplate.exchange("/students",
                HttpMethod.PUT, request, Faculty.class);
    }

    @Test
    public void testDeleteFaculty() throws Exception {
        int facultyId = 1;

        doNothing().when(facultyService).deleteFaculty(facultyId);

        ResponseEntity<Void> response = restTemplate.exchange("/faculty/" + facultyId, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(facultyService, times(1)).deleteFaculty(facultyId);
    }


    @Test
    public void testGetAllFaculty() throws Exception {
        Faculty faculty = new Faculty(1, "test", "test");
        Faculty faculty2 = new Faculty(2, "test2", "test2");

        Collection<Faculty> faculties = new ArrayList<>();

        faculties.add(faculty);
        faculties.add(faculty2);

        when(facultyService.getAllFaculty()).thenReturn(faculties);

        ObjectWriter ow = new ObjectMapper().writer();
        String json = ow.writeValueAsString(faculties);

        Assertions.
                assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty", String.class))
                .isEqualTo(json);
    }

    @Test
    public void testFilteredFacultyByColor() throws Exception {
        Faculty faculty = new Faculty(1, "test", "test");
        Faculty faculty2 = new Faculty(2, "test2", "test");
        Faculty faculty3 = new Faculty(3, "test3", "blue");

        Collection<Faculty> faculties = new ArrayList<>();

        faculties.add(faculty);
        faculties.add(faculty2);
        faculties.add(faculty3);

        Collection<Faculty> filteredFaculties = new ArrayList<>();

        filteredFaculties.add(faculty3);
        when(facultyService.getFacultyByNameOrColor(null, "blue")).thenReturn(filteredFaculties);

        ObjectWriter ow = new ObjectMapper().writer();
        String json = ow.writeValueAsString(filteredFaculties);

        Assertions.assertThat(this.restTemplate.getForObject("http://localhost:" + port +
                "/faculty/filter?color=blue", String.class)).isEqualTo(json);
    }

}