package com.anubhav.journal.controller;

import com.anubhav.journal.entity.User;
import com.anubhav.journal.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/all-users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAll();
        if (users != null && !users.isEmpty())
            return ResponseEntity.ok(users);
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(@RequestBody User user){
        userService.saveAdmin(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
}
