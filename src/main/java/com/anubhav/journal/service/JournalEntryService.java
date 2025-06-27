package com.anubhav.journal.service;

import com.anubhav.journal.entity.JournalEntry;
import com.anubhav.journal.entity.User;
import com.anubhav.journal.repository.JournalEntryRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {
    @Autowired
    private JournalEntryRepository journalEntryRepository;
    @Autowired
    private UserService userService;

    public List<JournalEntry> getAll() {
        return journalEntryRepository.findAll();
    }

    public List<JournalEntry> getAllByUser(String username) {
        User user = userService.findByUserName(username);
        return user.getJournalEntries();
    }

    @Transactional
    public JournalEntry addEntryByUser(JournalEntry entry, String username) {
        User user = userService.findByUserName(username);
        entry.setDate(LocalDateTime.now());
        JournalEntry journalEntry = journalEntryRepository.save(entry);
        user.getJournalEntries().add(journalEntry);
        userService.saveUser(user);
        return journalEntry;
    }

    public Optional<JournalEntry> findById(ObjectId id) {
        return journalEntryRepository.findById(id);
    }

    @Transactional
    public boolean deleteByUserAndId(String username, ObjectId id) {

        try {
            User user = userService.findByUserName(username);
            boolean removed = user.getJournalEntries().removeIf(x -> x.getId().toString().equals(id.toString()));
            if(removed){
                userService.saveUser(user);
                journalEntryRepository.deleteById(id);
            }
            return removed;
        }catch (Exception e){
            System.out.println(e);
            throw new RuntimeException("Error occurred while deleting the journal");
        }

    }

    public JournalEntry update(JournalEntry entry, ObjectId id) {
        JournalEntry oldJournal = journalEntryRepository.findById(id).orElse(null);
        if (oldJournal != null) {
            oldJournal.setTitle(entry.getTitle() != null && !entry.getTitle().isEmpty() ? entry.getTitle() : oldJournal.getTitle());
            oldJournal.setContent(entry.getContent() != null && !entry.getContent().isEmpty() ? entry.getContent() : oldJournal.getContent());
            journalEntryRepository.save(oldJournal);
        }
        return oldJournal;
    }
}
