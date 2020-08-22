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
import org.springframework.security.acls.model.AlreadyExistsException;

import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HobbyRepository hobbyRepository;

    @Autowired
	private PasswordEncoder passwordEncoder;

    public User registerUser(User user) throws AlreadyExistsException {
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		Authority authority = new Authority();
		authority.setAuthority("ROLE_USER");
		user.getAuthorities().add(authority);
		List<User> users = userRepository.findByUsername(user.getUsername());
        if(users.size()==0) {
		    return userRepository.save(user);
        } else {
            throw new AlreadyExistsException("Username already exists!");
        }
	}

    public void addHobby(User user, Optional<Hobby> hobbyOpt) {
        if(hobbyOpt.isPresent()) {
            Hobby hobby=hobbyOpt.get();
            user.getMyHobbies().add(hobby.getId());
            hobby.getUsers().add(user.getId());
            userRepository.save(user);
            hobbyRepository.save(hobby);
            System.out.println(user);
            System.out.println(hobby);
        }
        return;
    }

    public void removeHobby(User user, Hobby hobby) {
        user.getMyHobbies().remove(hobby.getId());
        hobby.getUsers().remove(user.getId());
        userRepository.save(user);
        hobbyRepository.save(hobby);
        return;
    }

    public void addConnection(User user1, User user2) {
        user1.getConnections().add(user2.getUsername());
        userRepository.save(user1);
        return;
    }

    public void removeConnection(User user1, User user2) {
        user1.getConnections().remove(user2.getUsername());
        userRepository.save(user1);
 
    }

    public void deleteUser(User user) {
        userRepository.deleteById(user.getId());
    }
}