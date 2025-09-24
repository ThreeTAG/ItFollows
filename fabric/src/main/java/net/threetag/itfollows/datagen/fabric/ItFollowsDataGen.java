package net.threetag.itfollows.datagen.fabric;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ItFollowsDataGen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        pack.addProvider(ItFollowsModelProvider::new);
        pack.addProvider(ItFollowsLangProvider.English::new);
        pack.addProvider(ItFollowsLangProvider.German::new);
        pack.addProvider(ItFollowsLangProvider.Saxon::new);
        pack.addProvider(ItFollowsSoundProvider::new);
        pack.addProvider(ItFollowsJukeboxSongsProvider::new);
    }
}
