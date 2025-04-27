package org.zerock.club.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.club.entity.ClubMember;
import org.zerock.club.entity.Note;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    @EntityGraph(attributePaths = "writer",type = EntityGraph.EntityGraphType.LOAD)
    @Query(" select m from Note m where m.num=:num ")
    Optional<Note> getWithWriter(Long num);

    @EntityGraph(attributePaths = {"writer"},type = EntityGraph.EntityGraphType.LOAD)
    @Query(" select m from Note m where m.writer.email=:email ")
    List<Note> getList(String email);
}
