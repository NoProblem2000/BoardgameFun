package com.petproject.boardgamefun.service

import com.petproject.boardgamefun.dto.DiaryDataDTO
import com.petproject.boardgamefun.dto.projection.DiaryWithRatingsProjection
import com.petproject.boardgamefun.exception.BadRequestException
import com.petproject.boardgamefun.exception.NoRecordFoundException
import com.petproject.boardgamefun.model.Diary
import com.petproject.boardgamefun.repository.DiaryRepository
import com.petproject.boardgamefun.repository.GameRepository
import com.petproject.boardgamefun.repository.UserRepository
import com.petproject.boardgamefun.service.mappers.DiaryMapper
import org.springframework.stereotype.Service
import java.time.OffsetDateTime
import javax.transaction.Transactional

@Service
open class DiaryService(private val diaryMapper: DiaryMapper,
                        private val diaryRepository: DiaryRepository,
                        private val userRepository: UserRepository,
                        private val gameRepository: GameRepository) {

    @Transactional
    open fun getDiaries(userId: Int?, gameId: Int?): List<DiaryDataDTO> {
        return if (userId != null)
            projectionsToDiaryDTO(diaryRepository.findUserDiaries(userId))
        else if (gameId != null)
            projectionsToDiaryDTO(diaryRepository.findGameDiaries(gameId))
        else projectionsToDiaryDTO(diaryRepository.allDiaries)
    }

    @Transactional
    open fun getDiaryById(diaryId: Int?): DiaryDataDTO {
        return projectionToDiaryDTO(diaryRepository.findDiaryUsingId(diaryId))
    }

    @Transactional
    open fun addDiary(userId: Int?, gameId: Int?, diary: Diary?): DiaryDataDTO {
        if (diary == null || diary.game == null) {
            throw BadRequestException("Invalid diary model")
        }

        val user = userRepository.findUserById(userId)
        val game = gameRepository.findGameById(gameId)

        if (game == null || user == null) {
            throw NoRecordFoundException("User or game not found")
        }

        diary.user = user
        diary.game = game
        diary.publicationTime = OffsetDateTime.now()
        diaryRepository.save(diary)

        return entityToDiaryDTO(diary)
    }

    @Transactional
    open fun deleteDiary(userId: Int?, diaryId: Int?): String {
        val diary = diaryRepository.findDiary_ByUserIdAndId(userId, diaryId)
                ?: throw NoRecordFoundException("No such diary found")
        diaryRepository.delete(diary)
        return diary.title
    }

    @Transactional
    open fun updateDiary(diaryId: Int?, userId: Int?, diaryRequest: Diary): DiaryDataDTO{
        if (diaryRequest.id == null) {
            throw BadRequestException("Invalid diary request model")
        }

        val diary = diaryRepository.findDiary_ByUserIdAndId(userId, diaryId)
                ?: throw NoRecordFoundException("No such diary found")

        if (diaryRequest.title != null && diary.title != diaryRequest.title) {
            diary.title = diaryRequest.title
        }
        if (diaryRequest.text != null && diary.text != diaryRequest.text) {
            diary.text = diaryRequest.text
        }

        diaryRepository.save(diary)
        return entityToDiaryDTO(diary)
    }

    fun projectionsToDiaryDTO(projections: List<DiaryWithRatingsProjection>): List<DiaryDataDTO> {
        val diaries: MutableList<DiaryDataDTO> = ArrayList()
        for (projection in projections) {
            diaries.add(DiaryDataDTO(diaryMapper.diaryToDiaryDTO(projection.diary), projection.rating))
        }
        return diaries
    }

    fun entityToDiaryDTO(diary: Diary): DiaryDataDTO {
        return DiaryDataDTO(diaryMapper.diaryToDiaryDTO(diary), 0.0)
    }

    fun projectionToDiaryDTO(projection: DiaryWithRatingsProjection): DiaryDataDTO {
        return DiaryDataDTO(diaryMapper.diaryToDiaryDTO(projection.diary), projection.rating)
    }
}