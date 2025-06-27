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
    public ResponseEntity<?> getJornalById(@PathVariable ObjectId id) {
        Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
        String userName = authenticatedUser.getName();
        User user = userService.findByUserName(userName);
        List<JournalEntry> journalEntries = user.getJournalEntries().stream().filter(x -> x.getId().equals(id)).toList();
        if(!journalEntries.isEmpty()){
            return ResponseEntity.ok().body(journalEntries);
        }
        else
            return ResponseEntity.notFound().build();
    }

    @DeleteMapping("id/{id}")
    public ResponseEntity<Void> deleteJornalByUserAndId(@PathVariable ObjectId id) {
        Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
        String userName = authenticatedUser.getName();
        boolean removed = journalEntryService.deleteByUserAndId(userName,id);
        if(removed)
            return ResponseEntity.noContent().build();
        else
            return ResponseEntity.notFound().build();

    }

    @PutMapping("/id/{id}")
    public ResponseEntity<JournalEntry> updateJornalById(@RequestBody JournalEntry entry, @PathVariable ObjectId id) {
        Authentication authUser = SecurityContextHolder.getContext().getAuthentication();
        String user = authUser.getName();
        JournalEntry updated = journalEntryService.update(entry, id);
        if (updated != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
