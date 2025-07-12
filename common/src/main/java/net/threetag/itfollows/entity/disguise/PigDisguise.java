package net.threetag.itfollows.entity.disguise;

import net.threetag.itfollows.entity.TheEntity;

public class PigDisguise extends DisguiseType {

    @Override
    public boolean isValid(TheEntity entity) {
        return true;
    }

    @Override
    public int getPriority() {
        return 10;
    }
}
