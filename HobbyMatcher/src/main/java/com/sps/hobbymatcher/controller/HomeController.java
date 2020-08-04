package com.sps.hobbymatcher.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sps.hobbymatcher.domain.User;
import com.sps.hobbymatcher.service.UserService;
import com.sps.hobbymatcher.domain.Hobby;
import com.sps.hobbymatcher.service.HobbyService;
import com.sps.hobbymatcher.repository.HobbyRepository;

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
        Set<Hobby> hobbies = hobbyRepository.findByUser(user);
        Set<User> users = userRepository.findByUser(user);
            
        model.put("hobbies", hobbies);
        
        return "dashboard";
    }
}