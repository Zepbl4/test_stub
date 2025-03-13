package com.example.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class Controller {

    @GetMapping("/user")
    public ResponseEntity<String> getUser() throws InterruptedException {

        Thread.sleep(1000 + (long) (Math.random() * 1000));

        return ResponseEntity.ok("{\"login\":\"Login1\",\"status\":\"ok\"}");
    }

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody User userRequest) throws InterruptedException {

        Thread.sleep(1000 + (long) (Math.random() * 1000));

        User userResponse = new User(userRequest.getLogin(), userRequest.getPassword());

        return ResponseEntity.ok(userResponse);
    }
}


