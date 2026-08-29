package com.patacerta.app.data.remote.model;

import com.google.gson.annotations.SerializedName;

/** Resposta de POST /api/login (reqres.in): {"token": "..."} */
public class LoginResponse {

    @SerializedName("token")
    private String token;

    @SerializedName("error")
    private String error;

    public String getToken() { return token; }
    public String getError() { return error; }
}
