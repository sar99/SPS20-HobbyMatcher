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
import com.sps.hobbymatcher.domain.Comment;
import com.sps.hobbymatcher.domain.Hobby;
import com.sps.hobbymatcher.service.UserService;
import com.sps.hobbymatcher.service.HobbyService;
import com.sps.hobbymatcher.service.PostService;
import com.sps.hobbymatcher.repository.HobbyRepository;
import com.sps.hobbymatcher.repository.PostRepository;
import com.sps.hobbymatcher.repository.UserRepository;
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
    private HobbyRepository hobbyRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("")
    public String createPost (ModelMap model, @PathVariable Long hobbyId) {

        Optional<Hobby> hobbyOpt = hobbyRepository.findById(hobbyId);

        if(hobbyOpt.isPresent()) {
            model.put("post", new Post());
        }
        else {
            model.put("ErrorMessage1","Invalid Hobby Id!");
        }

        return "post";
    }
    
    @PostMapping("")
    public String uploadPost (@PathVariable Long hobbyId, @AuthenticationPrincipal User user, @ModelAttribute Post post) {
        
        post.setUserId(user.getId());

        Optional<Hobby> hobbyOpt = hobbyRepository.findById(hobbyId);

        if(hobbyOpt.isPresent()) {
            Hobby hobby = hobbyOpt.get();
            post = postService.uploadPost(hobby, post);
        }
        
        return "redirect:/hobbies/"+hobbyId;
    }

    @GetMapping("/{postId}")
    public String displayPost(@PathVariable Long hobbyId, @PathVariable Long postId, ModelMap model) {

        Optional<Post> postOpt = postRepository.findById(postId);
        Optional<Hobby> hobbyOpt = hobbyRepository.findById(hobbyId);

        if(!hobbyOpt.isPresent()) {
            model.put("ErrorMessage2", "Invalid Hobby Id!");
        }

        if(postOpt.isPresent()) {

            Post post = postOpt.get();
            Hobby hobby = hobbyOpt.get();

            model.put("post", post);
            model.put("hobby", hobby);
            model.put("rootComment", new Comment());

            Set<Long> usersId = post.getUsersVoted();
            Long authorId = post.getUserId();

            Optional<User> authorOpt = userRepository.findById(authorId);
            
            if(authorOpt.isPresent()) {
                model.put("author", authorOpt.get());
            }

            List<String> usersVoted = new ArrayList<>();

            for (Iterator<Long> it = usersId.iterator(); it.hasNext(); ) {

                Optional<User> userVoted = userRepository.findById(it.next());

                if(userVoted.isPresent()) {
                    usersVoted.add(userVoted.get().getUsername());
                }
            }
            
            Collections.sort(usersVoted, new Comparator<String>(){
                @Override
                public int compare(String user1, String user2) {
                    return user1.compareTo(user2);
                }
            });

            List<Comment> comments = post.getComments();

            Collections.sort(comments, new Comparator<Comment>(){
                @Override
                public int compare(Comment comment1, Comment comment2) {
                    return (comment2.getCreatedDate()).compareTo(comment1.getCreatedDate());
                }
            });
            
            model.put("comments", comments);
            model.put("usersVoted", usersVoted);
        }
        else {
            model.put("ErrorMessage3", "Post does not exists!");
        }

        return "displayPost";
    }


    @PostMapping("/delete/{postId}")
    public String deletePost (@PathVariable Long hobbyId, @PathVariable Long postId) {
        
        postService.deletePost(postId, hobbyId);
        
        return "redirect:/hobbies/"+hobbyId;
    }
    


    @PostMapping("/like/{postId}")
    public String likePost (@AuthenticationPrincipal User user, @PathVariable Long postId, @PathVariable Long hobbyId) {

        Optional<Post> postOpt = postRepository.findById(postId);

        if(postOpt.isPresent()) {
            Optional<User> user1 = userRepository.findById(user.getId());
            postService.likePost(postOpt.get(), user1.get());
        }
           System.out.println("LIKKEDDDD!!!");

        return "redirect:/hobbies/"+hobbyId;
    }
}