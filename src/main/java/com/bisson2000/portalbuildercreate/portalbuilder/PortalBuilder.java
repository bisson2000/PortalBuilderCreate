package com.bisson2000.portalbuildercreate.portalbuilder;

import com.bisson2000.portalbuildercreate.PortalBuilderCreate;
import com.bisson2000.portalbuildercreate.block.ModBlocks;
import com.bisson2000.portalbuildercreate.config.PortalBuilderCreateConfig;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.kyrptonaught.customportalapi.util.ColorUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.registries.ForgeRegistries;

import java.awt.*;

public abstract class PortalBuilder {

    /**
     * Known bug: we need to be registering a custom portal block for each type of portal
     * */
    public static void buildPortals() {

        for (int i = 0; i < PortalBuilderCreateConfig.CUSTOM_PORTAL_LIST.size(); ++i) {
            buildIndividualPortal(i);
        }
    }

    private static void buildIndividualPortal(int customPortalIndex) {
        CustomPortal customPortal = PortalBuilderCreateConfig.CUSTOM_PORTAL_LIST.get(customPortalIndex);

        if (!customPortal.activationFlag) {
            PortalBuilderCreate.LOGGER.warn("A portal was left inactive! Was this intentional? Portal from {} to {}", customPortal.fromDim, customPortal.toDim);
            return;
        }

        // Init builder
        CustomPortalBuilder customPortalBuilder = CustomPortalBuilder.beginPortal();

        // bug bypass
        customPortalBuilder.customPortalBlock(ModBlocks.DUMMY_PORTAL_BLOCKS.get(customPortalIndex));

        // frame block
        customPortalBuilder.frameBlock(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(customPortal.frameBlock)));

        // igniter
        ResourceLocation igniter = new ResourceLocation(customPortal.igniter);
        if (ForgeRegistries.ITEMS.containsKey(igniter)) {
            customPortalBuilder.lightWithItem(ForgeRegistries.ITEMS.getValue(igniter));
        } else if (ForgeRegistries.FLUIDS.containsKey(igniter)) {
            customPortalBuilder.lightWithFluid(ForgeRegistries.FLUIDS.getValue(igniter));
        } else {
            PortalBuilderCreate.LOGGER.warn("Couldn't find the igniter for portal from {} to {}", customPortal.fromDim, customPortal.toDim);
        }

        // FromDim
        customPortalBuilder.returnDim(new ResourceLocation(customPortal.fromDim), false);

        // ToDim
        customPortalBuilder.destDimID(new ResourceLocation(customPortal.toDim));

        // rgb (tint)
        customPortalBuilder.tintColor(customPortal.r, customPortal.g, customPortal.b);

        // onlyIgnitableInReturnDim
        if (customPortal.onlyIgnitInDims) {
            customPortalBuilder.onlyLightInOverworld();
        }

        // isFlatPortal
        if (customPortal.isFlatPortal) {
            customPortalBuilder.flatPortal();
        }

        if (customPortal.forceSize) {
            customPortalBuilder.forcedSize(customPortal.forcedSizeWidth, customPortal.forcedSizeHeight);
        }

        // Register builder
        customPortalBuilder.registerPortal();
    }


}
