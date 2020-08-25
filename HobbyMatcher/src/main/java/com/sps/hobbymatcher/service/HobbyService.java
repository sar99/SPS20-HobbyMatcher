package com.sps.hobbymatcher.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sps.hobbymatcher.domain.Hobby;
import com.sps.hobbymatcher.domain.User;
import com.sps.hobbymatcher.domain.Post;
import com.sps.hobbymatcher.repository.HobbyRepository;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.DatastoreServiceConfig;
import com.google.appengine.api.datastore.ReadPolicy.Consistency;

import java.util.*;

@Service
public class HobbyService {

    @Autowired
    private HobbyRepository hobbyRepository;

    @Autowired
    private UserService userService;

    public Optional<Hobby> findHobbyById(Long id) {

        return hobbyRepository.findById(id);
    }

    public Set<Hobby> findAllHobbies() {

        return hobbyRepository.findAll();
    }

    public List<Hobby> sortHobbiesByName(List<Hobby> hobbies) {

        Collections.sort(hobbies, new Comparator<Hobby>(){
            @Override
            public int compare(Hobby hobby1, Hobby hobby2) {
                return hobby1.getName().compareTo(hobby2.getName());
            }
        });

        return hobbies;
    }

    public Hobby createHobby(Hobby hobby, User user) {

        List<Hobby> hobbyOpt=hobbyRepository.findByName(hobby.getName());

        if(hobbyOpt!=null) {

            if(hobbyOpt.size()==0) {
                
                Hobby saved=hobbyRepository.save(hobby);
                
                user.getMyHobbies().add(hobby.getId());
                userService.save(user);
                
                return saved;
            }
            else {
                return hobbyOpt.get(0);
            }
        }
        return hobby;
    }

    public Hobby save(Hobby hobby) {

        return hobbyRepository.save(hobby);
    }
}