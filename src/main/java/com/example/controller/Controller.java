package com.example.controller;

import com.example.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class Controller {

    @GetMapping("/user")
    public ResponseEntity<?> getUser() throws InterruptedException {

        Thread.sleep(1000 + (long) (Math.random() * 1000));

        return ResponseEntity.ok("{\"login\":\"Login1\",\"status\":\"ok\"}");
    }

    @PostMapping("/user")
    public ResponseEntity<?> createUser(@Valid @RequestBody User userRequest) throws InterruptedException {

        Thread.sleep(1000 + (long) (Math.random() * 1000));

        User userResponse = new User(userRequest.getLogin(), userRequest.getPassword());

        return ResponseEntity.ok(userResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(InterruptedException.class)
    public ResponseEntity<?> handleInterruptedException() {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Поток был прерван");
    }
}



