package com.bisson2000.portalbuildercreate.block;

import com.bisson2000.portalbuildercreate.PortalBuilderCreate;
import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.kyrptonaught.customportalapi.CustomPortalBlock;
import net.kyrptonaught.customportalapi.CustomPortalsMod;
import net.kyrptonaught.customportalapi.mixin.client.ChunkRendererRegionAccessor;
import net.kyrptonaught.customportalapi.util.CustomPortalHelper;
import net.kyrptonaught.customportalapi.util.PortalLink;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.chunk.RenderChunkRegion;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ModBlocks {
    private static int id = 0;

    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, PortalBuilderCreate.MOD_ID);

    private static int getId() {
        return id++;
    }

    public static final List<RegistryObject<CustomPortalBlock>> DUMMY_PORTAL_BLOCKS = new ArrayList<>();

    private static RegistryObject<CustomPortalBlock> registerDummyBlock() {
        String name = Integer.toString(getId());
        return registerBlock(name, () -> new CustomPortalBlock(BlockBehaviour.Properties.copy(Blocks.NETHER_PORTAL)
                .noCollission()
                .strength(-1.0F)
                .sound(SoundType.GLASS)
                .lightLevel(state -> 11)
        ));
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
