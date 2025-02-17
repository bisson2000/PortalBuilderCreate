package com.bisson2000.portalbuildercreate.networking;

import com.bisson2000.portalbuildercreate.PortalBuilderCreate;
import com.bisson2000.portalbuildercreate.networking.packet.ShowCreditsS2C;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {

    private static final String PROTOCOL_VERSION = "1.0";
    private static SimpleChannel INSTANCE;

    private static int packetId = 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(PortalBuilderCreate.MOD_ID, "messages"),
                () -> PROTOCOL_VERSION,
                PROTOCOL_VERSION::equals,
                PROTOCOL_VERSION::equals
        );

        INSTANCE.messageBuilder(ShowCreditsS2C.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(ShowCreditsS2C::new)
                .encoder(ShowCreditsS2C::encode)
                .consumerMainThread(ShowCreditsS2C::handle)
                .add();
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToClient(MSG message, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), message);
    }

}
