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
@RequestMapping("/hobbies/{hobbyId}/post")
public class PostController {

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
    
    @GetMapping("")
    public String createPost (ModelMap model) {

        model.put("post", new Post());

        return "post";

    }
    
    @PostMapping("")
    public String uploadPost (@PathVariable Long hobbyId, @AuthenticationPrincipal User user,Post post) {
        
        post.setUserId(user.getId());
        Post savedPost = postRepository.save(post);
        Optional<Hobby> hobbyOpt = hobbyRepository.findById(hobbyId);
        if(hobbyOpt.isPresent()) {
            Hobby hobby = hobbyOpt.get();
            post = postService.uploadPost(hobby, savedPost);
        }
        
        return "redirect:/hobbies/"+hobbyId;
    }

    @PostMapping("")
    public String deletePost (@PathVariable Long hobbyId, Post post) {
        
        postService.deletePost(post, hobbyId);
        
        return "redirect:/hobbies/"+hobbyId;
    }
    // @PostMapping("")
    // public String createHobby(@PathVariable Long hobbyId, @AuthenticationPrincipal User user) {   

    //     Optional<Hobby> hobbyOpt = hobbyRepository.findById(hobbyId);
    //     Post post = new Post();
    //     if(hobbyOpt.isPresent()) {
    //         Hobby hobby = hobbyOpt.get();
    //         post = postService.uploadPost(user, hobby);
    //     }
    //     return "redirect:/hobbies/"+hobbyId+"/post/"+post.getId();
    // }
}