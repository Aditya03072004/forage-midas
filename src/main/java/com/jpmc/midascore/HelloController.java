package com.jpmc.midascore;

import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/balance/{userId}")
    public String getBalance(@PathVariable long userId) {
        UserRecord user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return "User not found";
        }
        return user.getName() + " has balance " + user.getBalance();
    }

}
