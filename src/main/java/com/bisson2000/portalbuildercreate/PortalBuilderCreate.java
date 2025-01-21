package com.bisson2000.portalbuildercreate;

import com.bisson2000.portalbuildercreate.block.ModBlocks;
import com.bisson2000.portalbuildercreate.config.PortalBuilderCreateConfig;
import com.bisson2000.portalbuildercreate.portalbuilder.PortalBuilder;
import com.bisson2000.portalbuildercreate.portalregister.PortalRegisterHelper;
import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.NewRegistryEvent;
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.io.File;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Function;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PortalBuilderCreate.MOD_ID)
public class PortalBuilderCreate
{
    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "portalbuildercreate";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    private static final String COMMON_CONFIG_NAME = MOD_ID + "-common.json";

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

        //context.registerConfig(ModConfig.Type.COMMON, PortalBuilderCreateConfig.SPEC);

        Path configPath = FMLPaths.CONFIGDIR.get().resolve(COMMON_CONFIG_NAME);
        PortalBuilderCreateConfig.init(configPath.toFile());
        //modEventBus.addListener(this::createRegistry);
        //PortalBuilderCreateConfig.init(COMMON_CONFIG_NAME);

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
