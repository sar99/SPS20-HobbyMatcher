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
import org.springframework.web.bind.annotation.ResponseBody;

import com.sps.hobbymatcher.domain.User;
import com.sps.hobbymatcher.service.UserService;
import com.sps.hobbymatcher.domain.Hobby;
import com.sps.hobbymatcher.domain.Post;
import com.sps.hobbymatcher.domain.Authority;
import com.sps.hobbymatcher.service.HobbyService;
import com.sps.hobbymatcher.service.PostService;
import com.sps.hobbymatcher.repository.HobbyRepository;
import com.sps.hobbymatcher.repository.PostRepository;
import com.sps.hobbymatcher.repository.UserRepository;
import org.springframework.web.bind.annotation.ModelAttribute;

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

    @PostMapping("/dashboard/edit")
    public String edit(User user) {

        Authority authority = new Authority();
		authority.setAuthority("ROLE_USER");
		user.getAuthorities().add(authority);
        userRepository.save(user);
        return "redirect:/dashboard";

    }

    @GetMapping("/dashboard/{userId}")
    public String home(@PathVariable Long userId, ModelMap model, @AuthenticationPrincipal User loggedUser) {

        boolean isFollowing = false;

        Optional<User> userOpt = userRepository.findById(userId);

        if(userOpt.isPresent()) {

            User user = userOpt.get();

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

            if(loggedUser != null) {

                Optional<User> loggedUserOpt = userRepository.findById(loggedUser.getId()); 

                User curUser = loggedUserOpt.get();

                Set<String> following = curUser.getConnections();

                for (Iterator<String> it = following.iterator(); it.hasNext(); ) {

                    String id = it.next();

                    if(id.equals(user.getUsername())) {
                        isFollowing=true;
                        break;
                    }
                }

                model.put("isFollowing", isFollowing);
                
            }

        }
        else {
            model.put("errorMessage1", "Invalid User Id!");
        }
        
        return "dashboard";
    }


    @PostMapping("/follow/{userId}")
    public String addConnection(@AuthenticationPrincipal User user, @PathVariable Long userId) {

        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isPresent()) {
            Optional<User> user1 = userRepository.findById(user.getId());
            userService.addConnection(user1.get(), userOpt.get());
        }
        return "redirect:/dashboard/" + userId;
    }

    @PostMapping("/unfollow/{userId}")
    public String removeConnection(@AuthenticationPrincipal User user, @PathVariable Long userId) {

        Optional<User> userOpt = userRepository.findById(userId);
        if(userOpt.isPresent()) {
            Optional<User> user1 = userRepository.findById(user.getId());
            userService.removeConnection(user1.get(), userOpt.get());
        }
        return "redirect:/dashboard/" + userId;
    }
}