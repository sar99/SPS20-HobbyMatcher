package com.sps.hobbymatcher.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sps.hobbymatcher.domain.User;
import com.sps.hobbymatcher.domain.Hobby;
import com.sps.hobbymatcher.domain.Post;
import com.sps.hobbymatcher.domain.Comment;
import com.sps.hobbymatcher.repository.CommentRepository;
import com.sps.hobbymatcher.service.PostService;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.DatastoreServiceConfig;
import com.google.appengine.api.datastore.ReadPolicy.Consistency;

import java.awt.Image;
import java.util.Date;
import java.util.HashSet;
import java.util.*;

@Service
public class CommentService {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentRepository commentRepository;

    public List<Comment> sortCommentsByDate(List<Comment> comments) {

        Collections.sort(comments, new Comparator<Comment>(){
            @Override
            public int compare(Comment comment1, Comment comment2) {
                return comment2.getCreatedDate().compareTo(comment1.getCreatedDate());
            }
        });

        return comments;
    }

    public Optional<Comment> findCommentById(Long id) {

        return commentRepository.findById(id);
    }

    public Comment save(Comment comment) {

        return commentRepository.save(comment);
    }

    public void delete(Long commentId, Long postId) {

        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        Optional<Post> postOpt = postService.findPostById(postId);
        Post post = postOpt.get();
        Comment comment = commentOpt.get();

        (post.getComments()).remove(comment);
        postService.save(post);
        
        commentRepository.delete(comment);
        
        return ;
    }
}