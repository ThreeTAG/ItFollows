package net.threetag.itfollows.neoforge;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.neoforged.fml.common.Mod;

import net.threetag.itfollows.ItFollows;
import net.threetag.itfollows.client.ItFollowsClient;

@Mod(ItFollows.MOD_ID)
public final class ItFollowsNeoForge {

    public ItFollowsNeoForge() {
        ItFollows.init();

        if (Platform.getEnvironment() == Env.CLIENT) {
            ItFollowsClient.init();
        }
    }
}
