package com.roblox.classes;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.roblox.requests.Resources;
import com.roblox.requests.RobloxRequests;
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
public class Model {
    private final Resources resources = new Resources();
    private final String assetId;
    private final String creatorId;
    private final String productId;

    public Model(String assetId) throws IOException {
        this.assetId = assetId;

        RobloxRequests.AssetInformation assetInformationRequest = resources.getRetrofit()
                .create(RobloxRequests.AssetInformation.class);
        Call<String> assetInformationResponse = assetInformationRequest.information(this.assetId);
        Response<String> response = assetInformationResponse.execute();
        JsonObject responseJson = new Gson().fromJson(response.body(), JsonObject.class);

        this.creatorId = responseJson.getAsJsonObject("Creator").get("Id").getAsString();
        this.productId = responseJson.get("ProductId").getAsString();
    }
    public Model(@NotNull Model other) {
        this.assetId = other.assetId;
        this.creatorId = other.creatorId;
        this.productId = other.productId;
    }

    public int buy(String cookie) throws IOException {
        String body = String.format("{\"expectedCurrency\": 1,\n  \"expectedPrice\": 0,\n  \"expectedSellerId\": %s}",
                this.creatorId);

        RobloxRequests.PurchaseAsset purchaseAsset = resources.getRetrofit().create(RobloxRequests.PurchaseAsset.class);
        Call<String> purchaseResponse = purchaseAsset.purchase(this.productId, body, String.format(".ROBLOSECURITY=%s", cookie),
                resources.getCrossReference(cookie));

        return purchaseResponse.execute().code();
    }

    public int delete(String cookie) throws IOException {
        String body = String.format("assetId=%s", this.assetId);

        RobloxRequests.DeleteAsset deleteAsset = resources.getRetrofit().create(RobloxRequests.DeleteAsset.class);
        Call<String> deleteResponse = deleteAsset.delete(body,
                String.format(".ROBLOSECURITY=%s", cookie), resources.getCrossReference(cookie));

        return deleteResponse.execute().code();
    }

    public int toggleFavorite(String cookie) throws IOException {
        String body = String.format("itemTargetId=%s&favoriteType=Asset", this.assetId);

        RobloxRequests.ToggleFavorite deleteAsset = resources.getRetrofit().create(RobloxRequests.ToggleFavorite.class);
        Call<String> deleteResponse = deleteAsset.toggleFavorite(body, String.format(".ROBLOSECURITY=%s", cookie),
                resources.getCrossReference(cookie));

        return deleteResponse.execute().code();
    }

    public int like(String cookie) throws IOException {
        String body = String.format("itemTargetId=%s&favoriteType=Asset", this.assetId);

        RobloxRequests.LikeAsset likeAsset = resources.getRetrofit().create(RobloxRequests.LikeAsset.class);
        Call<String> likeResponse = likeAsset.like(this.assetId, "true", body,
                String.format(".ROBLOSECURITY=%s", cookie), resources.getCrossReference(cookie));

        return likeResponse.execute().code();
    }

    public int dislike(String cookie) throws IOException {
        String body = String.format("itemTargetId=%s&favoriteType=Asset", this.assetId);

        RobloxRequests.LikeAsset dislikeAsset = resources.getRetrofit().create(RobloxRequests.LikeAsset.class);
        Call<String> dislikeResponse = dislikeAsset.like(this.assetId, "false", body,
                String.format(".ROBLOSECURITY=%s", cookie), resources.getCrossReference(cookie));

        return dislikeResponse.execute().code();
    }

    public String getAssetId() {
        return this.assetId;
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

        resources.setRetrofit(retrofit);
    }
}