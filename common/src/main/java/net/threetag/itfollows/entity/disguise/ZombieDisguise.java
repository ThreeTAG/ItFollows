package net.threetag.itfollows.entity.disguise;

import net.minecraft.world.level.LightLayer;
import net.threetag.itfollows.entity.TheEntity;

public class ZombieDisguise extends DisguiseType {

    @Override
    public boolean isValid(TheEntity entity) {
        int i = entity.level().getBrightness(LightLayer.SKY, entity.blockPosition()) - entity.level().getSkyDarken();
        return i <= 5;
    }

    @Override
    public int getPriority() {
        return 30;
    }
}
