package com.example.TaskManager.controllers;

import com.example.TaskManager.daos.UserDao;
import com.example.TaskManager.models.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@PreAuthorize("hasAuthority('ADMIN')")
@RequestMapping("/users")
public class UserController {
    private UserDao userDao;

    public UserController (UserDao userDao) {
        this.userDao = userDao;
    }

    @GetMapping("")
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }
    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return userDao.getUserByUsername(username);
    }

    @PostMapping("")
    public User createUser(@RequestBody User user) {
        return userDao.createUser(user);
    }

    @PutMapping("/{username}")
    public User updateUser(@PathVariable String username, @RequestBody User user) {
       return userDao.updatePassword(user, false);
    }

    @DeleteMapping("/{username}")
    public void deleteUser(@PathVariable String username) {
         userDao.deleteUser(username);
    }

    @GetMapping("/{username}/roles")
    public List<String> getUserRoles(@PathVariable String username) {
        return userDao.getRolesForUsers(username);
    }

    @PostMapping("/{username}/roles")
    public void addUserRole(@PathVariable String username, @RequestBody String role) {
        userDao.addRole(username, role);
    }

    @DeleteMapping("/{username}/roles/{role}")
    public void removeRoleFromUser(@PathVariable String username, @PathVariable String role) {
        userDao.removeRole(username, role);
    }
}