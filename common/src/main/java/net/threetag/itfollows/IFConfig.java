package net.threetag.itfollows;

import eu.midnightdust.lib.config.MidnightConfig;

public class IFConfig extends MidnightConfig {

    public static final String CATEGORY_ENTITY = "entity";

    @Entry(category = CATEGORY_ENTITY)
    public static float MOVEMENT_SPEED = 0.23F;

    @Entry(category = CATEGORY_ENTITY)
    public static int BLOCK_BREAK_INTERVAL = 30;

}
