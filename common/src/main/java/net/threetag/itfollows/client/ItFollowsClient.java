package net.threetag.itfollows.client;

import net.threetag.itfollows.entity.IFEntityTypes;
import net.threetag.itfollows.entity.disguise.DisguiseTypes;

public class ItFollowsClient {

    public static void init() {
        IFEntityTypes.initRenderers();
        DisguiseTypes.initRenderers();
    }

}
