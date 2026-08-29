package com.patacerta.app.data.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.patacerta.app.data.remote.ApiClient;
import com.patacerta.app.data.remote.model.LoginRequest;
import com.patacerta.app.data.remote.model.LoginResponse;
import com.patacerta.app.data.remote.model.RegisterRequest;
import com.patacerta.app.data.remote.model.RegisterResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Camada responsável por autenticação. Consome a API reqres.in (ver ApiClient)
 * e guarda o token retornado em SharedPreferences para simular uma sessão
 * autenticada nas telas seguintes.
 */
public class AuthRepository {

    private static final String PREFS = "patacerta_session";
    private static final String KEY_TOKEN = "token";
    private static final String KEY_NAME = "user_name";

    public interface AuthCallback {
        void onSuccess(String token);
        void onError(String message);
    }

    private final SharedPreferences prefs;

    public AuthRepository(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void login(String email, String password, String displayName, AuthCallback callback) {
        ApiClient.reqResService()
                .login(new LoginRequest(email, password))
                .enqueue(new Callback<LoginResponse>() {
                    @Override
                    public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                        LoginResponse body = response.body();
                        if (response.isSuccessful() && body != null && body.getToken() != null) {
                            saveSession(body.getToken(), displayName);
                            callback.onSuccess(body.getToken());
                        } else {
                            String msg = body != null && body.getError() != null
                                    ? body.getError()
                                    : "Falha de autenticação (HTTP " + response.code() + ")";
                            callback.onError(msg);
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginResponse> call, Throwable t) {
                        callback.onError("Sem conexão com o servidor: " + t.getMessage());
                    }
                });
    }

    public void register(String email, String password, String displayName, AuthCallback callback) {
        ApiClient.reqResService()
                .register(new RegisterRequest(email, password))
                .enqueue(new Callback<RegisterResponse>() {
                    @Override
                    public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                        RegisterResponse body = response.body();
                        if (response.isSuccessful() && body != null && body.getToken() != null) {
                            saveSession(body.getToken(), displayName);
                            callback.onSuccess(body.getToken());
                        } else {
                            String msg = body != null && body.getError() != null
                                    ? body.getError()
                                    : "Não foi possível concluir o cadastro (HTTP " + response.code() + ")";
                            callback.onError(msg);
                        }
                    }

                    @Override
                    public void onFailure(Call<RegisterResponse> call, Throwable t) {
                        callback.onError("Sem conexão com o servidor: " + t.getMessage());
                    }
                });
    }

    private void saveSession(String token, String displayName) {
        prefs.edit()
                .putString(KEY_TOKEN, token)
                .putString(KEY_NAME, displayName)
                .apply();
    }

    public boolean isLoggedIn() {
        return prefs.getString(KEY_TOKEN, null) != null;
    }

    public String getDisplayName() {
        return prefs.getString(KEY_NAME, "Tutor(a)");
    }

    public void logout() {
        prefs.edit().clear().apply();
    }
}
