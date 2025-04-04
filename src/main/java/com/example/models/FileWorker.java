package com.example.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class FileWorker {
    private final Random random = new Random();
    private static final String USERS_WRITE_FILE = "users_write.txt";
    private static final String USERS_READ_FILE = "users_read.txt";
    private final ObjectMapper objectMapper = new ObjectMapper();

    {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new java.text.SimpleDateFormat("yyyy-MM-dd"));
    }

    public User readUserFile() throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(USERS_READ_FILE, "r")) {

            List<String> lines = new ArrayList<>(10);
            String line;

            while ((line = file.readLine()) != null) {
                lines.add(line);
            }

            String randomLine = lines.get(random.nextInt(10));

            return objectMapper.readValue(randomLine, User.class);

        }  catch (IOException e) {
            throw new IOException("Failed to read users file: " + e);
        }
    }

    public void writeUserToFile(User user) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_WRITE_FILE, true))) {

            String userJson = objectMapper.writeValueAsString(user);
            writer.write(userJson + System.lineSeparator());
        }
    }
}
