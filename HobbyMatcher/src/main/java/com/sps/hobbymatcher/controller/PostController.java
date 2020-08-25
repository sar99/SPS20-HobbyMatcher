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
import com.sps.hobbymatcher.domain.Comment;
import com.sps.hobbymatcher.service.UserService;
import com.sps.hobbymatcher.service.HobbyService;
import com.sps.hobbymatcher.service.PostService;
import com.sps.hobbymatcher.service.CommentService;
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

    @Autowired
    private CommentService commentService;
    
    @GetMapping("")
    public String createPost (ModelMap model, @PathVariable Long hobbyId) {

        Optional<Hobby> hobbyOpt = hobbyService.findHobbyById(hobbyId);

        if(hobbyOpt.isPresent()) {

            model.put("post", new Post());
        } else {

            model.put("errorMessage3","Invalid Hobby Id!");
            return "error";
        }

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

        Optional<Post> postOpt = postService.findPostById(postId);
        Optional<Hobby> hobbyOpt = hobbyService.findHobbyById(hobbyId);

        if(!hobbyOpt.isPresent()) {

            model.put("errorMessage4", "Invalid Hobby Id!");
            return "error";
        }

        if(postOpt.isPresent()) {

            Post post = postOpt.get();
            Hobby hobby = hobbyOpt.get();

            model.put("post", post);
            model.put("hobby", hobby);
            model.put("rootComment", new Comment());

            Set<Long> usersId = post.getUsersVoted();
            Long authorId = post.getUserId();
            Optional<User> authorOpt = userService.findUserById(authorId);

            if(authorOpt.isPresent()) {

                model.put("author", authorOpt.get());
            }

            List<User> usersVoted = new ArrayList<>();

            for (Iterator<Long> it = usersId.iterator(); it.hasNext(); ) {

                Optional<User> userVoted = userService.findUserById(it.next());

                if(userVoted.isPresent()) {

                    usersVoted.add(userVoted.get());
                }
            }

            List<Comment> comments = post.getComments();

            comments = commentService.sortCommentsByDate(comments);
            usersVoted = userService.sortUsersByName(usersVoted);

            model.put("comments", comments);
            model.put("usersVoted", usersVoted);
        } else {

            model.put("errorMessage5", "Post does not exists!");
            return "error";
        }

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