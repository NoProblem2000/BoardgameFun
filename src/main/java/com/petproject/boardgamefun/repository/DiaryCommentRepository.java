package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.DiaryComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiaryCommentRepository extends JpaRepository<DiaryComment, Integer> {
    DiaryComment findDiaryCommentById(Integer id);
    List<DiaryComment> findDiaryComment_ByDiaryId(Integer diaryId);
}