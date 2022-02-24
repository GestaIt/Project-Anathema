package com.roblox.classes;

import com.roblox.requests.RobloxRequests;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;

@SuppressWarnings("unused")
public class Cookie {
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://gestalt.com")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();
    private final String cookie;

    public Cookie(String cookie) {
        this.cookie = cookie;
    }

    public String getCrossReference() throws IOException {
        RobloxRequests.Logout logout = this.retrofit.create(RobloxRequests.Logout.class);
        Call<String> responseBodyCall = logout.logout(String.format(".ROBLOSECURITY=%s", this.cookie));

        return responseBodyCall.execute().headers().get("x-csrf-token"); // synchronous
    }

    public String toString() {
        return this.cookie;
    }
}
