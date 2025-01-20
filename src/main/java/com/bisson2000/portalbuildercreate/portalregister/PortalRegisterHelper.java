package com.bisson2000.portalbuildercreate.portalregister;

import com.bisson2000.portalbuildercreate.PortalBuilderCreate;
import com.simibubi.create.content.contraptions.glue.SuperGlueEntity;
import com.simibubi.create.content.trains.track.AllPortalTracks;
import com.simibubi.create.foundation.utility.BlockFace;
import com.simibubi.create.foundation.utility.Pair;
import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.kyrptonaught.customportalapi.util.CustomPortalHelper;
import net.kyrptonaught.customportalapi.util.CustomTeleporter;
import net.kyrptonaught.customportalapi.util.PortalLink;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Function;

public class PortalRegisterHelper {

    public static void RegisterAllPortals() {
        CustomPortalApiRegistry.getAllPortalLinks().forEach(portalLink -> {
            AllPortalTracks.registerIntegration(portalLink.getPortalBlock(), inbound -> {
                return createPortalTrackProvider(inbound, portalLink);
            });
        });
    }

    private static Pair<ServerLevel, BlockFace> createPortalTrackProvider(Pair<ServerLevel, BlockFace> inbound, PortalLink portalLink) {
        ResourceKey<Level> fromDimension = ResourceKey.create(Registries.DIMENSION, portalLink.returnDimID);
        ResourceKey<Level> toDimension = ResourceKey.create(Registries.DIMENSION, portalLink.dimID);
        return standardPortalProvider(inbound, fromDimension, toDimension, serverLevel -> {
            return createPortalForcer(inbound, portalLink);
        });
    }

    public static ITeleporter createPortalForcer(Pair<ServerLevel, BlockFace> inbound, PortalLink portalLink) {
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

    /**
     * Copied from AllPortalTracks:standardPortalProvider, and changed 1 line
     * See <a href="https://github.com/Creators-of-Create/Create/blob/d39f89983a6c36dcacda6635c2094c826703ed49/src/main/java/com/simibubi/create/content/trains/track/AllPortalTracks.java#L88">Github</a>
     *
     * */
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
        return Pair.of(otherLevel, new BlockFace(otherPos, targetDirection.getOpposite()));

    }
}
