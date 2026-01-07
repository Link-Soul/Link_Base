package com.link.desktop.controller;

import com.link.desktop.entity.DesktopNotes;
import com.link.desktop.service.DesktopNotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class DesktopNotesController {

    @Autowired
    private DesktopNotesService desktopNotesService;

    @GetMapping
    public List<DesktopNotes> getAllNotes() {
        return desktopNotesService.getAllNotes();
    }

    @GetMapping("/status/{status}")
    public List<DesktopNotes> getNotesByStatus(@PathVariable Integer status) {
        return desktopNotesService.getNotesByStatus(status);
    }

    @GetMapping("/{id}")
    public DesktopNotes getNoteById(@PathVariable Long id) {
        return desktopNotesService.getNoteById(id);
    }

    @PostMapping
    public boolean addNote(@RequestBody DesktopNotes note) {
        return desktopNotesService.addNote(note);
    }

    @PutMapping
    public boolean updateNote(@RequestBody DesktopNotes note) {
        return desktopNotesService.updateNote(note);
    }

    @DeleteMapping("/{id}")
    public boolean deleteNote(@PathVariable Long id) {
        return desktopNotesService.deleteNote(id);
    }
}