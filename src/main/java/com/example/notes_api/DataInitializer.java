package com.example.notes_api;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.notes_api.model.Note;
import com.example.notes_api.repository.NoteRepository;

@Component
public class DataInitializer implements CommandLineRunner {

    private final NoteRepository noteRepository;

    public DataInitializer(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Aquí puedes inicializar datos si es necesario
        //No data duplicada

        if (noteRepository.count() == 0) {

            Note note = new Note();
            note.setTitle("Nota de ejemplo");
            note.setContent("Este es el contenido de la nota de ejemplo.");

            noteRepository.save(note);

        }


    }

}
