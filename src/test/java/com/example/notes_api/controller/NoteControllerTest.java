package com.example.notes_api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.notes_api.dto.NoteRequest;
import com.example.notes_api.dto.NoteResponse;
import com.example.notes_api.exception.NoteNotFoundException;
import com.example.notes_api.service.NoteService;

@WebMvcTest(NoteController.class) //Spring Boot Test for NoteController -> Spring levanta la capa web
public class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc; //Objeto que simula las peticiones HTTP a la capa web

    @MockitoBean
    private NoteService noteService; //Simula la capa de servicio

    @Test
    void shouldReturnNoteWhenIdExists() throws Exception {

        //Arrange -> Preparar el escenario de la prueba
        NoteResponse response = new NoteResponse(
            1L,
            "Test title",
            "Test content",
            null,
            null
        );

        //Mockear el comportamiento del servicio    
        when(noteService.getById(1L)).thenReturn(response);


        //act & assert -> Ejecutar la acción y verificar el resultado
        mockMvc.perform(get("/api/notes/{id}", 1L)) //Simula una petición GET a /api/notes/1
            .andExpect(status().isOk()) //Verifica que el estatus de la respuesta sea 200 OK
            .andExpect(jsonPath("$.id").value(1L)) //Verifica que el campo id del JSON sea 1
            .andExpect(jsonPath("$.title").value("Test title")) //Verifica que el campo title del JSON sea "Test title"
            .andExpect(jsonPath("$.content").value("Test content")); //Verifica que el campo content del JSON sea "Test content"

    }

    @Test
    void shouldReturn404WhenNoteDoesNotExist() throws Exception {

        //Arrange -> Preparar el escenario de la prueba
        //Mockear el comportamiento del servicio    
        when(noteService.getById(99L))
        .thenThrow(new NoteNotFoundException("Note not found")); //Simula que el servicio lanza una excepción cuando la nota no existe

        //act & assert -> Ejecutar la acción y verificar el resultado
        mockMvc.perform(get("/api/notes/{id}", 99L)) //Simula una petición GET a /api/notes/99
            .andExpect(status().isNotFound()); //Verifica que el estatus de la respuesta sea 404 Not Found
        
    }

    @Test
    void shouldCreateNoteWhenRequestIsValid() throws Exception {

        //Arrange -> Preparar el escenario de la prueba

        String requestBody = """
        {
          "title": "My first note",
          "content": "This is a valid note"
        }
        """;

        NoteResponse response = new NoteResponse(
            1L,
            "My first note",
            "This is a valid note",
            null,
            null
        );

        //Mockear el comportamiento del servicio    
        when(noteService.create(any(NoteRequest.class))) //Cuando el servicio reciba cualquier NoteRequest
                        .thenReturn(response); //Simula que el servicio devuelve la nota creada

        //act & assert -> Ejecutar la acción y verificar el resultado
        mockMvc.perform(post("/api/notes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.title").value("My first note"))
            .andExpect(jsonPath("$.content").value("This is a valid note"));

    }

    @Test
    void shouldReturn400WhenRequestIsInvalid() throws Exception {
        //Arrange -> Preparar el escenario de la prueba

        String invalidRequest  = """
        {
          "title": "",
          "content": ""
        }
        """;

        //act & assert -> Ejecutar la acción y verificar el resultado
        //mockMvc.perform(post("/api/notes")
        //    .contentType("application/json")
        //    .content(invalidRequest ))
        //    .andExpect(status().isBadRequest());
        
        mockMvc.perform(post("/api/notes")
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidRequest ))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors").isArray())
        .andExpect(jsonPath("$.errors.length()").value(2));
    }

    @Test
    void shouldUpdateNoteWhenRequestIsValid() throws Exception {

        //Arrange -> Preparar el escenario de la prueba
        String requestBody = """
        {
          "title": "Updated title",
          "content": "Updated content"
        }
        """;

        NoteResponse response = new NoteResponse(
            1L,
            "Updated title",
            "Updated content",
            null,
            null
        );

        //Mockear el comportamiento del servicio    
        when(noteService.update(eq(1L), any(NoteRequest.class))) //Cuando el servicio reciba cualquier NoteRequest, eq 1L -> id específico, si usamos any no funciona
                        .thenReturn(response); //Simula que el servicio devuelve la nota actualizada

        //act & assert -> Ejecutar la acción y verificar el resultado
        mockMvc.perform(put("/api/notes/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.title").value("Updated title"))
            .andExpect(jsonPath("$.content").value("Updated content"));

    }

    @Test
    void shouldReturn400WhenUpdateRequestIsInvalid() throws Exception {

        //Arrange -> Preparar el escenario de la prueba
        String invalidRequest = """
        {
          "title": "",
          "content": ""
        }
        """;

        //act & assert -> Ejecutar la acción y verificar el resultado
        //mockMvc.perform(put("/api/notes/{id}", 1L)
        //    .contentType("application/json")
        //    .content(invalidRequest))
        //    .andExpect(status().isBadRequest());

        mockMvc.perform(put("/api/notes/{id}", 1L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(invalidRequest))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors").isArray());

    }

    @Test
    void shouldReturn404WhenUpdatingNonExistingNote() throws Exception {

        //Arrange -> Preparar el escenario de la prueba
        String requestBody = """
        {
          "title": "Updated title",
          "content": "Updated content"
        }
        """;

        //Mockear el comportamiento del servicio    
        when(noteService.update(eq(99L), any(NoteRequest.class))) //Cuando el servicio reciba cualquier NoteRequest, eq 99L -> id específico, si usamos any no funciona
                        .thenThrow(new NoteNotFoundException("Note not found")); //Simula que el servicio lanza una excepción cuando la nota no existe

        //act & assert -> Ejecutar la acción y verificar el resultado
        //mockMvc.perform(put("/api/notes/{id}", 99L)
        //    .contentType("application/json")
        //    .content(requestBody))
        //    .andExpect(status().isNotFound());

        mockMvc.perform(put("/api/notes/{id}", 99L)
            .contentType(MediaType.APPLICATION_JSON)
            .content(requestBody))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Note not found"));


    }

    @Test
    void shouldDeleteNoteWhenIdExists() throws Exception {
        //Arrange -> Preparar el escenario de la prueba
        doNothing().when(noteService).deleteById(1L); //doNothing -> Simula que el servicio no hace nada al eliminar la nota (void)

        //act & assert -> Ejecutar la acción y verificar el resultado
        mockMvc.perform(delete("/api/notes/{id}", 1L))
            .andExpect(status().isOk()); //Verifica que el estatus de la respuesta sea 204 No Content

        verify(noteService).deleteById(1L);
    }

    @Test
    void shouldReturn404WhenDeletingNonExistingNote() throws Exception {

        // Arrange
        doThrow(new NoteNotFoundException("Note not found"))
            .when(noteService).deleteById(99L);

        // Act + Assert
        mockMvc.perform(delete("/api/notes/{id}", 99L))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value("Note not found"));
            
    }












}
