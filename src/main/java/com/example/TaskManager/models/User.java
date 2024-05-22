package com.example.TaskManager.models;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class User {

    private int id; //should I add this? In video this isn't the case

    @Min(value = 5, message = "Username must be at least 5 characters")
    private String username;

    @Min(value = 8, message = "Password must be at least 8 characters long")
    private String password;
}
