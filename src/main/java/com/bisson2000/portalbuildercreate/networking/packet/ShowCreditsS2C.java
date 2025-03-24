package com.bisson2000.portalbuildercreate.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ShowCreditsS2C {

    public ShowCreditsS2C() {} // Empty constructor

    public ShowCreditsS2C(FriendlyByteBuf buf) {
        // No data needed
    }

    public void encode(FriendlyByteBuf buf) {
        // No data needed
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            // Client-side: Open the End Credits screen
            ShowCreditsClientHandler.handle();
        });
        ctx.get().setPacketHandled(true);
    }

    @OnlyIn(Dist.CLIENT) // This ensures the class is only loaded on the client
    private static class ShowCreditsClientHandler {
        public static void handle() {
            Minecraft.getInstance().setScreen(new WinScreen(false, () -> {
                Minecraft.getInstance().setScreen(null);
            }));
        }
    }

}
