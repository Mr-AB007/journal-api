package com.anubhav.journal.controller;

import com.anubhav.journal.entity.JournalEntry;
import com.anubhav.journal.entity.User;
import com.anubhav.journal.service.JournalEntryService;
import com.anubhav.journal.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserService userService;

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
        String userName = authenticatedUser.getName();
        User old = userService.update(user,userName);
        if (old != null) {
            return ResponseEntity.status(HttpStatus.OK).body(old);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @DeleteMapping()
    public ResponseEntity<Void> deleteByUser(){
        Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
        String userName = authenticatedUser.getName();
        if (userService.findByUserName(userName) != null) {
            userService.deleteByUser(userName);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }










    @GetMapping
    public ResponseEntity<List<User>> getAllUser() {
        return ResponseEntity.ok(userService.getAll());
    }


    @GetMapping("/{username}")
    public ResponseEntity<User> getJornalById(@PathVariable String username) {
        User user = userService.findByUserName(username);
        if (user != null)
            return ResponseEntity.ok(user);
        return ResponseEntity.notFound().build();
    }





}
