package com.patacerta.app.data.remote.model;

import com.google.gson.annotations.SerializedName;

/** Resposta de POST /api/register (reqres.in): {"id": 4, "token": "..."} */
public class RegisterResponse {

    @SerializedName("id")
    private int id;

    @SerializedName("token")
    private String token;

    @SerializedName("error")
    private String error;

    public int getId() { return id; }
    public String getToken() { return token; }
    public String getError() { return error; }
}
