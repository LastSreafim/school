package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repositories.FacultyRepository;

import java.util.Collection;
import java.util.NoSuchElementException;

@Service
public class FacultyService {

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepository = facultyRepository;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method for create faculty");
        return facultyRepository.save(faculty);
    }

    public Faculty findFaculty(long id) {
        logger.info("Was invoked method for find faculty by id {}", id);
        try {
            return facultyRepository.findById(id).orElseThrow(() -> {
                logger.error("There is no faculty with id = {}", id); //тут
                return new NoSuchElementException("Faculty not found with id: " + id);
            });
        } catch (NoSuchElementException e) {
            logger.error("Error while finding student with id: {}", id, e);
            throw e;
        }
    }

    public Faculty editFaculty(Faculty faculty) {
        logger.info("Was invoked method for edit faculty");
        return facultyRepository.save(faculty);
    }

    public void deleteFaculty(long id) {
        logger.info("Was invoked method for delete faculty with id {}", id);
        facultyRepository.deleteById(id);
    }

    public Collection<Faculty> getFacultyByNameOrColor(String name, String color) {
       logger.info("Was invoked method for get faculty by name or color");
        return facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(name, color);
    }

    public Collection<Faculty> getAllFaculty() {
       logger.info("Was invoked method for get all faculty");
        return facultyRepository.findAll();
    }


}
