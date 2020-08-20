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
    
    @GetMapping("/dashboard")
    public String home(@AuthenticationPrincipal User user, ModelMap model) {

        Optional<User> user1 = userRepository.findById(user.getId());
        
        Set<Long> hobbiesId = (user1.get()).getMyHobbies();
        Set<String> usersName = (user1.get()).getConnections();

        List<Hobby> hobbies = new ArrayList<>();
        List<User> users = new ArrayList<>();

        for (Iterator<Long> it = hobbiesId.iterator(); it.hasNext(); ) {
            Optional<Hobby> hobby = hobbyRepository.findById(it.next());
            if(hobby.isPresent()) {
                hobbies.add(hobby.get());
            }
        }

        Collections.sort(hobbies, new Comparator<Hobby>(){
            @Override
            public int compare(Hobby hobby1, Hobby hobby2) {
                return hobby1.getName().compareTo(hobby2.getName());
            }
        });
        
        for (Iterator<String> it = usersName.iterator(); it.hasNext(); ) {

            List<User> userList = userRepository.findByUsername(it.next());

            if(userList.size()>0) {
                users.add(userList.get(0));
            }
        }

        Collections.sort(users, new Comparator<User>(){
            @Override
            public int compare(User user1, User user2) {
                return user1.getName().compareTo(user2.getName());
            }
        });
        
        model.put("user", (user1.get()));
        model.put("hobbies", hobbies);
        model.put("connections", users);
        
        return "dashboard";
    }

    @GetMapping("/dashboard/{userId}")
    public String home(@PathVariable Long userId, ModelMap model) {

        Optional<User> userOpt = userRepository.findById(userId);

        User user = new User();

        if(userOpt.isPresent()) {
            user = userOpt.get();
        }
        Set<Long> hobbiesId = user.getMyHobbies();
        Set<String> usersName = user.getConnections();
        List<Hobby> hobbies = new ArrayList<>();
        List<User> users = new ArrayList<>();

        for (Iterator<Long> it = hobbiesId.iterator(); it.hasNext(); ) {
            Optional<Hobby> hobby = hobbyRepository.findById(it.next());
            if(hobby.isPresent()) {
                hobbies.add(hobby.get());
            }
        }

        Collections.sort(hobbies, new Comparator<Hobby>(){
            @Override
            public int compare(Hobby hobby1, Hobby hobby2) {
                return hobby1.getName().compareTo(hobby2.getName());
            }
        });
        
        for (Iterator<String> it = usersName.iterator(); it.hasNext(); ) {

            List<User> userList = userRepository.findByUsername(it.next());
            if(userList.size()>0) {
                users.add(userList.get(0));
            }
        }

        Collections.sort(users, new Comparator<User>(){
            @Override
            public int compare(User user1, User user2) {
                return user1.getName().compareTo(user2.getName());
            }
        });
 
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
        return "redirect:/dashboard";
    }

    @PostMapping("/unfollow/{userId}")
    public String removeConnection(@AuthenticationPrincipal User user, @PathVariable Long userId) {

        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isPresent()) {
            userService.removeConnection(user, userOpt.get());
        }
        return "redirect:/dashboard";
    }
}