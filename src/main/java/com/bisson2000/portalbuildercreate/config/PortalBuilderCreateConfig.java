package com.bisson2000.portalbuildercreate.config;

import com.bisson2000.portalbuildercreate.PortalBuilderCreate;
import com.bisson2000.portalbuildercreate.portalbuilder.CustomPortal;
import com.bisson2000.portalbuildercreate.portalbuilder.JsonDeserializerWithOptions;
import com.google.gson.reflect.TypeToken;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.List;

public class PortalBuilderCreateConfig {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(CustomPortal.class, new JsonDeserializerWithOptions<CustomPortal>())
            .setPrettyPrinting()
            .create();
    private static final Type CUSTOM_PORTAL_LIST_TYPE = new TypeToken<List<CustomPortal>>() {}.getType();
    private static List<CustomPortal> DEFAULT_LIST = new ArrayList<>(List.of(
            new CustomPortal("minecraft:diamond_block", "minecraft:lava", "minecraft:overworld", "minecraft:the_nether", Color.red),
            new CustomPortal("minecraft:diamond_ore", "minecraft:ender_eye", "minecraft:the_nether", "minecraft:the_end", Color.blue)
    ));
    private static String DEFAULT_INSTRUCTIONS = "/*\n" +
            " * Configuration file for " + PortalBuilderCreate.MOD_ID + "\n" +
            " * You can leave this comment in the file" + "\n" +
            " * " + "\n" +
            " * Parameters explained:" + "\n" +
            " * @Required: frameBlock \t\t\t The block used to create the portal frame. Must be in format modid:block_name. E.g.: minecraft:diamond_block" + "\n" +
            " * @Required: igniter \t\t\t\t The item/block/fluid used to ignite the Portal. Must be in format modid:block_name. E.g.: minecraft:fire" + "\n" +
            " * @Required: fromDim \t\t\t\t The dimension from which the portal starts. Must be in format modid:dim_name. E.g.: minecraft:overworld" + "\n" +
            " * @Required: toDim \t\t\t\t The dimension from which the portal ends. Must be in format modid:dim_name. E.g.: minecraft:the_nether" + "\n" +
            " * @Required: rgb \t\t\t\t\t The color of the portal. Red, Green and Blue values must be between 0 and 255" + "\n" +
            " * @Optional: onlyIgniteInFromDim \t True by default. True if a portal can only be ignited in one of its targeted dim. " +
                "For example, if the value is set to false, then a portal that links minecraft:the_end and minecraft:the_nether can be activated in minecraft:overworld" + "\n" +
            " * @Optional: isFlatPortal \t\t\t False by default. False means the portal is vertical, like the nether portal. True means the portal is horizontal, like the end portal" + "\n" +
            " * @Optional: forceSize \t\t\t False by default. If the portal is required to have a specific size and match the width and height specified" + "\n" +
            " * @Optional: showCredits \t\t\t False by default. If the player will view credits when traversing the portal" + "\n" +
            " * " + "\n" +
            " * The portals are specified as objects, in a list" + "\n" +
            " * Example how the json should look like" + "\n" +
            " * [" + "\n" +
            " *     { " + "\n" +
            " *         \"frameBlock\": \"minecraft:diamond_block\", " + "\n" +
            " *         \"igniter\": \"minecraft:lava\", " + "\n" +
            " *         \"fromDim\": \"minecraft:overworld\", " + "\n" +
            " *         \"toDim\": \"minecraft:the_nether\", " + "\n" +
            " *         \"r\": 255, " + "\n" +
            " *         \"g\": 0, " + "\n" +
            " *         \"b\": 0, " + "\n" +
            " *         \"onlyIgniteInDims\": true, " + "\n" +
            " *         \"isFlatPortal\": false, " + "\n" +
            " *         \"forceSize\": false, " + "\n" +
            " *         \"forcedSizeWidth\": 0, " + "\n" +
            " *         \"forcedSizeHeight\": 0, " + "\n" +
            " *         \"showCredits\": true " + "\n" +
            " *     }, " + "\n" +
            " *     { " + "\n" +
            " *         \"frameBlock\": \"minecraft:diamond_ore\", " + "\n" +
            " *         \"igniter\": \"minecraft:ender_eye\", " + "\n" +
            " *         \"fromDim\": \"minecraft:the_nether\", " + "\n" +
            " *         \"toDim\": \"minecraft:the_end\", " + "\n" +
            " *         \"r\": 0, " + "\n" +
            " *         \"g\": 0, " + "\n" +
            " *         \"b\": 255, " + "\n" +
            " *         \"onlyIgniteInDims\": true, " + "\n" +
            " *         \"isFlatPortal\": false, " + "\n" +
            " *         \"forceSize\": true, " + "\n" +
            " *         \"forcedSizeWidth\": 4, " + "\n" +
            " *         \"forcedSizeHeight\": 5, " + "\n" +
            " *         \"showCredits\": false " + "\n" +
            " *     } " + "\n" +
            " * ] " + "\n" +
            " */\n";

    /**
     *
     * */
    public static List<CustomPortal> CUSTOM_PORTAL_LIST;

    public static void init(File jsonConfig) {
        try {
            // Create the config if it doesn't already exist.
            if (!jsonConfig.exists() && jsonConfig.createNewFile()) {
                // Get a default map of blocks. You could just use a blank map, however.
                // Convert the map to JSON format. There is a built in (de)serializer for it already.
                String json = DEFAULT_INSTRUCTIONS;
                json += GSON.toJson(DEFAULT_LIST, CUSTOM_PORTAL_LIST_TYPE);
                FileWriter writer = new FileWriter(jsonConfig);
                // Write to the file you passed
                writer.write(json);
                // Always close when done.
                writer.close();
            }

            // If the file exists (or we just made one exist), convert it from JSON format to a populated Map object
            CUSTOM_PORTAL_LIST = GSON.fromJson(new FileReader(jsonConfig), CUSTOM_PORTAL_LIST_TYPE);
            cleanupPortalList();
        } catch (IOException e) {
            // Print an error if something fails
            PortalBuilderCreate.LOGGER.error("Could not create the default configuration for modid: " + PortalBuilderCreate.MOD_ID);
        }
    }

    private static void cleanupPortalList() {
        for (CustomPortal customPortal : CUSTOM_PORTAL_LIST) {
            customPortal.cleanupFields();
        }
    }
}
