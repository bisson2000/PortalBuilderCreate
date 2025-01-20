package com.bisson2000.portalbuildercreate.portalbuilder;

import com.bisson2000.portalbuildercreate.block.ModBlocks;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class PortalBuilder {

    /**
     * Known bug: we need to be registering a custom portal block for each type of portal
     * Shoutout to cyb0124 for finding this out!
     *
     * */
    public static void buildPortals() {
        CustomPortalBuilder customPortalBuilder = CustomPortalBuilder.beginPortal();
        customPortalBuilder.frameBlock(Blocks.DIAMOND_BLOCK);

        // Set the frame block to Diamond Blocks
        // You can use a registered Block or a Resource Location here


        // Set the item used to ignite the portal
        // Options include:
        // - Using an Ender Eye (as shown)
        // - Using water with lightWithWater()
        // - Using a custom fluid with lightWithFluid(MyFluids.CUSTOMFLUID)
        customPortalBuilder.lightWithItem(Items.ENDER_EYE)

                // (Optional) Set a forced size for the portal
                // Parameters are width and height
                //.forcedSize(4, 5)

                // (Optional) Set a custom block for the portal itself
                // Replace MyBlocks.CUSTOMPORTALBLOCK with your custom portal block
                .customPortalBlock(ModBlocks.DUMMY_PORTAL_BLOCKS.get(0)) // bug bypass

                // (Optional) Configure the portal's return dimension
                // `onlyIgnitInReturnDim` specifies if the portal can only be ignited in the return dimension
                // 1.21+ requires ResourceLocation.parse or similar methods instead of new ResourceLocation
                .returnDim(new ResourceLocation("overworld"), false)

                // (Optional) Restrict portal ignition to the Overworld
                // Use this if you want the portal to be ignitable only in the Overworld
                //.onlyLightInOverworld()

                // (Optional) Use the flat portal style, similar to the End Portal
                // Apply this style to make the portal's appearance flat
                //.flatPortal()

                // Set the dimension to travel to when the portal is used
                // In this case, it sets the destination dimension to The End
                // 1.21+ requires ResourceLocation.parse or similar methods instead of new ResourceLocation
                .destDimID(new ResourceLocation("the_nether"))

                // (Optional) Set the RGB color for the portal's tint
                // Customize the portal's appearance with a specific color
                .tintColor(45, 65, 101)

                // (Optional) Custom frame tester
                // 1.21+ requires ResourceLocation.parse or similar methods instead of new ResourceLocation
                //.customFrameTester(new ResourceLocation("mymod", "custom_frame_tester"))

                // (Optional) Custom ignition source by ResourceLocation
                // 1.21+ requires ResourceLocation.parse or similar methods instead of new ResourceLocation
                //.customIgnitionSource(new ResourceLocation("mymod", "custom_ignition_source"))

                // (Optional) Add event handling for pre/post teleportation
                //.registerBeforeTPEvent(entity -> {
                //    // Example logic: cancel for non-player entities
                //    return entity instanceof Player ? SHOULDTP.TP : SHOULDTP.CANCEL_TP;
                //})
                .registerPostTPEvent(entity -> {
                    // Example logic for after teleportation
                    System.out.println("Entity teleported: " + entity.getName().getString());
                });

        // (Optional) Add custom sounds
        //.registerInPortalAmbienceSound(player -> new CPASoundEventData(/* Your sound data */))
        //.registerPostTPPortalAmbience(player -> new CPASoundEventData(/* Your sound data */))

        // Register the custom portal with all the specified configurations
        customPortalBuilder.registerPortal();

        // 2
        CustomPortalBuilder.beginPortal()
                .frameBlock(Blocks.DIAMOND_ORE)
                .lightWithItem(Items.ENDER_EYE)
                .customPortalBlock(ModBlocks.DUMMY_PORTAL_BLOCKS.get(1)) // bug bypass
                .returnDim(new ResourceLocation("the_nether"), false)
                .destDimID(new ResourceLocation("the_end"))
                .registerPostTPEvent(entity -> {
                    // Example logic for after teleportation
                    System.out.println("Entity teleported: " + entity.getName().getString());
                })
                .registerPortal();

    }


}
