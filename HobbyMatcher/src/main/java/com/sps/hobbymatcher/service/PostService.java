package com.sps.hobbymatcher.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sps.hobbymatcher.domain.User;
import com.sps.hobbymatcher.domain.Hobby;
import com.sps.hobbymatcher.domain.Post;
import com.sps.hobbymatcher.domain.Comment;
import com.sps.hobbymatcher.repository.PostRepository;
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
    private HobbyService hobbyService;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentService commentService;

    public Optional<Post> findPostById(Long id) {

        return postRepository.findById(id);
    }
    
    public List<Post> sortPostsByDate(List<Post> posts) {

        Collections.sort(posts, new Comparator<Post>(){
            @Override
            public int compare(Post post1, Post post2) {
                return post2.getCreatedDate().compareTo(post1.getCreatedDate());
            }
        });

        return posts;
    }

    public Post uploadPost(Hobby hobby, Post post) {

        Set<Long> posts = hobby.getPosts();
       
        post.setCreatedDate(new Date());
        postRepository.save(post);
        posts.add(post.getId());

        hobbyService.save(hobby);

        return post;
    }

    public void likePost(Post post, User user) {

        if(post.getUsersVoted().contains(user.getId())){

            post.getUsersVoted().remove(user.getId());

            long votes=post.getVotes();
            votes--;

            post.setVotes(votes);
        } else {

            post.getUsersVoted().add(user.getId());

            long votes=post.getVotes();
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

        Optional<Hobby> hobbyOpt = hobbyService.findHobbyById(hobbyId);
        Optional<Post> postOpt = postRepository.findById(postId);
        Hobby hobby = hobbyOpt.get();
        Post post = postOpt.get();

        Set<Long> posts = hobby.getPosts();

        posts.remove(postId);
        hobbyService.save(hobby);

        List<Comment> comments = post.getComments();

        for (Iterator<Comment> it = comments.iterator(); it.hasNext(); ) {

            commentService.delete(it.next().getId(), postId);
        }

        postRepository.deleteById(postId);
    }
}