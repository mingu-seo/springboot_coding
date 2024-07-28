package org.zerock.club.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.club.dto.NoteDTO;
import org.zerock.club.entity.Note;
import org.zerock.club.service.Noteservice;

import java.util.List;

@RestController
@Log4j2
@RequestMapping("/notes/")
@RequiredArgsConstructor
public class NoteController {
    private final Noteservice noteservice;

    @PostMapping("")
    public ResponseEntity<Long> register(@RequestBody NoteDTO noteDTO) {
        log.info("---------reigster-----------");
        log.info(noteDTO);
        Long num = noteservice.register(noteDTO);
        return new ResponseEntity<>(num, HttpStatus.OK);
    }

    @GetMapping(value="/{num}")
    public ResponseEntity<NoteDTO> read(@PathVariable Long num) {
        log.info("---------read-----------");
        log.info(num);
        return new ResponseEntity<>(noteservice.get(num), HttpStatus.OK);
    }

    @GetMapping(value="/all")
    public ResponseEntity<List<NoteDTO>> getList(String email) {
        log.info("---------getList-----------");
        log.info(email);
        return new ResponseEntity<>(noteservice.getAllWithWriter(email), HttpStatus.OK);
    }

    @DeleteMapping("/{num}")
    public ResponseEntity<String> remove(@PathVariable Long num) {
        log.info("---------remove-----------");
        log.info(num);
        noteservice.remove(num);
        return new ResponseEntity<>("removed", HttpStatus.OK);
    }

    @PutMapping("/{num}")
    public ResponseEntity<String> modify(@RequestBody NoteDTO noteDTO) {
        log.info("---------modify-----------");
        log.info(noteDTO);
        noteservice.modify(noteDTO);
        return new ResponseEntity<>("modified", HttpStatus.OK);
    }
}
