package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.Diary;
import com.petproject.boardgamefun.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiaryRepository extends JpaRepository<Diary, Integer> {
    Diary findByUserAndId(User user, Integer id);


    Diary findDiary_ByUserIdAndId(Integer userId, Integer id);


    List<Diary>findDiary_ByUserId(Integer userId);


}