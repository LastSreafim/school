package ru.hogwarts.school.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repositories.AvatarRepository;
import ru.hogwarts.school.repositories.StudentRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class AvatarService {
    @Value("${path.to.avatars.folder}")
    private String avatarsDir;

    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);

    private final StudentRepository studentRepository;

    private final StudentService studentService;

    private final AvatarRepository avatarRepository;

    public AvatarService(StudentRepository studentRepository, StudentService studentService, AvatarRepository avatarRepository) {
        this.studentRepository = studentRepository;
        this.studentService = studentService;
        this.avatarRepository = avatarRepository;
    }

    @Transactional
    public void uploadAvatar(Long studentId, MultipartFile avatarFile) throws IOException {
        logger.info("Was invoked method for uploading avatar for student with id: {}", studentId);

        try {
            Student student = studentRepository.getById(studentId);
            logger.debug("Retrieved student with id: {}", studentId);

            Path filePath = Path.of(avatarsDir, student.getId() + "." + getExtensions(avatarFile.getOriginalFilename()));
            logger.debug("Avatar file path: {}", filePath);

            Files.createDirectories(filePath.getParent());
            logger.debug("Created directories for file path: {}", filePath.getParent());

            Files.deleteIfExists(filePath);
            logger.debug("Deleted existing file if it exists: {}", filePath);

            avatarRepository.deleteAvatarByStudentId(studentId);
            logger.debug("Deleted avatar for student with id: {}", studentId);

            try (
                    InputStream is = avatarFile.getInputStream();
                    OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                    BufferedInputStream bis = new BufferedInputStream(is, 1024);
                    BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
            ) {
                bis.transferTo(bos);
                logger.debug("Transferred avatar file to: {}", filePath);
            }

            Avatar avatar = new Avatar();
            avatar.setStudent(student);
            avatar.setFilePath(filePath.toString());
            avatar.setFileSize(avatarFile.getSize());
            avatar.setMediaType(avatarFile.getContentType());
            avatar.setData(avatarFile.getBytes());
            avatarRepository.save(avatar);
            logger.debug("Saved avatar for student with id: {}", studentId);

            logger.info("Successfully uploaded avatar for student with id: {}", studentId);
        } catch (Exception e) {
            logger.error("Error while uploading avatar for student with id: {}", studentId, e);
            throw e;
        }
    }

    private String getExtensions(String fileName) {
       logger.info("Was invoked method for getting the extensions of file: {}", fileName);
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }


    public Avatar findAvatar(Long studentId) {
        logger.info("Was invoked method for finding avatar for student with id: {}", studentId);
        return avatarRepository.findAvatarByStudentId(studentId);
    }

}
