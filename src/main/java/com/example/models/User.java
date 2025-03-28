package com.example.models;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @NotBlank(message = "Enter login")
    private String login;

    @NotBlank(message = "Enter password")
    private String password;

    private Date date;

    @NotBlank(message = "Enter email")
    private String email;
}
