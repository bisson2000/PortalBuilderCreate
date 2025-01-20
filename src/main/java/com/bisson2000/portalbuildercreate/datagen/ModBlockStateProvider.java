package com.bisson2000.portalbuildercreate.datagen;

import com.bisson2000.portalbuildercreate.PortalBuilderCreate;
import com.bisson2000.portalbuildercreate.block.ModBlocks;
import net.kyrptonaught.customportalapi.CustomPortalBlock;
import net.kyrptonaught.customportalapi.CustomPortalsMod;
import net.minecraft.core.Direction;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.concurrent.CompletableFuture;

public class ModBlockStateProvider extends BlockStateProvider {

    BlockModelProvider blockModelProvider;

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, PortalBuilderCreate.MOD_ID, exFileHelper);

        blockModelProvider =  new BlockModelProvider(output, CustomPortalsMod.MOD_ID, exFileHelper) {
            @Override public CompletableFuture<?> run(CachedOutput cache) { return CompletableFuture.allOf(); }

            @Override protected void registerModels() {}
        };
    }

    @Override
    protected void registerStatesAndModels() {
        for (RegistryObject<CustomPortalBlock> block : ModBlocks.DUMMY_PORTAL_BLOCKS) {
            blockWithoutItem(block);
        }
    }

    private void blockWithoutItem(RegistryObject<CustomPortalBlock> blockRegistryObject) {
        var axisX = new ModelFile.UncheckedModelFile(new ResourceLocation(CustomPortalsMod.MOD_ID, "block/customportalblock_ns"));
        var axisZ = new ModelFile.UncheckedModelFile(new ResourceLocation(CustomPortalsMod.MOD_ID, "block/customportalblock_ew"));
        var axisY = new ModelFile.UncheckedModelFile(new ResourceLocation(CustomPortalsMod.MOD_ID, "block/customportalblock_flat"));
        getVariantBuilder(blockRegistryObject.get())
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.X)
                .modelForState().modelFile(axisX).addModel()
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Z)
                .modelForState().modelFile(axisZ).addModel()
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y)
                .modelForState().modelFile(axisY).addModel();
    }
}
