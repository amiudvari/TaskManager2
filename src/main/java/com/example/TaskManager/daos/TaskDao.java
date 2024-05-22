package com.example.TaskManager.daos;



import com.example.TaskManager.DaoException.DaoException;
import com.example.TaskManager.models.Task;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Component
public class TaskDao {
    private JdbcTemplate jdbcTemplate;

    public TaskDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * from task WHERE completed = false");

        while (rowSet.next()) {
            Task task = mapRowToTask(rowSet);
            tasks.add(task);
        }
        return tasks;
    }

    public Task addTask(Task task) {
        Task newTask = null;
        String sql = "INSERT INTO task (task_name, priority, due_date, description) VALUES (?, ?, ?, ?) RETURNING task_id";
        try {
            int taskId = jdbcTemplate.queryForObject(sql, int.class, task.getTaskName(), task.getPriority(), task.getDueDate(), task.getDescription());
            newTask = getTaskById(taskId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return newTask;
    }

    public Task getTaskById(int taskId) {
        String sql = "SELECT * FROM task WHERE task_id = ?";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, taskId);
            if (rowSet.next()) {
                return mapRowToTask(rowSet);
            } else {
                return null;
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
    }

    public Task updateTask(Task task) {
        String sql = "UPDATE task SET task_name = ?, priority = ?,  due_date = ?, description = ? WHERE task_id = ?";
        try {
            int numberOfRows = jdbcTemplate.update(sql, task.getTaskName(), task.getPriority(), task.getDueDate(), task.getDescription(), task.getTaskId());
            if (numberOfRows == 0) {
                throw new DaoException("No task was updated.");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return task;
    }

    //!!!!!NOT IMPLEMENTED!!!!
    public int deleteTask(int taskId) {
        int numberOfRows = 0;
        try {
            numberOfRows = jdbcTemplate.update("DELETE FROM task WHERE task_id = ?", taskId);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation");
        }
        return numberOfRows;
    }


    public Task markAsComplete(Task task) {
        String sql = "UPDATE task SET completed = true WHERE task_id = ?";
        try {
            task.setCompleted(true);
            int rowsAffected = jdbcTemplate.update(sql, task.getTaskId());

            if (rowsAffected == 0) {
                throw new DaoException("Task was not updated.");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
        return task;
    }

    public List<Task> getCompletedTasks() {
        List<Task> completedTasks = new ArrayList<>();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * from task WHERE completed = true;");

        while (rowSet.next()) {
            Task task = mapRowToTask(rowSet);
            completedTasks.add(task);
        }
        return completedTasks;
    }

    public List<Task> updatePriorityForTasks() {
        String sql = "UPDATE task SET priority = 'high' WHERE DATE_PART('day', now() - due_date) >= 7";
        int numberOfRows = jdbcTemplate.update(sql);
        return null;
    }

    public List<Task> getHighPriorityTasks() {
        List<Task> highPriorityTasks = new ArrayList<>();
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet("SELECT * from task WHERE priority ILIKE 'high';");

        while (rowSet.next()) {
            Task task = mapRowToTask(rowSet);
            highPriorityTasks.add(task);
        }
        return highPriorityTasks;
    }

    public List<Task> getTasksByName (String taskName) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM task WHERE task_name ILIKE ?";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, "%" + taskName + "%");
            while (rowSet.next()) {
                Task task = mapRowToTask(rowSet);
                tasks.add(task);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return tasks;
    }

    public List<Task> getTasksByPriority (String priority) {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM task WHERE priority ILIKE ?";
        try {
            SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, "%" + priority + "%");
            while (rowSet.next()) {
                Task task = mapRowToTask(rowSet);
                tasks.add(task);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return tasks;
    }

    private Task mapRowToTask(SqlRowSet rowSet) {
        Task task = new Task();
        task.setTaskName(rowSet.getString("task_name"));
        task.setTaskId(rowSet.getInt("task_id"));
        task.setDescription(rowSet.getString("description"));
        task.setCompleted(rowSet.getBoolean("completed"));
        task.setDueDate(rowSet.getDate("due_date").toLocalDate());
        task.setPriority(rowSet.getString("priority"));
        return task;
    }
}

