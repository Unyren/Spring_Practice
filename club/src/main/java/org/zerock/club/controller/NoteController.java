package org.zerock.club.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.zerock.club.dto.NoteDTO;
import org.zerock.club.security.dto.ClubAuthMemberDTO;
import org.zerock.club.service.NoteService;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/notes/*")//http://localhost:8080/notes/
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostMapping("")
    public ResponseEntity<Long> register(@RequestBody NoteDTO noteDTO) {
        Long num=noteService.register(noteDTO);

        return new ResponseEntity<>(num, HttpStatus.OK);
    }

    @GetMapping("/{num}")//http://localhost:8080/notes/
    public ResponseEntity<NoteDTO> read(@PathVariable Long num) {
        return new ResponseEntity<>(noteService.get(num), HttpStatus.OK);
    }

    @GetMapping("/all")//http://localhost:8080/notes/all
    public ResponseEntity<List<NoteDTO>> getList(String email) {
        return new ResponseEntity<>(noteService.getAllWithWriter(email), HttpStatus.OK);
    }
    @DeleteMapping(value="/{num}",produces = MediaType.TEXT_PLAIN_VALUE)//http://localhost:8080/notes/
    public ResponseEntity<String> remove(@PathVariable Long num) {
        noteService.remove(num);
        return new ResponseEntity<>("removed", HttpStatus.OK);
    }

    @PutMapping(value="/{num}",produces = MediaType.TEXT_PLAIN_VALUE)//http://localhost:8080/notes/
    public ResponseEntity<String> modify(@RequestBody NoteDTO noteDTO) {
        noteService.modify(noteDTO);
        return new ResponseEntity<>("modified", HttpStatus.OK);
    }
}
