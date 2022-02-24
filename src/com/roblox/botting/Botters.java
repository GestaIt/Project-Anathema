package com.roblox.botting;

import com.roblox.classes.Cookie;
import com.roblox.classes.Model;

import java.io.IOException;

@SuppressWarnings("unused")
public class Botters {
    public interface BotterInterface {
        default void bot(String assetId) throws IOException {}
    }

    public interface ModelBotter extends BotterInterface {
        default void bot(String assetId, Cookie cookie) throws IOException {
            Model model = new Model(assetId);

            while(true) {
                model.buy();
                model.delete();
            }
        }
    }

    public interface FavoriteBotter extends BotterInterface {
        default void bot(String assetId, Cookie cookie) throws IOException {
            Model model = new Model(assetId);

            model.toggleFavorite();
        }
    }
}
