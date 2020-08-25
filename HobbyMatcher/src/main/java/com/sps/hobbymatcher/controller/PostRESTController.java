package com.sps.hobbymatcher.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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
import org.springframework.web.bind.annotation.ModelAttribute;

@RestController
@RequestMapping("/hobbies/{hobbyId}/post/api")
public class PostRESTController {

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

    @GetMapping("/isliked/{postId}")
    public boolean islikedPost (@AuthenticationPrincipal User user, @PathVariable Long postId, @PathVariable Long hobbyId) {

        Optional<Post> postOpt = postRepository.findById(postId);
        Long userId = user.getId();
        boolean hasLiked = false;

        if(postOpt.isPresent()) {
            // postService.likePost(postOpt.get(), user);

            Post post = postOpt.get();
            Set<Long> usersId = post.getUsersVoted();

            for (Iterator<Long> it = usersId.iterator(); it.hasNext(); ) {

                Long id = it.next();
                Optional<User> userVoted = userRepository.findById(id);

                if(userVoted.isPresent() && id.equals(userId)) {
                    hasLiked = true;
                }
            }
        }
           System.out.println("Has Liked?: " + hasLiked);
        return hasLiked;
    }

}