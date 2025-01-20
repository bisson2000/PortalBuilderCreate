package com.bisson2000.portalbuildercreate.registerportals;

import com.bisson2000.portalbuildercreate.PortalBuilderCreate;
import com.simibubi.create.content.contraptions.glue.SuperGlueEntity;
import com.simibubi.create.foundation.utility.BlockFace;
import com.simibubi.create.foundation.utility.Pair;
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
        ServerLevel level = (ServerLevel)inbound.getFirst();
        ResourceKey<Level> resourcekey = level.dimension() == secondDimension ? firstDimension : secondDimension;
        MinecraftServer minecraftserver = level.getServer();
        ServerLevel otherLevel = minecraftserver.getLevel(resourcekey);
        if (otherLevel != null && minecraftserver.isNetherEnabled()) {
            BlockFace inboundTrack = (BlockFace)inbound.getSecond();
            BlockPos portalPos = inboundTrack.getConnectedPos();
            BlockState portalState = level.getBlockState(portalPos);
            ITeleporter teleporter = (ITeleporter)customPortalForcer.apply(otherLevel);
            SuperGlueEntity probe = new SuperGlueEntity(level, new AABB(portalPos));
            probe.setYRot(inboundTrack.getFace().toYRot());
            probe.setPortalEntrancePos();
            Objects.requireNonNull(probe);
            PortalInfo portalinfo = teleporter.getPortalInfo(probe, otherLevel, probe::findDimensionEntryPoint);
            if (portalinfo == null) {
                return null;
            } else {
                BlockPos otherPortalPos = BlockPos.containing(portalinfo.pos);
                BlockState otherPortalState = otherLevel.getBlockState(otherPortalPos);
                if (otherPortalState.getBlock() != portalState.getBlock()) {
                    return null;
                } else {
                    Direction targetDirection = inboundTrack.getFace();
                    if (targetDirection.getAxis() == otherPortalState.getValue(BlockStateProperties.AXIS)) { // Only BlockStateProperties.AXIS changed
                        targetDirection = targetDirection.getClockWise();
                    }

                    BlockPos otherPos = otherPortalPos.relative(targetDirection);
                    return Pair.of(otherLevel, new BlockFace(otherPos, targetDirection.getOpposite()));
                }
            }
        } else {
            return null;
        }

    }
}
