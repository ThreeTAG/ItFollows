package net.threetag.itfollows.item;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.threetag.itfollows.ItFollows;

public class IFItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ItFollows.MOD_ID, Registries.ITEM);

    public static final RegistrySupplier<Item> MUSIC_DISC_SOLSTICE = ITEMS.register("music_disc_solstice", () -> new Item(new Item.Properties().setId(ResourceKey.create(Registries.ITEM, ItFollows.id("music_disc_solstice"))).stacksTo(1).rarity(Rarity.UNCOMMON).jukeboxPlayable(IFJukeboxSongs.SOLSTICE)));

    public static void addToTabs() {
        CreativeTabRegistry.modify(CreativeTabRegistry.defer(CreativeModeTabs.TOOLS_AND_UTILITIES), (flags, output, canUseGameMasterBlocks) -> {
            output.acceptAfter(Items.MUSIC_DISC_LAVA_CHICKEN, MUSIC_DISC_SOLSTICE.get());
        });
    }

}
