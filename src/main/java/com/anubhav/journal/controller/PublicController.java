package com.anubhav.journal.controller;

import com.anubhav.journal.entity.User;
import com.anubhav.journal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    UserService userService;

    @PostMapping("/create-user")
    public ResponseEntity<User> addJournal(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.add(user));
    }

}
