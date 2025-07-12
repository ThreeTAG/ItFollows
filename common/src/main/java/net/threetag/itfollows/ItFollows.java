package net.threetag.itfollows;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import net.minecraft.resources.ResourceLocation;
import net.threetag.itfollows.command.ItFollowsCommand;
import net.threetag.itfollows.entity.IFEntityTypes;
import net.threetag.itfollows.entity.disguise.DisguiseTypes;

public final class ItFollows {

    public static final String MOD_ID = "it_follows";

    public static void init() {
        IFEntityTypes.ENTITY_TYPES.register();
        DisguiseTypes.DISGUISE_TYPES.register();

        IFEntityTypes.init();

        CommandRegistrationEvent.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> {
            ItFollowsCommand.register(commandDispatcher);
        });
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
