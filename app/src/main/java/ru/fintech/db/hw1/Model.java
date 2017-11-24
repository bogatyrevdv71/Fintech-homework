package ru.fintech.db.hw1;

import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by DB on 24.11.2017.
 */


public class Model implements ModelInterface{
    private Api api;
    Model () {
        Gson gson = new GsonBuilder().
                registerTypeAdapter(NewsObject.class, new NewsDeserializer()).
                create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.tinkoff.ru/v1/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = retrofit.create(Api.class);
    }

    @Override
    public void get(final PresenterModelInterface i) {
        api.getNews().enqueue(new retrofit2.Callback<ApiResponse>() {
                    @Override
                    public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                        i.callback(response.body().payload);
                    }

                    @Override
                    public void onFailure(Call<ApiResponse> call, Throwable t) {
                        i.callback(null);
                    }
                });
        //return new NewsObject[0];
    }

    class ApiResponse {
        NewsObject[] payload;
    }
    public interface Api {

        @GET("news")
        Call<ApiResponse> getNews();
    }
    public class NewsDeserializer implements JsonDeserializer<NewsObject> {
        @Nullable
        public NewsObject deserialize(JsonElement json, Type typeOfT,
                                      JsonDeserializationContext context) throws JsonParseException {
            if (json.isJsonObject()) {
               String text = json.getAsJsonObject().getAsJsonPrimitive("text").
                       getAsString();
               long date = json.getAsJsonObject().getAsJsonObject("publicationDate").
                        getAsJsonPrimitive("milliseconds").getAsLong();
                return new NewsObject(text,date);
            }
            return null;
        }
    }

}
