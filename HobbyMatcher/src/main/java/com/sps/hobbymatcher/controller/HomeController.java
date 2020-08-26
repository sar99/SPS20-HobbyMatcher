package com.sps.hobbymatcher.controller;

import java.util.*;
import java.time.*;
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

        String[][] facts = { {"Rod Stewart hosted the largest ever free concert", "5679095853613056"},
                            {"Singing in a group boosts mood.", "5679095853613056"},
                            {"Dancers are known to be disciplined, focused and high achievers who tend to be successful students and hard workers. Research also proves that dancing also reduces stress and tension for the mind and body", "5720592418340864"},
                            {"The 'Dancing Plague' of 1518 was a mania that lasted a month and killed dozens of people in Strasbourg, France through exhaustion or heart attack. People just danced uncontrollably until they collapsed!One other famous case involved people dancing on a bridge. Eventually so many people danced that they broke the bridge and fell into the river.", "5720592418340864"},
                            {"A world record for the longest conga dance line was set by 119,986 people in Miami in 1988.", "5720592418340864"},
                            {"Sketching can even improve your holistic health.", "5699409840963584"},
                            {"Van Gogh has only sold one painting during his lifetime.", "5699409840963584"},
                            {"Picasso believed that art is done to wash away the dust of our daily lives from our souls.", "5699409840963584"},
                            {"The city of Monaco is smaller in size than Central Park in New York City", "5667525748588544"},
                            {"Only 4% of all the world’s languages are spoken by 96% of its population", "5667525748588544"},
                            {"San Marino has more cars in its country than people", "5667525748588544"},
                            {"Hiking has been known to increase the satisfaction level of many types of freeze dried food, as well as several flavors of energy bars, up to 35 percent.", "5714920645591040"},
                            {"Bears basically spend their entire lives hiking.", "5714920645591040"},
                            {"Walking along all of Switzerland’s hiking trails would be the equivalent of going one-and-a-half times around the world.", "5714920645591040"},
                            {"Reading can help prevent Alzheimer’s.", "5655374346584064"},
                            {"In the Harvard Library, there are three books suspected to be bound in human skin.", "5655374346584064"},
                            {"The word ‘mogigraphia’ means ‘writer’s cramp’.", "5715948350734336"},
                            {"The word ‘colygraphia’ means ‘writer’s block’.", "5715948350734336"},
                            {"The first programmer in the world was a woman. Her name was Ada Lovelace and she worked on an analytical engine back in the 1,800’s.", "5092424393162752"},
                            {"Nowadays, there are over 700 different programming languages.", "5092424393162752"},
                            {"Chopsticks were initially created for cooking, not as an eating utensil.", "5765243099676672"},
                            {"Black pepper was so valuable, it used to be a currency in the Middle Ages.", "5765243099676672"},
                            {"Studies indicate that surgeons who regularly play video games make 37% fewer mistakes and operate 27% faster than their peers.", "5760334488928256"},
                            {"Video gamers are found to be better in multitasking, driving and navigating around the streets.", "5760334488928256"},
                            {"Games provide a 23 percent gain over traditional learning.", "5760334488928256"},
                            {"Video gamers are found to be better in multitasking, driving and navigating around the streets.", "5760334488928256"},
                            {"The first computer virus was created in 1983.", "5092424393162752"},
                            {"Elizabethan scribe Peter Bales reportedly produced a complete, handwritten copy of the Bible so small it could fit inside a walnut shell.", "5715948350734336"},
                            {"The largest book ever published – “The Little Prince” – is almost 7 feet high and 10 feet wide.", "5655374346584064"},
                            {"Russia is bigger than Pluto.", "5667525748588544"},
                            {"Picasso believed that art is done to wash away the dust of our daily lives from our souls.", "5720592418340864"} }; 


        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int day = localDate.getDayOfMonth();

        Long factHobbyId = Long.parseLong(facts[day-1][1]);
        // System.out.println(factHobbyId);
        Optional<Hobby> factHobby = hobbyService.findHobbyById(factHobbyId);
        // System.out.println(factHobby.isPresent());
        model.put("fact", facts[day-1][0]);
        model.put("facthobby", factHobby.get());

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

            model.put("errorMessage2", "Invalid User Id!");
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