package com.link.desktop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.link.desktop.entity.DesktopNotes;
import com.link.desktop.mapper.DesktopNotesMapper;
import com.link.desktop.service.DesktopNotesService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DesktopNotesServiceImpl extends ServiceImpl<DesktopNotesMapper, DesktopNotes> implements DesktopNotesService {

    @Override
    public List<DesktopNotes> getAllNotes() {
        return list();
    }

    @Override
    public List<DesktopNotes> getNotesByStatus(Integer status) {
        QueryWrapper<DesktopNotes> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", status);
        return list(queryWrapper);
    }

    @Override
    public DesktopNotes getNoteById(Long id) {
        return getById(id);
    }

    @Override
    public boolean addNote(DesktopNotes note) {
        return save(note);
    }

    @Override
    public boolean updateNote(DesktopNotes note) {
        return updateById(note);
    }

    @Override
    public boolean deleteNote(Long id) {
        return removeById(id);
    }
}