package com.sps.hobbymatcher.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sps.hobbymatcher.domain.User;
import com.sps.hobbymatcher.domain.Hobby;
import com.sps.hobbymatcher.domain.Post;
import com.sps.hobbymatcher.repository.UserRepository;
import com.sps.hobbymatcher.repository.HobbyRepository;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(String name, String username, String password) {
        
        User user = new User();

        Optional<User> userOpt = userRepository.findByUsername(username);

        if(userOpt.isPresent()) {
            return user;
        }
        
        user.setName(name);
        user.setUsername(username);
        user.setPassword(password);
        return userRepository.save(user);
    }

    public void addHobby(User user, Hobby hobby) {
        user.getMyHobbies().add(hobby);
        hobby.getUsers().add(user);
        return;
    }

    public void removeHobby(User user, Hobby hobby) {
        user.getMyHobbies().remove(hobby);
        hobby.getUsers().remove(user);
    }

    public void addConnection(User user1, User user2) {
        user1.getConnections().add(user2);
        return;
    }

    public void removeConnection(User user1, User user2) {
        user1.getConnections().remove(user2);
        user2.getConnections().remove(user1);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.deleteById(user.getId());
    }
}