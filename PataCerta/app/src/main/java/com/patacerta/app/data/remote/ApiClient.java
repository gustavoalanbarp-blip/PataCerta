package com.patacerta.app.data.remote;

import com.patacerta.app.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

/**
 * Ponto único de configuração dos três clientes Retrofit usados pelo app.
 * Mantém timeouts curtos e um interceptor de log (apenas em debug) para
 * facilitar a depuração das integrações descritas no relatório técnico.
 */
public final class ApiClient {

    // Chave gratuita de demonstração da TheDogAPI. Em produção, mover para
    // local.properties / variável de ambiente e nunca versionar em texto puro.
    private static final String DOG_API_KEY = "live_demo_key_replace_me";

    private static Retrofit reqResRetrofit;
    private static Retrofit dogApiRetrofit;
    private static Retrofit nominatimRetrofit;

    private ApiClient() {}

    private static OkHttpClient baseClient(boolean withApiKeyHeader) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(BuildConfig.DEBUG
                ? HttpLoggingInterceptor.Level.BODY
                : HttpLoggingInterceptor.Level.NONE);

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(logging)
                // Identifica o app para a Política de Uso do Nominatim.
                .addInterceptor(chain -> {
                    Request req = chain.request().newBuilder()
                            .header("User-Agent", "PataCerta-Android/1.0 (contato@patacerta.app)")
                            .build();
                    return chain.proceed(req);
                });

        if (withApiKeyHeader) {
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request req = chain.request().newBuilder()
                            .header("x-api-key", DOG_API_KEY)
                            .build();
                    return chain.proceed(req);
                }
            });
        }
        return builder.build();
    }

    public static ReqResService reqResService() {
        if (reqResRetrofit == null) {
            reqResRetrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.REQRES_BASE_URL)
                    .client(baseClient(false))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return reqResRetrofit.create(ReqResService.class);
    }

    public static DogApiService dogApiService() {
        if (dogApiRetrofit == null) {
            dogApiRetrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.DOGAPI_BASE_URL)
                    .client(baseClient(true))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return dogApiRetrofit.create(DogApiService.class);
    }

    public static NominatimService nominatimService() {
        if (nominatimRetrofit == null) {
            nominatimRetrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.NOMINATIM_BASE_URL)
                    .client(baseClient(false))
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return nominatimRetrofit.create(NominatimService.class);
    }
}
