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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalController {


    @Autowired
    JournalEntryService journalEntryService;
    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAllJournalEntriesByUser() {
        Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
        String userName = authenticatedUser.getName();

        User user = userService.findByUserName(userName);
        if (user != null && user.getJournalEntries() != null && !user.getJournalEntries().isEmpty()) {
            return ResponseEntity.ok().body(user.getJournalEntries());
        }

        return ResponseEntity.notFound().build();
    }


//    @GetMapping
//    public ResponseEntity<List<JournalEntry>> getJouranlByUser() {
//        Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
//        String userName = authenticatedUser.getName();
//        List<JournalEntry> journalEntries = journalEntryService.getAllByUser(userName);
//        if (journalEntries != null && !journalEntries.isEmpty()) return ResponseEntity.ok().body(journalEntries);
//        return ResponseEntity.notFound().build();
//    }


    @PostMapping
    public ResponseEntity<JournalEntry> addJournalByUser(@RequestBody JournalEntry journal) {
        try {
            Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
            String userName = authenticatedUser.getName();

            JournalEntry savedEntry = journalEntryService.addEntryByUser(journal, userName);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedEntry);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


    @GetMapping("id/{id}")
    public ResponseEntity<JournalEntry> getJornalById(@PathVariable ObjectId id) {
        return journalEntryService.findById(id)
                //.map(entry -> ResponseEntity.ok(entry))
                .map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{username}/{id}")
    public ResponseEntity<Void> deleteJornalByUserAndId(@PathVariable ObjectId id, @PathVariable String username) {
        Optional<JournalEntry> entry = journalEntryService.findById(id);
        if (entry.isPresent()) {
            journalEntryService.deleteByUserAndId(username, id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PutMapping("/user/{username}/id/{id}")
    public ResponseEntity<JournalEntry> updateJornalById(@RequestBody JournalEntry entry, @PathVariable ObjectId id, @PathVariable String username) {
        Optional<JournalEntry> old = journalEntryService.findById(id);
        if (old.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(journalEntryService.update(entry, id));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
