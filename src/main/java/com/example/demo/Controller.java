package com.example.demo;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class Controller {

    @GetMapping("/user")
    public String getUser() {
        return "{\"login\":\"Login1\",\"status\":\"ok\"}";
    }

    @PostMapping("/user")
    public UserResponse createUser(@RequestBody UserRequest userRequest) throws InterruptedException {

        Thread.sleep(1000 + (long) (Math.random() * 1000));

        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        return new UserResponse(userRequest.getLogin(), userRequest.getPassword(), currentDate);
    }
}


