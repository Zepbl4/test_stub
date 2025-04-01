package com.example.controller;

import com.example.models.DataBaseWorker;
import com.example.models.User;
import com.example.models.DataBaseWorker.UserNotFoundException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Date;
import javax.validation.Valid;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api")
public class Controller {

    @Autowired
    DataBaseWorker dbWorker = new DataBaseWorker();

    private static final String USERS_WRITE_FILE = "users_write.txt";
    private static final String USERS_READ_FILE = "users_read.txt";
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Random random = new Random();

    {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new java.text.SimpleDateFormat("yyyy-MM-dd"));
    }

    @GetMapping("/user")
    public ResponseEntity<?> getUser(@RequestParam String login) throws InterruptedException,SQLException {

        Thread.sleep(1000 + (long) (Math.random() * 1000));

        try {
            User user = dbWorker.getUserByLogin(login);
            writeUserToFile(user);
            return ResponseEntity.ok(user);

        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", e.getMessage()));

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Failed to write users file: " + e.getMessage()));
        }
    }

    @GetMapping("/read")
    public ResponseEntity<?> getRandomUser() {
        try (RandomAccessFile file = new RandomAccessFile(USERS_READ_FILE, "r")) {
            String randomLine = getRandomLine(file);

            if (randomLine == null) {
                return ResponseEntity.status(404).body(Map.of("error", "File is empty"));
            }

            Map<String, Object> user = objectMapper.readValue(
                    randomLine,
                    new TypeReference<>() {}
            );
            return ResponseEntity.ok(user);

        } catch (IOException e) {
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Failed to read users file: " + e.getMessage()));
        }
    }

    private String getRandomLine(RandomAccessFile file) throws IOException {
        String selectedLine = null;
        int count = 0;
        String line;

        // Используем алгоритм Reservoir Sampling
        while ((line = file.readLine()) != null) {
            count++;
            if (random.nextInt(count) == 0) {
                selectedLine = line;
            }
        }

        return selectedLine;
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

    private void writeUserToFile(User user) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_WRITE_FILE, true))) {
            String userJson = objectMapper.writeValueAsString(user);
            writer.write(userJson + System.lineSeparator());
        }
    }
}



