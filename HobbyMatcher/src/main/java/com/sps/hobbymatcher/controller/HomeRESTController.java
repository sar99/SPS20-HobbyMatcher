package com.sps.hobbymatcher.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sps.hobbymatcher.domain.User;
import com.sps.hobbymatcher.service.UserService;
import com.sps.hobbymatcher.domain.Hobby;
import com.sps.hobbymatcher.domain.Post;
import com.sps.hobbymatcher.domain.Authority;
import com.sps.hobbymatcher.service.HobbyService;
import com.sps.hobbymatcher.service.PostService;
import org.springframework.web.bind.annotation.ModelAttribute;

@RestController
@RequestMapping("/api")
public class HomeRESTController {

    @Autowired
    private UserService userService;

    @Autowired
    private HobbyService hobbyService;

    @GetMapping("/isunique/{username}")
    public boolean isUniqueUsername (@PathVariable String username) {

        List<User> userOpt = userService.findUserByUsername(username);


        if(userOpt.size()==1) {
            // System.out.println("falseeeee");
            return false;
        } else {
            // System.out.println("trueeeeeee");
            return true;
        }
    }

    @GetMapping("/getname/{userId}")
    public String islikedPost (@AuthenticationPrincipal User user, @PathVariable Long userId) {

        Optional<User> userOpt = userService.findUserById(userId);

        if(userOpt.isPresent()) {
            System.out.println(userOpt.get().getUsername());
            return userOpt.get().getUsername();
        } else {
            System.out.println("falseeeeeee");
            return "Name";
        }
    }
}