package com.anubhav.journal.service;


import com.anubhav.journal.entity.User;
import com.anubhav.journal.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;

    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public User addNewUser(User user) {
        String encryptPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptPassword);
        user.setRoles(Arrays.asList("USER"));
        return userRepository.save(user);
    }
    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User findByUserName(String username) {
        return userRepository.findByUserName(username);
    }

    public void deleteByUser(String username) {
        userRepository.deleteByUserName(username);
    }

    public User update(User user, String username) {
        User oldUser = userRepository.findByUserName(username);
            oldUser.setUserName(user.getUserName());
            oldUser.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(oldUser);
        return oldUser;
    }

    public void deleteById(ObjectId id) {
        userRepository.deleteById(id);
    }
    public Optional<User> findById(ObjectId id) {
        return userRepository.findById(id);
    }
}
