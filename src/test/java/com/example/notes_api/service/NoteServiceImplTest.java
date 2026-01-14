package com.example.notes_api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.notes_api.dto.NoteRequest;
import com.example.notes_api.dto.NoteResponse;
import com.example.notes_api.exception.NoteNotFoundException;
import com.example.notes_api.model.Note;
import com.example.notes_api.repository.NoteRepository;

@ExtendWith(MockitoExtension.class) //Activa el soporte de Mockito en JUnit 5
public class NoteServiceImplTest {

    @Mock //Crea un objeto simulado de NoteRepository
    private NoteRepository noteRepository;

    @InjectMocks
    private INoteService noteService; //Inyecta los mocks en NoteServiceImpl

    @Test
    void shouldCreateNoteSuccessfully() {
        // Aquí iría la lógica de la prueba unitaria para el método de creación de notas

        //1 Arrange (Preparar) -> Datos de entrada
        NoteRequest request = new NoteRequest();
        request.setTitle("Test Title");
        request.setContent("Test Content");

        Note savedNote = new Note();
        savedNote.setTitle("Test Title");
        savedNote.setContent("Test Content");

        //2 Mock del Repository para simular el comportamiento del método save
        when(noteRepository.save(any(Note.class)))
                            .thenReturn(savedNote);

        //3 Act (Actuar) -> Llamar al método a probar
        NoteResponse response = noteService.create(request);

        //4 Assert (Afirmar) -> Verificar los resultados
        assertNotNull(response);
        assertEquals("Test Title", response.getTitle());
        assertEquals("Test Content", response.getContent());

        //5 Veritifación de interacciones con el mock
        verify(noteRepository, times(1)).save(any(Note.class));
        
    }

    @Test //Test si existe el id
    void shouldReturnNoteWhenIdExists() {

        // Arrange (Preparar) -> Datos de entrada
        Long noteId = 1L;

        Note note = new Note();
        note.setTitle("Test title");
        note.setContent("Test content");

        // Mock del Repository para simular el comportamiento del método findById
        when(noteRepository.findById(noteId))
                            .thenReturn(Optional.of(note));

        // Act (Actuar) -> Llamar al método a probar
        NoteResponse response = noteService.getById(noteId);

        // Assert (Afirmar) -> Verificar los resultados
        assertNotNull(response);
        assertEquals("Test title", response.getTitle());
        assertEquals("Test content", response.getContent());

        // Veritifación de interacciones con el mock
        verify(noteRepository, times(1)).findById(noteId);

    }

    @Test
    // Aquí iría la lógica de la prueba unitaria para el caso cuando la nota no existe
    void shouldThrowExceptionWhenNoteDoesNotExist() {

        // Arrange (Preparar) -> Datos de entrada
        Long noteId = 99L;

        // Mock del Repository para simular el comportamiento del método findById
        when(noteRepository.findById(noteId))
                            .thenReturn(Optional.empty()); // Simula que no se encuentra la nota en la BD .empty()
        
        // Act y Assert (Actuar y Afirmar) -> Llamar al método a probar y verificar que lanza la excepción
        NoteNotFoundException exception = 
                            assertThrows(NoteNotFoundException.class, () -> { // Lanza la excepción cuando no se encuentra la nota
                                noteService.getById(noteId);
                            });

        // Verificar el mensaje de la excepción
        assertEquals("Note not found with id: 99", exception.getMessage());

        // Veritifación de interacciones con el mock
        verify(noteRepository, times(1)).findById(noteId);

    }
    
    @Test
    void shouldUpdateNoteWhenIdExists() {

        //Arrange (Preparar) -> Datos de entrada
        Long noteId = 1L;
        NoteRequest request = new NoteRequest();
        request.setTitle("Updated Title");
        request.setContent("Updated Content");

        Note existingNote = new Note();
        existingNote.setTitle("Old Title");
        existingNote.setContent("Old Content");

        Note updateNote = new Note();
        updateNote.setTitle("Updated Title");
        updateNote.setContent("Updated Content");

        //Mock del Repository para simular el comportamiento del método findById y save
        when(noteRepository.findById(noteId))
                            .thenReturn(Optional.of(existingNote));

        when(noteRepository.save(any(Note.class)))
                            .thenReturn(existingNote);

        //Act (Actuar) -> Llamar al método a probar
        NoteResponse response = noteService.update(noteId, request);

        //Assert (Afirmar) -> Verificar los resultados
        assertNotNull(response);
        assertEquals("Updated Title", response.getTitle());
        assertEquals("Updated Content", response.getContent());

        //Veritifación de interacciones con el mock
        verify(noteRepository, times(1)).findById(noteId);
        verify(noteRepository, times(1)).save(any(Note.class));




    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingNote() {

        Long noteId = 99L;

        NoteRequest request = new NoteRequest();
        request.setTitle("Updated Title");
        request.setContent("Updated Content");

        when(noteRepository.findById(noteId))
                            .thenReturn(Optional.empty());
        
        NoteNotFoundException exception = 
                            assertThrows(NoteNotFoundException.class, () -> {
                                noteService.update(noteId, request);
                            });

        // Verificar el mensaje de la excepción
        assertEquals("Note not found with id: 99", exception.getMessage());

        // Veritifación de interacciones con el mock
        verify(noteRepository).findById(noteId);
        verify(noteRepository, never()).save(any());


    }

    @Test
    void shouldDeleteNoteWhenIdExists() {

        Long noteId = 1L;

        Note existiNote = new Note();

        when(noteRepository.findById(noteId))
                            .thenReturn(Optional.of(existiNote));
        
        noteService.deleteById(noteId);

        verify(noteRepository).findById(noteId);
        verify(noteRepository).delete(existiNote);


    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingNote() {

        Long noteId = 99L;

        when(noteRepository.findById(noteId))
                            .thenReturn(Optional.empty());
        
        NoteNotFoundException exception = 
                            assertThrows(NoteNotFoundException.class, () -> {
                                noteService.deleteById(noteId);
                            });

        // Verificar el mensaje de la excepción
        assertEquals("Note not found with id: 99", exception.getMessage());

        // Veritifación de interacciones con el mock
        verify(noteRepository).findById(noteId);
        verify(noteRepository, never()).delete(any());

    }



}
