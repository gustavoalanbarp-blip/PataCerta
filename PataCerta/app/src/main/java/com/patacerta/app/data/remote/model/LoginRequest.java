package com.patacerta.app.data.remote.model;

import com.google.gson.annotations.SerializedName;

/** Corpo da requisição POST /api/login (reqres.in). */
public class LoginRequest {

    @SerializedName("email")
    private final String email;

    @SerializedName("password")
    private final String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
