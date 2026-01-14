package com.example.notes_api.mapper;

import com.example.notes_api.dto.NoteRequest;
import com.example.notes_api.dto.NoteResponse;
import com.example.notes_api.model.Note;


/*
    Un Mapper es una clase cuya única responsabilidad es:

    Transformar un objeto en otro
    sin lógica de negocio, sin BD, sin reglas.

    Nada más.

*/
public class NoteMapper {

    public static NoteResponse toResponse(Note note) {
        NoteResponse response = new NoteResponse();
        response.setId(note.getId());
        response.setTitle(note.getTitle());
        response.setContent(note.getContent());
        response.setCreatedAt(note.getCreatedAt());
        response.setUpdatedAt(note.getUpdatedAt());
        return response;
    }

    public static Note toEntity(NoteRequest request) {
        Note note = new Note();
        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        return note;
    }

}
