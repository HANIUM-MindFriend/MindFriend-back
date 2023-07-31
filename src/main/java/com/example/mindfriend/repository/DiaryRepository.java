package com.example.mindfriend.repository;

import com.example.mindfriend.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findByUser_userIdAndCreatedAt(Long userId, LocalDateTime createdAt);
}