package ru.hogwarts.school.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Avatar;

import java.util.Optional;

@Repository
public interface AvatarRepository extends JpaRepository<Avatar, Long> {

    Optional<Avatar> findByStudentId(Long studentId);

    Avatar findAvatarByStudentId(Long studentId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM avatar WHERE student_id =:studentId", nativeQuery = true )
    void deleteAvatarByStudentId(Long studentId);



}
