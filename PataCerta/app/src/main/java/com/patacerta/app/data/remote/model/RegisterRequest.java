package com.patacerta.app.data.remote.model;

import com.google.gson.annotations.SerializedName;

/** Corpo da requisição POST /api/register (reqres.in). */
public class RegisterRequest {

    @SerializedName("email")
    private final String email;

    @SerializedName("password")
    private final String password;

    public RegisterRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}
