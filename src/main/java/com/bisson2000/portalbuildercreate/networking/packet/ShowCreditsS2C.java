package com.bisson2000.portalbuildercreate.networking.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.network.FriendlyByteBuf;
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
            Minecraft.getInstance().setScreen(new WinScreen(false, () -> {
                Minecraft.getInstance().setScreen(null);
            }));
        });
        ctx.get().setPacketHandled(true);
    }

}
