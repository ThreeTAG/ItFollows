package net.threetag.itfollows.entity.disguise;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.threetag.itfollows.ItFollows;
import net.threetag.itfollows.client.renderer.disguise.*;

public class DisguiseTypes {

    @SuppressWarnings("unchecked")
    public static final DeferredRegister<DisguiseType> DISGUISE_TYPES = DeferredRegister.create(ItFollows.MOD_ID, (ResourceKey<Registry<DisguiseType>>) DisguiseType.REGISTRY.key());

    public static final RegistrySupplier<BasicAnimalDisguise> PIG = DISGUISE_TYPES.register("pig", BasicAnimalDisguise::new);
    public static final RegistrySupplier<BasicAnimalDisguise> SHEEP = DISGUISE_TYPES.register("sheep", BasicAnimalDisguise::new);
    public static final RegistrySupplier<BasicMonsterDisguise> ZOMBIE = DISGUISE_TYPES.register("zombie", BasicMonsterDisguise::new);
    public static final RegistrySupplier<BasicMonsterDisguise> SKELETON = DISGUISE_TYPES.register("skeleton", BasicMonsterDisguise::new);
    public static final RegistrySupplier<BasicMonsterDisguise> CREEPER = DISGUISE_TYPES.register("creeper", BasicMonsterDisguise::new);
    public static final RegistrySupplier<DrownedDisguise> DROWNED = DISGUISE_TYPES.register("drowned", DrownedDisguise::new);
    public static final RegistrySupplier<EndermanDisguise> ENDERMAN = DISGUISE_TYPES.register("enderman", EndermanDisguise::new);

    @Environment(EnvType.CLIENT)
    public static void initRenderers() {
        DisguiseRendererRegistry.register(PIG, PigDisguiseRenderer::new);
        DisguiseRendererRegistry.register(SHEEP, SheepDisguiseRenderer::new);
        DisguiseRendererRegistry.register(ZOMBIE, ZombieDisguiseRenderer::new);
        DisguiseRendererRegistry.register(SKELETON, SkeletonDisguiseRenderer::new);
        DisguiseRendererRegistry.register(CREEPER, CreeperDisguiseRenderer::new);
        DisguiseRendererRegistry.register(DROWNED, DrownedDisguiseRenderer::new);
        DisguiseRendererRegistry.register(ENDERMAN, EndermanDisguiseRenderer::new);
    }
}
