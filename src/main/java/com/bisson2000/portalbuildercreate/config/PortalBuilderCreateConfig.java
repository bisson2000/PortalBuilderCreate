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

    public static List<CustomPortal> CUSTOM_PORTAL_LIST;

    public static void init(File jsonConfig) {
        try {
            // Create the config if it doesn't already exist.
            if (!jsonConfig.exists() && jsonConfig.createNewFile()) {
                // Get a default map of blocks. You could just use a blank map, however.
                List<CustomPortal> defaultList = DEFAULT_LIST;
                // Convert the map to JSON format. There is a built in (de)serializer for it already.
                String json = GSON.toJson(defaultList, CUSTOM_PORTAL_LIST_TYPE);
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
