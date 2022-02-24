package com.roblox.requests;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;

@SuppressWarnings("unused")
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

    public JsonObject lookupSuggestions(String keyword, int limit) throws IOException {
        RobloxRequests.SuggestionLookup suggestionLookup = this.retrofit.create(RobloxRequests.SuggestionLookup.class);
        Call<String> responseBodyCall = suggestionLookup.lookup(keyword, limit,
                String.format(".RBXID=%s", this.robloxId));

        return new Gson().fromJson(responseBodyCall.execute().body(), JsonObject.class);
    }

    public JsonObject lookupSuggestions(String keyword, int limit, int position) throws IOException {
        JsonObject suggestions = lookupSuggestions(keyword, limit);

        return suggestions.getAsJsonArray("Data").get(position).getAsJsonObject();
    }

    public JsonArray searchAssets(String assetName, String resultsPerPage) throws IOException {
        RobloxRequests.SearchAssets assetLookup = this.retrofit.create(RobloxRequests.SearchAssets.class);
        Call<String> responseBodyCall = assetLookup.search(resultsPerPage, assetName);

        return new Gson().fromJson(responseBodyCall.execute().body(), JsonArray.class);
    }

    public int uploadAsset(String name, String description, String assetInfo) throws IOException {
        RobloxRequests.UploadAsset uploadAsset = this.retrofit.create(RobloxRequests.UploadAsset.class);
        Call<String> responseBodyCall = uploadAsset.upload(name, description, "", assetInfo,
                String.format(".ROBLOSECURITY=%s", this.robloxCookie), this.getCrossReference());

        return responseBodyCall.execute().code();
    }

    public String downloadAsset(String assetId) throws IOException {
        RobloxRequests.GetAssetLocation assetLocation = this.retrofit.create(RobloxRequests.GetAssetLocation.class);
        Call<String> responseBodyCall = assetLocation.location(assetId);
        JsonObject responseJson = new Gson().fromJson(responseBodyCall.execute().body(), JsonObject.class);
        String location = responseJson.get("location").getAsString(); // asset hash location

        RobloxRequests.GetAssetInformation assetInformation = this.retrofit.create(
                RobloxRequests.GetAssetInformation.class);
        //noinspection GrazieInspection
        Call<String> responseBodyCall2 = assetInformation.download(location); // what the actual fuck

        return responseBodyCall2.execute().body();
    }

    public String getCrossReference() throws IOException {
        RobloxRequests.Logout logout = this.retrofit.create(RobloxRequests.Logout.class);
        Call<String> responseBodyCall = logout.logout(String.format(".ROBLOSECURITY=%s", this.robloxCookie));

        return responseBodyCall.execute().headers().get("x-csrf-token"); // synchronous
    }

    public void setCookie(String robloxCookie) {
        this.robloxCookie = robloxCookie;
    }

    public void setId(String robloxId) {
        this.robloxId = robloxId;
    }

    public Retrofit getRetrofit() {
        return this.retrofit;
    }
}