package com.example.mindfriend.repository;

import com.example.mindfriend.domain.Diary;
import com.example.mindfriend.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Long> {
    List<Diary> findByUserAndCreatedAtBetween(User user, LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<Diary> findByCreatedAtBetweenAndMainEmotion(LocalDateTime startDateTime, LocalDateTime endDateTime, Long emotion);

    List<Diary> findByCreatedAtBetweenAndContentContaining(LocalDateTime startDateTime, LocalDateTime endDateTime, String keyword);

    List<Diary> findByCreatedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);
    @Query("SELECT d FROM Diary d WHERE d.user = :user AND FUNCTION('DATE', d.createdAt) = FUNCTION('DATE', :today)")
    Diary findDiariesCreatedToday(@Param("user") User user, @Param("today") LocalDateTime today);
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Diary d SET d.mainEmotion = CASE" +
            "    WHEN d.angry >= d.disgust AND d.angry >= d.fear AND d.angry >= d.happiness " +
            "    AND d.angry >= d.neutral AND d.angry >= d.sadness AND d.angry >= d.surprise THEN '분노' " +
            "    WHEN d.disgust >= d.angry AND d.disgust >= d.fear AND d.disgust >= d.happiness " +
            "    AND d.disgust >= d.neutral AND d.disgust >= d.sadness AND d.disgust >= d.surprise THEN '혐오' " +
            "    WHEN d.fear >= d.angry AND d.fear >= d.disgust AND d.fear >= d.happiness " +
            "    AND d.fear >= d.neutral AND d.fear >= d.sadness AND d.fear >= d.surprise THEN '두려움' " +
            "    WHEN d.happiness >= d.angry AND d.happiness >= d.disgust AND d.happiness >= d.fear " +
            "    AND d.happiness >= d.neutral AND d.happiness >= d.sadness AND d.happiness >= d.surprise THEN '행복' " +
            "    WHEN d.neutral >= d.angry AND d.neutral >= d.disgust AND d.neutral >= d.fear " +
            "    AND d.neutral >= d.happiness AND d.neutral >= d.sadness AND d.neutral >= d.surprise THEN '중립' " +
            "    WHEN d.sadness >= d.angry AND d.sadness >= d.disgust AND d.sadness >= d.fear " +
            "    AND d.sadness >= d.happiness AND d.sadness >= d.neutral AND d.sadness >= d.surprise THEN '슬픔' " +
            "    ELSE '놀람' " +
            "END " +
            "WHERE d.diaryIdx = :diaryIdx")
    void updateMainEmotion(@Param("diaryIdx") Long diaryIdx);

    @Query(value = "SELECT SUM(d.angry), SUM(d.disgust), SUM(d.fear), " +
            "SUM(d.happiness), SUM(d.neutral)," +
            "SUM(d.sadness), SUM(d.surprise) " +
            "FROM Diary d " +
            "WHERE d.user = :user AND d.createdAt >= :startDate AND d.createdAt <= :endDate")
    List<Object[]> sumEmotionsByUserAndDate(@Param("user") User user, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);


}
