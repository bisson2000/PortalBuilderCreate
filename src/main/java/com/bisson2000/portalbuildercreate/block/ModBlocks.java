package com.bisson2000.portalbuildercreate.block;

import com.bisson2000.portalbuildercreate.PortalBuilderCreate;
import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.kyrptonaught.customportalapi.CustomPortalBlock;
import net.kyrptonaught.customportalapi.CustomPortalsMod;
import net.kyrptonaught.customportalapi.interfaces.EntityInCustomPortal;
import net.kyrptonaught.customportalapi.mixin.client.ChunkRendererRegionAccessor;
import net.kyrptonaught.customportalapi.util.CustomPortalHelper;
import net.kyrptonaught.customportalapi.util.PortalLink;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModBlocks {
    private static int id = 0;

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, PortalBuilderCreate.MOD_ID);

    private static String getId() {
        String res = "portalblock" + id;
        id++;
        return res;
    }

    public static final List<RegistryObject<CustomPortalBlock>> DUMMY_PORTAL_BLOCKS = new ArrayList<>();

    private static RegistryObject<CustomPortalBlock> registerDummyBlock() {
        return registerBlock(getId(), () -> new CustomPortalBlock(BlockBehaviour.Properties.copy(Blocks.NETHER_PORTAL)
                .strength(-1.0F)
                .noCollission()
                .sound(SoundType.GLASS)
                .lightLevel(state -> 11)
        ) {
            // Need to override default behavior because it does not work.
            // This is also a bug
            @Override
            public void entityInside(@NotNull BlockState state, @NotNull Level world, BlockPos pos, @NotNull Entity entity) {
                super.entityInside(state, world, pos, entity);
                if (world.isClientSide()) {
                    EntityInsideClientHandler.handle(entity);
                }
            }
        });
    }

    @OnlyIn(Dist.CLIENT) // This ensures the class is only loaded on the client
    private static class EntityInsideClientHandler {
        public static void handle(@NotNull Entity entity) {
            //EntityInCustomPortal entityInPortal = (EntityInCustomPortal)entity;
            if (entity instanceof LocalPlayer localPlayer && localPlayer.canChangeDimensions()) {
                if (localPlayer.isOnPortalCooldown()) {
                    localPlayer.setPortalCooldown();
                } else {
                    localPlayer.isInsidePortal = true;//!entityInPortal.didTeleport();
                }
            }
        }
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    public static void init(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    static {
        for (int i = 0; i < PortalBuilderCreate.NUMBER_OF_PORTALS_ALLOWED; ++i) {
            DUMMY_PORTAL_BLOCKS.add(registerDummyBlock());
        }
    }

    /**
     * Required to get the right color
     * */
    @Mod.EventBusSubscriber(modid = PortalBuilderCreate.MOD_ID, value = {Dist.CLIENT}, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class CustomPortalsModClient {

        // colored portals
        @SubscribeEvent
        public static void onBlockColors(RegisterColorHandlersEvent.Block event) {
            //
            Block[] blockArray = DUMMY_PORTAL_BLOCKS.stream().map(b -> (Block)b.get()).toArray(Block[]::new);
            event.getBlockColors().register((state, world, pos, tintIndex) -> {
                if (pos != null && world instanceof RenderChunkRegion) {
                    Block block = CustomPortalHelper.getPortalBase(((ChunkRendererRegionAccessor)world).getLevel(), pos);
                    PortalLink link = CustomPortalApiRegistry.getPortalLinkFromBase(block);
                    if (link != null) {
                        return link.colorID;
                    }
                }

                return 1908001;
            }, blockArray);
        }

        // translucent portals
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                DUMMY_PORTAL_BLOCKS.forEach(b -> {
                    ItemBlockRenderTypes.setRenderLayer(b.get(), RenderType.translucent());
                });
            });
        }
    }
}
