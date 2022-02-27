package com.roblox.requests;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;

@SuppressWarnings("unused")
public class Resources {
    private Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://gestalt.com")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();
    private String robloxCookie;
    private String robloxId;

    public Resources() {}
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
        Response<String> response = responseBodyCall.execute();

        return new Gson().fromJson(response.body(), JsonObject.class);
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

    public String[] uploadAsset(String name, String description, String assetInfo) throws IOException {
        RobloxRequests.UploadAsset uploadAsset = this.retrofit.create(RobloxRequests.UploadAsset.class);
        Call<String> responseBodyCall = uploadAsset.upload(name, description, "", assetInfo,
                String.format(".ROBLOSECURITY=%s", this.robloxCookie), this.getCrossReference());
        Response<String> response = responseBodyCall.execute();

        return new String[]{String.valueOf(response.code()), new Gson().fromJson(response.body(),
                JsonObject.class).get("AssetId").getAsString()};
    }

    public String[] uploadLua(String name, String description, String assetInfo) throws IOException {
        RobloxRequests.UploadLua uploadAsset = this.retrofit.create(RobloxRequests.UploadLua.class);
        Call<String> responseBodyCall = uploadAsset.upload(name, description, "", assetInfo,
                String.format(".ROBLOSECURITY=%s", this.robloxCookie), this.getCrossReference());
        Response<String> response = responseBodyCall.execute();

        return new String[]{String.valueOf(response.code()), new Gson().fromJson(response.body(),
                JsonObject.class).get("AssetId").getAsString()};
    }

    public String getAssetHash(String assetId) throws IOException {
        RobloxRequests.GetAssetLocation assetLocation = this.retrofit.create(RobloxRequests.GetAssetLocation.class);
        Call<String> responseBodyCall = assetLocation.location(assetId,
                String.format(".ROBLOSECURITY=%s", this.robloxCookie));
        JsonObject responseJson = new Gson().fromJson(responseBodyCall.execute().body(), JsonObject.class);
        String location = responseJson.get("location").getAsString(); // asset hash location

        return location.substring(location.lastIndexOf('=')+1);
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

    public String getCrossReference(String robloxCookie) throws IOException {
        RobloxRequests.Logout logout = this.retrofit.create(RobloxRequests.Logout.class);
        Call<String> responseBodyCall = logout.logout(String.format(".ROBLOSECURITY=%s", robloxCookie));
        Response<String> response = responseBodyCall.execute();

        if(response.code() == 429) {
            // set proxy lol
            return this.getCrossReference(robloxCookie);
        }

        return response.headers().get("x-csrf-token"); // synchronous
    }

    public void setCookie(String robloxCookie) {
        this.robloxCookie = robloxCookie;
    }

    public void setId(String robloxId) {
        this.robloxId = robloxId;
    }

    public void setProxy(@NotNull String proxyString) {
        String proxyHost = proxyString.substring(proxyString.contains(":") ? proxyString.lastIndexOf("/")+1 : 0,
                proxyString.lastIndexOf(':'));
        String proxyPort = proxyString.substring(proxyString.lastIndexOf(':')+1);

        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, Integer.parseInt(proxyPort)));
        OkHttpClient client = new OkHttpClient.Builder().proxy(proxy).build();

        Retrofit.Builder builder = new Retrofit.Builder().client(client);
        Retrofit retrofit = builder
                .baseUrl("https://gestalt.com")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        this.setRetrofit(retrofit);
    }

    public Retrofit getRetrofit() {
        return this.retrofit;
    }

    public void setRetrofit(Retrofit retrofit) {
        this.retrofit = retrofit;
    }
}