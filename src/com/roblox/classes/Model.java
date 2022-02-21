package com.roblox.classes;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.roblox.requests.Resources;
import com.roblox.requests.RobloxRequests;
import retrofit2.Call;

import java.io.IOException;

@SuppressWarnings("unused")
public class Model {
    private final Resources resources = new Resources("");
    private final String assetId;
    private final String creatorId;
    private final String productId;

    public Model(String assetId) throws IOException {
        this.assetId = assetId;

        RobloxRequests.AssetInformation assetInformationRequest = resources.getRetrofit()
                .create(RobloxRequests.AssetInformation.class);
        Call<String> assetInformationResponse = assetInformationRequest.information(this.assetId);
        JsonObject responseJson = new Gson().fromJson(assetInformationResponse.execute().body(), JsonObject.class);

        this.creatorId = responseJson.getAsJsonObject("Creator").get("Id").getAsString();
        this.productId = responseJson.get("ProductId").getAsString();
    }

    public int buy() throws IOException {
        String body = String.format("{\"expectedCurrency\": 1,\n  \"expectedPrice\": 0,\n  \"expectedSellerId\": %s}",
                this.creatorId);

        RobloxRequests.PurchaseAsset purchaseAsset = resources.getRetrofit().create(RobloxRequests.PurchaseAsset.class);
        Call<String> purchaseResponse = purchaseAsset.purchase(this.productId, body, "", resources.getCrossReference());

        return purchaseResponse.execute().code();
    }

    public int delete() throws IOException {
        String body = String.format("assetId=%s", this.assetId);

        RobloxRequests.DeleteAsset deleteAsset = resources.getRetrofit().create(RobloxRequests.DeleteAsset.class);
        Call<String> deleteResponse = deleteAsset.delete(body, "", resources.getCrossReference());

        return deleteResponse.execute().code();
    }

    public int toggleFavorite() throws IOException {
        String body = String.format("itemTargetId=%s&favoriteType=Asset", this.assetId);

        RobloxRequests.ToggleFavorite deleteAsset = resources.getRetrofit().create(RobloxRequests.ToggleFavorite.class);
        Call<String> deleteResponse = deleteAsset.toggleFavorite(body, "", resources.getCrossReference());

        return deleteResponse.execute().code();
    }

    public int like() throws IOException {
        String body = String.format("itemTargetId=%s&favoriteType=Asset", this.assetId);

        RobloxRequests.LikeAsset likeAsset = resources.getRetrofit().create(RobloxRequests.LikeAsset.class);
        Call<String> likeResponse = likeAsset.like(this.assetId, "true", body, "",
                resources.getCrossReference());

        return likeResponse.execute().code();
    }

    public int dislike() throws IOException {
        String body = String.format("itemTargetId=%s&favoriteType=Asset", this.assetId);

        RobloxRequests.LikeAsset dislikeAsset = resources.getRetrofit().create(RobloxRequests.LikeAsset.class);
        Call<String> dislikeResponse = dislikeAsset.like(this.assetId, "false", body, "",
                resources.getCrossReference());

        return dislikeResponse.execute().code();
    }

    @SuppressWarnings("unused")
    public String getAssetId() {
        return this.assetId;
    }
}