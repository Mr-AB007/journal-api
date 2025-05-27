package com.anubhav.journal.controller;

import com.anubhav.journal.entity.JournalEntry;
import com.anubhav.journal.service.JournalEntryService;
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

    @GetMapping
    public ResponseEntity<List<JournalEntry>> getAllJornal() {
        return ResponseEntity.ok(journalEntryService.getAll());
    }

    @PostMapping
    public ResponseEntity<JournalEntry> addJournal(@RequestBody JournalEntry journal) {
        return ResponseEntity.status(HttpStatus.CREATED).body(journalEntryService.add(journal));
    }

    @GetMapping("/{id}")
    public ResponseEntity<JournalEntry> getJornalById(@PathVariable ObjectId id) {
        return journalEntryService.findById(id)
                //.map(entry -> ResponseEntity.ok(entry))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJornalById(@PathVariable ObjectId id) {
        Optional<JournalEntry> entry = journalEntryService.findById(id);
        if (entry.isPresent()) {
            journalEntryService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<JournalEntry> updateJornalById(@RequestBody JournalEntry entry, @PathVariable ObjectId id) {
        Optional<JournalEntry> old = journalEntryService.findById(id);
        if (old.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(journalEntryService.update(entry, id));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
