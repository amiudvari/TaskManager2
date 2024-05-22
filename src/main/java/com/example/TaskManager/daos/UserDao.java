package com.example.TaskManager.daos;

import com.example.TaskManager.models.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class UserDao {
    private JdbcTemplate jdbcTemplate;
    private PasswordEncoder passwordEncoder;

    public UserDao(DataSource dataSource, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return jdbcTemplate.query("SELECT * FROM users", this::mapRowToUser);
    }

    public User getUserByUsername(String username) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM users WHERE username = ?", this::mapRowToUser, username);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public User createUser(User user) {
        try {
            jdbcTemplate.update("INSERT INTO users (username, password) VALUES (?, ?)", user.getUsername(), passwordEncoder.encode(user.getPassword()));
            return getUserByUsername(user.getUsername());
        } catch (Exception e) {
            return null;
        }
    }

    public User updatePassword(User user, boolean updatePassword) {
        jdbcTemplate.update("UPDATE users SET password = ? WHERE username = ?", passwordEncoder.encode(user.getPassword()), user.getUsername());
        return getUserByUsername(user.getUsername());
    }

    public void deleteUser(String username) {
        jdbcTemplate.update("DELETE FROM users WHERE username = ?", username);
    }

    public List<String> getRolesForUsers(String username) {
        return jdbcTemplate.queryForList("SELECT role FROM roles WHERE username = ?", String.class, username);
    }

    public void addRole(String username, String role) {
        try {
            jdbcTemplate.update("INSERT INTO roles (username, role) VALUES (?,?)", username, role);
        } catch (Exception e) {

        }
    }

    public void removeRole(String username, String role) {
        jdbcTemplate.update("DELETE FROM roles WHERE username = ? AND role = ?", username, role);
    }

    ///ASK ABOUT THIS!
    public boolean checkLogIn(String username, String password) {
        User user = getUserByUsername(username);
        return passwordEncoder.matches(password, user.getPassword());
    }

    private User mapRowToUser(ResultSet row, int rowNumber) throws SQLException {
        User user = new User();
        user.setUsername(row.getString("username"));
        user.setPassword(row.getString("password"));
        return user;
    }

}
