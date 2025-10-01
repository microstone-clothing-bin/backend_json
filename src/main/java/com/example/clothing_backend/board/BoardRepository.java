package com.example.clothing_backend.board;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    // JpaRepository -> 기본적인 CRUD 메소드 다 제공해줌 (save, findById, findAll, deleteById 등)
}