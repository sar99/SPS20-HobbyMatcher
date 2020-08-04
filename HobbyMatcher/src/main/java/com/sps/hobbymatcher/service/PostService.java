package com.sps.hobbymatcher.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sps.hobbymatcher.domain.User;
import com.sps.hobbymatcher.domain.Hobby;
import com.sps.hobbymatcher.domain.Post;
import com.sps.hobbymatcher.repository.PostRepository;
import com.sps.hobbymatcher.repository.HobbyRepository;

import java.awt.Image;
import java.util.Date;
import java.util.HashSet;
import java.util.*;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public Post uploadPost(String text, String video, Image image, Long votes, User user, Hobby hobby) {
        
        Post post = new Post();

        post.setText(text);
        post.setVideo(video);
        post.setImage(image);
        post.setVotes(votes);
        post.setUser(user);
        post.setHobby(hobby);
        user.getMyPosts().add(post);
        hobby.getPosts().add(post);
        return postRepository.save(post);
    }

    public void likePost(Post post, User user) {

        if(post.getUsersVoted().contains(user))
        {
            post.getUsersVoted().remove(user);
            Long votes=post.getVotes();
            votes--;
            post.setVotes(votes);
        }
        else
        {
            post.getUsersVoted().add(user);
            Long votes=post.getVotes();
            votes++;
            post.setVotes(votes);
        }
        return;
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    public void deletePost(Post post) {
        post.getUser().getMyPosts().remove(post);
        post.getHobby().getPosts().remove(post);
        postRepository.deleteById(post.getId());
    }
}