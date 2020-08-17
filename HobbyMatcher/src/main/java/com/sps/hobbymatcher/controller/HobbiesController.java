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
import com.sps.hobbymatcher.repository.UserRepository;

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

    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/hobbies")
    public String hobbies(@AuthenticationPrincipal User user, ModelMap model) {   

        Set<Hobby> hobbies = hobbyRepository.findAll();
        if(user == null) {
            model.put("hobbies", hobbies);

            // for (Iterator<Hobby> it = hobbies.iterator(); it.hasNext(); ){
            // System.out.println(it.next());
            // }
        } else {

            Set<Long> hobbiesId = user.getMyHobbies();
            Set<Hobby> otherHobbies  = new HashSet<>();

            for (Iterator<Hobby> it = hobbies.iterator(); it.hasNext(); ) {

                Hobby hobby = it.next();
                Long id = hobby.getId();
                if(hobbiesId.contains(id)) {
                    continue;
                } else {
                    otherHobbies.add(hobby);
                }
            }
            model.put("hobbies", otherHobbies);
            // for (Iterator<Hobby> it = otherHobbies.iterator(); it.hasNext(); ){
            // System.out.println(it.next());
            // }
        }

        return "hobbies";
    }

    @GetMapping("/hobbies/{hobbyId}/register")
    public String register(@PathVariable Long hobbyId, @AuthenticationPrincipal User user) {   
        
        Optional<Hobby> hobby = hobbyRepository.findById(hobbyId);
        userService.addHobby(user, hobby);
        
        return "redirect: /hobbies/"+hobbyId;
    }

    @GetMapping("/hobbies/{hobbyId}/unregister")
    public String unregister(@PathVariable Long hobbyId, @AuthenticationPrincipal User user) {   
        
        Optional<Hobby> hobbyOpt = hobbyRepository.findById(hobbyId);
        if(hobbyOpt.isPresent()) {
            Hobby hobby=hobbyOpt.get();
            userService.removeHobby(user, hobby);
        }
        return "redirect: /hobbies/"+hobbyId;
    }


    @GetMapping("/hobbies/{hobbyId}")
    public String posts(@PathVariable Long hobbyId, ModelMap model) {   
        
        Optional<Hobby> hobbyOpt = hobbyRepository.findById(hobbyId);
        if(hobbyOpt.isPresent()) {
            Hobby hobby = hobbyOpt.get();
            Set<Long> postsId = hobby.getPosts();
            Set<Long> usersId = hobby.getUsers();
            Set<Post> posts = new HashSet<>();
            Set<User> users = new HashSet<>();
            for (Iterator<Long> it = postsId.iterator(); it.hasNext(); ) {

                Optional<Post> post = postRepository.findById(it.next());
                if(post.isPresent()) {
                    posts.add(post.get());
                }
            }

            for (Iterator<Long> it = usersId.iterator(); it.hasNext(); ) {

                Optional<User> user = userRepository.findById(it.next());
                if(user.isPresent()) {
                    users.add(user.get());
                }
            }
            model.put("users", users);
            model.put("posts", posts);
            model.put("hobby", hobby);
        }
        return "hobby";
    }

    @GetMapping("/createhobby/{hobbyId}")
    public String createHobby(@PathVariable Long hobbyId, ModelMap model, HttpServletResponse response) throws IOException {

        Optional<Hobby> hobbyOpt = hobbyRepository.findById(hobbyId);
        if(hobbyOpt.isPresent()) {
            Hobby hobby = hobbyOpt.get();
            model.put("hobby", hobby);
        }
        
        return "hobby";
    }

    @PostMapping("/createhobby/{hobbyId}")
    public String saveHobby(@AuthenticationPrincipal User user, @PathVariable Long hobbyId) {   
        Optional<Hobby> hobbyOpt  = hobbyRepository.findById(hobbyId);
        Hobby hobby = new Hobby();
        if(hobbyOpt.isPresent()) {
            hobby = hobbyService.createHobby(hobbyOpt.get(), user);
        }

        return "redirect: /hobbies/"+hobby.getId();
    }

    @PostMapping("/createhobby")
    public String createHobby() {   
        Hobby hobby=new Hobby();
        hobby = hobbyRepository.save(hobby);
        return "redirect: /createhobby/"+hobby.getId();
    }
}