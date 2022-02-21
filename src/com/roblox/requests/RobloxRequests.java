package com.roblox.requests;

import retrofit2.Call;
import retrofit2.http.*;

public class RobloxRequests {
    public interface Logout {
        @POST("auth.roblox.com/v2/")
        @Headers("Content-Type: text/html; charset=UTF-8")
        Call<String> logout(@Header("Cookie") String robloxCookie);
    }

    public interface SuggestionLookup {
        @GET("https://apis.roblox.com/autocomplete-studio/v2/suggest?cat=model")
        @Headers("User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.102 Safari/537.36")
        Call<String> lookup(@Query("prefix") String prefix, @Query("limit") int limit,
                            @Header("Cookie") String robloxCookie);
    }

    public interface AssetInformation {
        @GET("https://api.roblox.com/marketplace/productinfo?assetId={assetId}")
        Call<String> information(@Path("assetId") String assetId);
    }

    public interface PurchaseAsset {
        @POST("https://economy.roblox.com/v1/purchases/products/{productId}")
        Call<String> purchase(@Path("productId") String productId, @Body String body,
                              @Header("Cookie") String robloxCookie, @Header("X-CSRF-TOKEN") String crossReferenceToken);
    }

    public interface DeleteAsset {
        @POST("https://www.roblox.com/asset/delete-from-inventory")
        Call<String> delete(@Body String body, @Header("Cookie") String robloxCookie,
                            @Header("X-CSRF-TOKEN") String crossReferenceToken);
    }

    public interface ToggleFavorite {
        @POST("https://www.roblox.com/v2/favorite/toggle")
        @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8")
        Call<String> toggleFavorite(@Body String body, @Header("Cookie") String robloxCookie,
                                    @Header("X-CSRF-TOKEN") String crossReferenceToken);
    }

    public interface LikeAsset {
        @POST("https://www.roblox.com/voting/vote")
        @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8")
        Call<String> like(@Path("assetId") String assetId, @Path("vote") String vote, @Body String body,
                          @Header("Cookie") String robloxCookie, @Header("X-CSRF-TOKEN") String crossReferenceToken);
    }
}