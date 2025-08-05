package net.threetag.itfollows.fabric;

import net.fabricmc.api.ModInitializer;
import net.threetag.itfollows.ItFollows;

public final class ItFollowsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ItFollows.init();
    }
}
