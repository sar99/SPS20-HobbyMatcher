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
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequestMapping("/hobbies/{hobbyId}/post")
public class PostController {

    @Autowired
    private UserService userService;

    @Autowired
    private HobbyService hobbyService;
    
    @Autowired
    private PostService postService;
    
    @GetMapping("")
    public String createPost (ModelMap model) {

        model.put("post", new Post());
        return "post";
    }
    
    @PostMapping("")
    public String uploadPost (@PathVariable Long hobbyId, @AuthenticationPrincipal User user, @ModelAttribute Post post) {
        
        post.setUserId(user.getId());
        Optional<Hobby> hobbyOpt = hobbyService.findHobbyById(hobbyId);

        if(hobbyOpt.isPresent()) {

            Hobby hobby = hobbyOpt.get();
            post = postService.uploadPost(hobby, post);
        }
        
        return "redirect:/hobbies/"+hobbyId;
    }

    @GetMapping("/{postId}")
    public String displayPost(@PathVariable Long hobbyId, @PathVariable Long postId, ModelMap model) {

        List<String> usersVoted = new ArrayList<>();
        Optional<Post> postOpt = postService.findPostById(postId);

        if(postOpt.isPresent()) {

            Post post = postOpt.get();

            model.put("post", post);

            Set<Long> usersId = post.getUsersVoted();
            Long authorId = post.getUserId();
            Optional<User> authorOpt = userService.findUserById(authorId);

            if(authorOpt.isPresent()) {

                model.put("author", authorOpt.get());
            }

            for (Iterator<Long> it = usersId.iterator(); it.hasNext(); ) {

                Optional<User> userVoted = userService.findUserById(it.next());

                if(userVoted.isPresent()) {

                    usersVoted.add(userVoted.get().getUsername());
                }
            }

            usersVoted = userService.sortUsersByName2(usersVoted);
        }

        model.put("usersVoted", usersVoted);

        return "postdisplay";
    }


    @PostMapping("/delete/{postId}")
    public String deletePost (@PathVariable Long hobbyId, @PathVariable Long postId) {
        
        postService.deletePost(postId, hobbyId);
        return "redirect:/hobbies/"+hobbyId;
    }
    


    @PostMapping("/like/{postId}")
    public String likePost (@AuthenticationPrincipal User user, @PathVariable Long postId, @PathVariable Long hobbyId) {

        Optional<Post> postOpt = postService.findPostById(postId);

        if(postOpt.isPresent()) {

            Optional<User> user1 = userService.findUserById(user.getId());
            postService.likePost(postOpt.get(), user1.get());
        }

        return "redirect:/hobbies/"+hobbyId;
    }
}