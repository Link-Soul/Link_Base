package com.link.desktop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.link.desktop.entity.DesktopNotes;

import java.util.List;

public interface DesktopNotesService extends IService<DesktopNotes> {
    List<DesktopNotes> getAllNotes();
    List<DesktopNotes> getNotesByStatus(Integer status);
    DesktopNotes getNoteById(Long id);
    boolean addNote(DesktopNotes note);
    boolean updateNote(DesktopNotes note);
    boolean deleteNote(Long id);
}