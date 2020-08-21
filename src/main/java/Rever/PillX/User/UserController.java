package Rever.PillX.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/add-user")
    public String addUser(@RequestParam String fullName) {
        User newUser = new User(fullName);

        userRepository.save(newUser);
        return String.format("Saved %s", fullName);
    }

    @RequestMapping(value = "/get-user")
    public User getUser(@RequestParam String fullName) {
        return userRepository.findByFullName(fullName);
    }
}