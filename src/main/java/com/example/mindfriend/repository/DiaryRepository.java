package com.example.mindfriend.repository;

import com.example.mindfriend.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    Optional<Diary> findByUser_userIdAndCreatedAt(Long userId, LocalDateTime createdAt);

    List<Diary> findByCreatedAtBetweenAndMainEmotion(LocalDateTime startDateTime, LocalDateTime endDateTime, Long emotion);

    List<Diary> findByCreatedAtBetweenAndContentContaining(LocalDateTime startDateTime, LocalDateTime endDateTime,String keyword);
    List<Diary> findByCreatedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
}
