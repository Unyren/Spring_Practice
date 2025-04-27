package org.zerock.club.service;

import org.zerock.club.dto.NoteDTO;
import org.zerock.club.entity.ClubMember;
import org.zerock.club.entity.Note;

import java.util.List;

public interface NoteService {
     Long register(NoteDTO noteDTO);//노트등록
     NoteDTO get(Long num);//해당글번호 노트 상세보기
    void modify(NoteDTO noteDTO);
    void remove(Long num);
    List<NoteDTO> getAllWithWriter(String writerEmail);

    default Note dtoToEntity(NoteDTO noteDTO) {
        ClubMember clubMember = ClubMember.builder()
                .email(noteDTO.getWriterEmail())
                .build();

        Note note= Note.builder()
                .num(noteDTO.getNum())
                .title(noteDTO.getTitle())
                .content(noteDTO.getContent())
                .writer(clubMember)
                .build();
        return note;
    }

    default NoteDTO entityToDTO(Note note) {
        NoteDTO noteDTO= NoteDTO.builder()
                .num(note.getNum())
                .title(note.getTitle())
                .content(note.getContent())
                .writerEmail(note.getWriter().getEmail())
                .regDate(note.getRegDate())
                .modDate(note.getModDate())
                .build();

        return noteDTO;
    }

}
