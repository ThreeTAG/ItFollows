package net.threetag.itfollows.datagen.fabric;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class ItFollowsDataGen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        pack.addProvider(IFModelProvider::new);
        pack.addProvider(IFLangProvider.English::new);
        pack.addProvider(IFLangProvider.German::new);
        pack.addProvider(IFLangProvider.Saxon::new);
        pack.addProvider(IFSoundProvider::new);
        pack.addProvider(IFJukeboxSongsProvider::new);
        pack.addProvider(IFAdvancementProvider::new);
        pack.addProvider(IFLootTableProvider::new);
    }
}
