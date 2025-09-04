package net.threetag.itfollows;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.resources.ResourceLocation;
import net.threetag.itfollows.command.ItFollowsCommand;
import net.threetag.itfollows.entity.IFEntityTypes;
import net.threetag.itfollows.entity.IFTicketTypes;
import net.threetag.itfollows.entity.disguise.DisguiseTypes;

public final class ItFollows {

    public static final String MOD_ID = "it_follows";

    public static void init() {
        MidnightConfig.init(MOD_ID, IFConfig.class);

        IFEntityTypes.ENTITY_TYPES.register();
        DisguiseTypes.DISGUISE_TYPES.register();
        IFTicketTypes.TICKET_TYPES.register();

        IFEntityTypes.init();
        IFEventHandler.init();

        CommandRegistrationEvent.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> {
            ItFollowsCommand.register(commandDispatcher);
        });
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
