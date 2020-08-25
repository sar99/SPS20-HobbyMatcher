package com.sps.hobbymatcher.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.*;
import java.io.UnsupportedEncodingException;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.sps.hobbymatcher.domain.User;
import com.sps.hobbymatcher.domain.Post;
import com.sps.hobbymatcher.domain.Hobby;
import com.sps.hobbymatcher.service.UserService;
import com.sps.hobbymatcher.service.HobbyService;
import com.sps.hobbymatcher.service.PostService;

@Controller
public class HobbiesController {

    @Autowired
    private UserService userService;

    @Autowired
    private HobbyService hobbyService;
    
    @Autowired
    private PostService postService;
    
    @GetMapping("/hobbies")
    public String hobbies(@AuthenticationPrincipal User user, ModelMap model) {   

        Set<Hobby> hobbies = hobbyService.findAllHobbies();
        List<Hobby> hobbyList = hobbyService.sortHobbiesByName(new ArrayList<>(hobbies));

        if(user == null) {

            model.put("hobbies", hobbyList);
        } else {

            Optional<User> user1 = userService.findUserById(user.getId());
            Set<Long> hobbiesId = (user1.get()).getMyHobbies();
            List<Hobby> otherHobbies  = new ArrayList<>();

            for (Iterator<Hobby> it = hobbies.iterator(); it.hasNext(); ) {

                Hobby hobby = it.next();
                Long id = hobby.getId();

                if(hobbiesId.contains(id)) {

                    continue;
                } else {

                    otherHobbies.add(hobby);
                }
            }

            otherHobbies = hobbyService.sortHobbiesByName(otherHobbies);

            model.put("hobbies", otherHobbies);
        }

        return "hobbies";
    }

    @PostMapping("/hobbies/{hobbyId}/register")
    public String register(@PathVariable Long hobbyId, @AuthenticationPrincipal User user) {   
        
        Optional<Hobby> hobby = hobbyService.findHobbyById(hobbyId);
        Optional<User> user1 = userService.findUserById(user.getId());

        userService.addHobby(user1.get(), hobby);
        
        return "redirect:/hobbies/"+hobbyId;
    }

    @PostMapping("/hobbies/{hobbyId}/unregister")
    public String unregister(@PathVariable Long hobbyId, @AuthenticationPrincipal User user) {   
        
        Optional<Hobby> hobbyOpt = hobbyService.findHobbyById(hobbyId);
        Optional<User> user1 = userService.findUserById(user.getId());

        if(hobbyOpt.isPresent()) {

            Hobby hobby=hobbyOpt.get();
            userService.removeHobby(user1.get(), hobby);
        }

        return "redirect:/hobbies/"+hobbyId;
    }


    @GetMapping("/hobbies/{hobbyId}")
    public String posts(@PathVariable Long hobbyId, ModelMap model, @AuthenticationPrincipal User loggedUser) {   
        
        boolean isRegistered = false;
        Optional<Hobby> hobbyOpt = hobbyService.findHobbyById(hobbyId);

        if(hobbyOpt.isPresent()) {

            Hobby hobby = hobbyOpt.get();

            Set<Long> postsId = hobby.getPosts();
            Set<Long> usersId = hobby.getUsers();

            List<Post> posts = new ArrayList<>();
            List<User> users = new ArrayList<>();

            for (Iterator<Long> it = postsId.iterator(); it.hasNext(); ) {

                Optional<Post> post = postService.findPostById(it.next());

                if(post.isPresent()) {

                    posts.add(post.get());
                }
            }

            posts = postService.sortPostsByDate(posts);

            for (Iterator<Long> it = usersId.iterator(); it.hasNext(); ) {
                
                Long id = it.next();
                             
                if(loggedUser!=null && id.equals(loggedUser.getId())) {

                    isRegistered=true;
                }

                Optional<User> user = userService.findUserById(id);

                if(user.isPresent()) {

                    users.add(user.get());
                }
            }

            users = userService.sortUsersByName(users);

            Optional<User> user1 = userService.findUserById(loggedUser.getId());

            model.put("user", user1.get());
            
            if(loggedUser!=null) {

                model.put("isRegistered", isRegistered);
            }

            model.put("users", users);
            model.put("posts", posts);
            model.put("hobby", hobby);
        } else {

            model.put("errorMessage1", "No Such Hobby!");
            return "error";
        }

        return "hobby";
    }

    @GetMapping("/createhobby/{hobbyId}")
    public String createHobby(@PathVariable Long hobbyId, ModelMap model, HttpServletResponse response) throws IOException {

        Optional<Hobby> hobbyOpt = hobbyService.findHobbyById(hobbyId);

        if(hobbyOpt.isPresent()) {

            Hobby hobby = hobbyOpt.get();
            model.put("hobby", hobby);
        }
        
        return "hobby";
    }

    @PostMapping("/createhobby/{hobbyId}")
    public String saveHobby(@AuthenticationPrincipal User user, @PathVariable Long hobbyId) {   

        Optional<Hobby> hobbyOpt  = hobbyService.findHobbyById(hobbyId);
        Hobby hobby = new Hobby();

        if(hobbyOpt.isPresent()) {

            Optional<User> user1 = userService.findUserById(user.getId());
            hobby = hobbyService.createHobby(hobbyOpt.get(), user1.get());
        }

        return "redirect:/hobbies/"+hobby.getId();
    }

    @PostMapping("/createhobby")
    public String createHobby() {   

        Hobby hobby=new Hobby();
        hobby = hobbyService.save(hobby);

        return "redirect:/createhobby/"+hobby.getId();
    }
}