package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.Diary;
import com.petproject.boardgamefun.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Integer> {
    Diary findDiary_ByUserIdAndId(Integer userId, Integer id);

    List<Diary>findDiary_ByUserId(Integer userId);

    Diary findDiaryById(Integer id);


}