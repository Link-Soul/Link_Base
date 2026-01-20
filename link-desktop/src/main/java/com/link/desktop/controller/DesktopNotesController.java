package com.link.desktop.controller;

import com.link.desktop.entity.DesktopNotes;
import com.link.desktop.service.DesktopNotesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 桌面备忘录控制器
 * <p>
 * 提供桌面备忘录相关的RESTful API接口，处理前端的HTTP请求
 * 映射路径为/notes，支持备忘录的增删改查操作
 * </p>
 *
 * @author Link
 * @version V1.0
 */
@RestController
@RequestMapping("/notes")
@Validated
public class DesktopNotesController {

    /**
     * 桌面备忘录服务接口
     * 用于调用业务逻辑层的方法处理备忘录相关业务
     */
    @Resource
    private DesktopNotesService desktopNotesService;

    /**
     * 获取所有备忘录
     *
     * @return 所有备忘录的列表
     */
    @GetMapping("/list")
    public List<DesktopNotes> getAllNotes() {
        return desktopNotesService.getAllNotes();
    }


    /**
     * 添加备忘录
     *
     * @param note 备忘录对象，由请求体JSON转换而来
     * @return 添加是否成功
     */
    @PostMapping("/add")
    public boolean addNote(@RequestBody @NotNull(message = "备忘录对象不能为空") @Validated DesktopNotes note) {
        return desktopNotesService.addNote(note);
    }

    /**
     * 更新备忘录
     *
     * @param note 备忘录对象，由请求体JSON转换而来，包含更新后的信息
     * @return 更新是否成功
     */
    @PutMapping
    public boolean updateNote(@RequestBody DesktopNotes note) {
        return desktopNotesService.updateNote(note);
    }

    /**
     * 删除备忘录
     *
     * @param id 备忘录ID
     * @return 删除是否成功
     */
    @DeleteMapping("/{id}")
    public boolean deleteNote(@PathVariable Long id) {
        return desktopNotesService.deleteNote(id);
    }
}