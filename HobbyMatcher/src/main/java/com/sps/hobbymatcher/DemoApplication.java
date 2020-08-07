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

@ShellComponent
@SpringBootApplication
public class DemoApplication {

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
        SpringApplication.run(DemoApplication.class, args);
    }

    @ShellMethod("saves a user")
	public void createUser() {
        userService.registerUser("Maliha", "Maliha", "123");
	}

	@ShellMethod("saves a hobby")
	public void createHobby() throws UsernameNotFoundException {

        List<User> user = userRepository.findByUsername("Maliha");
        if(user!=null) {
            System.out.println(user);
            hobbyService.createHobby("Singing", user.get(0));
        }
        else {
            throw new UsernameNotFoundException("Invalid User");
        }
    }

    @ShellMethod("saves a post")
	public void createPost() throws UsernameNotFoundException{

        List<User> user = userRepository.findByUsername("Maliha");
        if(user==null) {
            throw new UsernameNotFoundException("Invalid User");
        }
        else {
            List<Hobby> hobby = hobbyRepository.findByName("Singing");
            if(hobby.size()>0&&user.size()>0) {
                postService.uploadPost(user.get(0), hobby.get(0));
            }
        }
	}
    @ShellMethod("removes a hobby")
    public void removeHobby() {
        List<User> user = userRepository.findByUsername("Maliha");
        if(user==null) {
            throw new UsernameNotFoundException("Invalid User");
        }
        else {
            List<Hobby> hobby = hobbyRepository.findByName("Singing");
            if(hobby.size()>0&&user.size()>0) {
                userService.removeHobby(user.get(0), hobby.get(0));
            }
        }
    }

    @ShellMethod("removes a hobby")
    public void addHobby() {
        List<User> user = userRepository.findByUsername("Maliha");
        if(user==null) {
            throw new UsernameNotFoundException("Invalid User");
        }
        else {
            List<Hobby> hobby = hobbyRepository.findByName("Singing");
            if(hobby.size()>0&&user.size()>0) {
                Optional<Hobby> hobbyOpt = hobbyRepository.findById(hobby.get(0).getId());
                userService.addHobby(user.get(0), hobbyOpt);
            }
        }
    }

}