package com.anubhav.journal.service;

import com.anubhav.journal.entity.JournalEntry;
import com.anubhav.journal.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {
    @Autowired
    JournalEntryRepository journalEntryRepository;

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public JournalEntry add(JournalEntry entry) {
        entry.setDate(LocalDateTime.now());
        return journalEntryRepository.save(entry);
    }

    public Optional<JournalEntry> findById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    public void deleteById(ObjectId id) {
        journalEntryRepository.deleteById(id);
    }
    public JournalEntry update(JournalEntry entry, ObjectId id){
        JournalEntry oldJournal = journalEntryRepository.findById(id).orElse(null);
        if(oldJournal != null){
            oldJournal.setTitle(entry.getTitle() != null && !entry.getTitle().isEmpty() ?entry.getTitle(): oldJournal.getTitle());
            oldJournal.setContent(entry.getContent() != null && !entry.getContent().isEmpty() ?entry.getContent(): oldJournal.getContent());
            journalEntryRepository.save(oldJournal);
        }
        return oldJournal;
    }
}
