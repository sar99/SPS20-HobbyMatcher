package com.sps.hobbymatcher;

import java.util.*;

import com.sps.hobbymatcher.domain.User;
import com.sps.hobbymatcher.domain.Post;
import com.sps.hobbymatcher.domain.Hobby;
import com.sps.hobbymatcher.service.UserService;
import com.sps.hobbymatcher.service.PostService;
import com.sps.hobbymatcher.service.HobbyService;
import com.sps.hobbymatcher.repository.HobbyRepository;
import com.sps.hobbymatcher.repository.UserRepository;
import com.sps.hobbymatcher.repository.PostRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.*;

@SpringBootApplication
public class HobbyMatcherApplication {

    @Autowired
    private HobbyService hobbyService;

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HobbyRepository hobbyRepository;

    @Autowired
    private PostRepository postRepository;

    public static void main(String[] args) {
        SpringApplication.run(HobbyMatcherApplication.class, args);
    }

}