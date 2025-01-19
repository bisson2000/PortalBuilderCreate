package com.bisson2000.portalbuildercreate;

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
import org.slf4j.Logger;

import javax.annotation.Nullable;
import java.util.function.Function;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(PortalBuilderCreate.MODID)
public class PortalBuilderCreate
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "portalbuildercreate";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public PortalBuilderCreate(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
    }

    public void commonSetup(final FMLCommonSetupEvent event) {
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
                //.customPortalBlock(ForgeRegistries.BLOCKS.)

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
                //.tintColor(45, 65, 101)

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

        event.enqueueWork(() -> {
            CustomPortalApiRegistry.getAllPortalLinks().forEach(pl -> AllPortalTracks.registerIntegration(pl.getPortalBlock(), p -> createPortalTrackProvider(p, pl)));
        });
    }

    private static Pair<ServerLevel, BlockFace> createPortalTrackProvider(Pair<ServerLevel, BlockFace> inbound, PortalLink portalLink) {
        ResourceKey<Level> trainDepot = ResourceKey.create(Registries.DIMENSION, portalLink.dimID);
        return PortalBuilderCreate.standardPortalProvider(inbound, Level.OVERWORLD, trainDepot, (sl) -> PortalBuilderCreate.wrapCustomTeleporter(inbound, portalLink));
    }

    public static ITeleporter wrapCustomTeleporter(Pair<ServerLevel, BlockFace> inbound, PortalLink portalLink) {
        return new ITeleporter() {

            @Override
            public @Nullable PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {

                return CustomTeleporter.customTPTarget(
                        destWorld,
                        entity,
                        inbound.getSecond().getConnectedPos(),
                        CustomPortalHelper.getPortalBase(inbound.getFirst(), entity.getOnPos()),
                        portalLink.getFrameTester());
            }

        };
    }

    public static Pair<ServerLevel, BlockFace> standardPortalProvider(Pair<ServerLevel, BlockFace> inbound,
                                                                      ResourceKey<Level> firstDimension, ResourceKey<Level> secondDimension,
                                                                      Function<ServerLevel, ITeleporter> customPortalForcer) {
        ServerLevel level = inbound.getFirst();
        ResourceKey<Level> resourcekey = level.dimension() == secondDimension ? firstDimension : secondDimension;
        MinecraftServer minecraftserver = level.getServer();
        ServerLevel otherLevel = minecraftserver.getLevel(resourcekey);

        if (otherLevel == null || !minecraftserver.isNetherEnabled())
            return null;

        BlockFace inboundTrack = inbound.getSecond();
        BlockPos portalPos = inboundTrack.getConnectedPos();
        BlockState portalState = level.getBlockState(portalPos);
        ITeleporter teleporter = customPortalForcer.apply(otherLevel);

        SuperGlueEntity probe = new SuperGlueEntity(level, new AABB(portalPos));
        probe.setYRot(inboundTrack.getFace().toYRot());
        probe.setPortalEntrancePos();

        PortalInfo portalinfo = teleporter.getPortalInfo(probe, otherLevel, probe::findDimensionEntryPoint);
        if (portalinfo == null)
            return null;

        BlockPos otherPortalPos = BlockPos.containing(portalinfo.pos);
        BlockState otherPortalState = otherLevel.getBlockState(otherPortalPos);
        if (otherPortalState.getBlock() != portalState.getBlock())
            return null;

        Direction.Axis axis = otherPortalState.getValue(BlockStateProperties.AXIS);
        if (axis == Direction.Axis.X) {
            otherPortalState.setValue(BlockStateProperties.HORIZONTAL_AXIS, Direction.Axis.X);
            return AllPortalTracks.standardPortalProvider(inbound, firstDimension, secondDimension, customPortalForcer);
        } else if (axis == Direction.Axis.Z) {
            otherPortalState.setValue(BlockStateProperties.HORIZONTAL_AXIS, Direction.Axis.Z);
            return AllPortalTracks.standardPortalProvider(inbound, firstDimension, secondDimension, customPortalForcer);
        }

        return null;

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        // Do something when the server starts
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
        }
    }
}
