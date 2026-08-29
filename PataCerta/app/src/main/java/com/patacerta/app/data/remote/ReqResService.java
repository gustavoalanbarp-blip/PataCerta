package com.patacerta.app.data.remote;

import com.patacerta.app.data.remote.model.LoginRequest;
import com.patacerta.app.data.remote.model.LoginResponse;
import com.patacerta.app.data.remote.model.RegisterRequest;
import com.patacerta.app.data.remote.model.RegisterResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * API pública usada para demonstrar o fluxo de autenticação (login/cadastro)
 * end-to-end sem depender de um backend próprio: https://reqres.in
 *
 * Credenciais de teste documentadas pela própria reqres.in:
 *   email: eve.holt@reqres.in | password: cityslicka  -> login bem-sucedido
 */
public interface ReqResService {

    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest request);

    @POST("register")
    Call<RegisterResponse> register(@Body RegisterRequest request);
}
