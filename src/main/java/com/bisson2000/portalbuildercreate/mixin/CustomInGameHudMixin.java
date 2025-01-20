package com.bisson2000.portalbuildercreate.mixin;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import net.kyrptonaught.customportalapi.mixin.client.InGameHudMixin;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;

@OnlyIn(Dist.CLIENT)
@Mixin({Gui.class})
public class CustomInGameHudMixin {
// Lnet/minecraftforge/client/gui/overlay/VanillaGuiOverlay;PORTAL:Lnet/minecraftforge/client/gui/overlay/VanillaGuiOverlay;
//    @Final
//    @Shadow
//    private MinecraftClient client;
//
//    @Inject(method = "renderPortalOverlay", at = @At("HEAD"), cancellable = true)
//    private void renderPortalOverlay(DrawContext drawContext, float f, CallbackInfo ci) {
//        int color = ((EntityMixinAccess)this.client.player).getPortalColor();
//        if(color != 0 && !((EntityMixinAccess)this.client.player).isInNetherPortal()) {
//            Block spriteModel = PortalHelper.getPortalBlockFromColorId(color);
//            if (f < 1.0F) {
//                f *= f;
//                f *= f;
//                f = f * 0.8F + 0.2F;
//            }
//
//            int i = ColorHelper.getWhite(f);
//            Sprite sprite = this.client.getBlockRenderManager().getModels().getModelParticleSprite(spriteModel.getDefaultState().with(PortalBlock.LIT, true));
//            drawContext.drawSpriteStretched(RenderLayer::getGuiTexturedOverlay, sprite, 0, 0, drawContext.getScaledWindowWidth(), drawContext.getScaledWindowHeight(), i);
//            ci.cancel();
//        }
//    }
}
