package com.company;

import com.roblox.requests.Resources;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Scanner;

public class Prompter {
    private static final Resources resources = new Resources("_|WARNING:-DO-NOT-SHARE-THIS.--Sharing-this-will-allow-someone-to-log-in-as-you-and-to-steal-your-ROBUX-and-items.|_D15215D8060F01E3D276B1BF1092C80F5E524AF78809A2435D6C3B9016383BA0AD3B3970B6777F11C1AEA7C1167176F62560F4C33DDF5A971D808D05E256BD6CB554346C89DED14BBDEDA08AD53316E8F2C376EABA542A6360D5446B0A24C1512858EF8757D0F458E5B31698F55053C3E2491A7D58419B5F0B7D8C7583339F1F070046D77EFEB747FF55F46BEFDC9FBE9C2603943759B939475FC6574E19B5108404DEFCA7AF125E66F9437F5B1728A04C7FDD8836602E61E49031442BBAD28B7B2E2185BD76B69583762E7B8D511DCB09EEADB868A86F03248D8F22E7C28B048500D362D11EB7BC0330D4DBADC03AF2172D0D56124E386C7700A66541474483EE2AD656E0C9D54DD3D8E4047C6304A8A769546158E8FC913C2AA6AD90DA1EB712343F7DE1BAB1BE372EB54B2E3280EA4E27F54EF3DC417EE790E21DDD929C7B0EB64AA96B1160C1C45FDC91F742B585B42A65F1CF341EBA61EBA3BFDE83CCB2B74CA9CF",
            "_|WARNING:-DO-NOT-SHARE-THIS.--Sharing-this-will-allow-someone-to-log-in-as-you-and-to-steal-your-ROBUX-and-items.|_eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiJiZDExOTkxMC01YmM0LTQ1ZTktODAzZi0zODMzZDJkYWY4YTciLCJzdWIiOjc4NTg1NDE1fQ.FKQ5dRhxo88a1eDhduk6gfddX0Iy3ye-qkX8g7Zlr1U;");
    private static final Scanner scanner = new Scanner(System.in);

    public static void initialize() throws IOException {
        prompt();
    }

    private static void prompt() throws IOException {
        System.out.print("\u001B[33m[Anathema] $\u001B[0m");
        String command = scanner.nextLine();

        runCommand(command);
    }

    private static void runCommand(@NotNull String command) throws IOException {
        command = command.toLowerCase();

        switch(command) {
            case "start" -> System.out.println(resources.lookupSuggestions("a", 10));

            case "options" -> {
                // display options
            }
        }

        prompt();
    }
}