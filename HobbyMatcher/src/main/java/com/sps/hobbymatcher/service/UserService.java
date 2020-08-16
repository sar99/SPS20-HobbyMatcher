package com.sps.hobbymatcher.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sps.hobbymatcher.domain.User;
import com.sps.hobbymatcher.domain.Hobby;
import com.sps.hobbymatcher.domain.Post;
import com.sps.hobbymatcher.domain.Authority;
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

    @Autowired
	private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {

		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		Authority authority = new Authority();
		authority.setAuthority("ROLE_USER");
		user.getAuthorities().add(authority);
		return userRepository.save(user);
        
	}

    public void addHobby(User user, Optional<Hobby> hobbyOpt) {
        if(hobbyOpt.isPresent()) {
            Hobby hobby=hobbyOpt.get();
            user.getMyHobbies().add(hobby.getId());
            hobby.getUsers().add(user.getId());
        }
        return;
    }

    public void removeHobby(User user, Hobby hobby) {

        user.getMyHobbies().remove(hobby.getId());
        hobby.getUsers().remove(user.getId());
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

    public void deleteUser(User user) {
        userRepository.deleteById(user.getId());
    }
}