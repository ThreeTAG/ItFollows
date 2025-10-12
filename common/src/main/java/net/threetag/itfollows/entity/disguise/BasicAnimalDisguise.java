package net.threetag.itfollows.entity.disguise;

import net.minecraft.world.level.Level;
import net.threetag.itfollows.entity.TheEntity;

public class BasicAnimalDisguise extends DisguiseType {

    @Override
    public boolean isValid(TheEntity entity) {
        return entity.level().dimension().equals(Level.OVERWORLD);
    }

    @Override
    public int getWeight() {
        return 1;
    }
}
