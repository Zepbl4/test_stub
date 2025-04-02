package com.example.controller;

import com.example.models.DataBaseWorker;
import com.example.models.User;
import com.example.models.DataBaseWorker.UserNotFoundException;
import com.example.models.FileWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;
import java.util.Date;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class Controller {

    FileWorker fileWorker = new FileWorker();

    @Autowired
    DataBaseWorker dbWorker = new DataBaseWorker();

    @GetMapping("/user")
    public ResponseEntity<?> getUser(@RequestParam String login) throws InterruptedException,SQLException {

        Thread.sleep(1000 + (long) (Math.random() * 1000));

        try {
            User user = dbWorker.getUserByLogin(login);
            fileWorker.writeUserToFile(user);
            return ResponseEntity.ok(user);

        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Failed to write users file: " + e.getMessage()));
        }
    }

    @GetMapping("/read")
    public ResponseEntity<?> getRandomUser() throws InterruptedException {

        Thread.sleep(1000 + (long) (Math.random() * 1000));

        try {
            Map<String, Object> user = fileWorker.readUserFile();
            return ResponseEntity.ok(user);

        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to read users file: " + e.getMessage()));
        } catch (FileWorker.FileIsEmpty e){
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }



    @PostMapping("/user")
    public ResponseEntity<?> createUser(@Valid @RequestBody User userRequest) throws InterruptedException {

        Thread.sleep(1000 + (long) (Math.random() * 1000));

        userRequest.setDate(new Date());
        String result = dbWorker.insertUser(userRequest);

        return ResponseEntity.ok(result);
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



