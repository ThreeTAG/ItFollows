package net.threetag.itfollows.fabric.client;

import net.fabricmc.api.ClientModInitializer;
import net.threetag.itfollows.client.ItFollowsClient;

public final class ItFollowsFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ItFollowsClient.init();
    }
}
