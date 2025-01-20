package com.bisson2000.portalbuildercreate.block;

import com.bisson2000.portalbuildercreate.PortalBuilderCreate;
import net.kyrptonaught.customportalapi.CustomPortalBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
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
}
