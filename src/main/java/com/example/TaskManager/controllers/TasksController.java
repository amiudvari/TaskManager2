package com.example.TaskManager.controllers;

import com.example.TaskManager.DaoException.DaoException;
import com.example.TaskManager.daos.TaskDao;
import com.example.TaskManager.models.Task;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TasksController {
    private TaskDao taskDao;

    public TasksController (TaskDao taskDao) {
        this.taskDao = taskDao;
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public List<Task> getAllTasks(
            @RequestParam (required = false) String name,
            @RequestParam (required = false) String priority
    ) {
        if (name != null) {
            return taskDao.getTasksByName(name);
        } else if (priority != null) {
            return taskDao.getTasksByPriority(priority);
        } else {
            return taskDao.getAllTasks();
        }

    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("")
    public Task addTask (@RequestBody Task task) {
        return taskDao.addTask(task);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{taskId}")
    public Task getTaskById (@PathVariable int taskId) {
        return taskDao.getTaskById(taskId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{taskId}")
    public Task updateTaskById (@PathVariable int taskId, @RequestBody Task task){
        task.setTaskId(taskId);
        return taskDao.updateTask(task);
    }
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{taskId}")
    public void deleteTask(@PathVariable int taskId) {
        taskDao.deleteTask(taskId);
    }
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{taskId}/completed")
    public Task markAsComplete (@PathVariable int taskId){
        Task task = new Task();
        task.setTaskId(taskId);
        return taskDao.markAsComplete(task);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/completed")
    public List<Task> getCompletedTasks () {
        return taskDao.getCompletedTasks();
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/high-priority")
    public List<Task> getHighPriorityTasks() {
        return taskDao.getHighPriorityTasks();
    }

}
