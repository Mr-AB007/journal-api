package com.anubhav.journal.service;

import com.anubhav.journal.entity.JournalEntry;
import com.anubhav.journal.entity.User;
import com.anubhav.journal.repository.JournalEntryRepository;
import com.anubhav.journal.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    JournalEntryRepository journalEntryRepository;
    @Autowired
    UserRepository userRepository;

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User add(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findById(ObjectId id) {
        return userRepository.findById(id);
    }

    public User findByUserName(String username) {
        return userRepository.findByUserName(username);
    }

    public void deleteById(ObjectId id) {
        userRepository.deleteById(id);
    }

    public void deleteByUser(String username) {
        userRepository.deleteByUserName(username);
    }

    public User update(User user) {
        User oldUser = userRepository.findByUserName(user.getUserName());
        if (oldUser != null) {
            oldUser.setUserName(user.getUserName());
            oldUser.setPassword(user.getPassword());
            userRepository.save(oldUser);
        }
        return oldUser;
    }
}
