package net.threetag.itfollows.entity.disguise;

import net.threetag.itfollows.entity.TheEntity;

public class BasicAnimalDisguise extends DisguiseType {

    @Override
    public boolean isValid(TheEntity entity) {
        return true;
    }

    @Override
    public int getWeight() {
        return 10;
    }
}
