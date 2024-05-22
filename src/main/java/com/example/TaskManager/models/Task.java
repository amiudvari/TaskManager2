package com.example.TaskManager.models;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Task {
    private String taskName;
    private int taskId;
    private String priority;
    private LocalDate dueDate;
    private boolean completed;
    private String description;
}
