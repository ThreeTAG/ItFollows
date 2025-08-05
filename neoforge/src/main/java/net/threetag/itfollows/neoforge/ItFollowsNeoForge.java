package net.threetag.itfollows.neoforge;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

import net.threetag.itfollows.ItFollows;
import net.threetag.itfollows.attachment.neoforge.IFAttachmentsImpl;
import net.threetag.itfollows.client.ItFollowsClient;

@Mod(ItFollows.MOD_ID)
public final class ItFollowsNeoForge {

    public ItFollowsNeoForge(IEventBus modBus) {
        ItFollows.init();
        IFAttachmentsImpl.ATTACHMENT_TYPES.register(modBus);

        if (Platform.getEnvironment() == Env.CLIENT) {
            ItFollowsClient.init();
        }
    }
}
