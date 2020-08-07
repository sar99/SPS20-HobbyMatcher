package com.sps.hobbymatcher.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sps.hobbymatcher.domain.User;
import com.sps.hobbymatcher.domain.Hobby;
import com.sps.hobbymatcher.domain.Post;
import com.sps.hobbymatcher.repository.UserRepository;
import com.sps.hobbymatcher.repository.HobbyRepository;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(String name, String username, String password) {
        
        User user = new User();

        List<User> userOpt = userRepository.findByUsername(username);

        if(userOpt.size()>0) {
            return userOpt.get(0);
        }
        
        user.setName(name);
        user.setUsername(username);
        user.setPassword(password);
        return userRepository.save(user);
    }

    public void addHobby(User user, Optional<Hobby> hobbyOpt) {
        if(hobbyOpt.isPresent()) {
            Hobby hobby=hobbyOpt.get();
            user.getMyHobbies().add(hobby.getId());
            hobby.getUsers().add(user.getId());
            System.out.println(user);
            System.out.println(hobby);
        }
        return;
    }

    public void removeHobby(User user, Hobby hobby) {
        user.getMyHobbies().remove(hobby.getId());
        hobby.getUsers().remove(user.getId());
        System.out.println(user);
        System.out.println(hobby);
        return;
    }

    public void addConnection(User user1, User user2) {
        user1.getConnections().add(user2.getUsername());
        return;
    }

    public void removeConnection(User user1, User user2) {
        user1.getConnections().remove(user2.getUsername());
        user2.getConnections().remove(user1.getUsername());
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.deleteById(user.getId());
    }
}