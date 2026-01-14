package com.example.notes_api.service;

import java.util.List;

import com.example.notes_api.dto.NoteRequest;
import com.example.notes_api.dto.NoteResponse;


public interface NoteService {

    NoteResponse create (NoteRequest note);

    List<NoteResponse> getAll();

    NoteResponse getById(Long id);

    NoteResponse update(Long id, NoteRequest note);

    void deleteById(Long id);

}
