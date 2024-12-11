package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;

import javax.sql.DataSource;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class FacultyControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private FacultyRepository repository;

@Test
public void testDatabaseIsH2() throws SQLException {
    String databaseName = dataSource.getConnection().getMetaData().getDatabaseProductName();
    assertEquals("H2", databaseName);
}

    @Test
    public void testGetFaculty() throws Exception {
        Faculty faculty = new Faculty(1L, "test", "test");

        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty/1", String.class))
                .isNotNull();
    }

    @Test
    public void testCreateFaculty() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setName("test");
        faculty.setColor("test");
        faculty.setId(1L);

        assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/faculty", faculty, String.class))
                .isNotNull();

    }

    @Test
    public void testEditFaculty() {
        Faculty existingFaculty = new Faculty(null, "Old Name", "Old Color");
        existingFaculty = repository.save(existingFaculty);

        Faculty updatedFaculty = new Faculty(existingFaculty.getId(), "New Name", "New Color");

        HttpEntity<Faculty> request = new HttpEntity<>(updatedFaculty);
        ResponseEntity<Faculty> response = restTemplate.exchange(
                "http://localhost:" + port + "/faculty",
                HttpMethod.PUT,
                request,
                Faculty.class
        );

        // Проверяем ответ
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(response.getBody()).isNotNull();

        // Проверяем, что поля обновлены
        assertEquals("New Name", response.getBody().getName());
        assertEquals("New Color", response.getBody().getColor());
    }
    @Test
    public void testDeleteFaculty() throws Exception {
        Long facultyId = 1L;


        ResponseEntity<Void> response = restTemplate.exchange("/faculty/" + facultyId, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

    }


    @Test
    public void testGetAllFaculty() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/faculty", String.class))
                .isNotNull();
    }

    @Test
    public void testFilteredFacultyByColor() throws Exception {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port +
                "/faculty/filter?color=blue", String.class)).isNotNull();
    }
}