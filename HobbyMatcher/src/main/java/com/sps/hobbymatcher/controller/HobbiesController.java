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
import com.sps.hobbymatcher.repository.HobbyRepository;
import com.sps.hobbymatcher.repository.PostRepository;
import com.sps.hobbymatcher.repository.UserRepository;

@Controller
public class HobbiesController {

    @Autowired
    private UserService userService;

    @Autowired
    private HobbyService hobbyService;
    
    @Autowired
    private PostService postService;

    @Autowired
    private HobbyRepository hobbyRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/hobbies")
    public String hobbies(@AuthenticationPrincipal User user, ModelMap model) {   

        Set<Hobby> hobbies = hobbyRepository.findAll();

        List<Hobby> hobbyList = new ArrayList<>(hobbies);
        Collections.sort(hobbyList, new Comparator<Hobby>(){
            @Override
            public int compare(Hobby hobby1, Hobby hobby2) {
                return hobby1.getName().compareTo(hobby2.getName());
            }
        });

        if(user == null) {
            model.put("hobbies", hobbyList);

            System.out.println(user);
            for (Iterator<Hobby> it = hobbyList.iterator(); it.hasNext(); ){
            System.out.println(it.next());
            }
        } else {

            Optional<User> user1 = userRepository.findById(user.getId());

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

            Collections.sort(otherHobbies, new Comparator<Hobby>(){
                @Override
                public int compare(Hobby hobby1, Hobby hobby2) {
                    return hobby1.getName().compareTo(hobby2.getName());
                }
            });

            model.put("hobbies", otherHobbies);

            

            System.out.println(user);
            for (Iterator<Hobby> it = otherHobbies.iterator(); it.hasNext(); ){
            System.out.println(it.next());
            }
        }

        return "hobbies";
    }

    @PostMapping("/hobbies/{hobbyId}/register")
    public String register(@PathVariable Long hobbyId, @AuthenticationPrincipal User user) {   
        
        Optional<Hobby> hobby = hobbyRepository.findById(hobbyId);
        Optional<User> user1 = userRepository.findById(user.getId());

        userService.addHobby(user1.get(), hobby);
        
        return "redirect:/hobbies/"+hobbyId;
    }

    @PostMapping("/hobbies/{hobbyId}/unregister")
    public String unregister(@PathVariable Long hobbyId, @AuthenticationPrincipal User user) {   
        
        Optional<Hobby> hobbyOpt = hobbyRepository.findById(hobbyId);

        Optional<User> user1 = userRepository.findById(user.getId());
        if(hobbyOpt.isPresent()) {
            Hobby hobby=hobbyOpt.get();

            userService.removeHobby(user1.get(), hobby);

        }
        return "redirect:/hobbies/"+hobbyId;
    }


    @GetMapping("/hobbies/{hobbyId}")
    public String posts(@PathVariable Long hobbyId, ModelMap model, @AuthenticationPrincipal User loggedUser) {   
        
        boolean isRegistered = false;
        Optional<Hobby> hobbyOpt = hobbyRepository.findById(hobbyId);
        if(hobbyOpt.isPresent()) {
            Hobby hobby = hobbyOpt.get();
            Set<Long> postsId = hobby.getPosts();
            Set<Long> usersId = hobby.getUsers();
            List<Post> posts = new ArrayList<>();
            List<User> users = new ArrayList<>();
            for (Iterator<Long> it = postsId.iterator(); it.hasNext(); ) {

                Optional<Post> post = postRepository.findById(it.next());
                if(post.isPresent()) {
                    posts.add(post.get());
                }
            }

            Collections.sort(posts, new Comparator<Post>(){
                @Override
                public int compare(Post post1, Post post2) {
                    return post2.getCreatedDate().compareTo(post1.getCreatedDate());
                }
            });

            for (Iterator<Long> it = usersId.iterator(); it.hasNext(); ) {
                
                Long id = it.next();
                             
                if(loggedUser!=null && id.equals(loggedUser.getId()))
                {
                    
                    isRegistered=true;
                }
                Optional<User> user = userRepository.findById(id);
                if(user.isPresent()) {
                    users.add(user.get());
                }
            }

            Collections.sort(users, new Comparator<User>(){
                @Override
                public int compare(User user1, User user2) {
                    return user1.getName().compareTo(user2.getName());
                }
            });


            Optional<User> user1 = userRepository.findById(loggedUser.getId());

            model.put("user", user1.get());
            
            if(loggedUser!=null)
                model.put("isRegistered", isRegistered);
            model.put("users", users);
            model.put("posts", posts);
            model.put("hobby", hobby);
        }
        return "hobby";
    }

    @GetMapping("/createhobby/{hobbyId}")
    public String createHobby(@PathVariable Long hobbyId, ModelMap model, HttpServletResponse response) throws IOException {

        Optional<Hobby> hobbyOpt = hobbyRepository.findById(hobbyId);
        if(hobbyOpt.isPresent()) {
            Hobby hobby = hobbyOpt.get();
            model.put("hobby", hobby);
        }
        
        return "hobby";
    }

    @PostMapping("/createhobby/{hobbyId}")
    public String saveHobby(@AuthenticationPrincipal User user, @PathVariable Long hobbyId) {   
        Optional<Hobby> hobbyOpt  = hobbyRepository.findById(hobbyId);
        Hobby hobby = new Hobby();
        if(hobbyOpt.isPresent()) {
            Optional<User> user1 = userRepository.findById(user.getId());
            hobby = hobbyService.createHobby(hobbyOpt.get(), user1.get());
        }

        return "redirect:/hobbies/"+hobby.getId();
    }

    @PostMapping("/createhobby")
    public String createHobby() {   
        Hobby hobby=new Hobby();
        hobby = hobbyRepository.save(hobby);
        return "redirect:/createhobby/"+hobby.getId();
    }
}