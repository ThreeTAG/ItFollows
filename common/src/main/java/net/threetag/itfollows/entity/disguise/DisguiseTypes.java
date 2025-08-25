package net.threetag.itfollows.entity.disguise;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.threetag.itfollows.ItFollows;
import net.threetag.itfollows.client.renderer.disguise.DisguiseRendererRegistry;
import net.threetag.itfollows.client.renderer.disguise.DrownedDisguiseRenderer;
import net.threetag.itfollows.client.renderer.disguise.PigDisguiseRenderer;
import net.threetag.itfollows.client.renderer.disguise.ZombieDisguiseRenderer;

public class DisguiseTypes {

    @SuppressWarnings("unchecked")
    public static final DeferredRegister<DisguiseType> DISGUISE_TYPES = DeferredRegister.create(ItFollows.MOD_ID, (ResourceKey<Registry<DisguiseType>>) DisguiseType.REGISTRY.key());

    public static final RegistrySupplier<PigDisguise> PIG = DISGUISE_TYPES.register("pig", PigDisguise::new);
    public static final RegistrySupplier<ZombieDisguise> ZOMBIE = DISGUISE_TYPES.register("zombie", ZombieDisguise::new);
    public static final RegistrySupplier<DrownedDisguise> DROWNED = DISGUISE_TYPES.register("drowned", DrownedDisguise::new);

    @Environment(EnvType.CLIENT)
    public static void initRenderers() {
        DisguiseRendererRegistry.register(PIG, PigDisguiseRenderer::new);
        DisguiseRendererRegistry.register(ZOMBIE, ZombieDisguiseRenderer::new);
        DisguiseRendererRegistry.register(DROWNED, DrownedDisguiseRenderer::new);
    }
}
