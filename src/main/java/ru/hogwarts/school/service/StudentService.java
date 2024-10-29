package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student");
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        logger.info("Was invoked method for find student with id");
        try {
            return studentRepository.findById(id).orElseThrow(() -> {
                logger.error("There is no student with id = {}", id); //тут
                return new NoSuchElementException("Student not found with id: " + id);
            });
        } catch (Exception e) {
            logger.error("Error while finding student with id: {}", id, e);
            throw e;
        }

    }

    public Student editStudent(Student student) {
        logger.info("Was invoked method for edit student with id");
        return studentRepository.save(student);
    }

    public void deleteStudent(long id) {
        logger.info("Was invoked method for delete student with id: {}", id);
        studentRepository.deleteById(id);
    }

    public Collection<Student> getAllStudent() {
        logger.info("Was invoked method for get all students");
        return studentRepository.findAll();
    }

    public Collection<Student> getStudentByAge(int age) {
        logger.info("Was invoked method for get student by age {}", age);
        return studentRepository.getStudentByAge(age);
    }

    public Collection<Student> findStudentBetweenAge(int minAge, int maxAge) {
        logger.info("Was invoked method for get student between age");
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Collection<Student> findStudentByFacultyId(long facultyId) {
        logger.info("Was invoked method for get student by faculty id {}", facultyId);
        return studentRepository.findStudentByFacultyId(facultyId);
    }

    public Integer getStudentsCount() {
        logger.info("Was invoked method for get student count");
        return studentRepository.getStudentsCount();
    }

    public Double getAverageAge() {
        logger.info("Was invoked method for get student average age");
        return studentRepository.getAverageAge();
    }

    public List<Student> findLastFiveStudents() {
        logger.info("Was invoked method for get last five students");
        return studentRepository.findLastFiveStudents();
    }

    public List<String> getNamesStartingWithA() {
        logger.info("Was invoked method for get student names starting with A");
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .filter(name -> name.startsWith("А"))
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());
    }

    public Double getAverageAgeWithStream() {
        logger.info("Was invoked method for get student average age with stream");
        return studentRepository.findAll().stream()
                .mapToDouble(Student::getAge)
                .average()
                .orElse(0.0);
    }


    public int oldGetSum() {
        logger.info("Was invoked method for old get student sum");

        long startTime = System.nanoTime();

        int sum = Stream.iterate(1, a -> a + 1)
                .limit(1_000_000)
                .reduce(0, (a, b) -> a + b);

        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        System.out.println("Время выполнения oldGetSum: " + duration + " мс");

        return sum;
        //32455600 мс
    }


    public int getSum() {
        logger.info("Was invoked method for get sum");

        long startTime = System.nanoTime();

        int sum = IntStream.rangeClosed(1, 1_000_000)
                .sum();

        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        System.out.println("Время выполнения getSum: " + duration + " мс");

        return sum;
        //5753400 мс
    }

    public void getStudentNameParallel() {

        logger.info("Was invoked method for get student name parallel");

        logger.info("Main thread was started");
        System.out.println(studentRepository.findStudentById(1L).getName());
        System.out.println(studentRepository.findStudentById(2L).getName());


        logger.info("Thread1 was started");
        Thread thread1 = new Thread(() -> {
            System.out.println(studentRepository.findStudentById(3L).getName());
            System.out.println(studentRepository.findStudentById(4L).getName());

        });

        thread1.start();

        logger.info("Thread2 was started");
        Thread thread2 = new Thread(() -> {
            System.out.println(studentRepository.findStudentById(5L).getName());
            System.out.println(studentRepository.findStudentById(6L).getName());
        });

        thread2.start();

        try {
            thread1.join();
            thread2.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void synchronizedGetStudentName() {
        logger.info("Was invoked method for get student name synchronized");

        synchronizedToPrint(1L);
        synchronizedToPrint(2L);



        Thread thread1 = new Thread(() -> {
            synchronizedToPrint(3L);
            synchronizedToPrint(4L);

        });

        thread1.start();

        Thread thread2 = new Thread(() -> {
            synchronizedToPrint(5L);
            synchronizedToPrint(6L);
        });

        thread2.start();

        try {
            thread1.join();
            thread2.join();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public synchronized void synchronizedToPrint(Long id) {
        System.out.println(studentRepository.findStudentById(id).getName());
    }


}




