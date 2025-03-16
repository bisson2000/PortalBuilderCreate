package com.bisson2000.portalbuildercreate.portalregister;

import com.bisson2000.portalbuildercreate.PortalBuilderCreate;
import com.simibubi.create.Create;
import com.simibubi.create.api.contraption.train.PortalTrackProvider;
import com.simibubi.create.content.contraptions.glue.SuperGlueEntity;
import com.simibubi.create.content.trains.track.AllPortalTracks;
import net.createmod.catnip.math.BlockFace;
import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.kyrptonaught.customportalapi.util.CustomPortalHelper;
import net.kyrptonaught.customportalapi.util.CustomTeleporter;
import net.kyrptonaught.customportalapi.util.PortalLink;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.ITeleporter;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.function.Function;

public class PortalRegisterHelper {

    public static void RegisterAllPortals() {
        CustomPortalApiRegistry.getAllPortalLinks().forEach(portalLink -> {
            AllPortalTracks.tryRegisterIntegration(ForgeRegistries.BLOCKS.getKey(portalLink.getPortalBlock()), (inbound, blockface) -> {
                return createPortalTrackProvider(inbound, blockface, portalLink);
            });
        });
    }

    private static PortalTrackProvider.Exit createPortalTrackProvider(ServerLevel inbound, BlockFace blockFace, PortalLink portalLink) {
        ResourceKey<Level> fromDimension = ResourceKey.create(Registries.DIMENSION, portalLink.returnDimID);
        ResourceKey<Level> toDimension = ResourceKey.create(Registries.DIMENSION, portalLink.dimID);
        return standardPortalProvider(inbound, blockFace, fromDimension, toDimension, serverLevel -> {
            return createPortalForcer(inbound, blockFace, portalLink);
        });
    }

    public static ITeleporter createPortalForcer(ServerLevel inbound, BlockFace blockFace, PortalLink portalLink) {
        return new ITeleporter() {
            @Override
            public @Nullable PortalInfo getPortalInfo(Entity entity, ServerLevel destWorld, Function<ServerLevel, PortalInfo> defaultPortalInfo) {

                return CustomTeleporter.customTPTarget(
                        destWorld,
                        entity,
                        blockFace.getConnectedPos(),
                        CustomPortalHelper.getPortalBase(inbound, entity.getOnPos()),
                        portalLink.getFrameTester());
            }

        };
    }

    /**
     * Copied from AllPortalTracks:standardPortalProvider, and changed 1 line: BlockStateProperties.AXIS
     * See <a href="https://github.com/Creators-of-Create/Create/blob/d39f89983a6c36dcacda6635c2094c826703ed49/src/main/java/com/simibubi/create/content/trains/track/AllPortalTracks.java#L88">Github</a>
     *
     * */
    public static PortalTrackProvider.Exit standardPortalProvider(ServerLevel inbound, BlockFace blockFace,
                                                                      ResourceKey<Level> firstDimension, ResourceKey<Level> secondDimension,
                                                                      Function<ServerLevel, ITeleporter> customPortalForcer) {
        ServerLevel level = inbound;
        ResourceKey<Level> resourcekey = level.dimension() == secondDimension ? firstDimension : secondDimension;
        MinecraftServer minecraftserver = level.getServer();
        ServerLevel otherLevel = minecraftserver.getLevel(resourcekey);

        if (otherLevel == null || !minecraftserver.isNetherEnabled())
            return null;

        BlockFace inboundTrack = blockFace;
        BlockPos portalPos = inboundTrack.getConnectedPos();
        BlockState portalState = level.getBlockState(portalPos);
        ITeleporter teleporter = customPortalForcer.apply(otherLevel);

        SuperGlueEntity probe = new SuperGlueEntity(level, new AABB(portalPos));
        probe.setYRot(inboundTrack.getFace()
                .toYRot());
        probe.setPortalEntrancePos();

        PortalInfo portalinfo = teleporter.getPortalInfo(probe, otherLevel, probe::findDimensionEntryPoint);
        if (portalinfo == null)
            return null;

        BlockPos otherPortalPos = BlockPos.containing(portalinfo.pos);
        BlockState otherPortalState = otherLevel.getBlockState(otherPortalPos);
        if (otherPortalState.getBlock() != portalState.getBlock())
            return null;

        Direction targetDirection = inboundTrack.getFace();
        if (targetDirection.getAxis() == otherPortalState.getValue(BlockStateProperties.AXIS))
            targetDirection = targetDirection.getClockWise();
        BlockPos otherPos = otherPortalPos.relative(targetDirection);
        return new PortalTrackProvider.Exit(otherLevel, new BlockFace(otherPos, targetDirection.getOpposite()));

    }
}
