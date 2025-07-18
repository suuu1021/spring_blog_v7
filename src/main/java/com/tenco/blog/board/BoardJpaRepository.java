package com.tenco.blog.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

// @Repository 생략 가능 --> JpaRepository -> 선언 되어 있음
public interface BoardJpaRepository extends JpaRepository<Board, Long> {

    // 게시글 목록 - 페이징 처리
    @Query("SELECT b FROM Board b JOIN FETCH b.user u ORDER BY b.id DESC")
    Page<Board> findAllJoinUser(Pageable pageable);
//
    // 게시글 상세 보기
    @Query("SELECT b FROM Board b JOIN FETCH b.user u WHERE b.id = :id")
    Optional<Board> findByIdJoinUser(@Param("id") Long id);

}
