package com.petproject.boardgamefun.service;

import com.petproject.boardgamefun.dto.DiaryCommentDTO;
import com.petproject.boardgamefun.dto.request.DiaryCommentRequest;
import com.petproject.boardgamefun.exception.NoRecordFoundException;
import com.petproject.boardgamefun.model.DiaryComment;
import com.petproject.boardgamefun.repository.DiaryCommentRepository;
import com.petproject.boardgamefun.repository.DiaryRepository;
import com.petproject.boardgamefun.repository.UserRepository;
import com.petproject.boardgamefun.service.mappers.UserMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class DiaryCommentService {

    final UserMapper userMapper;
    final DiaryCommentRepository diaryCommentRepository;

    final UserRepository userRepository;

    final DiaryRepository diaryRepository;

    public DiaryCommentService(UserMapper userMapper, DiaryCommentRepository diaryCommentRepository, UserRepository userRepository, DiaryRepository diaryRepository) {
        this.userMapper = userMapper;
        this.diaryCommentRepository = diaryCommentRepository;
        this.userRepository = userRepository;
        this.diaryRepository = diaryRepository;
    }

    @Transactional
    public List<DiaryCommentDTO> getDiaryComments(Integer diaryId) {
        return entitiesToCommentDTO(diaryCommentRepository.findDiaryComment_ByDiaryId(diaryId));
    }

    @Transactional
    public List<DiaryCommentDTO> addDiaryComment(Integer diaryId, Integer userId, DiaryCommentRequest diaryCommentRequest) {
        var user = userRepository.findUserById(userId);
        var diary = diaryRepository.findDiaryById(diaryId);

        if (user == null || diary == null) {
            throw new NoRecordFoundException("User or diary not found");
        }

        var diaryComment = new DiaryComment();
        diaryComment.setDiary(diary);
        diaryComment.setUser(user);
        diaryComment.setCommentTime(OffsetDateTime.now());
        diaryComment.setComment(diaryCommentRequest.getComment());
        diaryCommentRepository.save(diaryComment);
        return entitiesToCommentDTO(diaryCommentRepository.findDiaryComment_ByDiaryId(diaryId));
    }

    @Transactional
    public List<DiaryCommentDTO> updateComment(Integer diaryId, Integer diaryCommentId, DiaryCommentRequest diaryCommentRequest) {
        var diaryComment = diaryCommentRepository.findDiaryCommentById(diaryCommentId);
        if (diaryComment == null) {
            throw new NoRecordFoundException("No such comment found");
        }
        if (diaryCommentRequest != null && !diaryCommentRequest.getComment().equals(diaryComment.getComment())) {
            diaryComment.setComment(diaryCommentRequest.getComment());
            diaryCommentRepository.save(diaryComment);
        }
        return entitiesToCommentDTO(diaryCommentRepository.findDiaryComment_ByDiaryId(diaryId));
    }

    @Transactional
    public List<DiaryCommentDTO> deleteComment(Integer diaryId, Integer diaryCommentId) {
        var diaryComment = diaryCommentRepository.findDiaryCommentById(diaryCommentId);
        if (diaryComment == null) {
            throw new NoRecordFoundException("No such comment found");
        }
        diaryCommentRepository.delete(diaryComment);
        return entitiesToCommentDTO(diaryCommentRepository.findDiaryComment_ByDiaryId(diaryId));
    }

    public DiaryCommentDTO entityToDiaryCommentDTO(DiaryComment diaryComment) {
        return new DiaryCommentDTO(diaryComment.getId(), diaryComment.getComment(), diaryComment.getCommentTime(), userMapper.userToUserDTO(diaryComment.getUser()));
    }

    public List<DiaryCommentDTO> entitiesToCommentDTO(List<DiaryComment> diaryComments) {
        ArrayList<DiaryCommentDTO> diaryCommentsDTO = new ArrayList<>();
        for (var diaryComment :
                diaryComments) {
            diaryCommentsDTO.add(new DiaryCommentDTO(diaryComment.getId(), diaryComment.getComment(), diaryComment.getCommentTime(), userMapper.userToUserDTO(diaryComment.getUser())));
        }
        return diaryCommentsDTO;
    }
}
