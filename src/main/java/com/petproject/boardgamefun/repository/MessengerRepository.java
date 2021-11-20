package com.petproject.boardgamefun.repository;

import com.petproject.boardgamefun.model.Messenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessengerRepository extends JpaRepository<Messenger, Integer> {
}