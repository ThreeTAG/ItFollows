package net.threetag.itfollows.neoforge;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.brewing.RegisterBrewingRecipesEvent;
import net.threetag.itfollows.ItFollows;
import net.threetag.itfollows.attachment.neoforge.IFAttachmentsImpl;
import net.threetag.itfollows.client.ItFollowsClient;
import net.threetag.itfollows.item.IFItems;

@Mod(ItFollows.MOD_ID)
@EventBusSubscriber(modid = ItFollows.MOD_ID)
public final class ItFollowsNeoForge {

    public ItFollowsNeoForge(IEventBus modBus) {
        ItFollows.init();
        IFAttachmentsImpl.ATTACHMENT_TYPES.register(modBus);

        if (Platform.getEnvironment() == Env.CLIENT) {
            ItFollowsClient.init();
        }
    }

    @SubscribeEvent
    public static void registerBrewingRecipes(RegisterBrewingRecipesEvent e) {
        e.getBuilder().addRecipe(Ingredient.of(Items.SPLASH_POTION), Ingredient.of(Items.ECHO_SHARD), IFItems.SPLASH_POTION_OF_SPREADING.get().getDefaultInstance());
    }
}
