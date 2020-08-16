package com.sps.hobbymatcher.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.sps.hobbymatcher.domain.User;
import com.sps.hobbymatcher.service.UserService;
import com.sps.hobbymatcher.repository.UserRepository;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/login")
    public String login() {
        
        return "login";
    }
    
    @GetMapping("/register")
    public String register (ModelMap model) {

        model.put("user", new User());

        return "register";

    }
    
    @PostMapping("/register")
    public String registerPost (@ModelAttribute User user) {

        userService.registerUser(user);
        
        return "redirect:/login";
    }
}
