package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.dto.projection.DiariesWithRatingsProjection;
import com.petproject.boardgamefun.model.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Integer> {
    Diary findDiary_ByUserIdAndId(Integer userId, Integer id);

    @Query("select d as diary, avg(dr.rating) as rating from Diary d " +
            "left join DiaryRating dr on d.id = dr.diary.id " +
            "join User u on u.id = d.user.id " +
            "where u.id = :userId " +
            "group by d")
    List<DiariesWithRatingsProjection>findUserDiaries(Integer userId);

    Diary findDiaryById(Integer id);

    @Query("select d as diary, avg(dr.rating) as rating from Diary d " +
            "left join DiaryRating dr on d.id = dr.diary.id" +
            " group by d")
    List<DiariesWithRatingsProjection> getAllDiaries();
}