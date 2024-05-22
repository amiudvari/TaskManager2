package com.example.TaskManager.controllers;

import com.example.TaskManager.DaoException.DaoException;
import com.example.TaskManager.daos.TaskDao;
import com.example.TaskManager.models.Task;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TasksController {
    private TaskDao taskDao;

    public TasksController (TaskDao taskDao) {
        this.taskDao = taskDao;
    }

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

    @PostMapping("")
    public Task addTask (@RequestBody Task task) {
        return taskDao.addTask(task);
    }

    @GetMapping("/{taskId}")
    public Task getTaskById (@PathVariable int taskId) {
        return taskDao.getTaskById(taskId);
    }

    @PutMapping("/{taskId}")
    public Task updateTaskById (@PathVariable int taskId, @RequestBody Task task){
        task.setTaskId(taskId);
        return taskDao.updateTask(task);
    }

    @DeleteMapping("/{taskId}")
    public void deleteTask(@PathVariable int taskId) {
        taskDao.deleteTask(taskId);
    }

    @PutMapping("/{taskId}/completed")
    public Task markAsComplete (@PathVariable int taskId){
        Task task = new Task();
        task.setTaskId(taskId);
        return taskDao.markAsComplete(task);
    }

    @GetMapping("/completed")
    public List<Task> getCompletedTasks () {
        return taskDao.getCompletedTasks();
    }

    @GetMapping("/high-priority")
    public List<Task> getHighPriorityTasks() {
        return taskDao.getHighPriorityTasks();
    }

}
