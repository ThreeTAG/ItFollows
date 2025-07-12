package net.threetag.itfollows.fabric;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricTrackedDataRegistry;
import net.threetag.itfollows.ItFollows;
import net.threetag.itfollows.entity.disguise.DisguiseType;

public final class ItFollowsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ItFollows.init();
        FabricTrackedDataRegistry.register(ItFollows.id("disguise_type"), DisguiseType.ENTITY_DATA);
    }
}
