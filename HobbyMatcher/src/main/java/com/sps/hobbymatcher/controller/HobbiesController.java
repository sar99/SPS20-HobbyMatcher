package com.sps.hobbymatcher.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;
import java.io.UnsupportedEncodingException;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.sps.hobbymatcher.domain.User;
import com.sps.hobbymatcher.domain.Post;
import com.sps.hobbymatcher.domain.Hobby;
import com.sps.hobbymatcher.service.UserService;
import com.sps.hobbymatcher.service.HobbyService;
import com.sps.hobbymatcher.service.PostService;
import com.sps.hobbymatcher.repository.HobbyRepository;
import com.sps.hobbymatcher.repository.PostRepository;

@Controller
public class HobbiesController {

    @Autowired
    private UserService userService;

    @Autowired
    private HobbyService hobbyService;
    
    @Autowired
    private PostService postService;

    @Autowired
    private HobbyRepository hobbyRepository;

    @Autowired
    private PostRepository postRepository;
    
    @GetMapping("/hobbies")
    public String hobbies(ModelMap model) {   
        
        Set<Hobby> hobbies = hobbyRepository.findAll();
        model.put("hobbies", hobbies);

        return "hobbies";
    }

    @GetMapping("/hobbies/{hobbyId}/users")
    public String users(@PathVariable Long hobbyId, ModelMap model) {   
        
        Set<User> users = hobbyRepository.findById(hobbyId).getUsers();
        model.put("users", users);
        
        return "hobbies";
    }

    @GetMapping("/hobbies/{hobbyId}/posts")
    public String posts(@PathVariable Long hobbyId, ModelMap model) {   
        
        Set<Post> posts = hobbyRepository.findById(hobbyId).getPosts();
        model.put("posts", posts);
        
        return "hobbies";
    }

    @GetMapping("/createhobby/{hobbyId}")
    public String createHobby(@PathVariable Long hobbyId, ModelMap model, HttpServletResponse response) throws IOException {

        Optional<Hobby> hobbyOpt = hobbyRepository.findByIdWithUser(hobbyId);
        
        if (hobbyOpt.isPresent()) {
            Hobby hobby = hobbyOpt.get();
            model.put("hobby", hobby);
        } 
        else {
            response.sendError(HttpStatus.NOT_FOUND.value(), "Hobby with id " + hobbyId + " was not found");
            return "hobby";
        }
        
        return "hobby";
    }

    @PostMapping("/createhobby/{hobbyId}")
    public String saveHobby(@PathVariable Long hobbyId, Hobby hobby) {   
        hobby = hobbyService.save(hobby);
        return "redirect: /createhobby/"+hobby.getId();
    }

    @PostMapping("/createhobby")
    public String createHobby(@AuthenticationPrincipal User user) {   
        Hobby hobby=hobbyService.createHobby();
        hobby.getUsers().add(user);
        user.getMyHobbies().add(hobby);
        hobby = hobbyRepository.save(hobby);
        return "redirect: /createhobby/"+hobby.getId();
    }
}