package com.sps.hobbymatcher.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sps.hobbymatcher.domain.User;
import com.sps.hobbymatcher.domain.Hobby;
import com.sps.hobbymatcher.domain.Post;
import com.sps.hobbymatcher.repository.PostRepository;
import com.sps.hobbymatcher.repository.HobbyRepository;
// import com.google.cloud.datastore.Datastore;
// import com.google.cloud.datastore.DatastoreService;
// import com.google.cloud.datastore.Entity;
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
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private HobbyRepository hobbyRepository;

    @Autowired 
    private HobbyService hobbyService;

    @Autowired
    private UserService userService;
    
    public Post uploadPost(Hobby hobby, Post post) {

        Set<Long> posts = hobby.getPosts();
        post.setCreatedDate(new Date());
        postRepository.save(post);
        posts.add(post.getId());
        hobbyRepository.save(hobby);

        return post;
    }

    public void likePost(Post post, User user) {

        if(post.getUsersVoted().contains(user.getId()))
        {
            post.getUsersVoted().remove(user.getId());
            Long votes=post.getVotes();
            votes--;
            post.setVotes(votes);
        }
        else
        {
            post.getUsersVoted().add(user.getId());
            Long votes=post.getVotes();
            votes++;
            post.setVotes(votes);
        }
        postRepository.save(post);
        return;
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public void deletePost(Long postId, Long hobbyId) {

        Optional<Hobby> hobbyOpt = hobbyRepository.findById(hobbyId);
        Optional<Post> postOpt = postRepository.findById(postId);

        if(hobbyOpt.isPresent() && postOpt.isPresent()) {
            Hobby hobby = hobbyOpt.get();
            Set<Long> posts = hobby.getPosts();
            posts.remove(postId);
            hobbyRepository.save(hobby);
        }
        postRepository.deleteById(postId);
    }
}