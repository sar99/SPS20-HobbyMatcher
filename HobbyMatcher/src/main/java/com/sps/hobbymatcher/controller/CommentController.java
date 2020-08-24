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
import com.sps.hobbymatcher.repository.HobbyRepository;
import com.sps.hobbymatcher.repository.PostRepository;
import com.sps.hobbymatcher.repository.CommentRepository;
import com.sps.hobbymatcher.repository.UserRepository;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class CommentController {

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

    @Autowired
    private CommentRepository commentRepository;

    @PostMapping("/hobbies/{hobbyId}/posts/{postId}/comment")
	public String postComment(@AuthenticationPrincipal User user, @PathVariable Long hobbyId, @PathVariable Long postId, Comment rootComment, 
			@RequestParam(required = false) Long parentId, @RequestParam(required = false) String childCommentText) {

		Optional<Hobby> hobbyOpt = hobbyRepository.findById(hobbyId);
        Optional<Post> postOpt = postRepository.findById(postId);

		if(!StringUtils.isEmpty(rootComment.getText())) {
            
            rootComment.setUserId(user.getId());
            rootComment.setCreatedDate(new Date());
            commentRepository.save(rootComment);
            (postOpt.get()).getComments().add(rootComment);
            postRepository.save(postOpt.get());

		}else if(parentId!=null) {

			Comment comment=new Comment(); 
			comment.setText(childCommentText);
			comment.setUserId(user.getId());
            comment.setCreatedDate(new Date());
            commentRepository.save(comment);

            Optional<Comment> parentCommentOpt = commentRepository.findById(parentId);

			if(parentCommentOpt.isPresent()) {
				(parentCommentOpt.get()).getReplies().add(comment);
                commentRepository.save(parentCommentOpt.get());
			}
		}
		
		return "redirect:/hobbies/"+hobbyId+"/post/"+postId;
	}
    
}