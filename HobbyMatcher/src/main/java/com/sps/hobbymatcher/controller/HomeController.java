package com.sps.hobbymatcher.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;

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
    
    @GetMapping("/")
    public String rootView () {
        return "index";
    }
    
    @GetMapping("/dashboard")
    public String home(@AuthenticationPrincipal User user, ModelMap model) {


        System.out.println(user);
        
        Set<Long> hobbies = user.getMyHobbies();
        Set<String> users = user.getConnections();

        
            
        if(hobbies==null)
        model.put("hobbies", new HashSet<>());    
        else 
        model.put("hobbies", hobbies);

        if(users==null)
        model.put("connections", new HashSet<>());    
        else 
        model.put("connections", users);
        
        return "dashboard";
    }
}