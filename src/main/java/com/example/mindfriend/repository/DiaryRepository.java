package com.example.mindfriend.repository;

import com.example.mindfriend.domain.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findByUser_userIdAndCreatedAt(Long userId, LocalDateTime createdAt);

    @Modifying
    @Query("delete from Diary d where d.diaryIdx in : ids")
    List<Diary> deleteAllByIds(@Param("ids")List<Long> ids);
}
