package com.example.mindfriend.repository;

import com.example.mindfriend.domain.Diary;
import com.example.mindfriend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findByUserAndCreatedAtBetween(User user, LocalDateTime startDateTime, LocalDateTime endDateTime);


    List<Diary> findByCreatedAtBetweenAndMainEmotion(LocalDateTime startDateTime, LocalDateTime endDateTime, Long emotion);

    List<Diary> findByCreatedAtBetweenAndContentContaining(LocalDateTime startDateTime, LocalDateTime endDateTime, String keyword);

    List<Diary> findByCreatedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
    @Query("SELECT d FROM Diary d WHERE d.user = :user AND FUNCTION('DATE', d.createdAt) = FUNCTION('DATE', :today)")
    Diary findDiariesCreatedToday(@Param("user") User user, @Param("today") LocalDateTime today);

}
