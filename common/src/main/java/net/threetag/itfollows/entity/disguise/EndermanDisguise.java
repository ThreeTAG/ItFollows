package net.threetag.itfollows.entity.disguise;

import net.minecraft.world.level.Level;
import net.threetag.itfollows.entity.TheEntity;

public class EndermanDisguise extends DisguiseType {

    @Override
    public boolean isValid(TheEntity entity) {
        return entity.level().dimension().equals(Level.END);
    }

    @Override
    public int getPriority() {
        return 50;
    }
}
