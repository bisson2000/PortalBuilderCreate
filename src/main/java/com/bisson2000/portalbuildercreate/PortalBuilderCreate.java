package com.bisson2000.portalbuildercreate;

import com.bisson2000.portalbuildercreate.block.ModBlocks;
import com.bisson2000.portalbuildercreate.portalbuilder.PortalBuilder;
import com.bisson2000.portalbuildercreate.portalregister.PortalRegisterHelper;
import com.mojang.logging.LogUtils;
import com.simibubi.create.content.contraptions.glue.SuperGlueEntity;
import com.simibubi.create.content.trains.track.AllPortalTracks;
import com.simibubi.create.foundation.utility.BlockFace;
import com.simibubi.create.foundation.utility.Pair;
import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.kyrptonaught.customportalapi.util.CustomPortalHelper;
import net.kyrptonaught.customportalapi.util.CustomTeleporter;
import net.kyrptonaught.customportalapi.util.PortalLink;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.NewRegistryEvent;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Function;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PortalBuilderCreate.MOD_ID)
public class PortalBuilderCreate
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "portalbuildercreate";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    /**
     * There is a bug with customportalAPI where the same block is used with the portal frame.
     * This causes issues with create when a new portal is created.
     * THe workaround is to use a different portal block for every portal.
     * */
    public static int NUMBER_OF_PORTALS_ALLOWED = 100;

    public PortalBuilderCreate(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        ModBlocks.init(modEventBus);
        //modEventBus.addListener(this::createRegistry);

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
    }

    public void commonSetup(final FMLCommonSetupEvent event) {
        PortalBuilder.buildPortals();

        event.enqueueWork(() -> {
            PortalRegisterHelper.RegisterAllPortals();
        });
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
        }
    }
}
