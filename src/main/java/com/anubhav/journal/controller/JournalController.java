package com.anubhav.journal.controller;

import com.anubhav.journal.entity.JournalEntry;
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
@RequestMapping("/journal")
public class JournalController {


    @Autowired
    JournalEntryService journalEntryService;
    @Autowired
    UserService userService;

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAllJornal() {
        List<JournalEntry> journalEntries = journalEntryService.getAll();
        if (journalEntries != null) return ResponseEntity.ok().body(journalEntries);
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<JournalEntry>> getJouranlByUser(@PathVariable String username) {
        List<JournalEntry> journalEntries = journalEntryService.getAllByUser(username);
        if (journalEntries != null && !journalEntries.isEmpty()) return ResponseEntity.ok().body(journalEntries);
        return ResponseEntity.notFound().build();
    }


    @PostMapping("/{username}")
    public ResponseEntity<JournalEntry> addJournalByUser(@RequestBody JournalEntry journal, @PathVariable String username) {
        return ResponseEntity.status(HttpStatus.CREATED).body(journalEntryService.add(journal, username));
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
