package com.sps.hobbymatcher.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sps.hobbymatcher.domain.User;
import com.sps.hobbymatcher.domain.Hobby;
import com.sps.hobbymatcher.domain.Post;
import com.sps.hobbymatcher.domain.Authority;
import com.sps.hobbymatcher.repository.UserRepository;
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
    private HobbyService hobbyService;

    @Autowired
	private PasswordEncoder passwordEncoder;

    public Optional<User> findUserById(Long id) {

        return userRepository.findById(id);
    }

    public List<User> findUserByUsername(String username) {

        return userRepository.findByUsername(username);
    }

    public List<User> sortUsersByName(List<User> users) {

        Collections.sort(users, new Comparator<User>(){
            @Override
            public int compare(User user1, User user2) {
                return user1.getName().compareTo(user2.getName());
            }
        });

        return users;
    }

    public List<String> sortUsersByName2(List<String> users) {

        Collections.sort(users, new Comparator<String>(){
            @Override
            public int compare(String user1, String user2) {
                return user1.compareTo(user2);
            }
        });

        return users;
    }

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
            hobbyService.save(hobby);
        }

        return;
    }

    public void removeHobby(User user, Hobby hobby) {

        user.getMyHobbies().remove(hobby.getId());
        hobby.getUsers().remove(user.getId());

        userRepository.save(user);
        hobbyService.save(hobby);

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

        return;
    }

    public User save(User user) {

        return userRepository.save(user);
    }

    public void deleteUser(User user) {

        userRepository.deleteById(user.getId());
        return;
    }
}