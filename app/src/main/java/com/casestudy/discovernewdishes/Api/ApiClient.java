package com.casestudy.discovernewdishes.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;

    private static Retrofit getClient(String baseUrl){

        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static UserService getUserService(){
        UserService userService = getClient(ApiUtils.URLBASE).create(UserService.class);
        return userService;
    }
}
