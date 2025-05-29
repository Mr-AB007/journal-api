package com.anubhav.journal.controller;

import com.anubhav.journal.entity.JournalEntry;
import com.anubhav.journal.entity.User;
import com.anubhav.journal.service.JournalEntryService;
import com.anubhav.journal.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {


    @Autowired
    JournalEntryService journalEntryService;
    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getAllUser() {
        return ResponseEntity.ok(userService.getAll());
    }

    @PostMapping
    public ResponseEntity<User> addJournal(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.add(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getJornalById(@PathVariable ObjectId id) {
        return userService.findById(id)
                //.map(entry -> ResponseEntity.ok(entry))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<User> getJornalById(@PathVariable String username) {
        User user = userService.findByUserName(username);
        if (user != null)
            return ResponseEntity.ok(user);
        return ResponseEntity.notFound().build();
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteJornalById(@PathVariable ObjectId id) {
//        Optional<JournalEntry> entry = journalEntryService.findById(id);
//        if (entry.isPresent()) {
//            journalEntryService.deleteById(id);
//            return ResponseEntity.noContent().build();
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//
//    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        User old = userService.update(user);
        if (old != null) {
            return ResponseEntity.status(HttpStatus.OK).body(old);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
