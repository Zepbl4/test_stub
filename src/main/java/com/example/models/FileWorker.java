package com.example.models;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.Random;

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

    private String getRandomLine(RandomAccessFile file) throws IOException {
        String selectedLine = null;
        int count = 0;
        String line;

        while ((line = file.readLine()) != null) {
            count++;
            if (random.nextInt(count) == 0) {
                selectedLine = line;
            }
        }
        return selectedLine;
    }

    public Map<String, Object> readUserFile() throws IOException {
        try (RandomAccessFile file = new RandomAccessFile(USERS_READ_FILE, "r")) {

            String randomLine = getRandomLine(file);

            if (randomLine != null) {
                return objectMapper.readValue(randomLine, new TypeReference<>() {});

            } else {
                throw new FileIsEmpty("File is empty");
            }

        }  catch (IOException e) {
            throw new IOException("Failed to read users file: " + e.getMessage());
        }
    }

    public void writeUserToFile(User user) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_WRITE_FILE, true))) {

            String userJson = objectMapper.writeValueAsString(user);
            writer.write(userJson + System.lineSeparator());
        }
    }

    static public class FileIsEmpty extends RuntimeException {
        public FileIsEmpty(String message) {
            super(message);
        }
    }
}
