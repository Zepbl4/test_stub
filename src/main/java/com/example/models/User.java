package com.example.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@NoArgsConstructor
public class User {
    private String login;
    private String password;
    private String date;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
        this.date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }
}
