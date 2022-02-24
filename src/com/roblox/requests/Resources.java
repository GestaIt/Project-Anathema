package com.roblox.requests;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;

public class Resources {
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://gestalt.com")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();
    private String robloxCookie;
    private String robloxId;

    public Resources(String robloxCookie, String robloxId) {
        this.robloxCookie = robloxCookie;
        this.robloxId = robloxId;
    }
    public Resources(String robloxCookie) {
        this.robloxCookie = robloxCookie;
    }

    @SuppressWarnings("UnusedReturnValue")
    public JsonObject lookupSuggestions(String keyword, int limit) throws IOException {
        RobloxRequests.SuggestionLookup suggestionLookup = this.retrofit.create(RobloxRequests.SuggestionLookup.class);
        Call<String> responseBodyCall = suggestionLookup.lookup(keyword, limit,
                String.format(".RBXID=%s", this.robloxId));

        return new Gson().fromJson(responseBodyCall.execute().body(), JsonObject.class);
    }

    @SuppressWarnings("unused")
    public String getCrossReference() throws IOException {
        RobloxRequests.Logout logout = this.retrofit.create(RobloxRequests.Logout.class);
        Call<String> responseBodyCall = logout.logout(String.format(".ROBLOSECURITY=%s", this.robloxCookie));

        return responseBodyCall.execute().headers().get("x-csrf-token"); // synchronous
    }

    @SuppressWarnings("unused")
    public void setCookie(String robloxCookie) {
        this.robloxCookie = robloxCookie;
    }
    @SuppressWarnings("unused")
    public void setId(String robloxId) {
        this.robloxId = robloxId;
    }

    @SuppressWarnings("unused")
    public Retrofit getRetrofit() {
        return this.retrofit;
    }
}