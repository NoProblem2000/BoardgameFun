package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.UserOwnGame;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserOwnGameRepository extends JpaRepository<UserOwnGame, Integer> {
    UserOwnGame findUserOwnGame_ByUserIdAndGameId(Integer userId, Integer gameId);
}
