package net.threetag.itfollows;

import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.server.level.ServerPlayer;
import net.threetag.itfollows.entity.CursePlayerHandler;

public class IFEventHandler implements PlayerEvent.PlayerClone {

    public static void init() {
        var instance = new IFEventHandler();
        PlayerEvent.PLAYER_CLONE.register(instance);
    }

    @Override
    public void clone(ServerPlayer oldPlayer, ServerPlayer newPlayer, boolean wonGame) {
        var old = CursePlayerHandler.get(oldPlayer);
        var newC = CursePlayerHandler.get(newPlayer);
        newC.copyFrom(old);
    }
}
