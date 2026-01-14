package com.example.notes_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.notes_api.dto.NoteRequest;
import com.example.notes_api.dto.NoteResponse;
import com.example.notes_api.exception.NoteNotFoundException;
import com.example.notes_api.mapper.NoteMapper;
import com.example.notes_api.model.Note;
import com.example.notes_api.repository.NoteRepository;

/*
        Como el repositorio y la base de datos trabajan con Entities,
        el service traduce entre Entities y DTOs para que el API nunca dependa de la BD.

*/

@Service
public class INoteService implements NoteService  {
    
    private final NoteRepository noteRepository;

    public INoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public NoteResponse create(NoteRequest noteRequest) {
        Note note = NoteMapper.toEntity(noteRequest); // Convierto en entidad DTO -> Entity
        Note savedNoted = noteRepository.save(note); // Guardo en BD como Entidad
        return NoteMapper.toResponse(savedNoted); // Convierto en DTO Entity -> DTO, y retorno el DTO 
    }

    @Override
    public List<NoteResponse> getAll() {
        return noteRepository.findAll() // Obtengo todas las entidades de la BD.
                             .stream() // Convierto la lista en un stream para poder mapear cada entidad a DTO
                             .map(NoteMapper::toResponse) // Mapeo cada entidad a su correspondiente DTO
                             .toList(); // Convierto el stream de DTOs de vuelta a una lista y la retorno
    }

    @Override
    public NoteResponse getById(Long id) {
        Note note = noteRepository.findById(id)
            .orElseThrow(() -> new NoteNotFoundException("Note not found with id: " + id));

        return NoteMapper.toResponse(note);
    }

    @Override
    public NoteResponse update(Long id, NoteRequest note) {
        Note existingNote = noteRepository.findById(id)
            .orElseThrow(() ->
                new NoteNotFoundException("Note not found with id: " + id)
            );

        existingNote.setTitle(note.getTitle());
        existingNote.setContent(note.getContent());

        Note updatedNote = noteRepository.save(existingNote);
        return NoteMapper.toResponse(updatedNote);
    }

    @Override
    public void deleteById(Long id) {
        Note existingNote = noteRepository.findById(id)
            .orElseThrow(() ->
                new NoteNotFoundException("Note not found with id: " + id)
            );

        noteRepository.delete(existingNote);
    }







}
