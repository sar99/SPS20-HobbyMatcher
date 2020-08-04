package com.sps.hobbymatcher.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sps.hobbymatcher.domain.Hobby;
import com.sps.hobbymatcher.domain.User;
import com.sps.hobbymatcher.domain.Post;
import com.sps.hobbymatcher.repository.HobbyRepository;

import java.util.Optional;

@Service
public class HobbyService {

    @Autowired
    private HobbyRepository hobbyRepository;

    public Hobby createHobby(String name, User user) {
        
        Hobby hobby = new Hobby();

        Optional<Hobby> hobbyOpt=hobbyRepository.findByName(name);
        if(hobbyOpt.isPresent()) {
            return hobby;
        }

        hobby.setName(name);
        hobby.getUsers().add(user);
        user.getMyHobbies().add(hobby);
        return hobbyRepository.save(hobby);
    }

    public Hobby save(Hobby hobby) {
        return hobbyRepository.save(hobby);
    }
}