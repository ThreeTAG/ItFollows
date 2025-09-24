package net.threetag.itfollows;

import dev.architectury.event.events.common.CommandRegistrationEvent;
import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.resources.ResourceLocation;
import net.threetag.itfollows.command.ItFollowsCommand;
import net.threetag.itfollows.entity.IFEntityTypes;
import net.threetag.itfollows.entity.disguise.DisguiseTypes;
import net.threetag.itfollows.item.IFItems;
import net.threetag.itfollows.sound.IFSoundEvents;

public final class ItFollows {

    public static final String MOD_ID = "it_follows";

    public static void init() {
        MidnightConfig.init(MOD_ID, IFConfig.class);

        IFItems.ITEMS.register();
        IFSoundEvents.SOUND_EVENTS.register();
        IFEntityTypes.ENTITY_TYPES.register();
        DisguiseTypes.DISGUISE_TYPES.register();

        IFEntityTypes.init();
        IFEventHandler.init();
        IFItems.addToTabs();

        CommandRegistrationEvent.EVENT.register((commandDispatcher, commandBuildContext, commandSelection) -> {
            ItFollowsCommand.register(commandDispatcher);
        });
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
