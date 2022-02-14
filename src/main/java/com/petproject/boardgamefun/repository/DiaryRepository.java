package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.dto.projection.DiaryWithRatingsProjection;
import com.petproject.boardgamefun.model.Diary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Integer> {
    // todo: only diary id?
    Diary findDiary_ByUserIdAndId(Integer userId, Integer id);

    @Query("select d as diary, avg(dr.rating) as rating from Diary d " +
            "left join DiaryRating dr on d.id = dr.diary.id " +
            "join User u on u.id = d.user.id " +
            "where u.id = :userId " +
            "group by d")
    List<DiaryWithRatingsProjection>findUserDiaries(Integer userId);

    @Query("select d as diary, avg(dr.rating) as rating from Diary d " +
            "left join DiaryRating dr on d.id = dr.diary.id " +
            "join Game g on g.id = d.game.id " +
            "where g.id = :gameId " +
            "group by d")
    List<DiaryWithRatingsProjection> findGameDiaries(Integer gameId);

    @Query("select d as diary, avg(dr.rating) as rating from Diary d " +
            "left join DiaryRating dr on d.id = dr.diary.id " +
            "where d.id = :id " +
            "group by d")
    DiaryWithRatingsProjection findDiaryUsingId(Integer id);

    Diary findDiaryById(Integer id);

    @Query("select d as diary, avg(dr.rating) as rating from Diary d " +
            "left join DiaryRating dr on d.id = dr.diary.id" +
            " group by d")
    List<DiaryWithRatingsProjection> getAllDiaries();
}