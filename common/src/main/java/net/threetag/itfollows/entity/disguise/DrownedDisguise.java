package net.threetag.itfollows.entity.disguise;

import net.minecraft.world.level.LightLayer;
import net.threetag.itfollows.entity.TheEntity;

public class DrownedDisguise extends DisguiseType {

    @Override
    public boolean isValid(TheEntity entity) {
        int i = entity.level().getBrightness(LightLayer.SKY, entity.blockPosition()) - entity.level().getSkyDarken();
        return i <= 5 || (entity.level().isDarkOutside() && entity.isInWater());
    }

    @Override
    public int getPriority() {
        return 35;
    }

}
