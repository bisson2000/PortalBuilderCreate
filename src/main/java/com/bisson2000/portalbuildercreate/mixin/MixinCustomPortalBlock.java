package com.bisson2000.portalbuildercreate.mixin;

import net.kyrptonaught.customportalapi.CustomPortalBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CustomPortalBlock.class)
public class MixinCustomPortalBlock {

    @Inject(at = @At(value = "HEAD"), method = "createBlockStateDefinition", cancellable = false)
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder, CallbackInfo info) {
        System.out.println("--------------------Inject!");
    }
}
