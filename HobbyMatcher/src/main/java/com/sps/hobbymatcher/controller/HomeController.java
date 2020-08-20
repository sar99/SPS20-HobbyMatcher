package com.sps.hobbymatcher.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.sps.hobbymatcher.domain.User;
import com.sps.hobbymatcher.service.UserService;
import com.sps.hobbymatcher.domain.Hobby;
import com.sps.hobbymatcher.domain.Post;
import com.sps.hobbymatcher.service.HobbyService;
import com.sps.hobbymatcher.service.PostService;
import com.sps.hobbymatcher.repository.HobbyRepository;
import com.sps.hobbymatcher.repository.PostRepository;
import com.sps.hobbymatcher.repository.UserRepository;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private HobbyService hobbyService;

    @Autowired
    private HobbyRepository hobbyRepository;

    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/")
    public String rootView () {
        return "index";
    }

    @GetMapping("/about")
    public String about () {
        return "about";
    }
    
    @GetMapping("/dashboard")
    public String home(@AuthenticationPrincipal User user, ModelMap model) {
        
        Set<Long> hobbiesId = user.getMyHobbies();
        Set<String> usersName = user.getConnections();

        Set<Hobby> hobbies = new HashSet<>();
        Set<User> users = new HashSet<>();

        for (Iterator<Long> it = hobbiesId.iterator(); it.hasNext(); ) {
            Optional<Hobby> hobby = hobbyRepository.findById(it.next());
            if(hobby.isPresent()) {
                hobbies.add(hobby.get());
            }
        }
        
        for (Iterator<String> it = usersName.iterator(); it.hasNext(); ) {

            List<User> userList = userRepository.findByUsername(it.next());

            if(userList.size()>0) {
                users.add(userList.get(0));
            }
        }
        
        model.put("user", user);
        model.put("hobbies", hobbies);
        model.put("connections", users);
        
        return "dashboard";
    }

    @GetMapping("/dashboard/{userId}")
    public String home(@PathVariable Long userId, ModelMap model, @AuthenticationPrincipal User loggedUser) {


        boolean isFollowing = false;

        Optional<User> userOpt = userRepository.findById(userId);

        User user = new User();

        if(userOpt.isPresent()) {
            user = userOpt.get();
        }
        Set<Long> hobbiesId = user.getMyHobbies();
        Set<String> usersName = user.getConnections();
        Set<Hobby> hobbies = new HashSet<>();
        Set<User> users = new HashSet<>();

        for (Iterator<Long> it = hobbiesId.iterator(); it.hasNext(); ) {
            Optional<Hobby> hobby = hobbyRepository.findById(it.next());
            if(hobby.isPresent()) {
                hobbies.add(hobby.get());
            }
        }
        
        for (Iterator<String> it = usersName.iterator(); it.hasNext(); ) {


            List<User> userList = userRepository.findByUsername(it.next());

            if(userList.size()>0) {
                users.add(userList.get(0));
            }
        }



        Optional<User> loggedUserOpt = userRepository.findById(loggedUser.getId()); 

        User curUser = new User();

        if(loggedUserOpt.isPresent()) {
            curUser = loggedUserOpt.get();
        }

        Set<String> following = curUser.getConnections();

        for (Iterator<String> it = following.iterator(); it.hasNext(); ) {

            String id = it.next();

            System.out.println("Logged: " + user.getUsername());
            System.out.println("Current: " + id);
            System.out.println(loggedUser!=null);
            System.out.println(id.equals(user.getUsername()));
            if(loggedUser!=null && id.equals(user.getUsername()))
            {
                isFollowing=true;
            }
        }



        System.out.println("isFollowing: " + isFollowing);
        if(loggedUser!=null)
                model.put("isFollowing", isFollowing);
        model.put("user", user);
        model.put("hobbies", hobbies);
        model.put("connections", users);
        
        return "dashboard";
    }


    @PostMapping("/follow/{userId}")
    public String addConnection(@AuthenticationPrincipal User user, @PathVariable Long userId) {

        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isPresent()) {
            userService.addConnection(user, userOpt.get());
        }
        return "redirect:/dashboard/" + userId;
    }

    @PostMapping("/unfollow/{userId}")
    public String removeConnection(@AuthenticationPrincipal User user, @PathVariable Long userId) {

        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isPresent()) {
            userService.removeConnection(user, userOpt.get());
        }
        return "redirect:/dashboard/" + userId;
    }
}