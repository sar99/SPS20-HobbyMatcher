package com.sps.hobbymatcher.controller;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;

import java.util.*;

import javax.servlet.http.HttpServletResponse;

import com.sps.hobbymatcher.domain.User;
import com.sps.hobbymatcher.domain.Post;
import com.sps.hobbymatcher.domain.Comment;
import com.sps.hobbymatcher.domain.Hobby;
import com.sps.hobbymatcher.service.UserService;
import com.sps.hobbymatcher.service.HobbyService;
import com.sps.hobbymatcher.service.PostService;
import com.sps.hobbymatcher.service.CommentService;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private UserService userService;

    @Autowired
    private HobbyService hobbyService;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @PostMapping("/hobbies/{hobbyId}/posts/{postId}")
	public String postComment(@AuthenticationPrincipal User user, @PathVariable Long hobbyId, @PathVariable Long postId, Comment comment) {

		Optional<Hobby> hobbyOpt = hobbyService.findHobbyById(hobbyId);
        Optional<Post> postOpt = postService.findPostById(postId);

        comment.setUserId(user.getId());
        comment.setCreatedDate(new Date());
        Comment savedComment = commentService.save(comment);

        (postOpt.get()).getComments().add(savedComment);
        postService.save(postOpt.get());

		return "redirect:/hobbies/"+hobbyId+"/post/"+postId;
	}

    @PostMapping("/hobbies/{hobbyId}/posts/{postId}/{commentId}") 
    public String deleteComment(@PathVariable Long hobbyId, @PathVariable Long commentId,@PathVariable Long postId) {

        commentService.delete(commentId, postId);

        return "redirect:/hobbies/"+hobbyId+"/post/"+postId;
    }
} 