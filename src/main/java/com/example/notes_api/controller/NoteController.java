package com.example.notes_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.notes_api.dto.NoteRequest;
import com.example.notes_api.dto.NoteResponse;
import com.example.notes_api.service.NoteService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;





@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping
    public List<NoteResponse> getNotes() {
        return noteService.getAll();
    }

    @GetMapping("/{id}")
    NoteResponse getById(@PathVariable Long id) {
        return noteService.getById(id);
    }

    @PostMapping
    public NoteResponse creaNote(@Valid @RequestBody NoteRequest noteRequest) {
        return noteService.create(noteRequest);
    }

    @PutMapping("/{id}")
    public NoteResponse updateNoteById(@PathVariable Long id, @Valid @RequestBody NoteRequest noteRequest) {
        return noteService.update(id, noteRequest);    
    }

    @DeleteMapping("/{id}")
    public void deleteNoteById(@PathVariable Long id) {
        noteService.deleteById(id);
    }

}
