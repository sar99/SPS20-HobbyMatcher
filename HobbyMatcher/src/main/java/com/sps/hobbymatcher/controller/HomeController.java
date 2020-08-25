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
import com.sps.hobbymatcher.domain.Hobby;
import com.sps.hobbymatcher.domain.Post;
import com.sps.hobbymatcher.domain.Authority;
import com.sps.hobbymatcher.service.UserService;
import com.sps.hobbymatcher.service.HobbyService;
import com.sps.hobbymatcher.service.PostService;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private HobbyService hobbyService;
    
    @GetMapping("/")
    public String rootView (@AuthenticationPrincipal User user, ModelMap model) {

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

        return "index";
    }

    @GetMapping("/about")
    public String about () {

        return "about";
    }

    @GetMapping("/dashboard")
    public String home(@AuthenticationPrincipal User user, ModelMap model) {

        Optional<User> user1 = userService.findUserById(user.getId());
        Set<Long> hobbiesId = (user1.get()).getMyHobbies();
        Set<String> usersName = (user1.get()).getConnections();
        List<Hobby> hobbies = new ArrayList<>();
        List<User> users = new ArrayList<>();

        for (Iterator<Long> it = hobbiesId.iterator(); it.hasNext(); ) {

            Optional<Hobby> hobby = hobbyService.findHobbyById(it.next());

            if(hobby.isPresent()) {

                hobbies.add(hobby.get());
            }
        }

        hobbies = hobbyService.sortHobbiesByName(hobbies);
        
        for (Iterator<String> it = usersName.iterator(); it.hasNext(); ) {

            List<User> userList = userService.findUserByUsername(it.next());

            if(userList.size()>0) {

                users.add(userList.get(0));
            }
        }

        users = userService.sortUsersByName(users);

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

        userService.save(user);

        return "redirect:/dashboard";
    }

    @GetMapping("/dashboard/{userId}")
    public String home(@PathVariable Long userId, ModelMap model, @AuthenticationPrincipal User loggedUser) {

        boolean isFollowing = false;
        Optional<User> userOpt = userService.findUserById(userId);

        if(userOpt.isPresent()) {

            User user = userOpt.get();

            Set<Long> hobbiesId = user.getMyHobbies();
            Set<String> usersName = user.getConnections();
            List<Hobby> hobbies = new ArrayList<>();
            List<User> users = new ArrayList<>();

            for (Iterator<Long> it = hobbiesId.iterator(); it.hasNext(); ) {

                Optional<Hobby> hobby = hobbyService.findHobbyById(it.next());

                if(hobby.isPresent()) {

                    hobbies.add(hobby.get());
                }
            }

            hobbies = hobbyService.sortHobbiesByName(hobbies);
            
            for (Iterator<String> it = usersName.iterator(); it.hasNext(); ) {

                List<User> userList = userService.findUserByUsername(it.next());

                if(userList.size()>0) {

                    users.add(userList.get(0));
                }
            }

            users = userService.sortUsersByName(users);

            model.put("user", user);
            model.put("hobbies", hobbies);
            model.put("connections", users);

            if(loggedUser != null) {

                Optional<User> loggedUserOpt = userService.findUserById(loggedUser.getId()); 
                User curUser = loggedUserOpt.get();
                Set<String> following = curUser.getConnections();

                for (Iterator<String> it = following.iterator(); it.hasNext(); ) {

                    String id = it.next();

                    if(loggedUser!=null && id.equals(user.getUsername())) {

                        isFollowing=true;
                    }
                }

                model.put("isFollowing", isFollowing);
            }
        } else {

            model.put("errorMessage1", "Invalid User Id!");
            return "error";
        }

        return "dashboard";
    }
    
    @PostMapping("/follow/{userId}")
    public String addConnection(@AuthenticationPrincipal User user, @PathVariable Long userId) {

        Optional<User> userOpt = userService.findUserById(userId);

        if(userOpt.isPresent()) {

            Optional<User> user1 = userService.findUserById(user.getId());
            userService.addConnection(user1.get(), userOpt.get());
        }

        return "redirect:/dashboard/" + userId;
    }

    @PostMapping("/unfollow/{userId}")
    public String removeConnection(@AuthenticationPrincipal User user, @PathVariable Long userId) {

        Optional<User> userOpt = userService.findUserById(userId);

        if(userOpt.isPresent()) {

            Optional<User> user1 = userService.findUserById(user.getId());
            userService.removeConnection(user1.get(), userOpt.get());
        }
        
        return "redirect:/dashboard/" + userId;
    }
}